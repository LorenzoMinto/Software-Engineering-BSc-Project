package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.message.CVMessage;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

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

        if (controller.placementRule.isMoveAllowed(pattern, pattern.getDiceOnCell(rowFrom, colFrom), rowTo, colTo)
                && pattern.moveDiceFromCellToCell(rowFrom, colFrom, rowTo, colTo)) {
            controller.movesCounter += 1;

            List<Integer> possibleMovesCount = new ArrayList<>();
            possibleMovesCount.addAll(controller.getActiveToolCard().getPossibleMovesCountSet());
            //if the moves counter is less than the maximum number of moves ask for another move
            if (possibleMovesCount.isEmpty() || controller.movesCounter < possibleMovesCount.get(possibleMovesCount.size())) {
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
    public CVMessage endToolCardEffect() {
        if (controller.getActiveToolCard().getPossibleMovesCountSet().contains(controller.movesCounter)) {
            this.controller.resetActiveToolCard();
            controller.setControllerState(controller.stateManager.getDraftControllerState());
            return new CVMessage(ACKNOWLEDGMENT_MESSAGE, "ToolCard effected ended.");
        } else {
            return new CVMessage(ERROR_MESSAGE,"Can't end the ToolCard effect now.");
        }
    }

    @Override
    public EnumSet<Move> getStatePermissions() {
        return EnumSet.of(Move.MOVE_DICE, Move.END_EFFECT);
    }
}
