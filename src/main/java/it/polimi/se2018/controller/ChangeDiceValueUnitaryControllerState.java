package it.polimi.se2018.controller;

import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Turn;
import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.Move;

import java.util.EnumSet;
import java.util.Set;

import static it.polimi.se2018.utils.ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.ViewBoundMessageType.ERROR_MESSAGE;

/**
 * This is the state during which, due to a toolcard, current player can increment or decrement a dice value
 * @author Lorenzo Minto
 */
public class ChangeDiceValueUnitaryControllerState extends ControllerState {

    /**
     * String passed as content of error message in incrementDice()
     */
    private static final String CANNOT_INCREMENT_DRAFTED_DICE_VALUE = "Cannot increment drafted dice value.";

    /**
     * String passed as content of acknowledgment message in incrementDice()
     */
    private static final String DICE_INCREMENTED = "Dice value incremented.";

    /**
     * String passed as content of acknowledgment message in decrementDice()
     */
    private static final String DICE_DECREMENTED = "Dice value decremented.";

    /**
     * String passed as content of error message in decrementDice()
     */
    private static final String CANNOT_DECREMENT_DRAFTED_DICE_VALUE = "Cannot decrement drafted dice value.";

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public ChangeDiceValueUnitaryControllerState(Controller controller) {
        super(controller);
        this.defaultMessage = MIDDLE_OF_EFFECT;
    }

    @Override
    public Message incrementDice() {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();
        if (currentTurn.getDraftedDice().incrementValue()) {
            currentTurn.setDraftedDice(currentTurn.getDraftedDice());
            controller.setControllerState(controller.stateManager.getNextState(this));
        } else {
            return new Message(ERROR_MESSAGE, CANNOT_INCREMENT_DRAFTED_DICE_VALUE);
        }
        return new Message(ACKNOWLEDGMENT_MESSAGE, DICE_INCREMENTED);
    }

    @Override
    public Message decrementDice() {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();

        if (currentTurn.getDraftedDice().decrementValue()) {
            currentTurn.setDraftedDice(currentTurn.getDraftedDice());
            controller.setControllerState(controller.stateManager.getNextState(this));
        } else {
            return new Message(ERROR_MESSAGE, CANNOT_DECREMENT_DRAFTED_DICE_VALUE);
        }
        return new Message(ACKNOWLEDGMENT_MESSAGE, DICE_DECREMENTED);
    }

    @Override
    public Set<Move> getStatePermissions() {
        return EnumSet.of(Move.INCREMENT_DRAFTED_DICE, Move.DECREMENT_DRAFTED_DICE, Move.END_TURN);
    }
}
