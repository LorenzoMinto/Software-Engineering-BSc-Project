package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.networking.message.CVMessage;
import it.polimi.se2018.networking.message.Message;
import it.polimi.se2018.utils.BadDiceReferenceException;

/**
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class ChooseFromTrackControllerState extends ControllerState {

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public ChooseFromTrackControllerState(Controller controller) {
        this.controller = controller;
        this.defaultMessage = MIDDLE_OF_EFFECT;
    }

    @Override
    public Message chooseDiceFromTrack(Dice dice, int slotNumber) {

        try {
            controller.game.getTrack().takeDice(dice, slotNumber);
        } catch (BadDiceReferenceException e) {
            return new CVMessage("Can't choose this Dice.");
        }

        controller.game.getCurrentRound().getCurrentTurn().setTrackChosenDice(dice);
        controller.game.getCurrentRound().getCurrentTurn().setSlotOfTrackChosenDice(slotNumber);
        controller.setControllerState(controller.stateManager.getNextState(this));

        return new CVMessage("Dice from Track chosen.");
    }
}
