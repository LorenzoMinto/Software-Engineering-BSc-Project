package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.message.CVMessage;
import it.polimi.se2018.utils.message.Message;

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
    public Message incrementDice() {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();
        if (currentTurn.hasDrafted()) {
            if (currentTurn.getDraftedDice().incrementValue()) {
                controller.setControllerState(controller.stateManager.getNextState(this));
            }
        } else {
            return NO_DICE_DRAFTED;
        }
        return new CVMessage("Dice incremented.");
    }

    @Override
    public Message decrementDice() {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();
        if (currentTurn.hasDrafted()) {
            if (currentTurn.getDraftedDice().decrementValue()) {

                controller.setControllerState(controller.stateManager.getNextState(this));
            }
        } else {
            return NO_DICE_DRAFTED;
        }
        return new CVMessage("Dice decremented.");
    }

    @Override
    public Message chooseDiceValue(int value) {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();
        if (currentTurn.hasDrafted()) {
            //NOTE: this assumes value is a legal value, otherwise need bool returned from setValue
            currentTurn.getDraftedDice().setValue(value);
            controller.setControllerState(controller.stateManager.getNextState(this));
        } else {
            return NO_DICE_DRAFTED;
        }
        return new CVMessage("Dice value changed.");
    }
}
