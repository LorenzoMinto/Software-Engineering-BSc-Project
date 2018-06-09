package it.polimi.se2018.model;

import it.polimi.se2018.utils.BadDiceReferenceException;
import it.polimi.se2018.utils.ValueOutOfBoundsException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * Class representing the Game's Track, where the leftover dices from the {@link DraftPool} are kept at the end of
 * every {@link Round}. The dices are stacked if more than one.
 *
 * @author Lorenzo Minto
 */
public class Track implements Serializable, Iterable<TrackSlot> {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 5006029947494003833L;
    /**
     * String passed as message of IllegalArgumentException when referenced dice is null
     */
    private static final String NULL_DICE = "Can't use or reference a null dice";
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
    }

    /**
     * Returns the size of the Track, that is how many TrackSlot it contains
     * @see TrackSlot
     * @return how many TrackSlot it contains
     */
    public int size(){
        return slots.size();
    }

    @Override
    public Iterator<TrackSlot> iterator() {
        return new Iterator<TrackSlot>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < slots.size();
            }

            @Override
            public TrackSlot next() {
                if(!hasNext()){
                    throw new NoSuchElementException();
                }
                return slots.get(currentIndex++).copy();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }


    /** Creates a copy of the track
     * @return a copy of the track
     */
    public Track copy() {
        Track trackCopy = new Track();
        this.slots.forEach(slot->trackCopy.processDices(slot.getDices()));
        return trackCopy;
    }
}