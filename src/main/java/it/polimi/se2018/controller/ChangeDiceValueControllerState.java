package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

public class ChangeDiceValueControllerState extends ControllerState {

    public ChangeDiceValueControllerState(Controller controller) {
        this.controller = controller;
        this.defaultMessage = MIDDLE_OF_EFFECT;
    }

    @Override
    public void incrementDice(View view) {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();
        if (currentTurn.hasDrafted()) {
            if (currentTurn.getDraftedDice().incrementValue()) {
                controller.setControllerState(controller.stateManager.getNextState(this));
            }
        } else {
            view.showMessage(NO_DICE_DRAFTED);
        }
    }

    @Override
    public void decrementDice(View view) {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();
        if (currentTurn.hasDrafted()) {
            if (currentTurn.getDraftedDice().decrementValue()) {
                controller.setControllerState(controller.stateManager.getNextState(this));
            }
        } else {
            view.showMessage(NO_DICE_DRAFTED);
        }
    }

    @Override
    public void chooseDiceValue(int value, View view) {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();
        if (currentTurn.hasDrafted()) {
            //NOTE: this assumes value is a legal value, otherwise need bool returned from setValue
            currentTurn.getDraftedDice().setValue(value);
            controller.setControllerState(controller.stateManager.getNextState(this));
        } else {
            view.showMessage(NO_DICE_DRAFTED);
        }
    }
}
