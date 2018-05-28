package it.polimi.se2018.model;

import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.message.MVMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the DraftPool: the place of the game where the drafted dices are left
 * waiting for being placed somewhere (windowpattern or - at the end of the round - track)
 *
 * @author Federico Haag
 */
public class DraftPool extends Observable implements Serializable {

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

        notifyGame();
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

        if(dices.remove(dice)) {

            notifyGame();
            return true;

        }else {
            return false;
        }
    }


    /**
     * Add the specified dice to the draftpool
     *
     * @param dice the dice to be added to the DraftPool
     * @return if the operation succeeded
     */
    public void putDice(Dice dice) {
        dices.add(dice);
        notifyGame();
    }

    /**
     * Method to notify observers (Game) with the updated draftpool
     *
     * @author Jacopo Pio Gargano
     */
    private void notifyGame() {
        //NOTIFYING
        Map<String, Object> messageAttributes = new HashMap<>();

        //draftpool was necessarily updated
        messageAttributes.put("draftPool", this);

        notify(new MVMessage(MVMessage.types.DRAFTPOOL, messageAttributes));
    }
}
