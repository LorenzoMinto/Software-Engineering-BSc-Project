package it.polimi.se2018.view;


import it.polimi.se2018.controller.RankingRecord;
import it.polimi.se2018.model.*;
import it.polimi.se2018.networking.*;
import it.polimi.se2018.utils.*;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.utils.Message;

import java.util.*;

import static it.polimi.se2018.utils.ViewBoundMessageType.PING;

/**
 * This is the View class of the MVC paradigm.
 * It's an abstract class because a lot of actions depends strongly on implementation.
 * Just the basic code is inserted here. Every method is then re-implemented or extended by
 * actual implementations (CLI and GUI).
 * 
 * @author Federico Haag
 */
public abstract class View implements Observer {

    /*  CONSTANTS FOR CONSOLE MESSAGES
        Following constants are not commented one by one because they are as self explaining as needed.
        Major information can be found looking for their usage.
        Being private, they are used only in this file. So if a change is needed, just look for usages in this file.
    */
    private static final String THE_GAME_IS_ENDED = "Game ended";
    private static final String WINDOW_PATTERNS_RECEIVED = "Received window patterns to choose from";
    private static final String YOU_HAVE_JOINED_THE_WAITING_ROOM = "You have joined the waiting room. Currently these are the waiting players: ";
    private static final String REMOVED_FROM_GAME = "You were successfully disconnected from the game";
    private static final String A_PLAYER_BECAME_INACTIVE = " has become inactive. Their turns will be skipped.";
    private static final String BACK_TO_GAME = "Welcome back";
    private static final String YOU_ARE_NOW_INACTIVE = "You were disconnected from the game for inactivity. Your turns will be skipped.";
    private static final String FAILED_SETUP_GAME = "Initial game setup failed. You could face crucial issues playing.";
    private static final String FAILED_SETUP_ROUND = "New round setup failed. You could face crucial issues playing.";
    private static final String FAILED_SETUP_TURN = "New turn setup failed. You could face crucial issues playing.";
    private static final String WINDOW_PATTERN_UPDATED = " update window pattern";
    private static final String ITS_YOUR_TURN = "It's your turn!";
    private static final String ERROR_MOVE = "An unexpected error wouldn't let you perform the move. Try again.";
    private static final String MAX_PLAYERS_ERROR = "You can't join the game as there is already the maximum number of players in the game.";
    private static final String NICKNAME_ALREADY_USED_ERROR = "You can't join the game as there already is a player with this nickname.";
    private static final String ALREADY_PLAYING_ERROR = "You can't join the game as it is not running.";
    private static final String USE_TOOL_CARD = " uses the toolCard ";
    private static final String YOU_HAVE_DRAFTED = "Was drafted ";
    private static final String JOINS_THE_WAITING_ROOM = " joins the waiting room";
    private static final String LEAVES_THE_WAITING_ROOM = " leaves the waiting room";
    private static final String ROUND_NOW_STARTS = "# Round now starts!";
    private static final String NOW_ITS_TURN_OF = "Now it's the turn of ";
    private static final String THE_GAME_IS_STARTED = "The game started!";
    private static final String PROBLEMS_WITH_CONNECTION = "There are some problems with connection. Check if it depends on you, if not wait or restart game.";
    private static final String CONNECTION_RESTORED = "Connection restored!";
    private static final String THE_ACTION_YOU_JUST_PERFORMED_WAS_NOT_VALID = "The action you just performed was not valid.";
    private static final String DISCONNECTED_DUE_TO_CONNECTION_PROBLEMS = " disconnected due to connection problems.";
    private static final String RECONNECTED_DUE_TO_FIXING_OF_CONNECTION_PROBLEMS = " reconnected due to fixing of connection problems.";
    private static final String TRACK_HAS_NOW_NEW_DICES = "Track has now new dices";
    private static final String THERE_IS_NO_MORE_DRAFTED_DICE = "There is no more drafted dice";
    private static final String GAME_WAS_ABORTED = "Game was aborted";
    private static final String QUITTED_THE_GAME = " quitted the game";

    /*  CONSTANTS FOR MESSAGES PARAMS
        Following constants are not commented one by one because they are as self explaining as needed.
        Major information can be found looking for their usage.
        Being private, they are used only in this file. So if a change is needed, just look for usages in this file.
     */
    //Note: this strings are strictly connected with the ones used in Controller and Model. DO NOT CHANGE THEM!
    private static final String PARAM_PLAYER = "player";
    private static final String PARAM_MESSAGE = "message";
    private static final String PARAM_DRAWN_TOOL_CARDS = "drawnToolCards";
    private static final String PARAM_DRAWN_PUBLIC_OBJECTIVE_CARDS = "drawnPublicObjectiveCards";
    private static final String PARAM_PLAYERS = "players";
    private static final String PARAM_TRACK = "track";
    private static final String PARAM_DRAFT_POOL_DICES = "draftPoolDices";
    private static final String PARAM_WINDOW_PATTERNS = "windowPatterns";
    private static final String PARAM_PRIVATE_OBJECTIVE_CARD = "privateObjectiveCard";
    private static final String PARAM_WHO_IS_PLAYING = "whoIsPlaying";
    private static final String PARAM_WINNER_PLAYER_ID = "winnerPlayerID";
    private static final String PARAM_RANKINGS = "rankings";
    private static final String PARAM_GLOBAL_RANKINGS = "globalRankings";
    private static final String PARAM_CURRENT_PLAYER = "currentPlayer";
    private static final String PARAM_WINDOW_PATTERN = "windowPattern";
    private static final String PARAM_TOOL_CARD = "toolCard";
    private static final String PARAM_TOOL_CARDS = "toolCards";
    private static final String PARAM_DRAFTED_DICE = "draftedDice";
    private static final String PARAM_PLAYERS_FAVOUR_TOKENS = "favourTokens";
    private static final String PARAM_MOVE = "move";
    private static final String PARAM_NICKNAME = "nickname";


