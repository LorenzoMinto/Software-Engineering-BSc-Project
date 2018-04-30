package it.polimi.se2018.model;

import java.util.List;

public class TrackSlot {
    //TODO: ask question about rep exposition, if this is private then getter methods returns a copy and accessibility changes.
    private List<Dice> dices;

    public TrackSlot(List<Dice> dices) { this.dices = dices; }

    public void addDice(Dice dice) {
        dices.add(dice);
    }

    public boolean removeDice(Dice dice) {
        if (dices.contains(dice)) { return dices.remove(dice); }
        return false;
    }

}
