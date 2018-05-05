package it.polimi.se2018.controller;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.ToolCard;

public class Controller {
    Game game;
    State state;

    ToolCard activeToolcard;


    public StartState getStartState() {
        return startState;
    }

    public PlaceState getPlaceState() {
        return placeState;
    }

    StartState startState;
    PlaceState placeState;

    public Controller(Game game) {
        this.game = game;
        this.state =  new StartState(this);

        this.startState = new StartState(this);
        this.placeState = new PlaceState(this);
    }

    public handleMove(PlayerMove move) {
        if (game.isCurrentPlayer(move.getPlayer())) {
            //switch that handles different kind of moves to different methods of current state
            if (move instanceof UseToolcardPlayerMove) {
                state.useToolCard(((UseToolcardPlayerMove)move).getToolcard());
            } else if (move instanceof ) {

            }
        } else {
            move.getView().reportError("Not your turn!");
        }
    }

    public void setState(State state) {
        this.state = state;
        //could change state implicitly
        this.state.executeImplicitBehaviour();
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
