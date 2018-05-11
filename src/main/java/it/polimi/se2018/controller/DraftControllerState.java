package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

public class DraftControllerState implements ControllerState {
    Controller controller;

    private static final String DRAFTFIRST= "Can't do. You have to draft first";
    private static final String ONLYDRAFTANDPLACE= "Can't do. You can only draft and place";


    public DraftControllerState(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void draftDiceFromDraftPool(Dice dice, View view) {
        Game game = controller.game;
        Round currentRound = game.getCurrentRound();

        if (currentRound.getDraftPool().draftDice(dice)) {
            currentRound.getCurrentTurn().setDraftedDice(dice);
        }

        //TODO: activeToolcard needs to be set to null after effect expires
        if (controller.getActiveToolCard() != null) {
            controller.setControllerState(controller.stateManager.getNextState(this));
        } else {
            controller.setControllerState(controller.stateManager.getPlaceState());
        }
    }

    @Override
    public void placeDice(int row, int col, View view) {
        view.showMessage(DRAFTFIRST);
    }

    @Override
    public void useToolCard(Player player, ToolCard toolcard, View view) {
        view.showMessage(ONLYDRAFTANDPLACE);
    }

    @Override
    public void chooseDiceFromTrack(Dice dice, int slotNumber, View view) {
        view.showMessage(ONLYDRAFTANDPLACE);
    }

    @Override
    public void moveDice(int rowFrom, int colFrom, int rowTo, int colTo, View view) {
        view.showMessage(ONLYDRAFTANDPLACE);
    }

    @Override
    public void incrementDice(View view) {
        view.showMessage(ONLYDRAFTANDPLACE);
    }

    @Override
    public void decrementDice(View view) {
        view.showMessage(ONLYDRAFTANDPLACE);
    }

    @Override
    public void chooseDiceValue(int value, View view) {
        view.showMessage(ONLYDRAFTANDPLACE);
    }

    @Override
    public void executeImplicitBehaviour() {
        //do nothing
    }
}
