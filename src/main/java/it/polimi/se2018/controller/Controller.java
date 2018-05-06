package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

import java.util.List;
import java.util.Set;

public class Controller implements ControllerInterface {

    //TODO: check access permissions to the following attributes

    Game game;  //Reference to the Model

    ControllerState controllerState; //State pattern

    ToolCard activeToolcard;

    DiceBag diceBag;

    ControllerStateManager controllerStateManager;

    int movesCounter = 0;

    //TODO: remove following states and move them to ControllerStateManager
    //Controller states
    StartControllerState startState;
    PlaceControllerState placeState;
    ToolCardControllerState toolCardState;
    DraftControllerState draftControllerState;

    //Getters for controller states
    public StartControllerState getStartState() {
        return startState;
    }
    public PlaceControllerState getPlaceState() {
        return placeState;
    }
    public ToolCardControllerState getToolCardState() { return toolCardState; }
    public DraftControllerState getDraftControllerState() { return draftControllerState; }

    //Constructor
    public Controller(Game game) {

        //Set controller states
        this.startState = new StartControllerState(this);
        this.placeState = new PlaceControllerState(this);
        this.toolCardState = new ToolCardControllerState(this);

        //Set main attributes
        this.game = game;
        this.controllerState =  this.startState;
        this.activeToolcard = null;
        this.diceBag = new DiceBag(18); //TODO: read this number from config file
        this.controllerStateManager = new ControllerStateManager(this);
    }

    //State pattern
    public void setControllerState(ControllerState controllerState) {
        this.controllerState = controllerState;
        //could change controllerState implicitly
        this.controllerState.executeImplicitBehaviour();
    }


    //Handling View->Controller (Moves)

    //call this function to make sure

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


    private void endGame(){
        Player winner;
        List<Player> rankings = getRankings();
        winner = rankings.get(0);

        //TODO: notify view of winners and scores

    }

    private List<Player> getRankings() {
        List<Player> playersOfLastRound = game.currentRound.getPlayers();
        Set<PublicObjectiveCard> publicObjectiveCards = game.drawnPublicObjectiveCards;

        return Scorer.getInstance().getRankings(playersOfLastRound, publicObjectiveCards);
    }


    public boolean canUseSpecificToolCard(Player player, ToolCard toolCard) {

        //If a player has already drafted a dice, then they can't use a ToolCard that needs drafting
        if(toolCard.needsDrafting() && game.currentRound.currentTurn.hasDrafted()){
            return false;
        }

        return player.canUseToolCard(toolCard);
    }

    public void setActiveToolCard(ToolCard toolCard) {
        toolCard.use();

        this.activeToolcard = toolCard;
        game.currentRound.currentTurn.setUsedToolCard(toolCard);
    }

    public ToolCard getActiveToolCard() {
        if(activeToolcard == null){
            return null;
        }
        return activeToolcard.copy();
    }

    private boolean advanceGame() {
        if (game.currentRound.hasNextTurn()) {
            game.currentRound.nextTurn();
            return true;
        }else {
            return proceedToNextRound();
        }
    }

    private boolean proceedToNextRound() {
        if (game.hasNextRound()) {

            int numberOfDicesPerRound = game.players.size() * 2 + 1;
            List<Dice> dices = diceBag.getDices(numberOfDicesPerRound);

            game.nextRound( dices );
            return true;
        }
        return false;
    }
}
