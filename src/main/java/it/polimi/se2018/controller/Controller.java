package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.*;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.view.View;

import java.util.*;
import java.util.logging.Logger;

/**
 * Class that represent the Controller according the MVC paradigm.
 * It contains the logic "in-the-large" while the logic "in-the-small" is
 * contained in Model classes.
 *
 * {@code Controller} receives messages from connected {@link View}(s) validating and, in case,
 * redirecting them to the connected {@link Game}.
 *
 * @author Lorenzo Minto
 * @author Jacopo Pio Gargano
 * @author Federico Haag
 *
 * @see Game
 * @see ControllerState
 * @see ControllerStateManager
 * @see ToolCard
 * @see ToolCardManager
 * @see DiceBag
 * @see ObjectiveCardManager
 * @see PlacementRule
 * @see WindowPatternManager
 */
public class Controller extends Observable {

    /**
     * String used as error message if controller receives a move message with an unknown move
     */
    private static final String UNRECOGNIZED_MOVE = "UnrecognizedMove";

    /**
     * Param name of messages of "move" type
     */
    private static final String PARAM_MOVE = "move";

    /**
     * Logger
     */
    private static final String STRING_PLAYER = "player";

    /**
     * Logger
     */
    private final Logger logger;

    /**
     * Variable that holds the game starting time in milliseconds
     */
    private long startTime;

    /**
     * The controlled {@link Game}
     */
    protected Game game;  //Reference to the Model

    /**
     * The current state of the {@link Controller}
     */
    protected ControllerState controllerState; //State pattern

    /**
     * Contains an instance of a {@link ControllerStateManager} that is the one
     * that creates the needed {@link ControllerState} during a change of state
     *
     * @see ControllerState
     */
    protected ControllerStateManager stateManager;

    /**
     * The {@link ToolCard} that is being used in the current {@link Turn}
     *
     * @see ToolCard
     */
    private ToolCard activeToolcard;

    /**
     * Contains an istance of a {@link DiceBag} in order to create the
     * needed dices for the game
     *
     * @see Dice
     */
    protected DiceBag diceBag;

    /**
     * The current Placement Rule that is used in the current {@link Turn}
     * in order to decide if a placement move is allowed or not
     *
     * @see PlaceControllerState that is the class handling the placement move
     * @see ToolCard that is the class that could change the placement rule
     * @see WindowPattern that is the final involved class
     */
    protected PlacementRule placementRule;

    /**
     * Contains an instance of a {@link ObjectiveCardManager} that is the one
     * that creates {@link PublicObjectiveCard}(s) and {@link PrivateObjectiveCard} (s)
     *
     * @see PublicObjectiveCard
     * @see PrivateObjectiveCard
     */
    private ObjectiveCardManager objectiveCardManager;

    /**
     * Contains an instance of a {@link WindowPatternManager} that is the one
     * that creates the {@link WindowPattern}(s) for player(s)
     *
     * @see WindowPattern
     */
    private WindowPatternManager windowPatternManager;

    /**
     * Configuration properties loaded from config file
     */
    private final Properties properties;

    /**
     * Timer used to start game after a while after it's asked to players to choose window pattern
     */
    private static final Timer TIMER = new Timer();

    /**
     * Timer waiting for all players choosing theirs patterns
     */
    private TimerTask waitingForPatternsChoice;

    /**
     * Timer waiting for the current player to perform his/her move
     */
    private TimerTask waitingForPlayerMove;

    /**
     * Map that contains the list of window pattern given to each player
     */
    private HashMap<String,List<WindowPattern>> assignedWindowPatterns = new HashMap<>();

    /**
     *  Counter used to count the number of dice moves done by the current player
     */
    int movesCounter = 0;

    /**
     * Set of inactive players
     */
    private HashSet<String> inactivePlayers = new HashSet<>();

    /**
     * Set of players that are disconnected due to connection lost and before that they were inactive
     */
    private HashSet<String> inactiveDisconnectedPlayers = new HashSet<>();

    /**
     * Persistency singleton that gives access to all persisted information.
     */
    private Persistency persistency;

    /**
     * Just for testing
     * @param game the game instance to be controlled
     * @param properties dictionary of parameters loaded from config file
     */
    public Controller(Game game, Properties properties){
        this(game,properties,Logger.getLogger("TestLogger"));
    }

