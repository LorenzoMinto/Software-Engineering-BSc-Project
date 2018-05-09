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

    protected boolean advanceGame() {

        this.placementRule = getDefaultPlacementRule();
        setControllerState(stateManager.getStartState());

        //if player's window pattern is empty
        if(game.getCurrentRound().getCurrentTurn().getPlayer().getWindowPattern().isEmpty()){
            this.placementRule = new BorderPlacementRuleDecorator( getDefaultPlacementRule() );
        }

        //Proceed with turns / rounds
        try {
            game.getCurrentRound().nextTurn();
        } catch (NoMoreTurnsAvailableException e) {

            try {
                game.nextRound( diceBag.getDices(game.getPlayers().size()*2 + 1) );
            } catch (NoMoreRoundsAvailableException e1) {

                endGame();
            }

            try { //NOTE: this should not happen
                game.getCurrentRound().nextTurn();
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
        List<Player> playersOfLastRound = game.getCurrentRound().getPlayersByTurnOrder();
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

}
