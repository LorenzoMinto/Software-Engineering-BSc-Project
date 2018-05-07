package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

public class ChangeDiceValueControllerState implements ControllerState {
    Controller controller;

    public ChangeDiceValueControllerState(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void draftDiceFromDraftPool(Dice dice, View view) {
        view.showMessage("Can't do. You have to change the dice value first.");
    }

    @Override
    public void placeDice(int row, int col, View view) {
        view.showMessage("Can't do. You have to change the dice value first.");
    }

    @Override
    public void useToolCard(Player player, ToolCard toolcard, View view) {
        view.showMessage("Can't do. You have to change the dice value first.");
    }

    @Override
    public void chooseDiceFromTrack(Dice dice, int slotNumber, View view) {
        view.showMessage("Can't do. You have to change the dice value first.");
    }

    @Override
    public void moveDice(int rowFrom, int colFrom, int rowTo, int colTo, View view) {
        view.showMessage("Can't do. You have to change the dice value first.");
    }

    @Override
    public void incrementDice(View view) {
        Game game = controller.game;
        Turn currentTurn = game.currentRound.currentTurn;
        if (currentTurn.hasDrafted()) {
            if (currentTurn.getDraftedDice().incrementValue()) {
                controller.setControllerState(controller.stateManager.getNextState(this));
            }
        } else {
            view.showMessage("Can't do. No dice has been drafted.");
        }
    }

    @Override
    public void decrementDice(View view) {
        Game game = controller.game;
        Turn currentTurn = game.currentRound.currentTurn;
        if (currentTurn.hasDrafted()) {
            if (currentTurn.getDraftedDice().decrementValue()) {
                controller.setControllerState(controller.stateManager.getNextState(this));
            }
        } else {
            view.showMessage("Can't do. No dice has been drafted.");
        }
    }

    @Override
    public void chooseDiceValue(int value, View view) {
        Game game = controller.game;
        Turn currentTurn = game.currentRound.currentTurn;
        if (currentTurn.hasDrafted()) {
            //NOTE: this assumes value is a legal value, otherwise need bool returned from setValue
            currentTurn.getDraftedDice().setValue(value);
            controller.setControllerState(controller.stateManager.getNextState(this));
        } else {
            view.showMessage("Can't do. No dice has been drafted.");
        }
    }

    @Override
    public void executeImplicitBehaviour() {

    }
}
