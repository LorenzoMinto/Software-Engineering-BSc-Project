package it.polimi.se2018.controller;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.ToolCard;

public class Controller {
    Game game;
    ControllerState controllerState;

    ToolCard activeToolcard;


    public StartControllerState getStartState() {
        return startState;
    }

    public PlaceControllerState getPlaceState() {
        return placeState;
    }

    StartControllerState startState;
    PlaceControllerState placeState;

    public Controller(Game game) {
        this.game = game;
        this.controllerState =  new StartControllerState(this);

        this.startState = new StartControllerState(this);
        this.placeState = new PlaceControllerState(this);
    }

    public handleMove(PlayerMove move) {
        if (game.isCurrentPlayer(move.getPlayer())) {
            //switch that handles different kind of moves to different methods of current controllerState
            if (move instanceof UseToolcardPlayerMove) {
                controllerState.useToolCard(((UseToolcardPlayerMove)move).getToolcard());
            } else if (move instanceof ) {

            }
        } else {
            move.getView().reportError("Not your turn!");
        }
    }

    public void setControllerState(ControllerState controllerState) {
        this.controllerState = controllerState;
        //could change controllerState implicitly
        this.controllerState.executeImplicitBehaviour();
    }

    public boolean canUseSpecificToolCard(Player player, ToolCard toolCard) {
        //define logic to check from Controller if toolcard is usable, these object are mere copies.

    }

    public void setActiveToolCard(ToolCard toolCard) {
        this.activeToolcard = toolCard;
        //TODO: set model active toolcard

    }

    public ToolCard getActiveToolCard() {
        //make a copy
        return this.activeToolcard;
    }

}