    /**
     * Construct a controller with reference to a game instance
     * and all manager and factory classes needed to the correct
     * working of the game
     *
     * @param game the game instance to be controlled
     * @param properties dictionary of parameters loaded from config file
     * @param logger the logger instance sent from server
     */
    public Controller(Game game, Properties properties, Logger logger) {
        this.logger = logger;
        this.properties = properties;

        int numberOfDicesPerColor = Integer.parseInt( properties.getProperty("numberOfDicesPerColor") );
        int numberOfToolCards = Integer.parseInt( properties.getProperty("numberOfToolCards") );
        int numberOfPublicObjectiveCards =  Integer.parseInt( properties.getProperty("numberOfPublicObjectiveCards") );

        String persistencyPath = properties.getProperty("persistencyPath");

        //Create Managers
        this.stateManager = new ControllerStateManager(this);

        this.windowPatternManager = new WindowPatternManager();

        /*
          Contains an instance of a ToolCardManager that is the one
          that creates the ToolCard(s) to be assigned to the Game
        */
        ToolCardManager toolCardManager = new ToolCardManager(this.getDefaultPlacementRule());

        this.objectiveCardManager = new ObjectiveCardManager();

        //Set main attributes
        this.game = game;
        this.controllerState =  this.stateManager.getStartState();
        this.activeToolcard = null;
        this.diceBag = new DiceBag(numberOfDicesPerColor);

        //Produces and sets cards to the game
        List<PublicObjectiveCard> publicObjectiveCards = objectiveCardManager.getPublicObjectiveCards(numberOfPublicObjectiveCards);
        List<ToolCard> toolCards = toolCardManager.getRandomToolCards(numberOfToolCards);


        this.game.setCards(toolCards,publicObjectiveCards);

        this.persistency = new Persistency(persistencyPath);
        this.persistency.loadRankings();
    }

    /**
     * Set as current state the one passed as method's argument
     *
     * @param controllerState state that must be set to the controller
     */
    protected void setControllerState(ControllerState controllerState) {
        this.controllerState = controllerState;
        this.controllerState.executeImplicitBehaviour(); //WARNING: could change controllerState implicitly
    }

    /**
     * @return a Message of type ViewBound of BAD_FORMATTED type
     */
    private Message errorMessage(){
        return new Message(ViewBoundMessageType.BAD_FORMATTED);
    }

    /**
     * @param text the error message
     * @return * @return a Message of type ViewBound of ERROR_MESSAGE type
     */
    private Message errorMessage(String text){
        return new Message(ViewBoundMessageType.ERROR_MESSAGE,text);
    }

    /**
     * For each different move, it executes the relative operations
     *
     * @param message the Message with the parameters to be analyzed and processed
     * @return an ACKNOWLEDGMENT_MESSAGE or an error message
     */
    public Message handleMoveMessage(Message message) {
        switch(game.getStatus()){
            case WAITING_FOR_PATTERNS_CHOICE:
                return handleMoveInWaitingForPatternsChoiceStatus(message);

            case PLAYING:
                return handleMoveInPlayingStatus(message);

            default:
                return new Message(ViewBoundMessageType.ERROR_MESSAGE);
        }
    }

    /**
     * For each different move, it executes the relative operations.
     * Assumes that game status is "playing"
     * @param message the Message with the parameters to be analyzed and processed
     * @return an ACKNOWLEDGMENT_MESSAGE or an error message
     */
    private Message handleMoveInPlayingStatus(Message message){
        String sendingPlayerID = message.getPlayerID(); //In this case, playerID is the sending player ID

        if (game.isCurrentPlayer(sendingPlayerID)) {

            Message returnMessage = handleMoveForCurrentPlayer(message);

            //Update permissions
            if (returnMessage.getType() == ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE) {
                returnMessage.setPermissions(controllerState.getStatePermissions());
                HashMap<String,Object> params = new HashMap<>();
                params.put("oldParams", message.getParams());
                params.put(STRING_PLAYER, sendingPlayerID);
                Message historyMessage = new Message(ViewBoundMessageType.HISTORY, params);
                notify(historyMessage);
            }
            return returnMessage;

        } else {
            if (message.isMove(Move.BACK_GAME)) {
                //this cause that the next turn of this player will not be skipped and player will be notified
                inactivePlayers.remove(sendingPlayerID);

                logger.info(() -> "Received BACK_GAMING message from: " + sendingPlayerID);

                return new Message(ViewBoundMessageType.BACK_TO_GAME);

            } else if(message.isMove(Move.QUIT)) {

                inactivePlayers.add(sendingPlayerID);
                notifyPlayerQuitted(sendingPlayerID);
                checkIfGameCanContinue();
                return null;

            } else {
                return new Message(ViewBoundMessageType.ERROR_MESSAGE, "Not your turn!");
            }
        }
    }

