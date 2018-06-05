package it.polimi.se2018.view;

import it.polimi.se2018.controller.ObjectiveCardManager;
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

    String playerID;

    final Logger logger;  //TODO: check if needed

    private EnumSet<Move> permissions = EnumSet.of(Move.JOIN_GAME);

    private ViewState state = ViewState.ACTIVE;

    private SenderInterface client;

    List<ToolCard> drawnToolCards; //TODO: check if needed
    List<PublicObjectiveCard> drawnPublicObjectiveCards; //TODO: check if needed
    List<String> players; //TODO: check if needed
    Track track; //TODO: check if needed
    List<Dice> draftPoolDices; //TODO: check if needed
    int roundNumber; //TODO: check if needed
    String playingPlayerID; //TODO: check if needed
    Dice draftedDice; //TODO: check if needed
    WindowPattern windowPattern; //TODO: check if needed
    List<WindowPattern> windowPatterns;
    PrivateObjectiveCard privateObjectiveCard; //TODO: check if needed


    LinkedHashMap<String, Integer> rankings;
    List<WindowPattern> drawnWindowPatterns;

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

    //MOVES

    void handleLeaveWaitingRoomMove(){
        sendMessage(new WaitingRoomMessage(WaitingRoomMessage.types.LEAVE,Message.fastMap("nickname",this.playerID)));
    }

    void handleBackGameMove(){
        sendMessage(new VCMessage(VCMessage.types.BACK_GAMING,null,this.playerID));
    }

    void handleEndTurnMove(){
        sendMessage(new VCMessage(VCMessage.types.END_TURN,null,this.playerID));
    }

    void handleDraftDiceFromDraftPoolMove(){
        //TODO: implement
    }

    void handlePlaceDiceOnWindowPatternMove(){
        //TODO: implement
    }

    void handleUseToolCardMove(){
        //TODO: implement
    }

    void handleIncrementDraftedDiceMove(){
        //TODO: implement
    }

    void handleDecrementDraftedDiceMove(){
        //TODO: implement
    }

    void handleChangeDraftedDiceValueMove(){
        //TODO: implement
    }

    void handleChooseDiceFromTrackMove(){
        //TODO: implement
    }

    void handleMoveDiceMove(){
        //TODO: implement
    }

     void handleJoinGameMove(){
         //TODO: implement
     }

    //EVENTS

    void handleGameEndedEvent(Message m){
        Object o;
        try {
            o = m.getParam("rankings");
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        LinkedHashMap<String, Integer> receivedRankings = (LinkedHashMap<String, Integer>) o;
        this.rankings = receivedRankings;
    }

    void handleGiveWindowPatternsEvent(Message m){
        Object o;
        try {
            o = m.getParam("patterns");
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        List<WindowPattern> patterns = (List<WindowPattern>) o;
        this.drawnWindowPatterns = patterns;
        showMessage("Ricevuti windowpattern da scegliere");
    }

    void handleAddedEvent(){
        showMessage("You have joined the waiting room");
    }

    void handleRemovedEvent(){
        showMessage("Sei stato correttamente rimosso dal gioco");
    }

    void handleCVAcknowledgmentEvent(Message m){
        Object o;
        try {
            o = m.getParam("message");
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        String ack = (String) o;

        if(!ack.equals("")){
            showMessage(ack);
        }
    }

    void handleInactivePlayerEvent(Message m){
        Object o;
        try {
            o = m.getParam("player");
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        String pID = (String) o;

        showMessage("Il giocatore ".concat(pID).concat(" è diventato inattivo. I suoi turni saranno saltati."));
    }

    void handleBackToGameEvent(){
        showMessage("You are back to game, now.");
    }

    void handleInactiveEvent(){
        changeStateTo(ViewState.INACTIVE);
        showMessage("Sei stato scollegato dal gioco per inattività. I tuoi turni saranno saltati.");
    }

    void handleCVErrorEvent(Message m){
        Object o;
        try {
            o = m.getParam("message");
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        String err = (String) o;

        if(!err.equals("")){
            errorMessage(err);
        }
    }

    void handleSetupEvent(Message m) {
        Object o;
        try {
            o = m.getParam("drawnToolCards");
        } catch (NoSuchParamInMessageException e) {
            showMessage("Il setup iniziale del gioco è fallito. Potresti riscontrare difficoltà a giocare.");
            return;
        }
        @SuppressWarnings("unchecked")
        List<ToolCard> mDrawnToolCards = (List<ToolCard>) o;

        try {
            o = m.getParam("drawnPublicObjectiveCards");
        } catch (NoSuchParamInMessageException e) {
            showMessage("Il setup iniziale del gioco è fallito. Potresti riscontrare difficoltà a giocare.");
            return;
        }
        @SuppressWarnings("unchecked")
        List<PublicObjectiveCard> mDrawnPublicObjectiveCards = (List<PublicObjectiveCard>) o;

        try {
            o = m.getParam("players");
        } catch (NoSuchParamInMessageException e) {
            showMessage("Il setup iniziale del gioco è fallito. Potresti riscontrare difficoltà a giocare.");
            return;
        }
        @SuppressWarnings("unchecked")
        List<String> mPlayers = (List<String>) o;

        try {
            o = m.getParam("track");
        } catch (NoSuchParamInMessageException e) {
            showMessage("Il setup iniziale del gioco è fallito. Potresti riscontrare difficoltà a giocare.");
            return;
        }
        @SuppressWarnings("unchecked")
        Track mTrack = (Track) o;

        try {
            o = m.getParam("draftPoolDices");
        } catch (NoSuchParamInMessageException e) {
            showMessage("Il setup iniziale del gioco è fallito. Potresti riscontrare difficoltà a giocare.");
            return;
        }
        @SuppressWarnings("unchecked")
        List<Dice> mDraftPoolDices = (List<Dice>) o;

        try {
            o = m.getParam("windowPatterns");
        } catch (NoSuchParamInMessageException e) {
            showMessage("Il setup iniziale del gioco è fallito. Potresti riscontrare difficoltà a giocare.");
            return;
        }
        @SuppressWarnings("unchecked")
        List<WindowPattern> mWindowPatterns = (List<WindowPattern>) o;

        try {
            o = m.getParam("privateObjectiveCard");
        } catch (NoSuchParamInMessageException e) {
            showMessage("Il setup iniziale del gioco è fallito. Potresti riscontrare difficoltà a giocare.");
            return;
        }
        @SuppressWarnings("unchecked")
        PrivateObjectiveCard mPrivateObjectiveCard = (PrivateObjectiveCard) o;

        //Assignments are done only at the end of parsing of all data to prevent partial update (due to errors)
        setDrawnToolCards(mDrawnToolCards);
        setDraftPoolDices(mDraftPoolDices);
        setDrawnPublicObjectiveCards(mDrawnPublicObjectiveCards);
        setPlayers(mPlayers);
        setTrack(mTrack);
        setPrivateObjectiveCard(mPrivateObjectiveCard);
        setWindowPatterns(mWindowPatterns);

        notifyGameVariablesChanged();

        notifyGameStarted();

        setPermissions(EnumSet.noneOf(Move.class));
    }

    void handleNewRoundEvent(Message m) {
        Object o;
        try {
            o = m.getParam("number");
        } catch (NoSuchParamInMessageException e) {
            showMessage("Il setup del nuovo round è fallito. Potresti riscontrare difficoltà a giocare.");
            return;
        }
        @SuppressWarnings("unchecked")
        int number = (int) o;

        try {
            o = m.getParam("track");
        } catch (NoSuchParamInMessageException e) {
            showMessage("Il setup del nuovo round è fallito. Potresti riscontrare difficoltà a giocare.");
            return;
        }
        @SuppressWarnings("unchecked")
        Track mTrack = (Track) o;

        try {
            o = m.getParam("draftPoolDices");
        } catch (NoSuchParamInMessageException e) {
            showMessage("Il setup del nuovo round è fallito. Potresti riscontrare difficoltà a giocare.");
            return;
        }
        @SuppressWarnings("unchecked")
        List<Dice> mDraftPoolDices = (List<Dice>) o;

        setRoundNumber(number);
        setDraftPoolDices(mDraftPoolDices);
        setTrack(mTrack);

        notifyNewRound();
    }

    void handleNewTurnEvent(Message m) {
        Object o;

        try {
            o = m.getParam("whoIsPlaying");
        } catch (NoSuchParamInMessageException e) {
            showMessage("Il setup del nuovo turn è fallito. Potresti riscontrare difficoltà a giocare.");
            return;
        }
        @SuppressWarnings("unchecked")
        String whoIsPlaying = (String) o;

        setPlayingPlayerID(whoIsPlaying);

        notifyNewTurn();
    }

    void handleRankingsEvent(Message m) {
        Object o;
        try {
            o = m.getParam("winner");
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        String winner = (String) o;
        //TODO: aggiungere anche gestione della classifica (getParam(rankings))
        showMessage("Il vincitore è "+winner);
    }

    void handleAssignedWindowPatternEvent(Message m) {
        Object o;
        try {
            o = m.getParam("windopattern");
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        WindowPattern wp = (WindowPattern) o;
        setWindowPattern(wp);
        showMessage("Ti è stato assegnato questo windowpattern: "+windowPattern);
    }

    void handleChangedDraftPoolEvent(Message m) {
        Object o;
        try {
            o = m.getParam("draftPoolDices");
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        List<Dice> mDraftPoolDices = (List<Dice>) o;
        setDraftPoolDices(mDraftPoolDices);
    }

    void handleYourTurnEvent() {
        showMessage("Tocca a te! E' il tuo turno!");
    }

    void handleBadFormattedEvent() {
        //TODO: check if this method is never called
    }

    void handleDeniedLimitEvent() {
        showMessage("Impossibile unirsi alla partita perché è stato raggiunto il limite massimo di giocatori.");
    }

    void handleDeniedNicknameEvent() {
        showMessage("Impossibile unirsi alla partita perché il nickname indicato è già presente.");
    }

    void hadleDeniedPlayingEvent() {
        showMessage("Impossibile unirsi alla partita perchè si sta già svolgendo");
    }

    void handleUsedToolCardEvent(Message m){
        Object o;
        try {
            o = m.getParam("toolcard");
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        ToolCard toolcard = (ToolCard) o;

        try {
            o = m.getParam("toolcards");
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        List<ToolCard> toolcards = (List<ToolCard>) o;

        try {
            o = m.getParam("player");
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        String p = (String) o;

        setDrawnToolCards(toolcards);

        showMessage("Il giocatore "+p+" usa la toolcard "+toolcard.getTitle());
    }

    // NOTIFY METHODS

    void notifyNewRound(){
        showMessage("The round #"+this.roundNumber+" now starts!");
    }

    void notifyNewTurn(){
        if(!playingPlayerID.equals(playerID)){
            showMessage("Now it's "+playingPlayerID+"'s turn");
        }
    }

    void notifyGameStarted(){
        showMessage("The game is started!");
    }

    abstract void notifyGameVariablesChanged();

    // MESSAGES HANDLING

    private void receiveMessage(Message m){

        if(state==ViewState.INACTIVE){

            //Gestisce le mosse consentite durante lo stato INACTIVE
            if(m.getType()==CVMessage.types.BACK_TO_GAME){
                changeStateTo(ViewState.ACTIVE);
                showMessage("Hai effettuato correttamente il ricollegamento al gioco. Al prossimo tuo turno potrai giocare.");
            }

        } else {

            updatePermissions(m);

            if(m instanceof CVMessage){
                handleCVMessages(m);

            } else if(m instanceof MVMessage){
                handleMVMessages(m);

            } else if(m instanceof WaitingRoomMessage){
                handleWLMessages(m);

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
            System.out.println("Permissions granted by: "+m.getType().name());
            for (Move perm: p) {
                System.out.println(perm.name());
            }
        }//else keep same permissions
    }

    private void handleCVMessages(Message m){

        switch ((CVMessage.types) m.getType()) {
            case ERROR_MESSAGE:
                handleCVErrorEvent(m);
                break;
            case ACKNOWLEDGMENT_MESSAGE:
                handleCVAcknowledgmentEvent(m);
                break;
            case INACTIVE_PLAYER:
                handleInactivePlayerEvent(m);
                break;
            case BACK_TO_GAME:
                handleBackToGameEvent();
                break;
            case INACTIVE:
                handleInactiveEvent();
                break;
            case GIVE_WINDOW_PATTERNS:
                handleGiveWindowPatternsEvent(m);
                break;
            case GAME_ENDED:
                handleGameEndedEvent(m);
                break;
            default:
                //if cases are updated with CVMessage.types, should never enter here
                throw new BadBehaviourRuntimeException();
        }
    }

    private void handleMVMessages(Message m){
        switch ((MVMessage.types) m.getType()) {
            case SETUP:
                handleSetupEvent(m);
                break;
            case NEW_ROUND:
                handleNewRoundEvent(m);
                break;
            case NEW_TURN:
                handleNewTurnEvent(m);
                break;
            case USED_TOOLCARD:
                handleUsedToolCardEvent(m);
                break;
            case RANKINGS:
                handleRankingsEvent(m);
                break;
            case WINDOWPATTERN:
                handleAssignedWindowPatternEvent(m);
                break;
            case DRAFTPOOL:
                handleChangedDraftPoolEvent(m);
                break;
            case YOUR_TURN: //needed just for setting permissions
                handleYourTurnEvent();
                break;
            default:
                //if cases are updated with MVMessage.types, should never enter here
                throw new BadBehaviourRuntimeException();
        }
    }


    private void handleWLMessages(Message m){
        switch ((WaitingRoomMessage.types) m.getType()) {
            case BAD_FORMATTED: //just for debug
                handleBadFormattedEvent();
                break;
            case DENIED_LIMIT:
                handleDeniedLimitEvent();
                break;
            case DENIED_NICKNAME:
                handleDeniedNicknameEvent();
                break;
            case DENIED_PLAYING:
                hadleDeniedPlayingEvent();
                break;
            case JOIN: //can't happen. is a message sent from the user
                break;
            case ADDED:
                handleAddedEvent();
                break;
            case LEAVE: //can't happen. is a message sent from the user
                break;
            case REMOVED:
                handleRemovedEvent();
                break;
            default:
                //if cases are updated with WaitingRoomMessage.types, should never enter here
                throw new BadBehaviourRuntimeException();
        }
    }

    abstract void showMessage(String message);

    abstract void errorMessage(String message);

    //UTILS

    private void changeStateTo(ViewState state){

        if(state==ViewState.INACTIVE){
            setPermissions(EnumSet.of(Move.BACK_GAME));
        } else {
            setPermissions(EnumSet.noneOf(Move.class));
        }

        this.state = state;
    }

    void sendMessage(Message m){
        try {
            this.client.sendMessage(m);
        } catch (RemoteException e) {
            errorMessage("Error sending message: ".concat(m.toString()));
            //TODO: check if this must be removed in production
        } catch (NullPointerException ex){
            errorMessage(MUST_CONNECT);
        }
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

        //askForMove();

        return true;
    }

    //GETTERS AND SETTERS

    public String getPlayerID() {
        return playerID;
    }

    public Set<Move> getPermissions() {
        return permissions;
    }


    public PrivateObjectiveCard getPrivateObjectiveCard() {
        //TODO: change this temporary implementation
        ObjectiveCardManager manager = new ObjectiveCardManager();
        return manager.getPrivateObjectiveCard();
    }

    public void setPlayer(String playerID) {
        this.playerID = playerID;
    }

    public void setPermissions(Set<Move> permissions) {
        this.permissions = (EnumSet<Move>)permissions;
        notifyPermissionsChanged();
    }

    abstract void notifyPermissionsChanged();

    public void setDrawnToolCards(List<ToolCard> drawnToolCards) {
        this.drawnToolCards = drawnToolCards;
    }

    public void setDrawnPublicObjectiveCards(List<PublicObjectiveCard> drawnPublicObjectiveCards) {
        this.drawnPublicObjectiveCards = drawnPublicObjectiveCards;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public void setDraftPoolDices(List<Dice> draftPoolDices) {
        this.draftPoolDices = draftPoolDices;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public void setPlayingPlayerID(String playingPlayerID) {
        this.playingPlayerID = playingPlayerID;
    }

    public void setDraftedDice(Dice draftedDice) {
        this.draftedDice = draftedDice;
    }

    public void setWindowPattern(WindowPattern windowPattern) {
        this.windowPattern = windowPattern;
    }

    public void setPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) {
        this.privateObjectiveCard = privateObjectiveCard;
    }

    public void setWindowPatterns(List<WindowPattern> windowPatterns) {
        this.windowPatterns = windowPatterns;
    }

    //NOTE: L'ultimo giocatore in ordine temporale che sceglie il wp causando l'inizio del gioco potrebbe vedere prima l'inizio del gioco e poi l'acknowledge del set del windowpattern
}
