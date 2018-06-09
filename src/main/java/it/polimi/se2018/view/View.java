package it.polimi.se2018.view;

import it.polimi.se2018.controller.ObjectiveCardManager;
import it.polimi.se2018.model.*;
import it.polimi.se2018.networking.Client;
import it.polimi.se2018.networking.ConnectionType;
import it.polimi.se2018.networking.SenderInterface;
import it.polimi.se2018.utils.*;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.utils.Message;

import java.rmi.RemoteException;
import java.util.*;

public abstract class View implements Observer {

    private static final String MUST_CONNECT = "You have to connect to the server";

    private String playerID;

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
        //do nothing
    }

    void connectToRemoteServer(ConnectionType type, String serverName, int port){

        if(client==null){ //client is effectively final
            this.client = new Client(type,serverName,port,this, false);
        }
    }

    //MOVES

    void handleLeaveWaitingRoomMove(){
        sendMessage(new Message(ControllerBoundMessageType.LEAVE_WR,Message.fastMap("nickname",this.playerID)));
    }

    void handleBackGameMove(){
        sendMessage(new Message(ControllerBoundMessageType.BACK_GAMING,null,this.playerID));
    }

    void handleEndTurnMove(){
        sendMessage(new Message(ControllerBoundMessageType.END_TURN,null,this.playerID));
    }

    void handleWindowPatternSelection(WindowPattern wp){
        sendMessage(new Message(ControllerBoundMessageType.END_TURN,null,this.playerID));
    }

    void handleDraftDiceFromDraftPoolMove(){
        //no behaviour in common between CLI and GUI
    }

    void handlePlaceDiceOnWindowPatternMove(){
        //no behaviour in common between CLI and GUI
    }

    void handleUseToolCardMove(){
        //no behaviour in common between CLI and GUI
    }

    void handleIncrementDraftedDiceMove(){
        sendMessage(new Message(ControllerBoundMessageType.INCREMENT_DICE));
    }

    void handleDecrementDraftedDiceMove(){
        sendMessage(new Message(ControllerBoundMessageType.DECREMENT_DICE));
    }

    void handleEndEffectMove(){ sendMessage(new Message(ControllerBoundMessageType.END_TOOLCARD_EFFECT)); }

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
         //no behaviour in common between CLI and GUI
     }

    //EVENTS

    void handleGameEndedEvent(Message m){
        showMessage("The game is ended.");
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
        showMessage("Ricevuti windowPattern da scegliere");
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
        String text = (String) o;

        ack(text);
    }

    void ack(String text){
        if(!text.equals("")){
            showMessage(text);
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
        changeStateTo(ViewState.ACTIVE);
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
            o = m.getParam("yourWindowPattern");
        } catch (NoSuchParamInMessageException e) {
            showMessage("Il setup iniziale del gioco è fallito. Potresti riscontrare difficoltà a giocare.");
            return;
        }
        @SuppressWarnings("unchecked")
        WindowPattern mWindowPattern = (WindowPattern) o;

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
        setWindowPattern(mWindowPattern);

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
            o = m.getParam("winnerPlayerID");
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        String winnerID = (String) o;

        try {
            o = m.getParam("rankings");
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        LinkedHashMap<String, Integer> receivedRankings = (LinkedHashMap<String, Integer>) o;

        this.rankings = receivedRankings;

        if(winnerID.equals(this.playerID)){
            showMessage("You are the winner! Congratulations!");
        } else {
            showMessage("The winner is "+winnerID+"!");
        }

        notifyGameVariablesChanged();
    }

    void handleUpdatedWindowPatternEvent(Message m) {
        Object o;
        try {
            o = m.getParam("windowPattern");
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        WindowPattern wp = (WindowPattern) o;

        try {
            o = m.getParam("currentPlayer");
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        String pID = (String) o;

        // Assume ordinamento corrispettivo PLAYERS_ID:WINDOWPATTERNS
        int index = players.indexOf(pID);
        windowPatterns.set(index, wp);

        showMessage("A Window pattern has been updated ");

        notifyGameVariablesChanged();
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
        showMessage("It's your turn!");
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

    void handleDeniedPlayingEvent() {
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

    void handleSlotOfTrackChosenDice(Message m) {
        //TODO: implement here
    }

    void handleTrackChosenDiceEvent(Message m) {

    }

    void handleDraftedDiceEvent(Message m) {
        Object o;
        try {
            o = m.getParam("draftedDice");
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        Dice mDraftedDice = (Dice) o;
        setDraftedDice(mDraftedDice);
        showMessage("You have drafted "+mDraftedDice);
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

    void notifyPermissionsChanged(){
        //no behaviour in common between CLI and GUI
    }

    void notifyGameVariablesChanged(){
        //no behaviour in common between CLI and GUI
    }

    // MESSAGES HANDLING

    private void parseMessageOnInactiveState(Message m){

        ViewBoundMessageType type = (ViewBoundMessageType) m.getType();

        switch (type) {
            case BACK_TO_GAME:
                handleBackToGameEvent();
                break;
            case GAME_ENDED:
                handleGameEndedEvent(m);
                break;
            case RANKINGS:
                handleRankingsEvent(m);
                break;
            default:
                //No other messages are evaluated in this state
                break;
        }
    }

    private void parseMessageOnActiveState(Message m){

        ViewBoundMessageType type = (ViewBoundMessageType) m.getType();

        switch (type) {
            case ERROR_MESSAGE:
                handleCVErrorEvent(m);
                break;
            case ACKNOWLEDGMENT_MESSAGE:
                handleCVAcknowledgmentEvent(m);
                break;
            case A_PLAYER_BECOME_INACTIVE:
                handleInactivePlayerEvent(m);
                break;
            case YOU_ARE_INACTIVE:
                handleInactiveEvent();
                break;
            case DISTRIBUTION_OF_WINDOW_PATTERNS:
                handleGiveWindowPatternsEvent(m);
                break;
            case GAME_ENDED:
                handleGameEndedEvent(m);
                break;
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
            case SOMETHING_CHANGED_IN_WINDOWPATTERN:
                handleUpdatedWindowPatternEvent(m);
                break;
            case SOMETHING_CHANGED_IN_DRAFTPOOL:
                handleChangedDraftPoolEvent(m);
                break;
            case DRAFTED_DICE:
                handleDraftedDiceEvent(m);
                break;
            case TRACK_CHOSEN_DICE:
                handleTrackChosenDiceEvent(m);
                break;
            case SLOT_OF_TRACK_CHOSEN_DICE:
                handleSlotOfTrackChosenDice(m);
                break;
            case IT_IS_YOUR_TURN: //needed just for setting permissions
                handleYourTurnEvent();
                break;
            case BAD_FORMATTED:
                handleBadFormattedEvent();
                break;
            case JOIN_WR_DENIED_PLAYING:
                handleDeniedPlayingEvent();
                break;
            case JOIN_WR_DENIED_NICKNAME:
                handleDeniedNicknameEvent();
                break;
            case JOIN_WR_DENIED_LIMIT:
                handleDeniedLimitEvent();
                break;
            case ADDED_TO_WR:
                handleAddedEvent();
                break;
            case REMOVED_FROM_WR:
                handleRemovedEvent();
                break;
            default:
                //No other messages are evaluated in this state
                break;
        }

        if(type!=ViewBoundMessageType.ERROR_MESSAGE){
            //UPDATE PERMISSIONS
            EnumSet<Move> p = (EnumSet<Move>) m.getPermissions();
            if(!p.isEmpty()){
                setPermissions(p);
            }//else keep same permissions
        }
    }

    private void receiveMessage(Message m){

        if( state==ViewState.INACTIVE ){
            parseMessageOnInactiveState(m);
        } else {
            parseMessageOnActiveState(m);
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

    @Override
    public boolean update(Message m) {
        receiveMessage(m);
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

    //NOTE: L'ultimo giocatore in ordine temporale che sceglie il wp causando l'inizio del gioco potrebbe vedere prima l'inizio del gioco e poi l'acknowledge del set del windowPattern
}
