package it.polimi.se2018.controller;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.ToolCard;

public class StartControllerState implements ControllerState {
    Controller controller;

    public StartControllerState(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void draftDiceFromDraftPool(Dice dice) {
        //work magic on model here
        controller.setControllerState(controller.getPlaceState());
    }

    @Override
    public void placeDice(int row, int col) {
        System.out.println("Can't do. You have to choose a dice from the draft pool first.");
    }

    @Override
    public boolean useToolCard(ToolCard toolcard, Player player) {
        if (controller.canUseSpecificToolCard(player, toolcard)) {
            controller.setActiveToolCard(toolcard);
            controller.setControllerState(toolcard.nextState(this));
            return true;
        }
        return false;
    }

    @Override
    public void draftDiceFromTrack(Dice dice) {
        System.out.println("Can't do. Choose a dice from the draft pool or activate a ToolCard");
    }

    @Override
    public void moveDice(int rowFrom, int colFrom, int rowTo, int colTo) {
        System.out.println("Can't do. Choose a dice from the draft pool or activate a ToolCard");
    }

    @Override
    public void incrementDice() {
        System.out.println("Can't do. Choose a dice from the draft pool or activate a ToolCard");
    }

    @Override
    public void deincrementDice() {
        System.out.println("Can't do. Choose a dice from the draft pool or activate a ToolCard");
    }

    @Override
    public void executeImplicitBehaviour() {
        //
    }
}
