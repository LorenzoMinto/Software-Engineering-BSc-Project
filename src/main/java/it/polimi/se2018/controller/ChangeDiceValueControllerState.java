package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.ValueOutOfBoundsException;
import it.polimi.se2018.utils.message.CVMessage;

import static it.polimi.se2018.utils.message.CVMessage.types.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.message.CVMessage.types.ERROR_MESSAGE;

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
    public CVMessage incrementDice() {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();
        if (currentTurn.getDraftedDice().incrementValue()) {
            controller.setControllerState(controller.stateManager.getNextState(this));
        } else {
            return new CVMessage(ERROR_MESSAGE, "Cannot increment drafted dice's value.");
        }
        return new CVMessage(ACKNOWLEDGMENT_MESSAGE,"Dice incremented.");
    }

    @Override
    public CVMessage decrementDice() {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();

        if (currentTurn.getDraftedDice().decrementValue()) {
            controller.setControllerState(controller.stateManager.getNextState(this));
        } else {
            return new CVMessage(ERROR_MESSAGE, "Cannot decrement drafted dice's value.");
        }
        return new CVMessage(ACKNOWLEDGMENT_MESSAGE,"Dice decremented.");
    }

    @Override
    public CVMessage chooseDiceValue(int value) {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();
        try {
            currentTurn.getDraftedDice().setValue(value);
        } catch (ValueOutOfBoundsException e) {
            return new CVMessage(ERROR_MESSAGE, "Illegal value for dice.");
        }
        controller.setControllerState(controller.stateManager.getNextState(this));
        return new CVMessage(ACKNOWLEDGMENT_MESSAGE,"Dice value changed.");
    }
}
