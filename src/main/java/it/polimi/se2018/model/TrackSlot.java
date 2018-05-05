package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.List;

public class TrackSlot {

    private List<Dice> dices;

    public TrackSlot(List<Dice> dices) { this.dices = dices; }

    public void addDice(Dice dice) {
        dices.add(dice);
    }

    public boolean removeDice(Dice dice) {
        if (dices.contains(dice)) { return dices.remove(dice); }
        return false;
    }

    public List<Dice> getDices() {
        return new ArrayList<>(dices);
    }

}
