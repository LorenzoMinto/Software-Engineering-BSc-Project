package it.polimi.se2018.controller;

import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Turn;
import it.polimi.se2018.utils.Message;

import static it.polimi.se2018.utils.ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.ViewBoundMessageType.ERROR_MESSAGE;

public class ChangeDiceValueUnitaryControllerState extends ControllerState {

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public ChangeDiceValueUnitaryControllerState(Controller controller) {
        this.controller = controller;
        this.defaultMessage = MIDDLE_OF_EFFECT;
    }

    @Override
    public Message incrementDice() {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();
        if (currentTurn.getDraftedDice().incrementValue()) {
            controller.setControllerState(controller.stateManager.getNextState(this));
        } else {
            return new Message(ERROR_MESSAGE, "Cannot increment drafted dice's value.");
        }
        return new Message(ACKNOWLEDGMENT_MESSAGE,"Dice incremented.");
    }

    @Override
    public Message decrementDice() {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();

        if (currentTurn.getDraftedDice().decrementValue()) {
            controller.setControllerState(controller.stateManager.getNextState(this));
        } else {
            return new Message(ERROR_MESSAGE, "Cannot decrement drafted dice's value.");
        }
        return new Message(ACKNOWLEDGMENT_MESSAGE,"Dice decremented.");
    }
}
