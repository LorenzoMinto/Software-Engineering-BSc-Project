package it.polimi.se2018.model;

import it.polimi.se2018.utils.BadDiceReferenceException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the single slots of the Game's Track (one for each round).
 *
 * @author Lorenzo Minto
 */
public class TrackSlot implements Serializable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -5716313610769359452L;

    /**
     * String passed as message of IllegalArgumentException when referenced dice is null
     */
    private static final String NULL_DICE = "Can't use or reference a null dice.";

    /**
     * String passed as message of IllegalArgumentException when it asked to remove a dice from a trackslot
     * but the given dice reference is not contained in this trackslot.
     */
    private static final String NO_DICE_IN_TRACKSLOT = "The given dice is not present in the track slot.";

    /**
     * List of dices kept in the TrackSlot
     */
    private List<Dice> dices;

    /**
     * Class constructor.
     *
     * @param dices the list of dices with which to initialize the TrackSlot.
     */
    public TrackSlot(List<Dice> dices) { this.dices = dices; }

    /**
     * Adds a given dice to the TrackSlot's list of dices.
     *
     * @param dice the dice to be added.
     */
    public void addDice(Dice dice) {
        if(dice == null){ throw new IllegalArgumentException(NULL_DICE);}
        dices.add(dice);
    }

    /**
     * Removes, if possible, a specified dice from the TrackSlot's dices.
     *
     * @param dice the dice to be removed.
     * @throws BadDiceReferenceException if the dice does not exists
     */
    public void removeDice(Dice dice) throws BadDiceReferenceException {
        if(dice == null){ throw new IllegalArgumentException(NULL_DICE);}
        if (dices.contains(dice)) {
            dices.remove(dice);
        } else {
            throw new BadDiceReferenceException(NO_DICE_IN_TRACKSLOT);
        }
    }

    /**
     * Returns a copy of the list of dices in the TrackSlot.
     *
     * @return a copy of the list of dices.
     */
    public List<Dice> getDices() {
        return dices;
    }

    /**
     * Returns a new TrackSlot instance with the same properties and data of the current one.
     *
     * @return a new TrackSlot instance with the same properties and data of the current one
     */
    public TrackSlot copy(){
        return new TrackSlot( new ArrayList<>(this.dices) );
    }

    /**
     * Returns the string representation of the Track Slot.
     *
     * @return the string representation of the Track Slot
     */
    @Override
    public String toString() {
        return this.dices.toString();
    }
}
