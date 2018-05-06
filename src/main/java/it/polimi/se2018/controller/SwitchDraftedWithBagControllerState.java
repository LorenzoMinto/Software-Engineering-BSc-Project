package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

public class SwitchDraftedWithBagControllerState implements ControllerState {
    Controller controller;

    public SwitchDraftedWithBagControllerState(Controller controller) {
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
        DiceBag diceBag = controller.diceBag;
        Turn currentTurn = controller.game.currentRound.currentTurn;
        controller.diceBag.addDice(currentTurn.getDraftedDice());
        currentTurn.setDraftedDice(diceBag.getDices(1).get(0));
        controller.setControllerState(controller.getActiveToolCard().nextState(this));
    }
}
