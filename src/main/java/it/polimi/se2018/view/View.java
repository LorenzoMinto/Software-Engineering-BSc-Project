package it.polimi.se2018.view;

import it.polimi.se2018.model.*;
import it.polimi.se2018.networking.Client;
import it.polimi.se2018.networking.ConnectionType;
import it.polimi.se2018.networking.SenderInterface;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.utils.message.*;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.*;

public abstract class View implements Observer {

    private static final String MUST_CONNECT = "You have to connect to the server";

    private String playerID;

    final Logger logger;

    Move currentMove;

    private EnumSet<Move> permissions = EnumSet.of(Move.JOIN_GAME);

    private ViewState state = ViewState.INACTIVE;

    private SenderInterface client;

    private List<ToolCard> drawnToolCards;
    private List<PublicObjectiveCard> drawnPublicObjectiveCards;
    private List<String> players;
    private Track track;
    private List<Dice> draftPoolDices;
    private int roundNumber;
    private String playingPlayerID;

    private enum ViewState{
        INACTIVE,
        ACTIVE
    }

    View() {
        logger = createLogger();
    }

    void connectToRemoteServer(ConnectionType type, String serverName, int port){

        if(client==null){ //client is effectively final
            this.client = new Client(type,serverName,port,this, false);
        }
    }

    abstract void askForMove();

    abstract Message handleEndTurnMove();

    abstract Message handleDraftDiceFromDraftPoolMove();

    abstract Message handlePlaceDiceOnWindowPatternMove();

    abstract Message handleUseToolCardMove();

    abstract Message handleIncrementDraftedDiceMove();

    abstract Message handleDecrementDraftedDiceMove();

    abstract Message handleChangeDraftedDiceValueMove();

    abstract Message handleChooseDiceFromTrackMove();

    abstract Message handleMoveDiceMove();

    abstract Message handleJoinGameMove();

    abstract Message handleGameEndedMove(LinkedHashMap<String, Integer> rankings);

    abstract Message handleGiveWindowPatterns(List<WindowPattern> patterns);

    abstract Message handleAddedWL();

    private void receiveMessage(Message m) {

        Message message = null;

        if(state==ViewState.INACTIVE){
            //TODO: aggiungi quì le cose che si possono fare se inattivo
            if(m.getType()==CVMessage.types.BACK_TO_GAME){
                changeStateTo(ViewState.ACTIVE);
                showMessage("Hai effettuato correttamente il ricollegamento al gioco. Al prossimo tuo turno potrai giocare.");
            }
        } else {
            message = handleMessage(m);
        }

        if(message!=null){
            sendMessage(message);
        }
    }

    private Message handleMessage(Message m){

        updatePermissions(m);

        if(state==ViewState.INACTIVE){

            //Gestisce le mosse consentite durante lo stato INACTIVE
            if(m.getType()==CVMessage.types.BACK_TO_GAME){
                changeStateTo(ViewState.ACTIVE);
                showMessage("Hai effettuato correttamente il ricollegamento al gioco. Al prossimo tuo turno potrai giocare.");
            }

            return null;

        } else {

            if(m instanceof CVMessage){
                return handleCVMessages(m);

            } else if(m instanceof MVMessage){
                return handleMVMessages(m);

            } else if(m instanceof WaitingRoomMessage){
                return handleWLMessages(m);

            } else {
                //should never enter here
                throw new BadBehaviourRuntimeException();
            }

        }

    }

    private void updatePermissions(Message m){
        EnumSet<Move> p = (EnumSet<Move>) m.getPermissions();
        if(p!=null && !p.isEmpty()){
            setPermissions(p);
        }//else keep same permissions
    }

