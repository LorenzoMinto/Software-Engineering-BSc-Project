package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
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
    public void setControllerState(ControllerState controllerState) {
        this.controllerState = controllerState;
        //could change controllerState implicitly
        this.controllerState.executeImplicitBehaviour();
    }


    //Handling View->Controller (Moves)

    public void handleMove(DraftDiceFromDraftPoolMove move){
        controllerState.draftDiceFromDraftPool(move.getDice());
    }

    public void handleMove(UseToolCardPlayerMove move) {
        controllerState.useToolCard(
                move.getPlayer(), move.getToolcard()
        );
    }

    public void handleMove(PlaceDiceMove move){
        controllerState.placeDice(move.getRow(), move.getColumn());
    }

    public void handleMove(IncrementOrDecrementMove move) {
        if(move.isIncrement()) {
            controllerState.incrementDice();
        }else{
            controllerState.decrementDice();
        }
    }

    public void handleMove(MoveDiceMove move){
        controllerState.moveDice(move.getRowFrom(), move.getColFrom(), move.getRowTo(), move.getColTo());
    }

    public void handleMove(DraftDiceFromTrackMove move){
        controllerState.draftDiceFromTrack(move.getDice());
    }

    public void handleMove(ChooseDiceValueMove move){
        controllerState.chooseDiceValue(move.getValue());
    }




    public boolean canUseSpecificToolCard(Player player, ToolCard toolCard) {
        //TODO: needs to check whether toolcard needs drafting and if turn has already drafted
        //define logic to check from Controller if toolcard is usable, these object are mere copies.
        return player.getFavorTokens() >= toolCard.getNeededTokens();
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

    public boolean goToNextRound() {
        if (game.hasNextRound()) {
            int numberOfDices = game.players.size() * 2 + 1;
            List<Dice> dices = diceBag.getDices(numberOfDices);

            game.nextRound( dices );
            return true;
        }
        return false;
    }

    public boolean goToNextTurn() {
        if (game.currentRound.hasNextTurn()) {

            game.currentRound.nextTurn();
            return true;
        }
        return false;
    }

    @Override
    public void update(PlayerMove move) {
        Turn currentTurn = game.currentRound.currentTurn;
        if(!currentTurn.isCurrentPlayer(move.getPlayer())){
            move.getView().reportError("It's not your turn.");
            return;
        }

        handleMove(move);
    }
}
