package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.message.CVMessage;
import it.polimi.se2018.utils.message.VCMessage;
import it.polimi.se2018.view.View;

import java.util.*;

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
 * @see ToolCardsManager
 * @see DiceBag
 * @see ObjectiveCardManager
 * @see PlacementRule
 * @see WindowPatternManager
 */
public class Controller extends Observable {

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
     * Contains an instance of a {@link ToolCardsManager} that is the one
     * that creates the {@link ToolCard} (s) to be assigned to the {@link Game}
     */
    private ToolCardsManager toolCardsManager;

    private final Properties properties;

    private Timer timerForStartingGame;

    int movesCounter = 0;


    /**
     * Construct a controller with reference to a game instance
     * and all manager and factory classes needed to the correct
     * working of the game
     *
     * @param game the game instance to be controlled
     * @param properties dictionary of parameters loaded from config file
     */
    public Controller(Game game, Properties properties) {

        this.properties = properties;

        int numberOfDicesPerColor = Integer.parseInt( properties.getProperty("numberOfDicesPerColor") );
        int numberOfToolCards = Integer.parseInt( properties.getProperty("numberOfToolCards") );
        int numberOfPublicObjectiveCards =  Integer.parseInt( properties.getProperty("numberOfPublicObjectiveCards") );

        //Create Managers
        this.stateManager = new ControllerStateManager(this);

        this.windowPatternManager = new WindowPatternManager();

        this.toolCardsManager = new ToolCardsManager(this.getDefaultPlacementRule());

        this.objectiveCardManager = new ObjectiveCardManager();

        //Set main attributes
        this.game = game;
        this.controllerState =  this.stateManager.getStartState();
        this.activeToolcard = null;
        this.diceBag = new DiceBag(numberOfDicesPerColor);

        //Produces and sets cards to the game
        List<ToolCard> toolCards = toolCardsManager.getRandomToolCards(numberOfToolCards);
        List<PublicObjectiveCard> publicObjectiveCards = objectiveCardManager.getPublicObjectiveCards(numberOfPublicObjectiveCards);

        this.game.setCards(toolCards,publicObjectiveCards);
    }

    /**
     * Set as current state the one passed as method's argument
     *
     * @param controllerState state that must be setted to the controller
     */
    public void setControllerState(ControllerState controllerState) {
        this.controllerState = controllerState;
        this.controllerState.executeImplicitBehaviour(); //WARNING: could change controllerState implicitly
    }

    private void assignWindowPatternToPlayer(WindowPattern wp, String playerID){
        game.assignWindowPatternToPlayer(wp, playerID);
    }

    public CVMessage handleMove(VCMessage message) {

        VCMessage.types type = (VCMessage.types) message.getType();
        CVMessage returnMessage;

        switch(game.getStatus()){
            case WAITING_FOR_PATTERNS_CHOICE:
                String playerID = message.getSendingPlayerID();
                WindowPattern wp = (WindowPattern) message.getParam("windowPattern");
                assignWindowPatternToPlayer(wp,playerID);
                returnMessage = new CVMessage(CVMessage.types.ACKNOWLEDGMENT_MESSAGE,"WindowPattern assigned");
                //TODO: a causa della chiamata diretta di startGame, l'ultimo che setta il wp potrebbe vedere prima l'inizio del gioco e poi l'acknowledge
                if(checkIfAllPlayersHaveChoosenWPattern()){
                    this.timerForStartingGame.cancel();
                    startGame();
                }
                break;
            case PLAYING:
                if (game.isCurrentPlayer(message.getSendingPlayerID())) {
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

    /**
     * Starts the game
     * @return true if game was started successfully, false if not
     */
    public void launchGame(Set<String> nicknames){

        Player player;
        for(String nickname : nicknames){
            player = new Player(nickname,objectiveCardManager.getPrivateObjectiveCard());

            game.addPlayer(player);

            Set<WindowPattern> patterns = windowPatternManager.getCouplesOfPatterns(getConfigProperty("amountOfCouplesOfPatternsPerPlayer"));
            HashMap<String,Object> params = new HashMap<>();
            params.put("patterns",patterns);
            notify(new CVMessage(CVMessage.types.GIVE_WINDOW_PATTERNS,params,player.getID()));
        }

        game.setStatusAsWaitingForPatternsChoice();

        this.timerForStartingGame = new Timer();
        this.timerForStartingGame.schedule(new TimerTask() {
            @Override
            public void run() {
                startGame();
            }
        },getConfigProperty("timeoutChoosingPatterns")*1000);

    }

    private boolean checkIfAllPlayersHaveChoosenWPattern(){
        for(Player p : game.getPlayers()){
            if(p.getWindowPattern()==null){ return false; }
        }
        return true;
    }

    private void startGame(){
        game.startGame( getDicesForNewRound() );
    }

    /**
     * Let the game advance in the turns sequence.
     * If in the current {@link Round} have been played all the possible {@link Turn}s
     * it is created a new {@link Round}. If the current {@link Round} is the last one,
     * the game is ended calling {@link Controller#endGame()}.
     */
    protected void advanceGame() {

        resetActiveToolCard();
        setControllerState(stateManager.getStartState());

        //if player's window pattern is empty
        if(game.getCurrentRound().getCurrentTurn().getPlayer().getWindowPattern().isEmpty()){
            this.placementRule = new BorderPlacementRuleDecorator( getDefaultPlacementRule() );
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
    }

    private List<Dice> getDicesForNewRound(){
        return diceBag.getDices( game.getPlayers().size()*2 + 1 );
    }

    /**
     * Ends the current game. Calculates rankings and scores and then notify them to players.
     */
    private void endGame(){

        Map<Player, Integer> rankings = getRankingsAndScores();

        registerRankingsOnUsersProfiles(rankings);

        //TODO: notify view of winners and scores

    }

    /**
     * Gets the rankings and scores of the current {@link Game}.
     * @return rankings and scores of the current {@link Game}
     */
    private Map<Player,Integer> getRankingsAndScores() {
        List<Player> playersOfLastRound = game.getCurrentRound().getPlayersByReverseTurnOrder();
        List<PublicObjectiveCard> publicObjectiveCards = game.getDrawnPublicObjectiveCards();

        return Scorer.getInstance().getRankings(playersOfLastRound, publicObjectiveCards);
    }

    /**
     * Sends scores and rankings to players profiles ({@link User})
     * in order to increase statistics of wins and played games.
     */
    private void registerRankingsOnUsersProfiles(Map<Player, Integer> rankings){
        /*
        TODO: valutare a seconda di cosa si deciderà di fare con User
        for(Player player : game.getPlayers()){

            User user = player.getUser();
            user.increaseGamesPlayed();

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
     * @return Dictionary of configuration's parameters
     */
    public int getConfigProperty(String p){
        return Integer.parseInt( this.properties.getProperty(p) );
    }

}