    private Message handleCVMessages(Message m){
        Message message = null;
        Object o;

        switch ((CVMessage.types) m.getType()) {
            case ERROR_MESSAGE:
            case ACKNOWLEDGMENT_MESSAGE:
                try {
                    o = m.getParam("message");
                } catch (NoSuchParamInMessageException e) {
                    break;
                }
                @SuppressWarnings("unchecked")
                String text = (String) o;

                if(!text.equals("")){
                    showMessage(text);
                }
                break;
            case INACTIVE_PLAYER:
                try {
                    o = m.getParam("player");
                } catch (NoSuchParamInMessageException e) {
                    break;
                }
                @SuppressWarnings("unchecked")
                String pID = (String) o;

                showMessage("Il giocatore ".concat(pID).concat(" è diventato inattivo. I suoi turni saranno saltati."));
                break;
            case BACK_TO_GAME:
                break;
            case INACTIVE:
                changeStateTo(ViewState.INACTIVE);
                showMessage("Sei stato scollegato dal gioco per inattività. I tuoi turni saranno saltati.");
                break;
            case GIVE_WINDOW_PATTERNS:
                try {
                    o = m.getParam("patterns");
                } catch (NoSuchParamInMessageException e) {
                    return null;
                }
                @SuppressWarnings("unchecked")
                List<WindowPattern> patterns = (List<WindowPattern>) o;

                message = handleGiveWindowPatterns(patterns);
                break;
            case GAME_ENDED:
                try {
                    o = m.getParam("rankings");
                } catch (NoSuchParamInMessageException e) {
                    break;
                }
                @SuppressWarnings("unchecked")
                LinkedHashMap<String, Integer> rankings = (LinkedHashMap<String, Integer>) o;
                message = handleGameEndedMove(rankings);
                break;
            default:
                //if cases are updated with CVMessage.types, should never enter here
                throw new BadBehaviourRuntimeException();
        }

        return message;
    }

    private Message handleMVMessages(Message m){
        Message message = null;

        switch ((MVMessage.types) m.getType()) {
            case SETUP:
                if( handleSetup(m) ){
                    notifyGameStarted();
                } else {
                    showMessage("Il setup iniziale del gioco è fallito. Potresti riscontrare difficoltà a giocare.");
                }
                break;
            case NEW_ROUND:
                if( handleNewRound(m) ){
                    showMessage("Inizia il #"+this.roundNumber+" round!");
                    notifyGameVariablesChanged();
                } else {
                    showMessage("Il setup del nuovo round è fallito. Potresti riscontrare difficoltà a giocare.");
                }
                break;
            case NEW_TURN:
                if( handleNewTurn(m) ){
                    if(!playingPlayerID.equals(playerID)){
                        showMessage("E' ora il turno di "+playingPlayerID);
                    }

                    notifyGameVariablesChanged();
                } else {
                    showMessage("Il setup del nuovo turn è fallito. Potresti riscontrare difficoltà a giocare.");
                }
                break;
            case USED_TOOLCARD:
                //TODO: verificare con Jacopo e Lorenzo questo evento
                break;
            case RANKINGS:
                //TODO: verificare con Jacopo e Lorenzo questo evento
                break;
            case WINDOWPATTERN:
                //TODO: verificare con Jacopo e Lorenzo questo evento
                break;
            case DRAFTPOOL:
                //TODO: verificare con Jacopo e Lorenzo questo evento
                break;
            case YOUR_TURN: //needed just for setting permissions
                showMessage("Tocca a te! E' il tuo turno!");
                break;
            default:
                //if cases are updated with MVMessage.types, should never enter here
                throw new BadBehaviourRuntimeException();
        }

        return message;
    }

    private Message handleWLMessages(Message m){
        Message message = null;

        switch ((WaitingRoomMessage.types) m.getType()) {
            case BAD_FORMATTED: //just for debug
                break;
            case DENIED_LIMIT:
                showMessage("Impossibile unirsi alla partita perché è stato raggiunto il limite massimo di giocatori.");
                break;
            case DENIED_NICKNAME:
                showMessage("Impossibile unirsi alla partita perché il nickname indicato è già presente.");
                break;
            case DENIED_PLAYING:
                showMessage("Impossibile unirsi alla partita perchè si sta già svolgendo");
                break;
            case JOIN: //can't happen. is a message sent from the user
                break;
            case ADDED:
                message = handleAddedWL();
                break;
            case LEAVE: //can't happen. is a message sent from the user
                break;
            case REMOVED:
                showMessage("Sei stato correttamente rimosso dal gioco");
                break;
            default:
                //if cases are updated with WaitingRoomMessage.types, should never enter here
                throw new BadBehaviourRuntimeException();
        }

        return message;
    }

    abstract void showMessage(String message);

    abstract void errorMessage(String message);

    abstract void notifyGameVariablesChanged();

    abstract void notifyGameStarted();

    private void changeStateTo(ViewState state){

        if(state==ViewState.INACTIVE){
            //TODO: setta permissions di gioco solo a BACK_GAME
        } else {
            //TODO: setta permissions di gioco base
        }

        this.state = state;
    }

    void sendMessage(Message m){
        try {
            this.client.sendMessage(m);
        } catch (RemoteException e) {
            errorMessage("Error sending message: ".concat(m.toString()));
        } catch (NullPointerException ex){
            errorMessage(MUST_CONNECT);
        }
    }

