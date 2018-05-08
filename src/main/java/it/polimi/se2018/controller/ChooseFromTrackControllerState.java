package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

public class ChooseFromTrackControllerState implements ControllerState {
    Controller controller;

    public ChooseFromTrackControllerState(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void draftDiceFromDraftPool(Dice dice, View view) {
        view.showMessage("Can't do. You are in the middle of a Toolcard effect.");
    }

    @Override
    public void placeDice(int row, int col, View view) {
        view.showMessage("Can't do. You are in the middle of a Toolcard effect.");
    }

    @Override
    public void useToolCard(Player player, ToolCard toolcard, View view) {
        view.showMessage("Can't do. You are in the middle of a Toolcard effect.");
    }

    @Override
    public void chooseDiceFromTrack(Dice dice, int slotNumber, View view) {
        Game game = controller.game;
        if (game.getTrack().takeDice(dice, slotNumber)) {
            game.getCurrentRound().getCurrentTurn().setTrackChosenDice(dice);
            game.getCurrentRound().getCurrentTurn().setSlotOfTrackChosenDice(slotNumber);
            controller.setControllerState(controller.stateManager.getNextState(this));
        }

    }

    @Override
    public void moveDice(int rowFrom, int colFrom, int rowTo, int colTo, View view) {
        view.showMessage("Can't do. You are in the middle of a Toolcard effect.");
    }

    @Override
    public void incrementDice(View view) {
        view.showMessage("Can't do. You are in the middle of a Toolcard effect.");
    }

    @Override
    public void decrementDice(View view) {
        view.showMessage("Can't do. You are in the middle of a Toolcard effect.");
    }

    @Override
    public void chooseDiceValue(int value, View view) {
        view.showMessage("Can't do. You are in the middle of a Toolcard effect.");
    }

    @Override
    public void executeImplicitBehaviour() {

    }
}
