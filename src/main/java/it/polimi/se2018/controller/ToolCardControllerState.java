package it.polimi.se2018.controller;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.view.View;

public class ToolCardControllerState implements ControllerState {
    Controller controller;

    public ToolCardControllerState(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void draftDiceFromDraftPool(Dice dice, View view) {
        view.showMessage("Can't do. You have already drafted and placed. You can only use a Toolcard");
    }

    @Override
    public void placeDice(int row, int col, View view) {
        view.showMessage("Can't do. You have already drafted and placed. You can only use a Toolcard");
    }

    @Override
    public void useToolCard(Player player, ToolCard toolcard, View view) {
        if (controller.canUseSpecificToolCard(player, toolcard)) {
            controller.setActiveToolCard(toolcard);
            controller.setControllerState(toolcard.nextState(this));
        } else {
            view.showMessage("Can't use this toolcard.");
        }
    }

    @Override
    public void chooseDiceFromTrack(Dice dice, int slotNumber, View view) {
        view.showMessage("Can't do. You can only use a Toolcard");
    }

    @Override
    public void moveDice(int rowFrom, int colFrom, int rowTo, int colTo, View view) {
        view.showMessage("Can't do. You can only use a Toolcard");
    }

    @Override
    public void incrementDice(View view) {
        view.showMessage("Can't do. You can only use a Toolcard");
    }

    @Override
    public void decrementDice(View view) {
        view.showMessage("Can't do. You can only use a Toolcard");
    }

    @Override
    public void chooseDiceValue(int value, View view) {
        view.showMessage("Can't do. You can only use a Toolcard");
    }

    @Override
    public void executeImplicitBehaviour() {

    }
}
