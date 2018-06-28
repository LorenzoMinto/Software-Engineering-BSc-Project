package it.polimi.se2018.model;

import it.polimi.se2018.utils.ValueOutOfBoundsException;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;
import java.util.function.IntFunction;

/**
 * Class that represents a Dice with 6 sides (value from 1 to 6)
 *
 * @author Federico Haag
 */
public class Dice implements Serializable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -5969531893571901155L;
    /**
     * String passed as message of IllegalArgumentException when it is asked to create a dice with no specified color
     */
    private static final String DICE_WITH_NO_COLOR = "Cannot create a Dice with no color.";
    /**
     * String passed as message of OutOfBoundsException when it is asked to create a dice with value not in the given range
     */
    private static final String DICE_WITH_VALUE_NOT_IN_RANGE = "Cannot create a dice with value not in range.";
    /**
     * Value given to a dice if no value is specified in constructor
     */
    private static final int DEFAULT_VALUE = 1;
    /**
     * Min value of the dice
     */
    private static final int MIN_VALUE = 1;
    /**
     * Max value of the dice
     */
    private static final int MAX_VALUE = 6;
    /**
     * In a standard dice the sum of the opposites sides is always 7
     * In a standard Dice this means for example: 6 becomes 1, 4 becomes 3, 1 becomes 6
     */
    private static final IntFunction<Integer> rollDiceFunction = v -> 7 - v;

    /**
     * Single Random class object used for all generated objects of the class
     */
    private static final Random RANDOM = new Random();

    /**
     * The value of the Dice
     */
    private int value;

    /**
     * The color of the Dice
     */
    private DiceColor color;

    /**
     * Constructor of a new Dice of a given color
     *
     * @param color the color of the new Dice
     */
    public Dice(DiceColor color) {
        this(color, DEFAULT_VALUE);
        this.roll();
    }

    /**
     * Constructor of a new Dice of a given color and value
     *
     * @param color the color of the new Dice
     * @param value the value of the new Dice
     */
    public Dice(DiceColor color, int value) {
        if(color== DiceColor.NOCOLOR){ throw new IllegalArgumentException(DICE_WITH_NO_COLOR); }
        if(value < MIN_VALUE || value > MAX_VALUE){ throw new ValueOutOfBoundsException(DICE_WITH_VALUE_NOT_IN_RANGE); }

        this.color = color;
        this.value = value;
    }

    /**
     * Sets the value of the Dice to the one given
     *
     * @param value the value to be setted to the Dice
     */
    public void setValue(int value) {
        if(value < MIN_VALUE || value > MAX_VALUE){ throw new ValueOutOfBoundsException(DICE_WITH_VALUE_NOT_IN_RANGE); }
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
    public DiceColor getColor() {
        return color;
    }


    /**
     * Rolls the Dice. Means that assign a new random value to it.
     */
    public void roll() {
        this.value = RANDOM.nextInt(MAX_VALUE)+1;
    }


    /**
     * Roll over the Dice according the built-in function rollDiceFunction
     */
    public void rollOver() {
        this.value = rollDiceFunction.apply(this.value);
    }

    /**
     * Increments by one the Dice's value
     *
     * @return if the increment was possible (can't increment a Dice that has the maximum value)
     */
    public boolean incrementValue(){
        if( this.value < MAX_VALUE ){
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
        if( this.value > MIN_VALUE ){
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
        return this.color.toOneLetter()+this.value;
    }

    /**
     * Returns whether or not some other Dice is "equal to" this one. Comparison is based on value and color
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
