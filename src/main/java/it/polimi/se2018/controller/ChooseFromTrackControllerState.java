package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.ValueOutOfBoundsException;
import it.polimi.se2018.utils.Message;

import java.util.EnumSet;
import java.util.List;
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
     * String used as content of error message in chooseDiceFromTrack()
     */
    private static final String DICE_NOT_IN_SELECTED_TRACK_SLOT = "Dice not in selected TrackSlot.";

    /**
     * String used as content of error message in chooseDiceFromTrack()
     */
    private static final String SELECTED_TRACK_SLOT_DOES_NOT_EXIST = "The selected TrackSlot does not exist.";

    /**
     * String used as content of acknowledgment message in chooseDiceFromTrack()
     */
    private static final String DICE_FROM_TRACK_CHOSEN = "Dice from Track chosen.";

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public ChooseFromTrackControllerState(Controller controller) {
        super(controller);
        this.defaultMessage = MIDDLE_OF_EFFECT;
    }

    @Override
    public Message chooseDiceFromTrack(Dice dice, int slotNumber) {

        List<Dice> dicesFromSlotNumber;

        try{
            dicesFromSlotNumber= controller.game.getTrack().getDicesFromSlotNumber(slotNumber);
        }catch (ValueOutOfBoundsException e){
            return new Message(ERROR_MESSAGE, SELECTED_TRACK_SLOT_DOES_NOT_EXIST);
        }
        if (dicesFromSlotNumber.contains(dice)) {
            controller.game.getCurrentRound().getCurrentTurn().setTrackChosenDice(dice);
            controller.game.getCurrentRound().getCurrentTurn().setSlotOfTrackChosenDice(slotNumber);
            controller.setControllerState(controller.stateManager.getNextState(this));
            return new Message(ACKNOWLEDGMENT_MESSAGE, DICE_FROM_TRACK_CHOSEN);
        } else {
            return new Message(ERROR_MESSAGE, DICE_NOT_IN_SELECTED_TRACK_SLOT);
        }
    }

    @Override
    public Set<Move> getStatePermissions() {
        return EnumSet.of(Move.CHOOSE_DICE_FROM_TRACK, Move.END_EFFECT);
    }
}
