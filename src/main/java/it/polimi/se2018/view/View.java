package it.polimi.se2018.view;

import it.polimi.se2018.controller.ObjectiveCardManager;
import it.polimi.se2018.controller.RankingRecord;
import it.polimi.se2018.model.*;
import it.polimi.se2018.networking.Client;
import it.polimi.se2018.networking.ConnectionType;
import it.polimi.se2018.networking.NetworkingException;
import it.polimi.se2018.networking.SenderInterface;
import it.polimi.se2018.utils.*;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.utils.Message;

import java.util.*;

public abstract class View implements Observer {

    // CONSTANTS FOR MESSAGES

    private static final String MUST_CONNECT = "You have to connect to the server";
    private static final String THE_GAME_IS_ENDED = "The game is ended";
    private static final String WINDOW_PATTERNS_RECEIVED = "Ricevuti windowPattern da scegliere";
    private static final String YOU_HAVE_JOINED_THE_WAITING_ROOM = "You have joined the waiting room";
    private static final String REMOVED_FROM_GAME = "Sei stato correttamente rimosso dal gioco";
    private static final String A_PLAYER_BECAME_INACTIVE = " è diventato inattivo. I suoi turni saranno saltati.";
    private static final String BACK_TO_GAME = "You are back to game, now.";
    private static final String YOU_ARE_NOW_INACTIVE = "Sei stato scollegato dal gioco per inattività. I tuoi turni saranno saltati.";
    private static final String FAILED_SETUP_GAME = "Il setup iniziale del gioco è fallito. Potresti riscontrare difficoltà a giocare.";
    private static final String FAILED_SETUP_ROUND = "Il setup del nuovo round è fallito. Potresti riscontrare difficoltà a giocare.";
    private static final String FAILED_SETUP_TURN = "Il setup del nuovo turn è fallito. Potresti riscontrare difficoltà a giocare.";
    private static final String YOU_ARE_THE_WINNER = "You are the winner! Congratulations!";
    private static final String THE_WINNER_IS = "The winner is ";
    private static final String WINDOW_PATTERN_UPDATED = "A Window pattern has been updated ";
    private static final String ITS_YOUR_TURN = "It's your turn!";
    private static final String ERROR_MOVE = "Un errore inatteso ha reso impossibile effettuare la mossa. Riprova.";
    private static final String MAX_PLAYERS_ERROR = "Impossibile unirsi alla partita perché è stato raggiunto il limite massimo di giocatori.";
    private static final String NICKNAME_ALREADY_USED_ERROR = "Impossibile unirsi alla partita perché il nickname indicato è già presente.";
    private static final String ALREADY_PLAYING_ERROR = "Impossibile unirsi alla partita perchè si sta già svolgendo";
    private static final String USE_TOOL_CARD = " usa la toolCard ";
    private static final String YOU_HAVE_DRAFTED = "You have drafted ";
    private static final String JOINS_THE_WAITING_ROOM = " joins the waiting room";
    private static final String LEAVES_THE_WAITING_ROOM = " leaves the waiting room";
    private static final String ROUND_NOW_STARTS = "# Round now starts!";
    private static final String NOW_ITS_TURN_OF = "Now it's the turn of";
    private static final String THE_GAME_IS_STARTED = "The game is started!";
    private static final String ERROR_SENDING_MESSAGE = "Error sending message: ";


    // CONSTANTS FOR MESSAGES PARAMS

    private static final String PARAM_PLAYER = "player";
    private static final String PARAM_MESSAGE = "message";
    private static final String PARAM_DRAWN_TOOL_CARDS = "drawnToolCards";
    private static final String PARAM_DRAWN_PUBLIC_OBJECTIVE_CARDS = "drawnPublicObjectiveCards";
    private static final String PARAM_PLAYERS = "players";
    private static final String PARAM_TRACK = "track";
    private static final String PARAM_DRAFT_POOL_DICES = "draftPoolDices";
    private static final String PARAM_WINDOW_PATTERNS = "windowPatterns";
    private static final String PARAM_YOUR_WINDOW_PATTERN = "yourWindowPattern";
    private static final String PARAM_PRIVATE_OBJECTIVE_CARD = "privateObjectiveCard";
    private static final String PARAM_WHO_IS_PLAYING = "whoIsPlaying";
    private static final String PARAM_WINNER_PLAYER_ID = "winnerPlayerID";
    private static final String PARAM_RANKINGS = "rankings";
    private static final String PARAM_CURRENT_PLAYER = "currentPlayer";


