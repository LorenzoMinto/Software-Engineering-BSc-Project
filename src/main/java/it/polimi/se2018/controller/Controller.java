package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Controller implements ControllerInterface {

    protected Game game;  //Reference to the Model

    protected ControllerState controllerState; //State pattern

    protected ToolCard activeToolcard;

    protected DiceBag diceBag;

    protected ControllerStateManager stateManager;

    protected ObjectiveCardManager objectiveCardManager;

    protected ToolCardsManager toolCardsManager;

    protected PlacementRule placementRule;

    protected WindowPatternManager windowPatternManager;

    int movesCounter = 0;

    //Constructor
    public Controller(Game game) {

        //Create Managers
        this.stateManager = new ControllerStateManager(this);

        try{
            this.windowPatternManager = new WindowPatternManager();
        } catch(NoPatternsFoundInFileSystemException e){
            e.printStackTrace();
        }

        try{
            this.toolCardsManager = new ToolCardsManager();
        } catch(NoToolCardsFoundInFileSystemException e){
            e.printStackTrace();
        }

        this.objectiveCardManager = new ObjectiveCardManager();

        //Set main attributes
        this.game = game;
        this.controllerState =  this.stateManager.getStartState();
        this.activeToolcard = null;
        this.diceBag = new DiceBag(18); //TODO: read this number from config file
    }

    //State pattern
    public void setControllerState(ControllerState controllerState) {
        this.controllerState = controllerState;
        //could change controllerState implicitly
        this.controllerState.executeImplicitBehaviour();
    }


    //Handling View->Controller (Initialization)

    @Override
    public void handlePlayerRegistration(int UserID, String username, String nickname, View view) {

       /*User user = new User(UserID, username);

       PrivateObjectiveCard playerPrivateObjectiveCarad = objectiveCardManager.getPrivateObjectiveCard();

       Player player = new Player(user, nickname, playerPrivateObjectiveCarad);

       game.players.add(player);*/
    }

    @Override
    public void handleChooseWindowPattern(WindowPattern windowPattern, View view) {

    }

    //Handling View->Controller (Moves)


    @Override
    public void handleDraftDiceFromDraftPoolMove(Dice dice, View view) {
        if(game.isCurrentPlayer(view.getPlayer())) {
            controllerState.draftDiceFromDraftPool(dice, view);
        } else { view.showMessage("It is not your turn.");}
    }

    @Override
    public void handleUseToolCardPlayerMove(ToolCard toolcard, View view) {
        if(game.isCurrentPlayer(view.getPlayer())) {
            controllerState.useToolCard(view.getPlayer(), toolcard, view);
        } else { view.showMessage("It is not your turn.");}
    }

    @Override
    public void handlePlaceDiceMove(int row, int col, View view){
        if(game.isCurrentPlayer(view.getPlayer())) {
            controllerState.placeDice(row, col, view);
        } else { view.showMessage("It is not your turn.");}

    }

    @Override
    public void handleIncrementOrDecrementMove(boolean isIncrement, View view) {
        if(game.isCurrentPlayer(view.getPlayer())) {
            if(isIncrement) {
                controllerState.incrementDice(view);
            }else{
                controllerState.decrementDice(view);
            }
        } else { view.showMessage("It is not your turn.");}
    }

    @Override
    public void handleMoveDiceMove(int rowFrom, int colFrom, int rowTo, int colTo, View view) {
        if(game.isCurrentPlayer(view.getPlayer())) {
            controllerState.moveDice(rowFrom, colFrom, rowTo, colTo, view);
        } else { view.showMessage("It is not your turn.");}
    }


    @Override
    public void handleDraftDiceFromTrackMove(Dice dice, int slotNumber, View view){
        if(game.isCurrentPlayer(view.getPlayer())) {

            controllerState.chooseDiceFromTrack(dice, slotNumber, view);
        } else { view.showMessage("It is not your turn.");}
    }

    @Override
    public void handleChooseDiceValueMove(int value, View view) {
        if(game.isCurrentPlayer(view.getPlayer())) {

            controllerState.chooseDiceValue(value, view);
        } else { view.showMessage("It is not your turn.");}
    }

    @Override
    public void handleEndMove(View view){
        if(game.isCurrentPlayer(view.getPlayer())) {
            //If the game has finished (no more rounds and turns are to be played) then end the game
            if(!advanceGame()){
                endGame();
            }
        } else { view.showMessage("It is not your turn.");}
    }

    protected boolean canUseSpecificToolCard(Player player, ToolCard toolCard) {

        //If a player has already drafted a dice, then they can't use a ToolCard that needs drafting
        if(toolCard.needsDrafting() && game.getCurrentRound().getCurrentTurn().hasDrafted()){
            return false;
        }

        return player.canUseToolCard(toolCard);
    }

    protected void setActiveToolCard(ToolCard toolCard) {

        game.useToolCard(toolCard);

        this.activeToolcard = toolCard;
        this.placementRule = toolCard.getPlacementRule();
    }

    protected ToolCard getActiveToolCard() {

        return activeToolcard;
    }

    protected void startGame(){

        try {
            nextRound();
        } catch (NoMoreRoundsAvailableException e) {
            throw new RuntimeException("Starting game but no rounds availables");
        }

        try {
            nextTurn();
        } catch (NoMoreTurnsAvailableException e) {
            throw new RuntimeException("Starting game but no turns availables");
        }

    }

    protected boolean advanceGame() {

        this.placementRule = getDefaultPlacementRule();
        setControllerState(stateManager.getStartState());

        //if player's window pattern is empty

        if(game.getCurrentRound().getCurrentTurn().getCurrentPlayer().getWindowPattern().isEmpty()){
            this.placementRule = new BorderPlacementRuleDecorator( getDefaultPlacementRule() );
        }


        //Proceed with turns / rounds
        try {
            nextTurn();
        } catch (NoMoreTurnsAvailableException e) {

            try {
                nextRound();
            } catch (NoMoreRoundsAvailableException e1) {

                endGame();
            }

            //NOTE: this should not happen
            try {
                nextTurn();
            } catch (NoMoreTurnsAvailableException e1) {
                throw new RuntimeException("Asked next turn. No turns availables. Created a new round. Still no turns availables.");
            }

        }

        return true;
    }

    protected void endGame(){

        getRankingsAndScores();

        //TODO: notify view of winners and scores

    }

    protected void getRankingsAndScores() {
        //TODO: fix problem due to removing of Round.getPlayers()
        List<Player> playersOfLastRound = game.getPlayers();
        Set<PublicObjectiveCard> publicObjectiveCards = game.getDrawnPublicObjectiveCards();

        Object[] results = Scorer.getInstance().compute(playersOfLastRound, publicObjectiveCards);

        List<Player> rankings = (List<Player>) results[0];
        HashMap<Player,Integer> scores= (HashMap<Player,Integer>) results[1];
    }

    protected PlacementRule getDefaultPlacementRule(){

        return new AdjacentValuePlacementRuleDecorator(
                new AdjacentDicePlacementRuleDecorator(
                        new AdjacentColorPlacementRuleDecorator(
                                new ColorPlacementRuleDecorator(
                                        new ValuePlacementRuleDecorator(
                                                new EmptyPlacementRule()))), false));

    }

    private void nextTurn() throws NoMoreTurnsAvailableException { //NOTE: Assumes that a round exists

        int nextRoundNumber = game.getCurrentRound().getNumber();

        int nextTurnNumber = getNextTurnNumber();   //throws NoMoreTurnsAvailableException

        Player nextPlayer = whoShouldBePlayingDuringTurn(game.getPlayers(),nextRoundNumber,nextTurnNumber);

        game.getCurrentRound().setCurrentTurn( new Turn(nextTurnNumber,nextPlayer) );
    }

    private void nextRound() throws NoMoreRoundsAvailableException{

        int nextRoundNumber = getNextRoundNumber(); //throws NoMoreRoundsAvailableException
        int numberOfDicesPerRound = game.getPlayers().size() * 2 + 1;
        List<Dice> nextRoundDices = diceBag.getDices( numberOfDicesPerRound );

        game.setCurrentRound( new Round(nextRoundNumber, new DraftPool( nextRoundDices ) ) );
    }

    private int getNextTurnNumber() throws NoMoreTurnsAvailableException { //NOTE: Assumes that a round exists

        Turn currentTurn = game.getCurrentRound().getCurrentTurn();

        if(currentTurn == null){ return 0; }

        int currentTurnNumber = currentTurn.getNumber();

        if( currentTurnNumber >= game.NUMBER_OF_TURNS_PER_ROUND - 1 ){ throw new NoMoreTurnsAvailableException(); }

        return currentTurnNumber++;
    }

    private int getNextRoundNumber() throws NoMoreRoundsAvailableException{

        Round currentRound = game.getCurrentRound();

        if(currentRound == null){ return 0; }

        int currentRoundNumber = currentRound.getNumber();

        if( currentRoundNumber >= game.NUMBER_OF_ROUNDS - 1 ){ throw new NoMoreRoundsAvailableException(); }

        return currentRoundNumber++;

    }

    /**
     * @author Federico Haag
     * @param roundNumber sequential number of the round (starting from 0)
     * @param turnNumber sequential number of the turn (starting from 0)
     * @return the Player obj relative to which player should play according to the game rules in the specified round/turn
     * @see Player
     */
    private Player whoShouldBePlayingDuringTurn(List<Player> players, int roundNumber, int turnNumber){

        int numberOfPlayers = players.size();

        if( turnNumber >= numberOfPlayers ){ turnNumber = game.NUMBER_OF_TURNS_PER_ROUND - turnNumber - 1; }

        int playerShouldPlayingIndex = (turnNumber + (roundNumber % numberOfPlayers)) % numberOfPlayers;

        return players.get(playerShouldPlayingIndex);
    }

}
