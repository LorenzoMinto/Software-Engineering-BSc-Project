package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

import java.util.List;

public class Controller implements ControllerInterface {

    //TODO: check access permissions to the following attributes

    Game game;  //Reference to the Model

    ControllerState controllerState; //State pattern

    ToolCard activeToolcard;

    DiceBag diceBag;

    //variables used to keep track of player moves
    boolean hasToolCardBeenUsed = false;
    boolean hasDrafted = false;
    int movesCounter = 0;


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
    }

    //State pattern
    public void setControllerState(ControllerState controllerState, View view) {
        this.controllerState = controllerState;
        //could change controllerState implicitly
        this.controllerState.executeImplicitBehaviour(view);
    }


    //Handling View->Controller (Moves)

    @Override
    public void handleDraftDiceFromDraftPoolMove(Dice dice, View view) {
        controllerState.draftDiceFromDraftPool(dice, view);
    }

    @Override
    public void handleUseToolCardPlayerMove(Player player, ToolCard toolcard, View view) {
        controllerState.useToolCard(player, toolcard, view);
    }

    @Override
    public void handlePlaceDiceMove(int row, int col, View view){
        if(row <= 4 && row >= 0 && col<=5 && col>=0) {
            controllerState.placeDice(row, col, view);
        }else { throw new IllegalArgumentException("ERROR: Cannot place dice on row "+row+" column " + col);}
    }

    @Override
    public void handleIncrementOrDecrementMove(boolean isIncrement, View view) {
        if(isIncrement) {
            controllerState.incrementDice(view);
        }else{
            controllerState.decrementDice(view);
        }
    }

    @Override
    public void handleMoveDiceMove(int rowFrom, int colFrom, int rowTo, int colTo, View view) {
        if(rowFrom <= 4 && rowFrom >= 0 &&
                colFrom <= 5 && colFrom >= 0 &&
                rowTo <= 4 && rowTo >= 0 &&
                colTo <= 5 && colTo >= 0) {
            controllerState.moveDice(rowFrom, colFrom, rowTo, colTo, view);
        }else {
            throw new IllegalArgumentException("ERROR: Cannot place dice from row " + rowFrom + " column " + colFrom +
                    " to row " + rowTo + " column " + colTo);

        }
    }


    @Override
    public void handleDraftDiceFromTrackMove(Dice dice, int slotNumber, View view){
        controllerState.chooseDiceFromTrack(dice, slotNumber, view);
    }

    @Override
    public void handleChooseDiceValueMove(int value, View view) {
        controllerState.chooseDiceValue(value, view);
    }

    @Override
    public void handleEndMove(){
        //If the game has finished (no more rounds and turns are to be played) then end the game
        if(!advanceGame()){
            endGame();
        }
    }


    public Player endGame(){
        //TODO: implement method. Returns the winner.
        Player winnerPlayer = null;

        return winnerPlayer;
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
