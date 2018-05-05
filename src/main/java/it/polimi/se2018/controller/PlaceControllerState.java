package it.polimi.se2018.controller;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.ToolCard;

public class PlaceControllerState implements ControllerState {
    Controller controller;

    public PlaceControllerState(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void draftDiceFromDraftPool(Dice dice) {
        System.out.println("Place the dice");
    }

    @Override
    public void placeDice(int row, int col) {

    }

    @Override
    public boolean useToolCard(ToolCard toolcard, Player player) {
        //if toolcard already used then different message
        System.out.println("Place the dice first");
    }

    //TOOLCARD ACTIVATED

    @Override
    public void draftDiceFromTrack(Dice dice) {
        System.out.println("Can't do. Choose a dice from the draft pool or activate a ToolCard");
    }

    @Override
    public void moveDice(int rowFrom, int colFrom, int rowTo, int colTo) {
        System.out.println("Can't do. Choose a dice from the draft pool or activate a ToolCard");
    }

    @Override
    public void incrementDice() {
        System.out.println("Can't do. Choose a dice from the draft pool or activate a ToolCard");
    }

    @Override
    public void deincrementDice() {
        System.out.println("Can't do. Choose a dice from the draft pool or activate a ToolCard");
    }
}
