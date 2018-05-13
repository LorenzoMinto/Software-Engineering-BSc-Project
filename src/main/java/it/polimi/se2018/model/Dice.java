package it.polimi.se2018.model;

import java.util.Objects;
import java.util.Random;

/**
 * Class that represents a Dice with 6 sides (value from 1 to 6)
 *
 * @author Federico Haag
 */
public class Dice {

    /**
     * The value of the Dice
     */
    private int value;

    /**
     * The color of the Dice
     */
    private DiceColors color;

    /**
     * Constructor of a new Dice of a given color
     *
     * @param color the color of the new Dice
     */
    public Dice(DiceColors color) {
        this(color,1);
        this.roll();
    }

    /**
     * Constructor of a new Dice of a given color and value
     *
     * @param color the color of the new Dice
     * @param value the value of the new Dice
     */
    public Dice(DiceColors color, int value) {
        if(color==DiceColors.NOCOLOR){ throw new IllegalArgumentException("ERROR: Cannot create a Dice with no color."); }
        if(value <= 0 || value > 6){ throw new IllegalArgumentException("ERROR: Cannot create a dice with value not in range [1,6]."); }

        this.color = color;
        this.value = value;
    }

    /**
     * Sets the value of the Dice to the one given
     *
     * @param value the value to be setted to the Dice
     */
    public void setValue(int value) {
        if(value <= 0 || value > 6){ throw new IllegalArgumentException("ERROR: Cannot create a dice with value not in range [1,6]."); }
        this.value = value;
    }

    /**
     * Returns the value of the Dice
     *
     * @return the value of the Dice
     */
    public int getValue() {
        return value;
    }


    /**
     * Returns the color of the Dice
     *
     * @return the color of the Dice
     */
    public DiceColors getColor() {
        return color;
    }


    /**
     * Rolls the Dice. Means that assign a new random value to it.
     */
    public void roll() {

        Random r = new Random();
        this.value = r.nextInt(5)+1;
    }


    /**
     * Roll over the Dice.
     * In a standard Dice this means for example: 6 becomes 1, 4 becomes 3, 1 becomes 6
     */
    public void rollOver() {

        //In a standard dice the sum of the opposites sides is always 7
        this.value = 7 - this.value;
    }

    /**
     * Increments by one the Dice's value
     *
     * @return if the increment was possible (can't increment a Dice that has the maximum value)
     */
    public boolean incrementValue(){
        if( this.value < 6 ){
            this.value += 1;
            return true;
        }
        return false;
    }

    /**
     * Decrement by one the Dice's value
     *
     * @return if the decrement was possible (can't decrement a Dice that has the minimum value)
     */
    public boolean decrementValue(){
        if( this.value > 1 ){
            this.value -= 1;
            return true;
        }
        return false;
    }


    /**
     * Returns a new dice instance with same properties of this dice
     *
     * @return new dice instance with same properties of this dice
     */
    public Dice copy(){

        return new Dice(this.color, this.value);
    }

    /**
     * Returns the string representation of the Dice
     *
     * @return the string representation of the Dice
     */
    @Override
    public String toString() {
        return this.value + ":" + this.color.toOneLetter();
    }

    /**
     * Indicates whether some other Dice is "equal to" this one.
     *
     * @param o some other Dice
     * @return if the other Dice is equal to this
     * @see Object#equals(Object)
     */
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

    /**
     * Returns a hash code value for the Dice.
     *
     * @return a hash code value for the Dice
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(value,color);
    }
}