    // CONSTANTS USED AS MESSAGE OF EXCEPTIONS
    private static final String CANT_TAKE_PERMISSIONS_IF_STORED_PERMISSIONS_SET_IS_NULL = "Can't take permissions if set is null";

    // CONFIGURATION INFORMATION

    /**
     * Set of moves that the player can do with this view
     */
    private EnumSet<Move> permissions = EnumSet.of(Move.JOIN);

    /**
     * Permissions before connection lost
     */
    private EnumSet<Move> storedPermissions = null;

    /**
     * State of the view: becomes "inactive" when player is marked from server as "inactive"
     */
    private ViewState state = ViewState.ACTIVE;

    /**
     * The client that handles communication of this view
     */
    private ClientInterface client;

    /**
     * List of messages that are being handled
     */
    Message handlingMessage = null;

    /**
     * List of game moves
     */
    List<Move> historyMove = new ArrayList<>();

    /**
     * Reference to last move made params
     */
    HashMap<String, Object> lastMoveParams = null;

    /**
     * Reference to last move made
     */
    Move lastMove = null;

    /**
     * Reference to last move made player
     */
    String lastMovePlayer = null;

    // COPIES OF GAME INFORMATION TO GRAPHICALLY REPRESENT THEM

    /**
     * The ID of the player of this view
     */
    private String playerID;

    /**
     * The ID of the winning player of this game
     */
    private String winnerID;

    /**
     * Number of the current round
     */
    private int roundNumber;

    /**
     * ToolCards that have been distributed at the beginning of the game
     */
    List<ToolCard> drawnToolCards;

    /**
     * Public Objective Cards that have been distributed at the beginning of the game
     */
    List<PublicObjectiveCard> drawnPublicObjectiveCards;

    /**
     * Window Patterns that were given at the beginning of the game to the view's player
     * to choose one of them.
     */
    List<WindowPattern> drawnWindowPatterns;
    
    /**
     * List of players, ordered statically.
     */
    List<String> players;

    /**
     * List of players in waiting room.
     */
    List<String> waitingRoomPlayers = new ArrayList<>();

    /**
     * List of the players' favour tokens, order corresponds to player's list.
     */
    List<Integer> playersFavourTokens;

    /**
     * Track
     */
    Track track;

    /**
     * Dices currently contained in draft pool
     */
    List<Dice> draftPoolDices;

    /**
     * ID of the current playing player
     */
    String playingPlayerID;

    /**
     * Reference to the dice that has been drafted and it has not been placed somewhere yet
     */
    Dice draftedDice;

    /**
     * Window Pattern of the view's player
     */
    WindowPattern windowPattern;

    /**
     * Window Patterns of players.
     */
    List<WindowPattern> windowPatterns;

    /**
     * The private objective card that has been given to the player at the beginning of the game
     */
    PrivateObjectiveCard privateObjectiveCard;

    /**
     * Final rankings of the game
     */
    List<RankingRecord> rankings;

    /**
     * Global rankings of the game
     */
    List<RankingRecord> globalRankings;

    /**
     * True if view was in "INACTIVE" state before loosing connection
     */
    private boolean wasInactiveBeforeConnectionDrop = false;

    // HANDLING OF MOVES (PERFORMED BY THE VIEW'S PLAYER)

    /**
     * Handles the move "Return dice to draft pool move"
     */
    void handleReturnDiceToDraftpoolMove(){
        try {
            notifyGame(new Message(ControllerBoundMessageType.MOVE,Message.fastMap(PARAM_MOVE,Move.RETURN_DICE_TO_DRAFTPOOL)));
        } catch (NetworkingException e) {
            showInformation(e.getMessage());
        }
    }

    /**
     * Handles the move "Leave Waiting Room"
     */
    void handleLeaveWaitingRoomMove(){
        try {
            HashMap<String,Object> params = new HashMap<>();
            params.put(PARAM_MOVE,Move.LEAVE);
            params.put(PARAM_NICKNAME,this.playerID);
            notifyGame(new Message(ControllerBoundMessageType.MOVE,params));
        } catch (NetworkingException e) {
            showInformation(e.getMessage());
        }
    }

    /**
     * Handles the move "Quit"
     */
    void handleQuitMove(){
        changeStateTo(ViewState.DISCONNECTED);
        try {
            notifyGame(new Message(ControllerBoundMessageType.MOVE,Message.fastMap(PARAM_MOVE,Move.QUIT)));
        } catch (NetworkingException e) {
            showInformation(e.getMessage());
        }
    }

