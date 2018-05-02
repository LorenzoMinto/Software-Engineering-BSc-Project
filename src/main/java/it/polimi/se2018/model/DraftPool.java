package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.List;

public class DraftPool {

    //List of dices of the draftpool
    public List<Dice> dices;

    public DraftPool() {
        this(new ArrayList<>());
    }

    public DraftPool(List<Dice> dices){
        this.dices = dices;
    }

    //Rolls each dice in draftpool
    public void reroll() {

        for (Dice dice : dices) {
            dice.roll();
        }
    }

    //Removes from the draftpool the specified dice
    public boolean takeDice(Dice dice) {

        return dices.remove(dice);
    }

    //Add the specified dice to the draftpool
    public void addDice(Dice dice) {

        dices.add(dice);
    }
}
