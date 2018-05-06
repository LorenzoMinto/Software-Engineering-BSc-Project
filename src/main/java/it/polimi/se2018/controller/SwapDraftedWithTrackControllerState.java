package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

public class SwapDraftedWithTrackControllerState implements ControllerState {
    Controller controller;

    public SwapDraftedWithTrackControllerState(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void draftDiceFromDraftPool(Dice dice, View view) {

    }

    @Override
    public void placeDice(int row, int col, View view) {

    }

    @Override
    public void useToolCard(Player player, ToolCard toolcard, View view) {

    }

    @Override
    public void chooseDiceFromTrack(Dice dice, int slotNumber, View view) {

    }

    @Override
    public void moveDice(int rowFrom, int colFrom, int rowTo, int colTo, View view) {

    }

    @Override
    public void incrementDice(View view) {

    }

    @Override
    public void decrementDice(View view) {

    }

    @Override
    public void chooseDiceValue(int value, View view) {

    }

    @Override
    public void executeImplicitBehaviour(View view) {
        Game game = controller.game;
        Turn turn = game.currentRound.currentTurn;
        game.track.putDice(turn.getDraftedDice(), turn.getSlotOfTrackChosenDice());
        turn.setDraftedDice(turn.getTrackChosenDice());
        controller.setControllerState(controller.getActiveToolCard().nextState(this));
    }
}