    /**
     * Handles the move "Back to game"
     */
    void handleBackGameMove(){
        try {
            notifyGame(new Message(ControllerBoundMessageType.MOVE,Message.fastMap(PARAM_MOVE,Move.BACK_GAME),this.playerID));
        } catch (NetworkingException e) {
            showInformation(e.getMessage());
        }
    }

    /**
     * Handles the move "End turn"
     */
    void handleEndTurnMove(){
        try {
            notifyGame(new Message(ControllerBoundMessageType.MOVE,Message.fastMap(PARAM_MOVE,Move.END_TURN),this.playerID));
        } catch (NetworkingException e) {
            showInformation(e.getMessage());
        }
    }

    /**
     * Handles the move "Drafted dice from Drat Pool"
     */
    void handleDraftDiceFromDraftPoolMove(){
        //no behaviour in common between CLI and GUI
    }

    /**
     * Handles the move "Place dice on window pattern"
     */
    void handlePlaceDiceOnWindowPatternMove(){
        //no behaviour in common between CLI and GUI
    }

    /**
     * Handles the move "Use tool card"
     */
    void handleUseToolCardMove(){
        //no behaviour in common between CLI and GUI
    }

    /**
     * Handles the move "Increment drafted dice"
     */
    void handleIncrementDraftedDiceMove(){
        try {
            notifyGame(new Message(ControllerBoundMessageType.MOVE,Message.fastMap(PARAM_MOVE,Move.INCREMENT_DRAFTED_DICE)));
        } catch (NetworkingException e) {
            showInformation(e.getMessage());
        }
    }

    /**
     * Handles the move "Decrement drafted dice"
     */
    void handleDecrementDraftedDiceMove(){
        try {
            notifyGame(new Message(ControllerBoundMessageType.MOVE,Message.fastMap(PARAM_MOVE,Move.DECREMENT_DRAFTED_DICE)));
        } catch (NetworkingException e) {
            showInformation(e.getMessage());
        }
    }

    /**
     * Handles the move "End effect"
     */
    void handleEndEffectMove(){
        try {
            notifyGame(new Message(ControllerBoundMessageType.MOVE,Message.fastMap(PARAM_MOVE,Move.END_EFFECT)));
        } catch (NetworkingException e) {
            showInformation(e.getMessage());
        }
    }

    /**
     * Handles the move "Change drafted dice value"
     */
    void handleChangeDraftedDiceValueMove(){
        //no behaviour in common between CLI and GUI
    }

    /**
     * Handles the move "Choose dice from track"
     */
    void handleChooseDiceFromTrackMove(){
        //no behaviour in common between CLI and GUI
    }

    /**
     * Handles the move "Move Dice"
     */
    void handleMoveDiceMove(){
        //no behaviour in common between CLI and GUI
    }

    /**
     * Handles the move "Join game"
     */
    void handleJoinGameMove(){
        //no behaviour in common between CLI and GUI
    }


    /*  HANDLING OF EVENTS. EVENTS ARE BASICALLY MESSAGES RECEIVED FROM SERVER.
        Some of the following methods are private because they are not extended or overridden by CLI and GUI.
     */

