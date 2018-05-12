package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

/**
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class MoveControllerState extends ControllerState {

    public MoveControllerState(Controller controller) {
        this.controller = controller;
        this.defaultMessage = MIDDLE_OF_EFFECT;
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
}
