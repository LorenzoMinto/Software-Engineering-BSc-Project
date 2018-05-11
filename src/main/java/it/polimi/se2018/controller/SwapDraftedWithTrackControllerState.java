package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

public class SwapDraftedWithTrackControllerState implements ControllerState {
    Controller controller;

    private static final String IMPLICITWARNING= "This is an implicit state.";


    public SwapDraftedWithTrackControllerState(Controller controller) {
        this.controller = controller;
    }

    //IMPLICIT STATE (SELF CONSUMING)

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
        Game game = controller.game;
        Turn turn = game.getCurrentRound().getCurrentTurn();
        game.getTrack().putDice(turn.getDraftedDice(), turn.getSlotOfTrackChosenDice());
        turn.setDraftedDice(turn.getTrackChosenDice());
        controller.setControllerState(controller.stateManager.getNextState(this));
    }
}
