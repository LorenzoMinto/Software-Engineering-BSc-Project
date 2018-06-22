package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.Message;

import java.util.EnumSet;
import java.util.Set;

import static it.polimi.se2018.utils.ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.ViewBoundMessageType.ERROR_MESSAGE;

/**
 *  This is the state during which the current player can draft a dice from draftpool.
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class DraftControllerState extends ControllerState {

    /**
     * String used as content of acknowledgment message in draftDiceFromDraftPool()
     */
    private static final String DICE_DRAFTED = "Dice drafted.";

    /**
     * String used as content of error message in draftDiceFromDraftPool()
     */
    private static final String DICE_NOT_IN_DRAFT_POOL = "Can't draft a dice that is not in the Draft Pool.";

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public DraftControllerState(Controller controller) {
        super(controller);
        this.defaultMessage = ONLY_DRAFT_AND_PLACE;
    }

    @Override
    public Message draftDiceFromDraftPool(Dice dice) {
        Game game = controller.game;
        Round currentRound = game.getCurrentRound();

        if (currentRound.getDraftPool().draftDice(dice)) {
            currentRound.getCurrentTurn().setDraftedDice(dice);
        } else{
            return new Message(ERROR_MESSAGE, DICE_NOT_IN_DRAFT_POOL);
        }

        if (controller.getActiveToolCard() != null) {
            controller.setControllerState(controller.stateManager.getNextState(this));
        } else {
            controller.setControllerState(controller.stateManager.getPlaceState());
        }

        return new Message(ACKNOWLEDGMENT_MESSAGE, DICE_DRAFTED);
    }

    @Override
    public Message placeDice(int row, int col) {
        return new Message(ERROR_MESSAGE, NO_DICE_DRAFTED);
    }

    @Override
    public Set<Move> getStatePermissions() {
        return EnumSet.of(Move.DRAFT_DICE_FROM_DRAFTPOOL, Move.END_TURN);
    }
}
