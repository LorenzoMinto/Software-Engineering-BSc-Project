package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.ValueOutOfBoundsException;
import it.polimi.se2018.utils.Message;

import java.util.EnumSet;
import java.util.Set;

import static it.polimi.se2018.utils.ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.ViewBoundMessageType.ERROR_MESSAGE;

/**
 *  This is the state during which, due to a toolcard, current player can change the value of the drafted dice
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class ChangeDiceValueControllerState extends ControllerState {

    /**
     * String passed as content of error message in chooseDiceValue()
     */
    private static final String ILLEGAL_VALUE_FOR_DICE = "Illegal value for dice.";

    /**
     * String passed as content of acknowledgment message in chooseDiceValue()
     */
    private static final String DICE_VALUE_CHANGED = "Dice value changed.";

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public ChangeDiceValueControllerState(Controller controller) {
        super(controller);
        this.defaultMessage = MIDDLE_OF_EFFECT;
    }

    @Override
    public Message chooseDiceValue(int value) {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();
        try {
            currentTurn.getDraftedDice().setValue(value);
        } catch (ValueOutOfBoundsException e) {
            return new Message(ERROR_MESSAGE, ILLEGAL_VALUE_FOR_DICE);
        }
        currentTurn.setDraftedDice(currentTurn.getDraftedDice());
        controller.setControllerState(controller.stateManager.getNextState(this));
        return new Message(ACKNOWLEDGMENT_MESSAGE, DICE_VALUE_CHANGED);
    }

    @Override
    public Set<Move> getStatePermissions() {
        return EnumSet.of(Move.CHANGE_DRAFTED_DICE_VALUE, Move.END_TURN);
    }
}
