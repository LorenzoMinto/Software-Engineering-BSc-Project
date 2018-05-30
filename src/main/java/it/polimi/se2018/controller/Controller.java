package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.message.CVMessage;
import it.polimi.se2018.utils.message.VCMessage;
import it.polimi.se2018.view.View;

import java.util.*;
import java.util.logging.Logger;

import static it.polimi.se2018.utils.message.CVMessage.types.ERROR_MESSAGE;

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

    private final Logger logger;

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
     * Contains an instance of a {@link ToolCardManager} that is the one
     * that creates the {@link ToolCard} (s) to be assigned to the {@link Game}
     */
    private ToolCardManager toolCardManager;

    /**
     * Configuration properties loaded from config file
     */
    private final Properties properties;

    /**
     * Timer used to start game after a while after it's asked to players to choose window pattern
     */
    private static final Timer TIMER = new Timer();

    private TimerTask waitingForPatternsChoice;
    private TimerTask waitingForPlayerMove;

    //TODO: commentare
    int movesCounter = 0;

    private HashSet<String> inactivePlayers = new HashSet<>();

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

        //Create Managers
        this.stateManager = new ControllerStateManager(this);

        this.windowPatternManager = new WindowPatternManager();

        this.toolCardManager = new ToolCardManager(this.getDefaultPlacementRule());

        this.objectiveCardManager = new ObjectiveCardManager();

        //Set main attributes
        this.game = game;
        this.controllerState =  this.stateManager.getStartState();
        this.activeToolcard = null;
        this.diceBag = new DiceBag(numberOfDicesPerColor);

        //Produces and sets cards to the game
        List<ToolCard> toolCards = toolCardManager.getRandomToolCards(numberOfToolCards);
        List<PublicObjectiveCard> publicObjectiveCards = objectiveCardManager.getPublicObjectiveCards(numberOfPublicObjectiveCards);

        this.game.setCards(toolCards,publicObjectiveCards);
    }

    /**
     * Set as current state the one passed as method's argument
     *
     * @param controllerState state that must be setted to the controller
     */
    protected void setControllerState(ControllerState controllerState) {
        this.controllerState = controllerState;
        this.controllerState.executeImplicitBehaviour(); //WARNING: could change controllerState implicitly
    }

    private CVMessage assignWindowPatternToPlayer(WindowPattern wp, String playerID){

        boolean assigned = game.assignWindowPatternToPlayer(wp, playerID);

        if( assigned ){
            return new CVMessage(CVMessage.types.ACKNOWLEDGMENT_MESSAGE,"WindowPattern assigned");
        } else {
            return new CVMessage(CVMessage.types.ERROR_MESSAGE,"Player has already an assigned WindowPattern");
        }
    }

    public CVMessage handleMove(VCMessage message) {

        VCMessage.types type = (VCMessage.types) message.getType();
        CVMessage returnMessage;

        switch(game.getStatus()){
            case WAITING_FOR_PATTERNS_CHOICE:
                if(type==VCMessage.types.CHOOSE_WINDOW_PATTERN){
                    String playerID = message.getSendingPlayerID();
                    WindowPattern wp = (WindowPattern) message.getParam("windowPattern");
                    returnMessage = assignWindowPatternToPlayer(wp,playerID);
                    checkIfAllPlayersHaveChosenWindowPattern(); //may call startGame()
                    break;
                } else {
                    returnMessage = new CVMessage(ERROR_MESSAGE);
                }
                break;

            case PLAYING:
                if(type==VCMessage.types.BACK_GAMING){
                    inactivePlayers.remove(message.getSendingPlayerID()); //this cause that the next turn of this player will not be skipped and player will be notified
                    returnMessage = new CVMessage(CVMessage.types.BACK_TO_GAME);
                    logger.info("Received BACK_GAMING message from: "+message.getSendingPlayerID());

                } else if (game.isCurrentPlayer(message.getSendingPlayerID())) {
                    switch (type) {
                        case DRAFT_DICE_FROM_DRAFTPOOL:
                            Dice dice = (Dice) message.getParam("dice");
                            returnMessage = controllerState.draftDiceFromDraftPool(dice);
                            break;
                        case PLACE_DICE:
                            int row = (int) message.getParam("row");
                            int col = (int) message.getParam("col");
                            returnMessage = controllerState.placeDice(row, col);
                            break;
                        case USE_TOOLCARD:
                            ToolCard toolCard = (ToolCard) message.getParam("toolcard");
                            returnMessage = controllerState.useToolCard(toolCard);
                            break;
                        case MOVE_DICE:
                            int rowFrom = (int) message.getParam("rowFrom");
                            int colFrom = (int) message.getParam("colFrom");
                            int rowTo = (int) message.getParam("rowTo");
                            int colTo = (int) message.getParam("colTo");
                            returnMessage = controllerState.moveDice(rowFrom, colFrom, rowTo, colTo);
                            break;
                        case CHOOSE_DICE_VALUE:
                            int value = (int) message.getParam("value");
                            returnMessage = controllerState.chooseDiceValue(value);
                            break;
                        case INCREMENT_DICE:
                            returnMessage = controllerState.incrementDice();
                            break;
                        case DECREMENT_DICE:
                            returnMessage = controllerState.decrementDice();
                            break;
                        case CHOOSE_DICE_FROM_TRACK:
                            int value2 = (int) message.getParam("value");
                            returnMessage = controllerState.chooseDiceValue(value2);
                            break;
                        default:
                            returnMessage = new CVMessage(CVMessage.types.ERROR_MESSAGE,"UnrecognizedMove");
                            break;
                    }
                } else {
                    returnMessage = new CVMessage(ERROR_MESSAGE, "Not your turn!");
                }

                //Timer for move is reset only if the move was valid. This prevent blocking of game due to unlimited bad messages
                if(returnMessage.getType()==CVMessage.types.ACKNOWLEDGMENT_MESSAGE){
                    logger.info(()->"Resetted PlayerMoveTimer due to move:"+type);
                    resetPlayerMoveTimer();
                }

                break;
            default:
                returnMessage = new CVMessage(ERROR_MESSAGE);
                break;
        }

        return returnMessage;
    }

    /**
     * Checks if a given {@link ToolCard} can be used by the specified {@link Player}
     * @param toolCard the {@link ToolCard} to be checked
     * @return if the specified {@link ToolCard} can be used or not by the specified {@link Player} (true=can use)
     */
    protected boolean canUseSpecificToolCard(ToolCard toolCard) {

        //If a player has already drafted a dice, then they can't use a ToolCard that needs drafting
        if(toolCard.needsDrafting() && game.getCurrentRound().getCurrentTurn().hasDrafted()){
            return false;
        }

        Player currentPlayer = game.getCurrentRound().getCurrentTurn().getPlayer();

        return currentPlayer.canUseToolCard(toolCard);
    }

    /**
     * Sets the specified {@link ToolCard} as active. This means that it's being used
     * during the current {@link Turn}
     * @param toolCard the {@link ToolCard} that is being used
     */
    protected void setActiveToolCard(ToolCard toolCard) {

        game.useToolCard(toolCard);

        this.activeToolcard = game.getToolCard(toolCard);
        this.placementRule = this.activeToolcard.getPlacementRule();
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

    public void launchGame(Set<String> nicknames){

        Player player;
        for(String nickname : nicknames){
            player = new Player(nickname,objectiveCardManager.getPrivateObjectiveCard());

            game.addPlayer(player);

            Set<WindowPattern> patterns = windowPatternManager.getPairsOfPatterns(getConfigProperty("amountOfCouplesOfPatternsPerPlayer"));
            HashMap<String,Object> params = new HashMap<>();
            params.put("patterns",patterns);
            notify(new CVMessage(CVMessage.types.GIVE_WINDOW_PATTERNS,params,player.getID()));
        }

        game.setStatusAsWaitingForPatternsChoice();

        //Start the timer for patterns choice
        this.waitingForPatternsChoice = new TimerTask() {
            @Override
            public void run() {
                logger.info("waitingForPatternsChoice timer has expired. Calling startGame()...");
                startGame();
            }
        };
        TIMER.schedule(this.waitingForPatternsChoice,(long)(getConfigProperty("timeoutChoosingPatterns")*1000));
    }

    private void checkIfAllPlayersHaveChosenWindowPattern(){

        for(Player p : game.getPlayers()){
            if(p.getWindowPattern()==null){ return; }
        }

        //Next lines are executed only if all window patterns are set
        this.waitingForPatternsChoice.cancel();
        startGame();
    }

    private void startGame(){
        game.startGame(getDicesForNewRound());
        //TODO: verificare la correttezza della chiamata a advanceGame (secondo me va rimosso perché chiama game.nextTurn, che è sbagliato, perché così non si eseguirebbe il primo turn del game), ma va copiata la restante logica che c'è dentro advanceGame
        this.advanceGame();

        startPlayerMoveTimer();
    }

    private void resetPlayerMoveTimer(){
        this.waitingForPlayerMove.cancel();
        startPlayerMoveTimer();
    }

    private void startPlayerMoveTimer(){
        this.waitingForPlayerMove = new TimerTask() {
            @Override
            public void run() {
                logger.info("waitingForPlayerMove timer has expired. Calling advanceGameDueToPlayerInactivity()...");
                advanceGameDueToPlayerInactivity();
            }
        };
    }

    private void advanceGameDueToPlayerInactivity() {

        String currentPlayerID = getCurrentPlayer().getID();

        //If statement prevents sending every turn the notification for all inactive players
        if( inactivePlayers.add(currentPlayerID) ){
            notify(new CVMessage(CVMessage.types.INACTIVE_PLAYER,currentPlayerID)); //notify everyone that the player is now inactive
            notify(new CVMessage(CVMessage.types.INACTIVE,null,currentPlayerID)); //notify view of inactive player that it is now considered inactive
        }

        //Checks if due to players inactivity game can continuing or not
        if( game.getPlayers().size() - inactivePlayers.size() < getConfigProperty("minNumberOfPlayers") ){
            endGame(inactivePlayers);
        }

        //NOTE: aggiungere quì eventuale codice di pulizia delle mosse lasciate in sospeso dal player

        advanceGame();
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
     * the game is ended calling {@link Controller#endGame()}.
     */
    protected void advanceGame() {

        resetActiveToolCard();

        //NOTE: aggiungere quì eventuale codice aggiuntivo di pulizia del turno precedente

        setControllerState(stateManager.getStartState());

        //if player's window pattern is empty
        if(getCurrentPlayer().getWindowPattern().isEmpty()){
            this.placementRule = new BorderPlacementRuleDecorator(
                    new ColorPlacementRuleDecorator(
                            new ValuePlacementRuleDecorator(
                                    new EmptyPlacementRule())));
        }

        //Proceed with turns / rounds
        try {
            game.nextTurn();
        } catch (NoMoreTurnsAvailableException e) {

            try {
                game.nextRound( getDicesForNewRound() );
            } catch (NoMoreRoundsAvailableException e1) {

                endGame();
            }

            try {
                game.nextTurn();
            } catch (NoMoreTurnsAvailableException e1) {
                throw new BadBehaviourRuntimeException("Asked next turn. No turns availables. Created a new round. Still no turns availables.");
            }
        }

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

    private List<Dice> getDicesForNewRound(){
        return diceBag.getDices( game.getPlayers().size()*2 + 1 );
    }

    /**
     * Ends the current game. Calculates rankings and scores and then notify them to players.
     */
    private void endGame(){
        endGame(new HashSet<>());
    }

    private void endGame(HashSet<String> inactivePlayers){
        Map<String, Integer> rankings = getRankingsAndScores(inactivePlayers);

        registerRankingsOnUsersProfiles(rankings);

        //TODO: notify view of winners and scores
    }

    /**
     * Gets the rankings and scores of the current {@link Game}.
     * @return rankings and scores of the current {@link Game}
     */
    private Map<String,Integer> getRankingsAndScores(HashSet<String> inactivePlayers) {
        List<PublicObjectiveCard> publicObjectiveCards = game.getDrawnPublicObjectiveCards();
        List<Player> playersOfLastRound = game.getCurrentRound().getPlayersByReverseTurnOrder();

        //The evaluation is made only on players who completed the game (inactive players are excluded)
        List<Player> playersToEvaluate = new ArrayList<>();
        for(Player p : playersOfLastRound){
            if(!inactivePlayers.contains(p.getID())){
                playersToEvaluate.add(p);
            }
        }
        Map<Player,Integer> activePlayersRankingsAndScores = Scorer.getInstance().getRankings(playersToEvaluate, publicObjectiveCards);

        //Convert from Player,Integer to String,Integer as required by method signature
        LinkedHashMap<String,Integer> allPlayersRankingsAndScores = new LinkedHashMap<>();
        for (Map.Entry<Player, Integer> entry : activePlayersRankingsAndScores.entrySet()){
            allPlayersRankingsAndScores.put(entry.getKey().getID(),entry.getValue());
        }
        //The returned map must contain a score for all players, so inactive players are added
        for(String inactivePlayerID : inactivePlayers){
            allPlayersRankingsAndScores.put(inactivePlayerID,0);
        }

        return allPlayersRankingsAndScores;
    }

    /**
     * Update scores and rankings of players profiles
     * in order to increase statistics of wins and played games.
     */
    private void registerRankingsOnUsersProfiles(Map<String, Integer> rankings){
        /*
        TODO: implementare
        for(Player player : game.getPlayers()){

            Player winner = Scorer.getInstance().getWinner(rankings);

            if(winner.equals(player)){
                user.increaseGamesWon();
            }
        }
        */
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

}
