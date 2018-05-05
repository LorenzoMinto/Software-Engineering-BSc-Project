package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.List;

public class DraftPool {

    //List of dices of the draftpool
    private List<Dice> dices;

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

    public List<Dice> getDices() {
        //TODO: implement this method
        return dices;
    }

    //Removes from the draftpool the specified dice
    public boolean draftDice(Dice dice) {

        return dices.remove(dice);
    }

    //Add the specified dice to the draftpool
    public void putDice(Dice dice) {

        dices.add(dice);
    }

    public List<Dice> empty(){
        List<Dice> d = new ArrayList<>(this.dices);
        this.dices = new ArrayList<>();
        return d;
    }
}
