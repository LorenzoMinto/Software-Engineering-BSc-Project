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
        if(dice == null){ throw new IllegalArgumentException("Can't add null dice to the track slot");}
        dices.add(dice);
    }

    /**
     * Removes, if possible, a specified dice from the TrackSlot's dices.
     *
     * @param dice the dice to be removed.
     * @throws BadDiceReferenceException if the dice does not exists
     */
    public void removeDice(Dice dice) throws BadDiceReferenceException {
        if(dice == null){ throw new IllegalArgumentException("Can't remove a null dice from the track slot.");}
        if (dices.contains(dice)) {
            dices.remove(dice);
        } else {
            throw new BadDiceReferenceException("Asked to remove a dice that is not present in the track slot.");
        }
    }

    /**
     * Returns a copy of the list of dices in the TrackSlot.
     *
     * @return a copy of the list of dices.
     */
    public List<Dice> getDices() {
        return new ArrayList<>(dices);
    }

    public TrackSlot copy(){
        return new TrackSlot( new ArrayList<>(this.dices) );
    }

    @Override
    public String toString() {
        return this.dices.toString();
    }
}
