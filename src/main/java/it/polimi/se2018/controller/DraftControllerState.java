package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

public class DraftControllerState implements ControllerState {
    Controller controller;

    public DraftControllerState(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void draftDiceFromDraftPool(Dice dice, View view) {
        Game game = controller.game;
        Round currentRound = game.currentRound;

        if (currentRound.draftPool.draftDice(dice)) {
            currentRound.currentTurn.setDraftedDice(dice);
        }

        //TODO: activeToolcard needs to be set to null after effect expires
        if (controller.activeToolcard != null) {
            controller.setControllerState(controller.getActiveToolCard().nextState(this));
        } else {
            controller.setControllerState(controller.getPlaceState());
        }
    }

    @Override
    public void placeDice(int row, int col, View view) {
        view.showMessage("Can't do. You have to draft a dice first.");
    }

    @Override
    public void useToolCard(Player player, ToolCard toolcard, View view) {
        view.showMessage("Can't do. Toolcard already used. You can only draft and place.");
    }

    @Override
    public void chooseDiceFromTrack(Dice dice, int slotNumber, View view) {
        view.showMessage("Can't do. You can only draft and place.");
    }

    @Override
    public void moveDice(int rowFrom, int colFrom, int rowTo, int colTo, View view) {
        view.showMessage("Can't do. You can only draft and place.");
    }

    @Override
    public void incrementDice(View view) {
        view.showMessage("Can't do. You can only draft and place.");
    }

    @Override
    public void decrementDice(View view) {
        view.showMessage("Can't do. You can only draft and place.");
    }

    @Override
    public void chooseDiceValue(int value, View view) {
        view.showMessage("Can't do. You can only draft and place.");
    }

    @Override
    public void executeImplicitBehaviour(View view) {
        view.showMessage("Can't do. You can only draft and place.");
    }
}