    private void notifyPlayerQuitted(String playerID){
        notify(new Message(ViewBoundMessageType.A_PLAYER_QUITTED,Message.fastMap("nickname",playerID)));
    }

    /**
     * For each different move, it executes the relative operations.
     * Assumes that game status is "playing" and the sender of the message is the current player
     * @param message the Message with the parameters to be analyzed and processed
     * @return an ACKNOWLEDGMENT_MESSAGE or an error message
     */
    private Message handleMoveForCurrentPlayer(Message message){
        Move move;

        try {
            move = (Move) message.getParam(PARAM_MOVE);
        } catch (NoSuchParamInMessageException e) {
            throw new BadBehaviourRuntimeException();
        }

        switch (move) {
            case END_TURN:
                return controllerState.endCurrentTurn();

            case DRAFT_DICE_FROM_DRAFTPOOL:
                Dice dice;
                try {
                    dice = (Dice) message.getParam("dice");
                } catch (NoSuchParamInMessageException e) {
                    return errorMessage();
                }
                return controllerState.draftDiceFromDraftPool(dice);

            case PLACE_DICE_ON_WINDOWPATTERN:
                int row;
                int col;
                try {
                    row = (int) message.getParam("row");
                    col = (int) message.getParam("col");
                } catch (NoSuchParamInMessageException e) {
                    return errorMessage();
                }
                return controllerState.placeDice(row, col);

            case USE_TOOLCARD:
                ToolCard toolCard;
                try {
                    toolCard = (ToolCard) message.getParam("toolCard");
                } catch (NoSuchParamInMessageException e) {
                    return errorMessage();
                }
                return controllerState.useToolCard(toolCard);

            case INCREMENT_DRAFTED_DICE:
                return controllerState.incrementDice();

            case DECREMENT_DRAFTED_DICE:
                return controllerState.decrementDice();

            case CHANGE_DRAFTED_DICE_VALUE:
                int value;
                try {
                    value = (int) message.getParam("value");
                } catch (NoSuchParamInMessageException e) {
                    return errorMessage();
                }
                return controllerState.chooseDiceValue(value);

            case CHOOSE_DICE_FROM_TRACK:
                Dice trackDice;
                int trackSlotNumber;
                try {
                    trackDice = (Dice) message.getParam("dice");
                    trackSlotNumber = (int) message.getParam("slotNumber");
                } catch (NoSuchParamInMessageException e) {
                    return errorMessage();
                }
                return controllerState.chooseDiceFromTrack(trackDice,trackSlotNumber);

            case RETURN_DICE_TO_DRAFTPOOL:
                return controllerState.returnDiceToDraftPool();

            case MOVE_DICE:
                int rowFrom;
                int colFrom;
                int rowTo;
                int colTo;
                try {
                    rowFrom = (int) message.getParam("rowFrom");
                    colFrom = (int) message.getParam("colFrom");
                    rowTo = (int) message.getParam("rowTo");
                    colTo = (int) message.getParam("colTo");
                } catch (NoSuchParamInMessageException e){
                    return errorMessage();
                }
                return controllerState.moveDice(rowFrom, colFrom, rowTo, colTo);

            case END_EFFECT:
                return controllerState.endToolCardEffect();

            case JOIN:
                //Never enter this case because it is handled before in Server
                return errorMessage();

            case BACK_GAME:
                //Never enter this case because it is handled before this method's call in handleMoveInPlayingStatus()
                return errorMessage();

            case LEAVE:
                //Never enter this case because it is handled before in Server
                return errorMessage();

            case QUIT:
                inactivePlayers.add(message.getPlayerID());
                notifyPlayerQuitted(message.getPlayerID());
                advanceGameDueToPlayerInactivity();
                return new Message(ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE,"You are successfully quitted");

            default:
                return errorMessage(UNRECOGNIZED_MOVE);
        }
    }

