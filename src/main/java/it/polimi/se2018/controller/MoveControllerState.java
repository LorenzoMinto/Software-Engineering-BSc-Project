package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;


public class MoveControllerState implements ControllerState {
    Controller controller;

    private static final String MIDDLEOFEFFECT= "Can't do. You are in the middle of a Toolcard effect";


    public MoveControllerState(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void draftDiceFromDraftPool(Dice dice, View view) {
        view.showMessage(MIDDLEOFEFFECT);
    }

    @Override
    public void placeDice(int row, int col, View view) {
        view.showMessage(MIDDLEOFEFFECT);
    }

    @Override
    public void useToolCard(Player player, ToolCard toolcard, View view) {
        view.showMessage(MIDDLEOFEFFECT);
    }

    @Override
    public void chooseDiceFromTrack(Dice dice, int slotNumber, View view) {
        view.showMessage(MIDDLEOFEFFECT);
    }

    @Override
    public void moveDice(int rowFrom, int colFrom, int rowTo, int colTo, View view) {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();
        WindowPattern pattern = currentTurn.getPlayer().getWindowPattern();

        //check if move conforms to current placementRules and general physical constraints
        if (controller.placementRule.checkIfMoveIsAllowed(pattern, pattern.getDiceOnCell(rowFrom, colFrom), rowTo, colTo)
                && pattern.moveDiceFromCellToCell(rowFrom, colFrom, rowTo, colTo)) {
            view.showMessage("Move made.");
            controller.movesCounter += 1;
            if (controller.movesCounter < 2) {
                controller.setControllerState(controller.stateManager.getNextState(this));
            } else {
                controller.setControllerState(controller.stateManager.getDraftControllerState());
            }
        } else {
            view.showMessage("Can't make this move.");
        }
    }

    @Override
    public void incrementDice(View view) {
        view.showMessage(MIDDLEOFEFFECT);
    }

    @Override
    public void decrementDice(View view) {
        view.showMessage(MIDDLEOFEFFECT);
    }

    @Override
    public void chooseDiceValue(int value, View view) {
        view.showMessage(MIDDLEOFEFFECT);
    }

    @Override
    public void executeImplicitBehaviour() {
        //do nothing
    }
}
