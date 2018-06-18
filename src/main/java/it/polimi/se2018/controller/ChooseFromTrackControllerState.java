package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.ValueOutOfBoundsException;
import it.polimi.se2018.utils.BadDiceReferenceException;
import it.polimi.se2018.utils.Message;

import java.util.EnumSet;
import java.util.Set;

import static it.polimi.se2018.utils.ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.ViewBoundMessageType.ERROR_MESSAGE;

/**
 *  This is the state during which, due to a toolcard, current player can draft a dice from the track
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
        if (controller==null) { throw new IllegalArgumentException("Can't create a State Controller without a Controller");}
        this.controller = controller;
        this.defaultMessage = MIDDLE_OF_EFFECT;
    }

    @Override
    public Message chooseDiceFromTrack(Dice dice, int slotNumber) {

        try {
            controller.game.getTrack().takeDice(dice, slotNumber);
        } catch (BadDiceReferenceException e) {
            return new Message(ERROR_MESSAGE,"Dice not in selected TrackSlot.");
        } catch (ValueOutOfBoundsException e) {
            return new Message(ERROR_MESSAGE,"Selected TrackSlot does not exist.");
        }

        controller.game.getCurrentRound().getCurrentTurn().setTrackChosenDice(dice);
        controller.game.getCurrentRound().getCurrentTurn().setSlotOfTrackChosenDice(slotNumber);
        controller.setControllerState(controller.stateManager.getNextState(this));

        return new Message(ACKNOWLEDGMENT_MESSAGE, Message.fastMap("message","Dice from Track chosen."));
    }

    @Override
    public Set<Move> getStatePermissions() {
        return EnumSet.of(Move.CHOOSE_DICE_FROM_TRACK);
    }
}
