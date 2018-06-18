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
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public ChangeDiceValueControllerState(Controller controller) {
        if (controller==null) { throw new IllegalArgumentException("Can't create a State Controller without a Controller");}
        this.controller = controller;
        this.defaultMessage = MIDDLE_OF_EFFECT;
    }

    @Override
    public Message chooseDiceValue(int value) {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();
        try {
            currentTurn.getDraftedDice().setValue(value);
        } catch (ValueOutOfBoundsException e) {
            return new Message(ERROR_MESSAGE, "Illegal value for dice.");
        }
        controller.setControllerState(controller.stateManager.getNextState(this));
        return new Message(ACKNOWLEDGMENT_MESSAGE,"Dice value changed.");
    }

    @Override
    public Set<Move> getStatePermissions() {
        return EnumSet.of(Move.CHANGE_DRAFTED_DICE_VALUE, Move.END_TURN);
    }
}
