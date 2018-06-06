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
public class PlaceControllerState extends ControllerState {

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public PlaceControllerState(Controller controller) {
        this.controller = controller;
        this.defaultMessage = PLACE_DICE;
    }

    @Override
    public CVMessage placeDice(int row, int col) {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();
        WindowPattern pattern = currentTurn.getPlayer().getWindowPattern();

        ControllerState next;
        EnumSet<Move> permissions;

        if (controller.placementRule.isMoveAllowed(pattern, currentTurn.getDraftedDice(), row, col)
                && pattern.putDiceOnCell(currentTurn.getDraftedDice(), row, col)) {
            currentTurn.resetDraftedDice();
            if (controller.getActiveToolCard() != null) {
                controller.setControllerState(controller.stateManager.getNextState(this));
                return new CVMessage(ACKNOWLEDGMENT_MESSAGE,"Dice placed!");
            } else {
                if (currentTurn.hasUsedToolCard()) {
                    controller.setControllerState(controller.stateManager.getEndControllerState());
                } else {
                    controller.setControllerState(controller.stateManager.getToolCardState());
                }
                return new CVMessage(ACKNOWLEDGMENT_MESSAGE,"Dice placed!");
            }
        } else {
            return new CVMessage(ERROR_MESSAGE,"Move is illegal. There's another dice in that position.");
        }
    }

    @Override
    public EnumSet<Move> getStatePermissions() {
        return EnumSet.of(Move.PLACE_DICE_ON_WINDOWPATTERN);
    }

}