    /**
     * For each different move, it executes the relative operations.
     * Assumes that game status is "WaitingForPatternsChoice"
     * @param message the Message with the parameters to be analyzed and processed
     * @return an ACKNOWLEDGMENT_MESSAGE or an error message
     */
    private Message handleMoveInWaitingForPatternsChoiceStatus(Message message) {

        if(message.isMove(Move.CHOOSE_WINDOW_PATTERN)){
            String playerID = message.getPlayerID(); //In this case, playerID is the sending player ID
            WindowPattern wp;
            try {
                wp = (WindowPattern) message.getParam("windowPattern");
            } catch (NoSuchParamInMessageException e) {
                return errorMessage("Bad Formatted");
            }

            if( game.assignWindowPatternToPlayer(wp,playerID) ){

                if( checkIfAllPlayersChoseWP() ){
                    waitingForPatternsChoice.cancel();
                    startGame();
                    return null;

                } else {
                    return new Message(ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE,"WindowPattern assigned");
                }

            } else {
                return new Message(ViewBoundMessageType.ERROR_MESSAGE,"Player has already an assigned WindowPattern");
            }
        } else {
            return new Message(ViewBoundMessageType.ERROR_MESSAGE,"IllegalState");
        }
    }

    /**
     * Checks if all players chose a {@link WindowPattern}
     *
     * @return true if all players chose a {@link WindowPattern}
     */
    private boolean checkIfAllPlayersChoseWP() {
        boolean allPlayersHaveWindowPattern = true;
        for(Player p : game.getPlayers()){
            if(p.getWindowPattern()==null){
                allPlayersHaveWindowPattern = false;
                break;
            }
        }
        return allPlayersHaveWindowPattern;
    }

    /**
     * Sets the specified {@link ToolCard} as active. This means that it's being used
     * during the current {@link Turn}
     * @param toolCard the {@link ToolCard} that is being used
     * @return true if the operation was successful
     */
    protected boolean setActiveToolCard(ToolCard toolCard) {

        //If a player has already drafted a dice, then they can't use a ToolCard that needs drafting
        if(!toolCard.getTitle().equals("Running Pliers") && toolCard.needsDrafting() && game.getCurrentRound().getCurrentTurn().hasDraftedAndPlaced()){
            return false;
        }

        //check for card's timing constraints
        Boolean x = conformsToCardTiminingContraints(toolCard);
        if (!x) return x;

        Player currentPlayer = game.getCurrentRound().getCurrentTurn().getPlayer();
        if( currentPlayer.decreaseTokens(toolCard.getNeededTokens()) ){
            game.useToolCard(toolCard);
            this.activeToolcard = game.getToolCard(toolCard);
            if (currentPlayer.getWindowPattern().isEmpty()) {
                this.placementRule = new BorderPlacementRuleDecorator(this.activeToolcard.getPlacementRule());
            } else {
                this.placementRule = this.activeToolcard.getPlacementRule();
            }
            return true;
        } else {
            return false;
        }
    }

