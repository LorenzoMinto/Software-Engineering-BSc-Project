package it.polimi.se2018.model;

import java.util.Random;

public class Dice {

    private int value;

    private DiceColors color;

    /**
     * @param color The color of the Dice
     */
    public Dice(DiceColors color) {
        this.color = color;
        this.roll();
    }

    //Changes the Dice value
    public void roll() {
        Random r = new Random();
        this.value = r.nextInt(5)+1;
    }

    public void rollOver() {
        //In a standard dice the sum of the opposites sides is always 7
        this.value = 7 - this.value;
    }
}
