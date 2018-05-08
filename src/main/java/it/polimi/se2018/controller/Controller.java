package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

import java.util.List;
import java.util.Set;

public class Controller implements ControllerInterface {

    //TODO: check access permissions to the following attributes

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


    protected void endGame(){
        Player winner;
        List<Player> rankings = getRankings();
        winner = rankings.get(0);

        //TODO: notify view of winners and scores

    }

    protected List<Player> getRankings() {
        List<Player> playersOfLastRound = game.currentRound.getPlayers();
        Set<PublicObjectiveCard> publicObjectiveCards = game.drawnPublicObjectiveCards;

        return Scorer.getInstance().getRankings(playersOfLastRound, publicObjectiveCards);
    }


    protected boolean canUseSpecificToolCard(Player player, ToolCard toolCard) {

        //If a player has already drafted a dice, then they can't use a ToolCard that needs drafting
        if(toolCard.needsDrafting() && game.currentRound.currentTurn.hasDrafted()){
            return false;
        }

        return player.canUseToolCard(toolCard);
    }

    protected void setActiveToolCard(ToolCard toolCard) {
        //TODO: this methods doesn't do anything, it only changes the copy. Make a method on Game that changes it directly!!
        toolCard.use();

        this.activeToolcard = toolCard;
        this.placementRule = toolCard.getPlacementRule();
        game.currentRound.currentTurn.setUsedToolCard(toolCard);
    }

    protected ToolCard getActiveToolCard() {
        if(activeToolcard == null){
            return null;
        }
        return activeToolcard;
    }

    protected boolean advanceGame() {
        this.placementRule = getDefaultPlacementRule();
        setControllerState(stateManager.getStartState());

        //if player's window pattern is empty

        if(game.currentRound.currentTurn.currentPlayer.getWindowPattern().isEmpty()){
            this.placementRule = new BorderPlacementRuleDecorator(this.getDefaultPlacementRule());
        }

        if (game.currentRound.hasNextTurn()) {
            game.currentRound.nextTurn();
            return true;
        }else {
            return proceedToNextRound();
        }
    }

    protected boolean proceedToNextRound() {
        if (game.hasNextRound()) {
            int numberOfDicesPerRound = game.players.size() * 2 + 1;
            List<Dice> dices = diceBag.getDices(numberOfDicesPerRound);

            game.nextRound( dices );
            return true;
        }
        return false;
    }

    protected PlacementRule getDefaultPlacementRule(){
        //NOTE: the DefaultPlacementRule is an empty rule
        PlacementRule defaultPlacementRule = new AdjacentValuePlacementRuleDecorator(
                new AdjacentDicePlacementRuleDecorator(
                        new AdjacentColorPlacementRuleDecorator(
                                new ColorPlacementRuleDecorator(
                                        new ValuePlacementRuleDecorator(
                                                new DefaultPlacementRule()))), false));

        return defaultPlacementRule;
    }

}
