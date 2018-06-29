package it.polimi.se2018.model;

import it.polimi.se2018.utils.*;
import it.polimi.se2018.utils.Observable;

import java.io.Serializable;
import java.util.*;


/**
 * Class representing the Game's Track, where the leftover dices from the {@link DraftPool} are kept at the end of
 * every {@link Round}. The dices are stacked if more than one.
 *
 * @author Lorenzo Minto
 */
public class Track extends Observable implements Serializable{
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 5006029947494003833L;

    /**
     * String passed as message of IllegalArgumentException when referenced dice is null
     */
    private static final String NULL_DICE = "Can't use or reference a null dice.";

    /**
     * String passed as message of ValueOutOfBoundsException when referenced to a trackslot that does not exists.
     */
    private static final String SELECTED_TRACK_SLOT_DOES_NOT_EXIST = "TrackSlot selected doesn't exist.";


    /**
     * List of {@link TrackSlot}, on which the leftover dices of the draft pool of every round are kept.
     */
    private ArrayList<TrackSlot> slots = new ArrayList<>();

    /**
     * Creates new TrackSlot with the list of dice passed and appends it to the list.
     * Notifies game of the updates
     *
     * @param dices list of dices passed.
     */
    public void processDicesAndNotify(List<Dice> dices) {
        processDices(dices);
        notifyGame();
    }


    /**
     * Returns true if the track is empty
     *
     * @return if the track is empty, false otherwise
     */
    public boolean isEmpty() {
        return slots.isEmpty();
    }

    /**
     * Creates new TrackSlot with the list of dice passed and appends it to the list.
     *
     * @param dices list of dices passed.
     */
    public void processDices(List<Dice> dices) {
        if(dices == null){ throw new IllegalArgumentException(NULL_DICE);}
        TrackSlot slot = new TrackSlot(dices);
        slots.add(slot);
    }

    /**
     * Return a list of dices kept in the TrackSlot with given slot number.
     *
     * @param slotNumber the number of the wanted TrackSlot.
     * @return the list of dices kept in the wanted TrackSlot.
     */
    public List<Dice> getDicesFromSlotNumber(int slotNumber) {

        if (slotNumber < 0 || slotNumber >= slots.size()) {
            throw new ValueOutOfBoundsException(SELECTED_TRACK_SLOT_DOES_NOT_EXIST);
        }
        TrackSlot slot = slots.get(slotNumber);
        return slot.getDices();
    }

    /**
     * Removes the specified dice kept on the TrackSlot with specified slot number.
     *
     * @param dice the dice to be removed.
     * @param slotNumber the TrackSlot number from which to remove the dice.
     * @throws BadDiceReferenceException if the dice does not exist
     */
    public void takeDice(Dice dice, int slotNumber) throws BadDiceReferenceException {

        if (slotNumber >= slots.size()) {
            throw new ValueOutOfBoundsException(SELECTED_TRACK_SLOT_DOES_NOT_EXIST);
        }
        TrackSlot slot = slots.get(slotNumber);
        slot.removeDice(dice); //throws BadDiceReferenceException
        notifyGame();
    }

    /**
     * Puts given dice in the TrackSlot with specified slot number.
     *
     * @param dice the dice to be put.
     * @param slotNumber the TrackSlot number on which to put the dice.
     */
    public void putDice(Dice dice, int slotNumber){
        if(dice == null){ throw new IllegalArgumentException(NULL_DICE);}
        if (slotNumber >= slots.size()) {
            throw new ValueOutOfBoundsException(SELECTED_TRACK_SLOT_DOES_NOT_EXIST);
        }
        TrackSlot slot = slots.get(slotNumber);
        slot.addDice(dice);
        notifyGame();
    }

    /**
     * Returns the size of the Track, that is how many TrackSlot it contains
     * @see TrackSlot
     * @return how many TrackSlot it contains
     */
    public int size(){
        return slots.size();
    }

    /** Creates a copy of the track
     * @return a copy of the track
     */
    public Track copy() {
        Track trackCopy = new Track();
        for(TrackSlot trackSlot : this.slots){
            List<Dice> dices = trackSlot.getDices();
            List<Dice> copiedDices = new ArrayList<>();
            for(Dice dice : dices){
                copiedDices.add(dice.copy());
            }
            trackCopy.processDices(copiedDices);
        }
        return trackCopy;
    }

    /**
     * Method to notify observers (Game) with the updated track
     *
     * @author Jacopo Pio Gargano
     */
    private void notifyGame() {
        Map<String, Object> messageAttributes = new HashMap<>();
        messageAttributes.put("track", this.copy());

        notify(new Message(ViewBoundMessageType.SOMETHING_CHANGED_IN_TRACK, messageAttributes));
    }

    @Override
    public String toString() {
        String returnString = "";
        for(TrackSlot trackSlot : this.slots){
            returnString = returnString.concat("Slot:");
            for(Dice dice : trackSlot.getDices()){
                returnString = returnString.concat(dice.toString());
            }
            returnString = returnString.concat(".");
        }
        return returnString;
    }
}