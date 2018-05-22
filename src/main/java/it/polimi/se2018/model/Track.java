package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Class representing the Game's Track, where the leftover dices from the {@link DraftPool} are kept at the end of
 * every {@link Round}. The dices are stacked if more than one.
 *
 * @author Lorenzo Minto
 */
public class Track {


    /**
     * List of {@link TrackSlot}, on which the leftover dices of the draft pool of every round are kept.
     */
    private List<TrackSlot> slots = new ArrayList<>();

    /**
     * Creates new TrackSlot with the list of dice passed and appends it to the list.
     *
     * @param dices list of dices passed.
     */
    public void processDices(List<Dice> dices) {
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
        //TODO: aggiungi bound exc
        if (slotNumber < 0 || slotNumber >= slots.size()) {
            throw new IllegalArgumentException("TrackSlot selected doesn't exist.");
        }
        TrackSlot slot = slots.get(slotNumber);
        return slot.getDices();
    }

    /**
     * Removes the specified dice kept on the TrackSlot with specified slot number.
     *
     * @param dice the dice to be removed.
     * @param slotNumber the TrackSlot number from which to remove the dice.
     * @return whether or not the specified dice could be removed from the specified TrackSlot.
     */
    public void takeDice(Dice dice, int slotNumber){
        if (slotNumber >= slots.size()) {
            //TODO: bound exc
            throw new IllegalArgumentException("The selected track slot does not exist.");
        }
        TrackSlot slot = slots.get(slotNumber);
        slot.removeDice(dice);
    }

    /**
     * Puts given dice in the TrackSlot with specified slot number.
     *
     * @param dice the dice to be put.
     * @param slotNumber the TrackSlot number on which to put the dice.
     * @return whether or not the specified dice could be put on the specified TrackSlot.
     */
    public boolean putDice(Dice dice, int slotNumber){
        if (slotNumber >= slots.size()) {
            //TODO: inserisci bound exc
            throw new IllegalArgumentException("The selected track slot does not exist.");
        }
        TrackSlot slot = slots.get(slotNumber);
        slot.addDice(dice);
        return true;
    }

}