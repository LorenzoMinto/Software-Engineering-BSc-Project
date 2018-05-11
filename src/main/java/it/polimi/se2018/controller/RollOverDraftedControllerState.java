package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

public class RollOverDraftedControllerState implements ControllerState {
    Controller controller;

    private static final String IMPLICITWARNING= "This is an implicit state.";


    public RollOverDraftedControllerState(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void draftDiceFromDraftPool(Dice dice, View view) {
        System.out.println(IMPLICITWARNING);
    }

    @Override
    public void placeDice(int row, int col, View view) {
        System.out.println(IMPLICITWARNING);
    }

    @Override
    public void useToolCard(Player player, ToolCard toolcard, View view) {
        System.out.println(IMPLICITWARNING);
    }

    @Override
    public void chooseDiceFromTrack(Dice dice, int slotNumber, View view) {
        System.out.println(IMPLICITWARNING);
    }

    @Override
    public void moveDice(int rowFrom, int colFrom, int rowTo, int colTo, View view) {
        System.out.println(IMPLICITWARNING);
    }

    @Override
    public void incrementDice(View view) {
        System.out.println(IMPLICITWARNING);
    }

    @Override
    public void decrementDice(View view) {
        System.out.println(IMPLICITWARNING);
    }

    @Override
    public void chooseDiceValue(int value, View view) {
        System.out.println(IMPLICITWARNING);
    }

    @Override
    public void executeImplicitBehaviour() {
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        Dice draftedDice = currentTurn.getDraftedDice();
        draftedDice.rollOver();
        currentTurn.setDraftedDice(draftedDice);
        controller.setControllerState(controller.stateManager.getNextState(this));
    }
}
