package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the single slots of the Game's Track (one for each round).
 *
 * @author Lorenzo Minto
 */
public class TrackSlot {

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
        dices.add(dice);
    }

    /**
     * Removes, if possible, a specified dice from the TrackSlot's dices.
     *
     * @param dice the dice to be removed.
     * @return whether or not specified dice could be removed from the TrackSlot's dices.
     */
    public boolean removeDice(Dice dice) {
        return dices.contains(dice) && dices.remove(dice);
    }

    /**
     * Returns a copy of the list of dices in the TrackSlot.
     *
     * @return a copy of the list of dices.
     */
    public List<Dice> getDices() {
        return new ArrayList<>(dices);
    }

}
