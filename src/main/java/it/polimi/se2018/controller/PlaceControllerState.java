package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.Message;

import java.util.EnumSet;
import java.util.Set;

import static it.polimi.se2018.utils.ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.ViewBoundMessageType.ERROR_MESSAGE;

/**
 *  This is the state in which the current player can place a dice on the own windowPattern.
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class PlaceControllerState extends ControllerState {

    /**
     * String used as content of acknowledgment message in placeDice()
     */
    private static final String DICE_PLACED = "Dice placed.";

    /**
     * String used as content of acknowledgment message in returnDiceToDraftPool()
     */
    private static final String DICE_RETURNED = "Dice returned to draft pool.";

    /**
     * String used as content of error message in placeDice()
     */
    private static final String MOVE_IS_ILLEGAL = "Move is illegal.";

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public PlaceControllerState(Controller controller) {
        super(controller);
        this.defaultMessage = PLACE_DICE;
    }

    @Override
    public Message placeDice(int row, int col) {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();
        WindowPattern pattern = currentTurn.getPlayer().getWindowPattern();

        if (controller.placementRule.isMoveAllowed(pattern, currentTurn.getDraftedDice(), row, col)
                && !pattern.isThereADice(row, col)) {
            Dice drafted = currentTurn.getDraftedDice();
            currentTurn.resetDraftedDice();
            pattern.putDiceOnCell(drafted, row, col);
            if (controller.getActiveToolCard() != null) {
                controller.setControllerState(controller.stateManager.getNextState(this));
            } else {
                if (currentTurn.hasUsedToolCard()) {
                    controller.setControllerState(controller.stateManager.getEndControllerState());
                } else {
                    controller.setControllerState(controller.stateManager.getToolCardState());
                }
            }
            return new Message(ACKNOWLEDGMENT_MESSAGE, DICE_PLACED);
        } else {
            return new Message(ERROR_MESSAGE, MOVE_IS_ILLEGAL);
        }
    }


    @Override
    public Message returnDiceToDraftPool() {
        Round currentRound = controller.game.getCurrentRound();
        Turn currentTurn = currentRound.getCurrentTurn();

        Dice draftedDice = currentTurn.getDraftedDice();

        if (draftedDice != null) {
            currentTurn.resetDraftedDice();

            currentRound.getDraftPool().putDice(draftedDice);


            if (controller.getActiveToolCard() != null) {
                controller.setControllerState(controller.stateManager.getNextState(this));
            } else {
                if (currentTurn.hasUsedToolCard()) {
                    controller.setControllerState(controller.stateManager.getEndControllerState());
                } else {
                    controller.setControllerState(controller.stateManager.getToolCardState());
                }
            }
            return new Message(ACKNOWLEDGMENT_MESSAGE, DICE_RETURNED);
        } else {
            return new Message(ERROR_MESSAGE, MOVE_IS_ILLEGAL);
        }
    }

    @Override
    public Set<Move> getStatePermissions() {
        return EnumSet.of(Move.PLACE_DICE_ON_WINDOWPATTERN, Move.RETURN_DICE_TO_DRAFTPOOL);
    }
}
