package it.polimi.se2018.view;

import it.polimi.se2018.networking.Client;
import it.polimi.se2018.networking.ConnectionType;
import it.polimi.se2018.networking.SenderInterface;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.utils.message.CVMessage;
import it.polimi.se2018.utils.message.MVMessage;
import it.polimi.se2018.utils.message.Message;
import it.polimi.se2018.utils.message.WaitingRoomMessage;

import java.rmi.RemoteException;
import java.util.EnumSet;
import java.util.Set;
import java.util.logging.*;

public abstract class View implements Observer {

    private static final String MUST_CONNECT = "You have to connect to the server";

    private String playerID;

    final Logger logger;

    private Move currentMove;

    private EnumSet<Move> permissions = EnumSet.of(Move.JOIN_GAME);

    private ViewState state = ViewState.INACTIVE;

    private SenderInterface client;

    private enum ViewState{
        INACTIVE,
        ACTIVE
    }

    public View() {
        logger = createLogger();
    }

    void connectToRemoteServer(ConnectionType type){

        if(client==null){ //client is effectively final
            this.client = new Client(type,this, false);
        }
    }

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

    abstract void askForMove();

    void handleMove() {
        //Check if currentMove is a valid one
        if(currentMove==null){
            showMessage("Mossa non riconosciuta");
            return;
        }

        Message message = null;

        //Here the code that handles each move. Each case can contain multiple writeToConsole and / or readFromConsole
        switch (currentMove) {
            case END_TURN:
                message = handleEndTurnMove();
                break;
            case DRAFT_DICE_FROM_DRAFTPOOL:
                message = handleDraftDiceFromDraftPoolMove();
                break;
            case PLACE_DICE_ON_WINDOWPATTERN:
                message = handlePlaceDiceOnWindowPatternMove();
                break;
            case USE_TOOLCARD:
                message = handleUseToolCardMove();
                break;
            case INCREMENT_DRAFTED_DICE:
                message = handleIncrementDraftedDiceMove();
                break;
            case DECREMENT_DRAFTED_DICE:
                message = handleDecrementDraftedDiceMove();
                break;
            case CHANGE_DRAFTED_DICE_VALUE:
                message = handleChangeDraftedDiceValueMove();
                break;
            case CHOOSE_DICE_FROM_TRACK:
                message = handleChooseDiceFromTrackMove();
                break;
            case MOVE_DICE:
                message = handleMoveDiceMove();
                break;
            case JOIN_GAME:
                message = handleJoinGameMove();
                break;
        }

        //Send message (if created) to server (through client)
        if(message!=null){
            sendMessage(message);
        }
    }

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

    private void receiveMessage(Message m) {

        EnumSet<Move> p = (EnumSet<Move>) m.getPermissions();
        if(p!=null && !p.isEmpty()){
            setPermissions(p);
        }//else keep same permissions

        if(m instanceof CVMessage){
            switch ((CVMessage.types) m.getType()) {
                case ERROR_MESSAGE:
                    break;
                case ACKNOWLEDGMENT_MESSAGE:
                    break;
                case INACTIVE_PLAYER:
                    break;
                case BACK_TO_GAME:
                    state = ViewState.ACTIVE;
                    break;
                case INACTIVE:
                    state = ViewState.INACTIVE;
                    break;
                case GIVE_WINDOW_PATTERNS:
                    break;
                case GAME_ENDED:
                    break;
                default:
                    //if cases are updated with CVMessage.types, should never enter here
                    throw new BadBehaviourRuntimeException();
            }
        } else if(m instanceof MVMessage){

            if(state==ViewState.INACTIVE){
                //TODO: implementato così perchè trovata annotazione che diceva di farlo, ma verificare che effettivamente sia corretto
                return;
            }

            switch ((MVMessage.types) m.getType()) {
                case SETUP:
                    break;
                case NEXT_ROUND:
                    break;
                case NEW_TURN:
                    break;
                case USED_TOOLCARD:
                    break;
                case RANKINGS:
                    break;
                case WINDOWPATTERN:
                    break;
                case DRAFTPOOL:
                    break;
                case YOUR_TURN:
                    //needed just for setting permissions
                    break;
                default:
                    //if cases are updated with MVMessage.types, should never enter here
                    throw new BadBehaviourRuntimeException();
            }
        } else if(m instanceof WaitingRoomMessage){
            switch ((WaitingRoomMessage.types) m.getType()) {
                case BAD_FORMATTED:
                    break;
                case DENIED:
                    break;
                case JOIN:
                    break;
                case ADDED:
                    break;
                case LEAVE:
                    break;
                case REMOVED:
                    break;
                default:
                    //if cases are updated with WaitingRoomMessage.types, should never enter here
                    throw new BadBehaviourRuntimeException();
            }
        } else {
            //should never enter here
            throw new BadBehaviourRuntimeException();
        }
    }

    abstract void showMessage(String message);

    private void sendMessage(Message m){
        try {
            this.client.sendMessage(m);
        } catch (RemoteException e) {
            showMessage("Error sending message: ".concat(m.toString()));
            //TODO: da rivedere il fatto che stampi l'errore
        } catch (NullPointerException ex){
            showMessage(MUST_CONNECT);
        }
    }

    //UTILS
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

    //TODO: view must assume permissions (see CVMessage types comments) in case of receiving CVMessage of INACTIVE or BACK_GAME. Otherwise permissions are usually sent by Model through MVMessage


    //NOTE: L'ultimo giocatore in ordine temporale che sceglie il wp causando l'inizio del gioco potrebbe vedere prima l'inizio del gioco e poi l'acknowledge del set del windowpattern

    //TODO: riceve un messaggio MVMessage di tipo NEW_TURN e se il player della view è uguale a param:currentPlayer allora imposta le permissions, sennò setta permissions vuote fino ad un nuovo NEW_TURN

}