    /**
     * Handles an Acknowledgment
     * @param m message containing the acknowledgment
     */
    private void handleAcknowledgmentEvent(Message m){
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

    /**
     * Handles the event "Inactive Player" that happens when another player becomes inactive
     * @param m the message relative to this event
     */
    private void handleInactivePlayerEvent(Message m){
        Object o;
        try {
            o = m.getParam(PARAM_PLAYER);
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        String pID = (String) o;

        showInformation(pID.concat(A_PLAYER_BECAME_INACTIVE));
    }

    /**
     * Handles the event "Game ended"
     */
    void handleGameEndedEvent(){
        showInformation(THE_GAME_IS_ENDED);
    }

    /**
     * Handles the event "A player quitted"
     * @param m the message containing information about the error
     */
    private void handleAPlayerQuittedEvent(Message m){
        Object o;
        try {
            o = m.getParam(PARAM_NICKNAME);
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        String nickname = (String) o;
        showInformation(nickname+ QUITTED_THE_GAME);
    }

    /**
     * Handles Error Messages sent from server
     * @param m the message containing information about the error
     */
    private void handleErrorEvent(Message m){
        Object o;
        try {
            o = m.getParam(PARAM_MESSAGE);
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        String err = (String) o;

        if(!err.equals("")){
            showError(err);
        } else {
            showError(THE_ACTION_YOU_JUST_PERFORMED_WAS_NOT_VALID);
        }
    }

    /**
     * Handles the event "New Round"
     * @param m the message containing new round information
     */
    private void handleNewRoundEvent(Message m) {
        Object o;
        try {
            o = m.getParam("number");
        } catch (NoSuchParamInMessageException e) {
            showInformation(FAILED_SETUP_ROUND);
            return;
        }
        @SuppressWarnings("unchecked")
        int number = (int) o;

        try {
            o = m.getParam(PARAM_DRAFT_POOL_DICES);
        } catch (NoSuchParamInMessageException e) {
            showInformation(FAILED_SETUP_ROUND);
            return;
        }
        @SuppressWarnings("unchecked")
        List<Dice> mDraftPoolDices = (List<Dice>) o;

        setRoundNumber(number);
        setDraftPoolDices(mDraftPoolDices);

        notifyNewRound();
    }

    /**
     * Handles the event "New Turn"
     * @param m the message containing the new turn information
     */
    private void handleNewTurnEvent(Message m) {
        Object o;

        try {
            o = m.getParam(PARAM_WHO_IS_PLAYING);
        } catch (NoSuchParamInMessageException e) {
            showInformation(FAILED_SETUP_TURN);
            return;
        }
        @SuppressWarnings("unchecked")
        String whoIsPlaying = (String) o;

        setPlayingPlayerID(whoIsPlaying);

        notifyNewTurn();
    }

    /**
     * Handles the event "A player disconnected"
     * @param m the message containing the playerID
     */
    private void handleAPlayerDisconnectedEvent(Message m){
        Object o;
        try {
            o = m.getParam("playerID");
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        String mPlayerID = (String)o;
        showInformation(mPlayerID + DISCONNECTED_DUE_TO_CONNECTION_PROBLEMS);
    }

    /**
     * Handles the event "A player disconnected"
     * @param m the message containing the playerID
     */
    private void handleAPlayerReconnectedEvent(Message m){
        Object o;
        try {
            o = m.getParam("playerID");
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        String mPlayerID = (String)o;
        showInformation(mPlayerID + RECONNECTED_DUE_TO_FIXING_OF_CONNECTION_PROBLEMS);
    }


    //Following methods are extended or overridden by CLI and/or GUI

    /**
     * Handles the event "Connection Lost"
     */
    @SuppressWarnings("unused")
    void handleConnectionLostEvent(Message m){
        showInformation(PROBLEMS_WITH_CONNECTION);
        this.wasInactiveBeforeConnectionDrop = this.state == ViewState.INACTIVE;
        changeStateTo(ViewState.DISCONNECTED);
    }

    void handleAbortedEvent(){
        showInformation(GAME_WAS_ABORTED);
    }

    /**
     * Handles the event "Connection Restored"
     */
    void handleConnectionRestoredEvent(){
        showInformation(CONNECTION_RESTORED);
        changeStateTo( (this.wasInactiveBeforeConnectionDrop) ? ViewState.INACTIVE : ViewState.ACTIVE );

        //Handles again message that did not ended handling due to connection drop
        if(this.handlingMessage!=null){
            handleReceivedMessage(this.handlingMessage);
        }
    }

    /**
     * Handles the event "Give Window Pattern"
     */
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
        showInformation(WINDOW_PATTERNS_RECEIVED);

        try {
            o = m.getParam(PARAM_PRIVATE_OBJECTIVE_CARD);
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        PrivateObjectiveCard pobj = (PrivateObjectiveCard) o;
        setPrivateObjectiveCard(pobj);
    }

    /**
     * Handles the event "Added to the waiting room"
     */
    void handleAddedEvent(Message m){
        Object o;
        try {
            o = m.getParam(PARAM_PLAYERS);
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        List<String> playersList = (ArrayList<String>) o;

        waitingRoomPlayers = playersList;

        String msg = "";
        int index = 0;
        for(String player : playersList){
            if(index>0){
                msg = msg.concat(", ");
            }
            msg = msg.concat(player);
            index++;
        }

        showInformation(YOU_HAVE_JOINED_THE_WAITING_ROOM + msg);
    }

    /**
     * Handles the event "Removed from the waiting room"
     */
    @SuppressWarnings("unused")
    void handleRemovedEvent(Message m){
        showInformation(REMOVED_FROM_GAME);
        waitingRoomPlayers = new ArrayList<>();
    }

    /**
     * Handles the printing of the acknowledgment message
     * @param text the text to be printed
     */
    void ack(String text){
        if(!text.equals("")){
            showInformation(text);
        }
    }

    /**
     * Handles the event "Back to game"
     */
    @SuppressWarnings("unused")
    void handleBackToGameEvent(Message m){
        changeStateTo(ViewState.ACTIVE);
        showInformation(BACK_TO_GAME);
    }

    /**
     * Handles the event "You are inactive"
     */
    @SuppressWarnings("unused")
    void handleInactiveEvent(Message m){
        changeStateTo(ViewState.INACTIVE);
        showInformation(YOU_ARE_NOW_INACTIVE);
    }

    /**
     * Handles the event "A player has been removed from the waiting room"
     * @param m the message containing the removed player information
     */
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
            showInformation(nickname + LEAVES_THE_WAITING_ROOM);
        }

        waitingRoomPlayers.remove(nickname);
    }

    /**
     * Handles the event "A player has been added to the waiting room"
     * @param m the message containing the added player information
     */
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
            showInformation(nickname+ JOINS_THE_WAITING_ROOM);
            waitingRoomPlayers.add(nickname);
        }
    }

    /**
     * Handles the event "Setup"
     * @param m the message containing setup information
     */
    void handleSetupEvent(Message m) {
        Object o;
        try {
            o = m.getParam(PARAM_DRAWN_TOOL_CARDS);
        } catch (NoSuchParamInMessageException e) {
            showInformation(FAILED_SETUP_GAME);
            return;
        }
        @SuppressWarnings("unchecked")
        List<ToolCard> mDrawnToolCards = (List<ToolCard>) o;

        try {
            o = m.getParam(PARAM_DRAWN_PUBLIC_OBJECTIVE_CARDS);
        } catch (NoSuchParamInMessageException e) {
            showInformation(FAILED_SETUP_GAME);
            return;
        }
        @SuppressWarnings("unchecked")
        List<PublicObjectiveCard> mDrawnPublicObjectiveCards = (List<PublicObjectiveCard>) o;

        try {
            o = m.getParam(PARAM_PLAYERS);
        } catch (NoSuchParamInMessageException e) {
            showInformation(FAILED_SETUP_GAME);
            return;
        }
        @SuppressWarnings("unchecked")
        List<String> mPlayers = (List<String>) o;

        try {
            o = m.getParam(PARAM_TRACK);
        } catch (NoSuchParamInMessageException e) {
            showInformation(FAILED_SETUP_GAME);
            return;
        }
        @SuppressWarnings("unchecked")
        Track mTrack = (Track) o;

        try {
            o = m.getParam(PARAM_DRAFT_POOL_DICES);
        } catch (NoSuchParamInMessageException e) {
            showInformation(FAILED_SETUP_GAME);
            return;
        }
        @SuppressWarnings("unchecked")
        List<Dice> mDraftPoolDices = (List<Dice>) o;

        try {
            o = m.getParam(PARAM_WINDOW_PATTERNS);
        } catch (NoSuchParamInMessageException e) {
            showInformation(FAILED_SETUP_GAME);
            return;
        }
        @SuppressWarnings("unchecked")
        List<WindowPattern> mWindowPatterns = (List<WindowPattern>) o;

        try {
            o = m.getParam(PARAM_PRIVATE_OBJECTIVE_CARD);
        } catch (NoSuchParamInMessageException e) {
            showInformation(FAILED_SETUP_GAME);
            return;
        }
        @SuppressWarnings("unchecked")
        PrivateObjectiveCard mPrivateObjectiveCard = (PrivateObjectiveCard) o;

        try {
            o = m.getParam(PARAM_PLAYERS_FAVOUR_TOKENS);
        } catch (NoSuchParamInMessageException e) {
            showInformation(FAILED_SETUP_GAME);
            return;
        }
        @SuppressWarnings("unchecked")
        List<Integer> mPlayersFavourTokens = (List<Integer>) o;

        //Assignments are done only at the end of parsing of all data to prevent partial update (due to errors)
        setDrawnToolCards(mDrawnToolCards);
        setDraftPoolDices(mDraftPoolDices);
        setDrawnPublicObjectiveCards(mDrawnPublicObjectiveCards);
        setPlayers(mPlayers);
        setTrack(mTrack);
        setPrivateObjectiveCard(mPrivateObjectiveCard);
        setWindowPatterns(mWindowPatterns);
        setPlayersFavourTokens(mPlayersFavourTokens);

        notifyGameStarted();

        setPermissions(EnumSet.noneOf(Move.class));
    }

    /**
     * Handles the event "Received rankings"
     * @param m the message containing rankings.
     */
    void handleRankingsEvent(Message m) {
        Object o;
        try {
            o = m.getParam(PARAM_WINNER_PLAYER_ID);
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        String mwinnerID = (String) o;

        try {
            o = m.getParam(PARAM_RANKINGS);
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        List<RankingRecord> receivedRankings = (List<RankingRecord>) o;

        try {
            o = m.getParam(PARAM_GLOBAL_RANKINGS);
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        List<RankingRecord> receivedGlobalRankings = (List<RankingRecord>) o;


        this.rankings = receivedRankings;
        this.globalRankings = receivedGlobalRankings;

        this.winnerID = mwinnerID;
    }

    /**
     * Handles the event "A Window Pattern has been updated"
     * @param m the message containing window pattern information
     */
    void handleUpdatedWindowPatternEvent(Message m) {
        Object o;
        try {
            o = m.getParam(PARAM_WINDOW_PATTERN);
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

        updateMyWindowPattern();

        showInformation(pID+WINDOW_PATTERN_UPDATED);
    }

    /**
     * Handles the event "Change Draft Pool"
     * @param m the message containing draftpool information
     */
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

    void handleChangedTrackEvent(Message m) {
        Object o;
        try {
            o = m.getParam(PARAM_TRACK);
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        Track mTrack = (Track)o;
        setTrack(mTrack);
        showInformation(TRACK_HAS_NOW_NEW_DICES);
    }

    /**
     * Handles the event "It is now your turn"
     */
    @SuppressWarnings("unused")
    void handleYourTurnEvent(Message m) {
        showInformation(ITS_YOUR_TURN);
    }

    /**
     * Handles the event "Bad Formatted". It is received when some previous message
     * sent to server was bad formatted or contained unexpected data.
     */
    @SuppressWarnings("unused")
    void handleBadFormattedEvent(Message m) {
        showInformation(ERROR_MOVE);
    }

    /**
     * Handles the event "Can't join waiting room because players limit has been reached"
     */
    @SuppressWarnings("unused")
    void handleDeniedLimitEvent(Message m) {
        showInformation(MAX_PLAYERS_ERROR);
    }

    /**
     * Handles the event "Can't join waiting room because your requested nickname is already used in this game"
     */
    @SuppressWarnings("unused")
    void handleDeniedNicknameEvent(Message m) {
        showInformation(NICKNAME_ALREADY_USED_ERROR);
    }

    /**
     * Handles the event "Can't join because game is already running"
     */
    @SuppressWarnings("unused")
    void handleDeniedPlayingEvent( Message m) {
        showInformation(ALREADY_PLAYING_ERROR);
    }

    /**
     * Handles the event "Used toolcard"
     * @param m the message containing toolcard information
     */
    void handleUsedToolCardEvent(Message m){
        Object o;
        try {
            o = m.getParam(PARAM_TOOL_CARD);
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        ToolCard toolCard = (ToolCard) o;

        try {
            o = m.getParam(PARAM_TOOL_CARDS);
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

        try {
            o = m.getParam(PARAM_PLAYERS_FAVOUR_TOKENS);
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        List<Integer> favourTokens = (List<Integer>) o;

        setDrawnToolCards(toolCards);
        setPlayersFavourTokens(favourTokens);

        showInformation(p+ USE_TOOL_CARD +toolCard.getTitle());
    }


    /**
     * Handles the event "Slot Of Track Chosen Dice"
     * @param m the message containing slot information
     */
    void handleSlotOfTrackChosenDiceEvent(Message m) {
        //no action is required by the view under this implementation of the game
    }

    /**
     * Handles the event "Track Chosen Dice"
     * @param m the message containing track information
     */
    void handleTrackChosenDiceEvent(Message m) {
        //no action is required by the view under this implementation of the game
    }

    /**
     * Handles the event "Drafted Dice"
     * @param m the message containing drafted dice information
     */
    void handleDraftedDiceEvent(Message m) {
        try {  //drafted dice changed to null update
            m.getParam("noDrafted");
            setDraftedDice(null);
            showInformation(THERE_IS_NO_MORE_DRAFTED_DICE);
            return;
        } catch (NoSuchParamInMessageException e) {
            //do nothing
        }
        Object o;
        try {
            o = m.getParam(PARAM_DRAFTED_DICE);
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        Dice mDraftedDice = (Dice) o;
        setDraftedDice(mDraftedDice);
        showInformation(YOU_HAVE_DRAFTED +mDraftedDice);
    }

    // NOTIFY METHODS

    /**
     * Notify classes that extends View (CLI and GUI) about the beginning of a new round
     */
    void notifyNewRound(){
        showInformation((this.roundNumber+1)+ ROUND_NOW_STARTS);
    }

    /**
     * Notify classes that extends View (CLI and GUI) about the beginning of a new turn
     */
    void notifyNewTurn(){
        if(!playingPlayerID.equals(playerID)){
            showInformation(NOW_ITS_TURN_OF + playingPlayerID);
            setPermissions(EnumSet.noneOf(Move.class));
        }
    }

    /**
     * Notify classes that extends View (CLI and GUI) about the beginning of the game
     */
    void notifyGameStarted(){
        showInformation(THE_GAME_IS_STARTED);
    }

    /**
     * Notify classes that extends View (CLI and GUI) that permissions has changed
     */
    void notifyPermissionsChanged(){
        //no behaviour in common between CLI and GUI
    }

    /**
     * Notify classes that extends View (CLI and GUI) that game variables changed
     */
    void notifyGameVariablesChanged(){
        //no behaviour in common between CLI and GUI
    }


    // MESSAGES HANDLING

    /**
     * Sends the given message to server
     * @param m the message to send to server
     */
    void notifyGame(Message m) throws NetworkingException{
        try {
            this.client.sendMessage(m);
        } catch (Exception ex){
            throw new NetworkingException(ex.getMessage());
        }
    }

    /**
     * Handles a message and calls relative handle method
     * @param m the message to handle
     */
    private void handleReceivedMessage(Message m){

        switch (state) {
            case INACTIVE:
                handleReceivedMessageOnInactiveState(m);
                break;
            case ACTIVE:
                handleReceivedMessageOnActiveState(m);
                break;
            case DISCONNECTED:
                handleReceivedMessageOnDisconnectedState(m);
                break;
            default:
                break;
        }
    }

    /**
     * Adds the given message to the handling messages list
     * @param message the message to be added
     */
    private void addHandlingMessage(Message message){
        EnumSet<ViewBoundMessageType> ignoreList = EnumSet.of(
                ViewBoundMessageType.CONNECTION_LOST,
                ViewBoundMessageType.CONNECTION_RESTORED,
                PING
        );
        ViewBoundMessageType type = (ViewBoundMessageType) message.getType();
        if(!ignoreList.contains(type)){
            this.handlingMessage = message;
        }
    }

    /**
     * Removes the given message to the handling messages list
     * @param message the message to be removed
     */
    void removeHandlingMessage(Message message){
        if(this.handlingMessage.equals(message)){
            this.handlingMessage = null;
        }
    }

    /**
     * Handles the received message if the current view state is "DISCONNECTED"
     * @param m the received message
     */
    private void handleReceivedMessageOnDisconnectedState(Message m){

        ViewBoundMessageType type = (ViewBoundMessageType) m.getType();

        if (type == ViewBoundMessageType.CONNECTION_RESTORED) {
            handleConnectionRestoredEvent();

        } else if( type == ViewBoundMessageType.A_PLAYER_QUITTED ){
                handleAPlayerQuittedEvent(m);

        }
        //No other messages are evaluated in this state
    }

    /**
     * Handles the received message if the current view state is "INACTIVE"
     * @param m the received message
     */
    private void handleReceivedMessageOnInactiveState(Message m){

        ViewBoundMessageType type = (ViewBoundMessageType) m.getType();

        switch (type) {
            case BACK_TO_GAME:
                handleBackToGameEvent(m);
                break;
            case GAME_ENDED:
                handleGameEndedEvent();
                break;
            case RANKINGS:
                handleRankingsEvent(m);
                break;
            case CONNECTION_LOST:
                handleConnectionLostEvent(m);
                break;
            case A_PLAYER_QUITTED:
                handleAPlayerQuittedEvent(m);
                break;
            default:
                //No other messages are evaluated in this state
                break;
        }
    }

    /**
     * Handles the received message if the current view state is "ACTIVE"
     * @param m the received message
     */
    private void handleReceivedMessageOnActiveState(Message m){

        ViewBoundMessageType type = (ViewBoundMessageType) m.getType();

        if(type!=ViewBoundMessageType.ERROR_MESSAGE){
            //UPDATE PERMISSIONS
            EnumSet<Move> p = (EnumSet<Move>) m.getPermissions();
            if(!p.isEmpty()){
                setPermissions(p);
            }//else keep same permissions
        }

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
                handleInactiveEvent(m);
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
            case SOMETHING_CHANGED_IN_TRACK:
                handleChangedTrackEvent(m);
                break;
            case DRAFTED_DICE:
                handleDraftedDiceEvent(m);
                break;
            case TRACK_CHOSEN_DICE:
                handleTrackChosenDiceEvent(m);
                break;
            case SLOT_OF_TRACK_CHOSEN_DICE:
                handleSlotOfTrackChosenDiceEvent(m);
                break;
            case IT_IS_YOUR_TURN: //needed just for setting permissions
                handleYourTurnEvent(m);
                break;
            case BAD_FORMATTED:
                handleBadFormattedEvent(m);
                break;
            case JOIN_WR_DENIED_PLAYING:
                handleDeniedPlayingEvent(m);
                break;
            case JOIN_WR_DENIED_NICKNAME:
                handleDeniedNicknameEvent(m);
                break;
            case JOIN_WR_DENIED_LIMIT:
                handleDeniedLimitEvent(m);
                break;
            case ADDED_TO_WR:
                handleAddedEvent(m);
                break;
            case REMOVED_FROM_WR:
                handleRemovedEvent(m);
                break;
            case PLAYER_ADDED_TO_WR:
                handlePlayerAddedToWREvent(m);
                break;
            case PLAYER_REMOVED_FROM_WR:
                handlePlayerRemovedFromWREvent(m);
                break;
            case CONNECTION_LOST:
                handleConnectionLostEvent(m);
                break;
            case A_PLAYER_DISCONNECTED:
                handleAPlayerDisconnectedEvent(m);
                break;
            case A_PLAYER_RECONNECTED:
                handleAPlayerReconnectedEvent(m);
                break;
            case ABORTED:
                handleAbortedEvent();
                break;
            case A_PLAYER_QUITTED:
                handleAPlayerQuittedEvent(m);
                break;
            case HISTORY:
                handleHistoryMessageEvent(m);
                break;
            default:
                //No other messages are evaluated in this state
                break;
        }
    }

    /**
     * Handles message of "history" type
     * @param m the received history message
     */
    void handleHistoryMessageEvent(Message m) {
        Object o;
        try {
            o = m.getParam(PARAM_PLAYER);
        } catch (NoSuchParamInMessageException e) {
            return;
        }
        @SuppressWarnings("unchecked")
        String playerMove = (String) o;

        try {
            o = m.getParam("oldParams");
        } catch (NoSuchParamInMessageException e) {
            return;
        }

        @SuppressWarnings("unchecked")
        HashMap<String, Object> params = (HashMap<String, Object>) o;

        o = params.get("move");
        @SuppressWarnings("unchecked")
        Move move = (Move) o;

        this.lastMove = move;
        this.lastMovePlayer = playerMove;
        this.lastMoveParams = params;
        this.historyMove.add(move);
    }


    //UTILS

    /**
     * Display a message to the user. Abstract, so it is implemented differently by CLI and GUI.
     * @param message the message to be displayed
     */
    abstract void showInformation(String message);

    /**
     * Display an error message to the user. Abstract, so it is implemented differently by CLI and GUI.
     * @param message the error message to be displayed
     */
    abstract void showError(String message);

    /**
     * Connects View to the server, creating a new Client instance.
     * @param type the connection's type
     * @param serverName the server's name
     * @param port the server's port to connect to
     */
    void connectToRemoteServer(ConnectionType type, String serverName, int port) throws NetworkingException {

        if(client==null){ //client is effectively final
            this.client = new Client(type,serverName,port,this, false);

        }
    }

    /**
     * Changes view's state to the one specified
     * @param state the state to be setted as current one
     */
    private void changeStateTo(ViewState state){
        if(state!=this.state) {
            switch (state) {
                case INACTIVE:
                    setPermissions(EnumSet.of(Move.BACK_GAME));
                    break;

                case ACTIVE:
                    if (this.state == ViewState.DISCONNECTED) {
                        setPermissions( takeOldPermissions() );
                    } else {
                        setPermissions(EnumSet.noneOf(Move.class));
                    }
                    break;

                case DISCONNECTED:
                    storePermissions();
                    setPermissions(EnumSet.noneOf(Move.class));
                    break;

                default:
                    break;
            }

            this.state = state;
        }
    }

    @Override
    public boolean update(Message m) {
        addHandlingMessage(m);
        handleReceivedMessage(m);

        return true;
    }


    // GETTERS

    /**
     * Returns the view's player's id
     * @return the view's player's id
     */
    public String getPlayerID() {
        return playerID;
    }

    /**
     * Returns the game's winner's id
     * @return the game's winner's id
     */
    String getWinnerID() {
        return winnerID;
    }

    /**
     * Returns the view's player's id
     * @return the view's player's id
     */
    Integer getPlayerFavourTokens() {
        return playersFavourTokens.get(players.indexOf(playerID));
    }

    /**
     * Returns the current permissions
     * @return the current permissions
     */
    public Set<Move> getPermissions() {
        return permissions;
    }

    /**
     * Returns old permissions
     * @return old permissions
     */
    private EnumSet<Move> takeOldPermissions(){
        if(this.storedPermissions==null){
            throw new BadBehaviourRuntimeException(CANT_TAKE_PERMISSIONS_IF_STORED_PERMISSIONS_SET_IS_NULL);
        } else {
            EnumSet<Move> temp = this.storedPermissions;
            this.storedPermissions = null;
            return temp;
        }
    }


    /*  SETTERS
        The following methods are not commented because they are self explaining
     */

    /**
     * @param playerID the player id
     * @see View#playerID
     */
    public void setPlayer(String playerID) {
        this.playerID = playerID;
    }

    /**
     * @param permissions set of permissions
     * @see View#permissions
     */
    public void setPermissions(Set<Move> permissions) {
        this.permissions = (EnumSet<Move>) permissions;
        notifyPermissionsChanged();
    }

    /**
     * Store current permissions in this.storedPermissions
     */
    private void storePermissions(){
        if(this.storedPermissions == null){
            this.storedPermissions = (EnumSet<Move>) getPermissions();
        }
    }

    /**
     * @param drawnToolCards  list of draw Tool cards
     * @see View#drawnToolCards
     */
    private void setDrawnToolCards(List<ToolCard> drawnToolCards) {
        this.drawnToolCards = drawnToolCards;
    }

    /**
     * @param drawnPublicObjectiveCards list of drawn Public Objective cards
     * @see View#drawnPublicObjectiveCards
     */
    private void setDrawnPublicObjectiveCards(List<PublicObjectiveCard> drawnPublicObjectiveCards) {
        this.drawnPublicObjectiveCards = drawnPublicObjectiveCards;
    }

    /**
     * @param players list of players
     * @see View#players
     */
    public void setPlayers(List<String> players) {
        this.players = players;
    }

    /**
     * @param track track
     * @see View#track
     */
    public void setTrack(Track track) {
        this.track = track;
    }

    /**
     * @param draftPoolDices dices in the draftpool
     * @see View#draftPoolDices
     */
    private void setDraftPoolDices(List<Dice> draftPoolDices) {
        this.draftPoolDices = draftPoolDices;
    }

    /**
     * @param roundNumber sequential number of round
     * @see View#roundNumber
     */
    private void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    /**
     * @param playingPlayerID id of the playing player
     * @see View#playingPlayerID
     */
    private void setPlayingPlayerID(String playingPlayerID) {
        this.playingPlayerID = playingPlayerID;
    }

    /**
     * @param draftedDice drafted dice
     * @see View#draftedDice
     */
    public void setDraftedDice(Dice draftedDice) {
        this.draftedDice = draftedDice;
    }

    /**
     * @param privateObjectiveCard Private Objective card
     * @see View#privateObjectiveCard
     */
    public void setPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) {
        this.privateObjectiveCard = privateObjectiveCard;
    }

    /**
     * @param playersFavourTokens favour tokens of players
     * @see View#playersFavourTokens
     */
    private void setPlayersFavourTokens(List<Integer> playersFavourTokens) {
        this.playersFavourTokens = playersFavourTokens;
    }

    /**
     * @param windowPatterns Window Patterns of players
     * @see View#windowPatterns
     */
    public void setWindowPatterns(List<WindowPattern> windowPatterns) {
        this.windowPatterns = windowPatterns;
        updateMyWindowPattern();
    }

    /**
     * Updates the windowpattern of the current user
     */
    private void updateMyWindowPattern(){
        this.windowPattern = windowPatterns.get(players.indexOf(this.playerID));
    }
}