    private Boolean conformsToCardTiminingContraints(ToolCard toolCard) {
        switch (toolCard.getTitle()) {
            case "Glazing Hammer": //second turn before drafting only
                if (game.getCurrentRound().getCurrentTurn().hasDraftedAndPlaced() ||  //or is player's first turn in the round
                        game.getCurrentRound().getCurrentTurn().getNumber()/game.getPlayers().size()==0) {
                    return false;
                }
                break;
            case "Running Pliers": //first turn only
                if (game.getCurrentRound().getCurrentTurn().getNumber()/game.getPlayers().size()==1
                        || !game.getCurrentRound().getCurrentTurn().hasDraftedAndPlaced()) {
                    return false;
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Resets the current active toolCard and resets the placement rules to their default.
     *
     * @author Lorenzo Minto
     */
    protected void resetActiveToolCard() {

        this.movesCounter = 0;
        this.activeToolcard = null;
        this.placementRule = getDefaultPlacementRule();
    }

    /**
     * Gets the {@link ToolCard} that is being used in the current {@link Turn}
     *
     * @return the {@link ToolCard} that is being used in the current {@link Turn}
     */
    protected ToolCard getActiveToolCard() {

        return activeToolcard;
    }

    /**
     * Launches the {@link Game}
     *
     * @param nicknames the set of nicknames of the {@link Game}
     */
    public void launchGame(Set<String> nicknames){

        Player player;
        for(String nickname : nicknames){
            player = new Player(nickname,objectiveCardManager.getPrivateObjectiveCard());

            game.addPlayer(player);

            Set<WindowPattern> patterns = windowPatternManager.getPairsOfPatterns(getConfigProperty("amountOfCouplesOfPatternsPerPlayer"));

            assignedWindowPatterns.put(nickname,new ArrayList<>(patterns));

            HashMap<String,Object> params = new HashMap<>();
            params.put("windowPatterns",new ArrayList<>(patterns));
            params.put("privateObjectiveCard", player.getPrivateObjectiveCard());
            notify(new Message(ViewBoundMessageType.DISTRIBUTION_OF_WINDOW_PATTERNS,params,player.getID()));
        }

        game.setStatusAsWaitingForPatternsChoice();

        //Start the timer for patterns choice
        this.waitingForPatternsChoice = new TimerTask() {
            @Override
            public void run() {
                forcePatternChoice();
            }
        };
        TIMER.schedule(this.waitingForPatternsChoice,(long)(getConfigProperty("timeoutChoosingPatterns")*1000));
    }

    /**
     * Players who did not choose a pattern, receive one randomly
     */
    private void forcePatternChoice(){
        logger.info("waitingForPatternsChoice timer has expired. Setting windowPatterns automatically...");

        //Assign the first of the given windowPattern to players that did not choose
        for(Player p : game.getPlayers()){
            if(p.getWindowPattern()==null){
                p.setWindowPattern(assignedWindowPatterns.get(p.getID()).get(0));
            }
        }

        if( checkIfAllPlayersChoseWP() ){ startGame(); }
    }

    /**
     * Start the {@link Game} by setting the initial permissions, the placement rule and starts the player move timer
     */
    private void startGame(){
        startTime = System.currentTimeMillis();
        EnumSet<Move> permissions = EnumSet.of(Move.DRAFT_DICE_FROM_DRAFTPOOL, Move.USE_TOOLCARD, Move.END_TURN);

        game.startGame(getDicesForNewRound(),permissions);

        this.placementRule = new BorderPlacementRuleDecorator(
                new ColorPlacementRuleDecorator(
                        new ValuePlacementRuleDecorator(
                                new EmptyPlacementRule())));

        startPlayerMoveTimer();
    }

    /**
     * Resets the player move timer
     * @see Controller#startPlayerMoveTimer()
     */
    private void resetPlayerMoveTimer(){
        this.waitingForPlayerMove.cancel();
        startPlayerMoveTimer();
    }

    /**
     * Starts the player move timer
     */
    private void startPlayerMoveTimer(){
        this.waitingForPlayerMove = new TimerTask() {
            @Override
            public void run() {
                logger.info("waitingForPlayerMove timer has expired. Calling advanceGameDueToPlayerInactivity()...");
                handlePlayerInactivity();
            }
        };
        TIMER.schedule(waitingForPlayerMove,(long)(getConfigProperty("timeoutPlayerMove")*1000));
    }

    /**
     * Handles the becoming inactive of a player
     */
    private void handlePlayerInactivity(){
        String currentPlayerID = getCurrentPlayer().getID();

        //If statement prevents sending every turn the notification for all inactive players
        if( inactivePlayers.add(currentPlayerID) ){
            notify(new Message(ViewBoundMessageType.A_PLAYER_BECOME_INACTIVE,Message.fastMap(STRING_PLAYER,currentPlayerID))); //notify everyone that the player is now inactive
            notify(new Message(ViewBoundMessageType.YOU_ARE_INACTIVE,null,currentPlayerID)); //notify view of inactive player that it is now considered inactive
        }

        advanceGameDueToPlayerInactivity();
    }

    /**
     * Advances the game to the next turn, if available, due to player inactivity
     * @see Controller#advanceGame()
     */
    private void advanceGameDueToPlayerInactivity() {

        //Checks if due to players inactivity game can continuing or not
        if( checkIfGameCanContinue() ){
            advanceGame();
        }
    }

    private boolean checkIfGameCanContinue(){
        if( game.getPlayers().size() - inactivePlayers.size() < getConfigProperty("minNumberOfPlayers") ){
            try{
                game.forceEndGameDueToInactivity();
            } catch (IllegalStateException e){
                notify(new Message(ViewBoundMessageType.ABORTED));
                throw new BadBehaviourRuntimeException("Game was aborted due to premature disconnection of a player");
            }

            manageRankings();
            return false;
        }
        return true;
    }

    /**
     * Method used just to increase code readability
     * @return the current player
     */
    private Player getCurrentPlayer(){
        return game.getCurrentRound().getCurrentTurn().getPlayer();
    }

    /**
     * Let the game advance in the turns sequence.
     * If in the current {@link Round} have been played all the possible {@link Turn}s
     * it is created a new {@link Round}. If the current {@link Round} is the last one,
     * the game is ended calling {@link Controller#manageRankings()}.
     */
    protected void advanceGame() {

        resetActiveToolCard();

        //NOTE: aggiungere quì eventuale codice aggiuntivo di pulizia del turno precedente

        setControllerState(stateManager.getStartState());

        //Proceed with turns / rounds
        EnumSet<Move> permissions = EnumSet.of(Move.DRAFT_DICE_FROM_DRAFTPOOL, Move.USE_TOOLCARD, Move.END_TURN);
        try {
            game.nextTurn(permissions);
        } catch (NoMoreTurnsAvailableException e) {

            try {
                game.nextRound( getDicesForNewRound(), permissions );
            } catch (NoMoreRoundsAvailableException e1) {

                manageRankings();
                return;
            }
        }

        //if player's window pattern is empty
        if(getCurrentPlayer().getWindowPattern().isEmpty()){
            this.placementRule = new BorderPlacementRuleDecorator(
                    new ColorPlacementRuleDecorator(
                            new ValuePlacementRuleDecorator(
                                    new EmptyPlacementRule())));
        }

        resetPlayerMoveTimer();

        if( inactivePlayers.contains(getCurrentPlayer().getID()) ){
            /*

            advanceGame() è chiamato subito se il giocatore è inattivo.
            Questo serve per accelerare lo svolgimento del gioco. E' inutile che i giocatori attivi
            aspettino ogni volta lo scadere del timer del turno per ogni giocatore inattivo.
            I turni vengono saltati immediatamente; se il giocatore inattivo vuole riunirsi deve mandare
            un messaggio specifico (BACK_GAMING).
            Questo approccio è stato scelto per rispettare le specifiche di progetto.

             */

            advanceGame();

            /*

            NOTA SUI SIDE EFFECTS DI QUESTO APPROCCIO

            Dal punto di vista del MODEL, il giocatore ha ricevuto - per un attimo - il suo turno,
            per questo motivo il model invierà un messaggio a tale player notificando la possibilità
            di giocare. Tuttavia la view impedisce azioni a tale giocatore se in stato "inattivo" ignorando
            tale messaggio.

            Certamente una view malevola potrebbe ignorare tale blocco e provare a eseguire comune una mossa,
            sperando che il messaggio giunga al controller prima che questo abbia eseguito advanceGame() - evento
            altamente improbabile visto che si parla da un alto di velocità di rete e dall'altro di velocità
            di esecuzione di codice java sicrono su server.

            Tale eventualità però non costituisce una vulnerabilità, infatti nell'ipotesi di view malevola questa
            potrebbe benissimo evitare questa complessa operazione e semplicemente:
            o non diventare mai inattivo
            o inviare un messaggio di BACK_GAMING per rientrare in gioco dopo eventuale inattività.

            */
        }

    }

    /**
     * Gets the dices for a new {@link Round}
     *
     * @return the dices for a new {@link Round}
     */
    private List<Dice> getDicesForNewRound(){
        return diceBag.getDices( game.getPlayers().size()*2 + 1 );
    }

    /**
     * Ends the current game. Calculates rankings and scores and then notify them to players.
     */
    private void manageRankings(){
        LinkedHashMap<Player, Integer> rankings = (LinkedHashMap<Player,Integer>) getRankingsAndScores();
        game.setRankings(rankings);

        List<RankingRecord> localRanking = new ArrayList<>();

        int index = 0;

        for (Map.Entry<Player, Integer> entry : rankings.entrySet())
        {
            int timePlayedInMinute = (int) (System.currentTimeMillis() - startTime)/1000/60;
            Player player =  entry.getKey();
            int score = entry.getValue();
            //the first entry has won
            persistency.updateRankingForPlayerID(player.getID(), score, index == 0, timePlayedInMinute);

            //gets player's updated playing record to account for past games
            RankingRecord ranking = persistency.getRankingForPlayerID(player.getID());

            //this games' points are added separately to the player for the local ranking
            ranking.setPoints(score);

            localRanking.add(ranking);
            index++;
        }

        List<RankingRecord> globalRankings = persistency.getGlobalRankings();

        Map <String, Object> messageAttributes = new HashMap<>();
        messageAttributes.put("rankings", localRanking);
        messageAttributes.put("globalRankings", globalRankings);
        messageAttributes.put("winnerPlayerID", localRanking.get(0).getPlayerID());
        notify(new Message(ViewBoundMessageType.RANKINGS, messageAttributes, null, EnumSet.noneOf(Move.class)));

        //this is called to save the new updated rankings to memory
        persistency.persist();

        notify(new Message(ViewBoundMessageType.GAME_ENDED, null, null,EnumSet.noneOf(Move.class)));

        this.waitingForPlayerMove.cancel();
    }

    /**
     * Gets the rankings and scores of the current {@link Game}.
     * @return rankings and scores of the current {@link Game}
     */
    private Map<Player,Integer> getRankingsAndScores() {
        Set<PublicObjectiveCard> publicObjectiveCards = new HashSet<>(game.getDrawnPublicObjectiveCards());
        Set<Player> playersOfLastRound = game.getCurrentRound().getPlayersByReverseTurnOrder();
        return Scorer.getInstance().getRankings(playersOfLastRound, inactivePlayers, publicObjectiveCards);
    }

    /**
     * Gets the default {@link PlacementRule}
     *
     * @return the default {@link PlacementRule}
     */
    private PlacementRule getDefaultPlacementRule(){

        return new AdjacentValuePlacementRuleDecorator(
                new AdjacentDicePlacementRuleDecorator(
                        new AdjacentColorPlacementRuleDecorator(
                                new ColorPlacementRuleDecorator(
                                        new ValuePlacementRuleDecorator(
                                                new EmptyPlacementRule())))));

    }

    /**
     * Returns Dictionary of configuration's parameters
     *
     * @param p the configuration property to retrieve
     * @return Dictionary of configuration's parameters
     */
    public int getConfigProperty(String p){
        return Integer.parseInt( this.properties.getProperty(p) );
    }


    /**
     * Called by server when a player loose connection
     * @param playerID the player id of the player who lost connection
     */
    public void playerLostConnection(String playerID){
        if(this.inactivePlayers.add(playerID)){
            this.inactiveDisconnectedPlayers.add(playerID);
        }
        notify(new Message(ViewBoundMessageType.A_PLAYER_DISCONNECTED,Message.fastMap(STRING_PLAYER,playerID)));
        if(waitingForPatternsChoice.cancel()){
            forcePatternChoice();
        }

        if(this.game.getStatus()!=GameStatus.ENDED
                && checkIfGameCanContinue()
                && this.getCurrentPlayer().getID().equals(playerID)){
            advanceGameDueToPlayerInactivity();
        }
    }

    /**
     * Called by server when a player restore its connection
     * @param playerID the player id of the player who restored its connection
     */
    public void playerRestoredConnection(String playerID){
        if(this.inactivePlayers.contains(playerID)){
            if(this.inactiveDisconnectedPlayers.contains(playerID)){
                this.inactiveDisconnectedPlayers.remove(playerID);
                //player is not removed from inactive ones because it was so before loosing connection
            } else {
                this.inactivePlayers.remove(playerID);
                notify(new Message(ViewBoundMessageType.A_PLAYER_RECONNECTED,Message.fastMap(STRING_PLAYER,playerID)));
            }
        }
    }
}