    // CONFIGURATION INFORMATION

    /**
     * Set of moves that the player can do with this view
     */
    private EnumSet<Move> permissions = EnumSet.of(Move.JOIN_GAME);

    /**
     * State of the view: becomes "inactive" when player is marked from server as "inactive"
     */
    private ViewState state = ViewState.ACTIVE;

    /**
     * The client that handles communication of this view
     */
    private SenderInterface client;



    // COPIES OF GAME INFORMATION TO GRAPHICALLY REPRESENT THEM

    /**
     * The ID of the player of this view
     */
    private String playerID;

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

    List<RankingRecord> rankings;

    List<WindowPattern> drawnWindowPatterns;



    //HANDLING OF MOVES

    void handleLeaveWaitingRoomMove(){
        sendMessage(new Message(ControllerBoundMessageType.LEAVE_WR,Message.fastMap("nickname",this.playerID)));
    }

    void handleBackGameMove(){
        sendMessage(new Message(ControllerBoundMessageType.BACK_GAMING,null,this.playerID));
    }

    void handleEndTurnMove(){
        sendMessage(new Message(ControllerBoundMessageType.END_TURN,null,this.playerID));
    }

    void handleWindowPatternSelection(WindowPattern wp){ //TODO: questo metodo non è mai usato. come mai?
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

    void handleEndEffectMove(){
        sendMessage(new Message(ControllerBoundMessageType.END_TOOLCARD_EFFECT));
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
        //no behaviour in common between CLI and GUI
    }



    //HANDLING OF EVENTS

    void handleGameEndedEvent(){
        showMessage(THE_GAME_IS_ENDED);
    }

    void handleGiveWindowPatternsEvent(Message m){
        Object o;
        try {
            o = m.getParam(PARAM_WINDOW_PATTERNS);
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        List<WindowPattern> patterns = (List<WindowPattern>) o;
        this.drawnWindowPatterns = patterns;
        showMessage(WINDOW_PATTERNS_RECEIVED);
    }

    void handleAddedEvent(){
        showMessage(YOU_HAVE_JOINED_THE_WAITING_ROOM);
    }

    void handleRemovedEvent(){
        showMessage(REMOVED_FROM_GAME);
    }

    void handleAcknowledgmentEvent(Message m){
        Object o;
        try {
            o = m.getParam(PARAM_MESSAGE);
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
            o = m.getParam(PARAM_PLAYER);
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        String pID = (String) o;

        showMessage(pID.concat(A_PLAYER_BECAME_INACTIVE));
    }

    void handleBackToGameEvent(){
        changeStateTo(ViewState.ACTIVE);
        showMessage(BACK_TO_GAME);
    }

    void handleInactiveEvent(){
        changeStateTo(ViewState.INACTIVE);
        showMessage(YOU_ARE_NOW_INACTIVE);
    }

    void handleErrorEvent(Message m){
        Object o;
        try {
            o = m.getParam(PARAM_MESSAGE);
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
            o = m.getParam(PARAM_DRAWN_TOOL_CARDS);
        } catch (NoSuchParamInMessageException e) {
            showMessage(FAILED_SETUP_GAME);
            return;
        }
        @SuppressWarnings("unchecked")
        List<ToolCard> mDrawnToolCards = (List<ToolCard>) o;

        try {
            o = m.getParam(PARAM_DRAWN_PUBLIC_OBJECTIVE_CARDS);
        } catch (NoSuchParamInMessageException e) {
            showMessage(FAILED_SETUP_GAME);
            return;
        }
        @SuppressWarnings("unchecked")
        List<PublicObjectiveCard> mDrawnPublicObjectiveCards = (List<PublicObjectiveCard>) o;

        try {
            o = m.getParam(PARAM_PLAYERS);
        } catch (NoSuchParamInMessageException e) {
            showMessage(FAILED_SETUP_GAME);
            return;
        }
        @SuppressWarnings("unchecked")
        List<String> mPlayers = (List<String>) o;

        try {
            o = m.getParam(PARAM_TRACK);
        } catch (NoSuchParamInMessageException e) {
            showMessage(FAILED_SETUP_GAME);
            return;
        }
        @SuppressWarnings("unchecked")
        Track mTrack = (Track) o;

        try {
            o = m.getParam(PARAM_DRAFT_POOL_DICES);
        } catch (NoSuchParamInMessageException e) {
            showMessage(FAILED_SETUP_GAME);
            return;
        }
        @SuppressWarnings("unchecked")
        List<Dice> mDraftPoolDices = (List<Dice>) o;

        try {
            o = m.getParam(PARAM_WINDOW_PATTERNS);
        } catch (NoSuchParamInMessageException e) {
            showMessage(FAILED_SETUP_GAME);
            return;
        }
        @SuppressWarnings("unchecked")
        List<WindowPattern> mWindowPatterns = (List<WindowPattern>) o;

        try {
            o = m.getParam(PARAM_YOUR_WINDOW_PATTERN);
        } catch (NoSuchParamInMessageException e) {
            showMessage(FAILED_SETUP_GAME);
            return;
        }
        @SuppressWarnings("unchecked")
        WindowPattern mWindowPattern = (WindowPattern) o;

        try {
            o = m.getParam(PARAM_PRIVATE_OBJECTIVE_CARD);
        } catch (NoSuchParamInMessageException e) {
            showMessage(FAILED_SETUP_GAME);
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

        notifyGameStarted();

        setPermissions(EnumSet.noneOf(Move.class));
    }

    void handleNewRoundEvent(Message m) {
        Object o;
        try {
            o = m.getParam("number");
        } catch (NoSuchParamInMessageException e) {
            showMessage(FAILED_SETUP_ROUND);
            return;
        }
        @SuppressWarnings("unchecked")
        int number = (int) o;

        try {
            o = m.getParam(PARAM_TRACK);
        } catch (NoSuchParamInMessageException e) {
            showMessage(FAILED_SETUP_ROUND);
            return;
        }
        @SuppressWarnings("unchecked")
        Track mTrack = (Track) o;

        try {
            o = m.getParam(PARAM_DRAFT_POOL_DICES);
        } catch (NoSuchParamInMessageException e) {
            showMessage(FAILED_SETUP_ROUND);
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
            o = m.getParam(PARAM_WHO_IS_PLAYING);
        } catch (NoSuchParamInMessageException e) {
            showMessage(FAILED_SETUP_TURN);
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
            o = m.getParam(PARAM_WINNER_PLAYER_ID);
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        String winnerID = (String) o;

        try {
            o = m.getParam(PARAM_RANKINGS);
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        List<RankingRecord> receivedRankings = (List<RankingRecord>) o;

        this.rankings = receivedRankings;

        if(winnerID.equals(this.playerID)){
            showMessage(YOU_ARE_THE_WINNER);
        } else {
            showMessage(THE_WINNER_IS +winnerID);
        }
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
            o = m.getParam(PARAM_CURRENT_PLAYER);
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        String pID = (String) o;

        // Assume ordinamento corrispettivo PLAYERS_ID:WINDOWPATTERNS
        int index = players.indexOf(pID);
        windowPatterns.set(index, wp);

        showMessage(WINDOW_PATTERN_UPDATED);
    }

    void handleChangedDraftPoolEvent(Message m) {
        Object o;
        try {
            o = m.getParam(PARAM_DRAFT_POOL_DICES);
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        List<Dice> mDraftPoolDices = (List<Dice>) o;
        setDraftPoolDices(mDraftPoolDices);
    }

    void handleYourTurnEvent() {
        showMessage(ITS_YOUR_TURN);
    }

    void handleBadFormattedEvent() {
        showMessage(ERROR_MOVE);
    }

    void handleDeniedLimitEvent() {
        showMessage(MAX_PLAYERS_ERROR);
    }

    void handleDeniedNicknameEvent() {
        showMessage(NICKNAME_ALREADY_USED_ERROR);
    }

    void handleDeniedPlayingEvent() {
        showMessage(ALREADY_PLAYING_ERROR);
    }

    void handleUsedToolCardEvent(Message m){
        Object o;
        try {
            o = m.getParam("toolCard");
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        ToolCard toolCard = (ToolCard) o;

        try {
            o = m.getParam("toolCards");
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        List<ToolCard> toolCards = (List<ToolCard>) o;

        try {
            o = m.getParam(PARAM_PLAYER);
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        String p = (String) o;

        setDrawnToolCards(toolCards);

        showMessage(p+ USE_TOOL_CARD +toolCard.getTitle());
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
        showMessage(YOU_HAVE_DRAFTED +mDraftedDice);
    }

    void handlePlayerAddedToWREvent(Message m){
        Object o;
        try {
            o = m.getParam(PARAM_PLAYER);
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        String nickname = (String) o;

        if(!nickname.equals(this.playerID)){
            showMessage(nickname+ JOINS_THE_WAITING_ROOM);
        }
    }

    void handlePlayerRemovedFromWREvent(Message m){
        Object o;
        try {
            o = m.getParam(PARAM_PLAYER);
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        String nickname = (String) o;

        if(!nickname.equals(this.playerID)) {
            showMessage(nickname + LEAVES_THE_WAITING_ROOM);
        }
    }



    // NOTIFY METHODS

    void notifyNewRound(){
        showMessage(this.roundNumber+ ROUND_NOW_STARTS);
    }

    void notifyNewTurn(){
        if(!playingPlayerID.equals(playerID)){
            showMessage(NOW_ITS_TURN_OF + playingPlayerID);
            setPermissions(EnumSet.noneOf(Move.class));
        }
    }

    void notifyGameStarted(){
        showMessage(THE_GAME_IS_STARTED);
    }

    void notifyPermissionsChanged(){
        //no behaviour in common between CLI and GUI
    }

    void notifyGameVariablesChanged(){
        //no behaviour in common between CLI and GUI
    }



    // MESSAGES HANDLING

    void sendMessage(Message m){
        try {
            this.client.sendMessage(m);
        } catch (NetworkingException e) {
            errorMessage(ERROR_SENDING_MESSAGE.concat(m.toString()));
            //TODO: check if this must be removed in production
        } catch (NullPointerException ex){
            errorMessage(MUST_CONNECT);
        }
    }

    private void receiveMessage(Message m){

        if( state==ViewState.INACTIVE ){
            parseMessageOnInactiveState(m);
        } else {
            parseMessageOnActiveState(m);
        }
    }

    private void parseMessageOnInactiveState(Message m){

        ViewBoundMessageType type = (ViewBoundMessageType) m.getType();

        switch (type) {
            case BACK_TO_GAME:
                handleBackToGameEvent();
                break;
            case GAME_ENDED:
                handleGameEndedEvent();
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
                handleErrorEvent(m);
                break;
            case ACKNOWLEDGMENT_MESSAGE:
                handleAcknowledgmentEvent(m);
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
                handleGameEndedEvent();
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
            case PLAYER_ADDED_TO_WR:
                handlePlayerAddedToWREvent(m);
                break;
            case PLAYER_REMOVED_FROM_WR:
                handlePlayerRemovedFromWREvent(m);
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



    //UTILS

    abstract void showMessage(String message);

    abstract void errorMessage(String message);

    void connectToRemoteServer(ConnectionType type, String serverName, int port){

        if(client==null){ //client is effectively final
            this.client = new Client(type,serverName,port,this, false);
        }
    }

    private void changeStateTo(ViewState state){

        if(state==ViewState.INACTIVE){
            setPermissions(EnumSet.of(Move.BACK_GAME));
        } else {
            setPermissions(EnumSet.noneOf(Move.class));
        }

        this.state = state;
    }

    @Override
    public boolean update(Message m) {
        receiveMessage(m);
        return true;
    }



    // GETTERS

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



    // SETTERS

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
}