    //UTILS

    private boolean handleSetup(Message m){
        Object o;
        try {
            o = m.getParam("drawnToolCards");
        } catch (NoSuchParamInMessageException e) {
            return false;
        }
        @SuppressWarnings("unchecked")
        List<ToolCard> mDrawnToolCards = (List<ToolCard>) o;

        try {
            o = m.getParam("drawnPublicObjectiveCards");
        } catch (NoSuchParamInMessageException e) {
            return false;
        }
        @SuppressWarnings("unchecked")
        List<PublicObjectiveCard> mDrawnPublicObjectiveCards = (List<PublicObjectiveCard>) o;

        try {
            o = m.getParam("players");
        } catch (NoSuchParamInMessageException e) {
            return false;
        }
        @SuppressWarnings("unchecked")
        List<String> mPlayers = (List<String>) o;

        try {
            o = m.getParam("track");
        } catch (NoSuchParamInMessageException e) {
            return false;
        }
        @SuppressWarnings("unchecked")
        Track mTrack = (Track) o;

        try {
            o = m.getParam("draftPoolDices");
        } catch (NoSuchParamInMessageException e) {
            return false;
        }
        @SuppressWarnings("unchecked")
        List<Dice> mDraftPoolDices = (List<Dice>) o;

        //Assignments are done only at the end of parsing of all data to prevent partial update (due to errors)
        this.drawnToolCards=mDrawnToolCards;
        this.drawnPublicObjectiveCards=mDrawnPublicObjectiveCards;
        this.players=mPlayers;
        this.track=mTrack;
        this.draftPoolDices=mDraftPoolDices;

        notifyGameVariablesChanged();

        return true;
    }

    private boolean handleNewRound(Message m){
        Object o;
        try {
            o = m.getParam("number");
        } catch (NoSuchParamInMessageException e) {
            return false;
        }
        @SuppressWarnings("unchecked")
        int number = (int) o;

        try {
            o = m.getParam("track");
        } catch (NoSuchParamInMessageException e) {
            return false;
        }
        @SuppressWarnings("unchecked")
        Track mTrack = (Track) o;

        try {
            o = m.getParam("draftPoolDices");
        } catch (NoSuchParamInMessageException e) {
            return false;
        }
        @SuppressWarnings("unchecked")
        List<Dice> mDraftPoolDices = (List<Dice>) o;

        this.roundNumber = number;
        this.draftPoolDices = mDraftPoolDices;
        this.track = mTrack;

        return true;
    }

    private boolean handleNewTurn(Message m) {
        Object o;

        try {
            o = m.getParam("whoIsPlaying");
        } catch (NoSuchParamInMessageException e) {
            return false;
        }
        @SuppressWarnings("unchecked")
        String whoIsPlaying = (String) o;

        this.playingPlayerID = whoIsPlaying;
        return true;
    }

    private Logger createLogger(){
        Logger newLogger = Logger.getLogger(View.class.getName());
        newLogger.setUseParentHandlers(false);
        newLogger.setLevel(Level.FINE);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.FINE);
        handler.setFormatter(new SimpleFormatter(){
            private static final String FORMAT = "[VIEW] %1$s %n";

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(FORMAT,lr.getMessage());
            }

        });
        newLogger.addHandler(handler);
        return newLogger;
    }

    @Override
    public boolean update(Message m) {
        logger.info(()->"Received: "+m.getType().toString());

        receiveMessage(m);

        askForMove();

        return true;
    }

    //GETTERS AND SETTERS

    public String getPlayerID() {
        return playerID;
    }

    public Set<Move> getPermissions() {
        return permissions;
    }

    public void setPlayer(String playerID) {
        this.playerID = playerID;
    }

    void setCurrentMove(Move currentMove) {
        this.currentMove = currentMove;
    }

    public void setPermissions(Set<Move> permissions) {
        this.permissions = (EnumSet<Move>)permissions;
    }


    //TODO: view must assume permissions (see CVMessage types comments) in case of receiving CVMessage of INACTIVE or BACK_GAME. Otherwise permissions are usually sent by Model through MVMessage


    //NOTE: L'ultimo giocatore in ordine temporale che sceglie il wp causando l'inizio del gioco potrebbe vedere prima l'inizio del gioco e poi l'acknowledge del set del windowpattern

    //TODO: riceve un messaggio MVMessage di tipo NEW_TURN e se il player della view è uguale a param:currentPlayer allora imposta le permissions, sennò setta permissions vuote fino ad un nuovo NEW_TURN

}
