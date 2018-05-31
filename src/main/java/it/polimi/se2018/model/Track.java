package it.polimi.se2018.model;

import it.polimi.se2018.utils.BadDiceReferenceException;
import it.polimi.se2018.utils.ValueOutOfBoundsException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Class representing the Game's Track, where the leftover dices from the {@link DraftPool} are kept at the end of
 * every {@link Round}. The dices are stacked if more than one.
 *
 * @author Lorenzo Minto
 */
public class Track implements Serializable {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 5006029947494003833L;

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
            throw new ValueOutOfBoundsException("TrackSlot selected doesn't exist.");
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
            throw new ValueOutOfBoundsException("The selected track slot does not exist.");
        }
        TrackSlot slot = slots.get(slotNumber);
        slot.removeDice(dice); //throws BadDiceReferenceException
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
            throw new ValueOutOfBoundsException("The selected track slot does not exist.");
        }
        TrackSlot slot = slots.get(slotNumber);
        slot.addDice(dice);
        return true;
    }

}