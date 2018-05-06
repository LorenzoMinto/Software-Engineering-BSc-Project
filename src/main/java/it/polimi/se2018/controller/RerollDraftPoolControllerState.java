package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

public class RerollDraftPoolControllerState implements ControllerState {
    Controller controller;

    public RerollDraftPoolControllerState(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void draftDiceFromDraftPool(Dice dice, View view) {
        System.out.println("This is an implicit state.");
    }

    @Override
    public void placeDice(int row, int col, View view) {
        System.out.println("This is an implicit state.");
    }

    @Override
    public void useToolCard(Player player, ToolCard toolcard, View view) {
        System.out.println("This is an implicit state.");
    }

    @Override
    public void chooseDiceFromTrack(Dice dice, int slotNumber, View view) {
        System.out.println("This is an implicit state.");
    }

    @Override
    public void moveDice(int rowFrom, int colFrom, int rowTo, int colTo, View view) {
        System.out.println("This is an implicit state.");
    }

    @Override
    public void incrementDice(View view) {
        System.out.println("This is an implicit state.");
    }

    @Override
    public void decrementDice(View view) {
        System.out.println("This is an implicit state.");
    }

    @Override
    public void chooseDiceValue(int value, View view) {
        System.out.println("This is an implicit state.");
    }

    @Override
    public void executeImplicitBehaviour() {
        Round currentRound = controller.game.currentRound;
        currentRound.draftPool.reroll();
        controller.setControllerState(controller.getActiveToolCard().nextState(this));
    }
}
