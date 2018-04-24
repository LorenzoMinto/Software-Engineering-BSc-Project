package it.polimi.se2018.model;

import java.util.Random;

public class Dice {

    private int value;

    private DiceColors color;

    public Dice(DiceColors color) {

        this.color = color;
        this.roll();
    }

    private Dice(DiceColors color, int value) {

        this.color = color;
        this.value = value;
    }

    //Returns value of the dice
    public int getValue() {
        return value;
    }

    //Returns color of the dice
    public DiceColors getColor() {
        return color;
    }

    //Rolls the Dice
    public void roll() {

        Random r = new Random();
        this.value = r.nextInt(5)+1;
    }

    //Rolls over the Dice
    public void rollOver() {

        //In a standard dice the sum of the opposites sides is always 7
        this.value = 7 - this.value;
    }

    //Returns a new dice instance with same properties of this dice
    public Dice copy(){

        return new Dice(this.color, this.value);
    }
}
