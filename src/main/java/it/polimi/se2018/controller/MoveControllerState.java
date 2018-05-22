package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.networking.message.ControllerMessage;
import it.polimi.se2018.networking.message.Message;

/**
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class MoveControllerState extends ControllerState {

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public MoveControllerState(Controller controller) {
        this.controller = controller;
        this.defaultMessage = MIDDLE_OF_EFFECT;
    }

    @Override
    public Message moveDice(int rowFrom, int colFrom, int rowTo, int colTo) {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();
        WindowPattern pattern = currentTurn.getPlayer().getWindowPattern();

        //check if move conforms to current placementRules and general physical constraints
        if (controller.placementRule.checkIfMoveIsAllowed(pattern, pattern.getDiceOnCell(rowFrom, colFrom), rowTo, colTo)
                && pattern.moveDiceFromCellToCell(rowFrom, colFrom, rowTo, colTo)) {
            //TODO: what if multiple messages needs to be returned to the view, sequentially
            controller.movesCounter += 1;
            if (controller.movesCounter <= 2) {
                controller.setControllerState(controller.stateManager.getNextState(this));
                return new ControllerMessage("Move made.");
            } else {
                controller.setControllerState(controller.stateManager.getEndToolCardEffectControllerState());
                return new ControllerMessage("Move made.");
            }
        } else {
            return new ControllerMessage("Can't make this move.");
        }
    }
}
