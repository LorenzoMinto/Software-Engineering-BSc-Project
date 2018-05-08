package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

public class RerollDraftedControllerState implements ControllerState {
    Controller controller;

    public RerollDraftedControllerState(Controller controller) {
        this.controller = controller;
    }

    //IMPLICIT STATE (SELF CONSUMING)

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
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        Dice draftedDice = currentTurn.getDraftedDice();
        draftedDice.roll();
        currentTurn.setDraftedDice(draftedDice);
        controller.setControllerState(controller.stateManager.getNextState(this));
    }
}
