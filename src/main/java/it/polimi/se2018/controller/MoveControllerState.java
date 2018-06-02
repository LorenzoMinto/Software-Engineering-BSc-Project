package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.message.CVMessage;

import java.util.EnumSet;

import static it.polimi.se2018.utils.message.CVMessage.types.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.message.CVMessage.types.ERROR_MESSAGE;

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
    public CVMessage moveDice(int rowFrom, int colFrom, int rowTo, int colTo) {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();
        WindowPattern pattern = currentTurn.getPlayer().getWindowPattern();

        if (controller.placementRule.checkIfMoveIsAllowed(pattern, pattern.getDiceOnCell(rowFrom, colFrom), rowTo, colTo)
                && pattern.moveDiceFromCellToCell(rowFrom, colFrom, rowTo, colTo)) {
            controller.movesCounter += 1;
            if (controller.movesCounter <= 2) {
                controller.setControllerState(controller.stateManager.getNextState(this));

                return new CVMessage(ACKNOWLEDGMENT_MESSAGE,"Move made.");
            } else {
                controller.setControllerState(controller.stateManager.getEndToolCardEffectControllerState());
                return new CVMessage(ACKNOWLEDGMENT_MESSAGE,"Move made.");
            }
        } else {
            return new CVMessage(ERROR_MESSAGE,"Can't make this move.");
        }
    }

    @Override
    public EnumSet<Move> getStatePermissions() {
        return EnumSet.of(Move.MOVE_DICE);
    }
}
