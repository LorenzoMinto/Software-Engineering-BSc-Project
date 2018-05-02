package it.polimi.se2018.model;

import java.util.Random;

public class Dice {

    private int value;

    private DiceColors color;

    public Dice(DiceColors color) {
        this(color,1);
        this.roll();
    }

    public Dice(DiceColors color, int value) {
        if(color==DiceColors.NOCOLOR){ throw new IllegalArgumentException("ERROR: Cannot create a " +
                "Dice with no color."); }
        if(value > 0 && value <= 6){ throw new IllegalArgumentException("ERROR: Cannot create a " +
                "dice with value not in range [1,6]."); }
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

    public boolean incrementValue(){
        if( this.value < 6 ){
            this.value += 1;
            return true;
        }
        return false;
    }

    public boolean decrementValue(){
        if( this.value > 1 ){
            this.value -= 1;
            return true;
        }
        return false;
    }

    //Returns a new dice instance with same properties of this dice
    public Dice copy(){

        return new Dice(this.color, this.value);
    }

    @Override
    public String toString() {
        return this.value + ":" + this.color.toOneLetter();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Dice)) {
            return false;
        }

        Dice c = (Dice) o;

        return this.value == c.getValue()
                && this.color == c.getColor();
    }
}
