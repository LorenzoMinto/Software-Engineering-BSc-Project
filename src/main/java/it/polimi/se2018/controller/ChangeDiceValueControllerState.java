package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.ValueOutOfBoundsException;
import it.polimi.se2018.utils.Message;

import static it.polimi.se2018.utils.ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.ViewBoundMessageType.ERROR_MESSAGE;

/**
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
}
