package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.view.View;

public class Controller implements Observer<PlayerMove> {

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

    //Getters for controller states
    public StartControllerState getStartState() {
        return startState;
    }
    public PlaceControllerState getPlaceState() {
        return placeState;
    }


    //Constructor
    public Controller(Game game) {

        //Set controller states
        this.startState = new StartControllerState(this);
        this.placeState = new PlaceControllerState(this);

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



    public void handleMove(UseToolCardPlayerMove move) {
        controllerState.useToolCard(
                move.getPlayer(), move.getToolcard()
        );
    }

    public void handleMove(PlayerMove move) {

    }

















    public boolean canUseSpecificToolCard(Player player, ToolCard toolCard) {

        //define logic to check from Controller if toolcard is usable, these object are mere copies.
        if(player.getFavorTokens() >= toolCard.getNeededTokens()){
            return true;
        }else {
            return false;
        }

    }

    public void setActiveToolCard(ToolCard toolCard) {
        toolCard.use();

        this.activeToolcard = toolCard;
        game.currentRound.currentTurn.setUsedToolCard(toolCard);
    }

    public ToolCard getActiveToolCard() {
        //TODO: COMPLETE toolcard with tokens
        return new ToolCard(activeToolcard.getTitle(), activeToolcard.getDescription(), activeToolcard.getImageURL());
    }

    public boolean goToNextRound() {
        if (game.hasNextRound()) {

            game.nextRound( diceBag.getDices(game.players.size() * 2 + 1) );
            return true;
        }
        return false;
    }

    public boolean goToNextTurn() {
        if (game.currentRound.hasNextTurn()) {

            int currentRoundNumber = game.currentRound.getNumber();
            int currentTurnNumber = game.currentRound.currentTurn.getNumber();

            game.currentRound.nextTurn( whoShouldPlayingOnTurn(currentRoundNumber, currentTurnNumber) );
            return true;
        }
        return false;
    }


    //UTILS

    private Player whoShouldPlayingOnTurn(int roundNumber, int turnNumber){

        int playerShouldPlayingIndex = 0;

        //TODO: implement here using Game.NUMBER_OF_ROUNDS, Game.NUMBER_OF_TURNS_PER_ROUND, game.players

        return game.players.get(playerShouldPlayingIndex);
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
