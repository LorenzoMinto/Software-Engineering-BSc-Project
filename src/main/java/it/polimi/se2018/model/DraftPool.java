package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the DraftPool: the place of the game where the drafted dices are left
 * waiting for being placed somewhere (windowpattern or - at the end of the round - track)
 *
 * @author Federico Haag
 */
public class DraftPool {

    /**
     * List of dices of the draftpool
     */
    private List<Dice> dices;

    /**
     * Constructor of an empty DraftPool
     */
    public DraftPool() {
        this(new ArrayList<>());
    }

    /**
     * Constructor of a DraftPool containing the give dices
     *
     * @param dices the dices that are contained in the new DraftPool
     */
    public DraftPool(List<Dice> dices){
        if(dices == null){ throw new IllegalArgumentException("ERROR: Can't create a draftpool with null dices.");}
        this.dices = dices;
    }

    /**
     * Rolls each dice in draftpool.
     * @see Dice#roll()
     */
    public void reroll() {

        for (Dice dice : dices) {
            dice.roll();
        }
    }

    /**
     * Returns the list of dices contained in the DraftPool
     *
     * @return the list of dices contained in the DraftPool
     */
    public List<Dice> getDices() {
        List<Dice> d = new ArrayList<>();

        for(Dice dice : dices){
            d.add( dice.copy() );
        }

        return d;
    }

    /**
     * Removes from the DraftPool the specified dice
     *
     * @param dice the dice to be removed from the DraftPool
     * @return if the removal succeeded or not
     */
    public boolean draftDice(Dice dice) {

        return dices.remove(dice);
    }

    /**
     * Add the specified dice to the draftpool
     *
     * @param dice the dice to be added to the DraftPool
     * @return if the operation succeeded
     */
    public boolean putDice(Dice dice) {

        return dices.add(dice);
    }
}
