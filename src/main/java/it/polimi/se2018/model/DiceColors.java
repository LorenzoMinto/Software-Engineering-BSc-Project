package it.polimi.se2018.model;

import java.util.Random;

/**
 * Enum of Dice colors. It is included also a placeholder color named "NOCOLOR"
 * to better handling when a {@link Cell} has not a specified color constraint.
 *
 * @author Federico Haag
 */
public enum DiceColors {
    NOCOLOR,
    RED,
    YELLOW,
    GREEN,
    BLUE,
    PURPLE;

    /**
     * Returns a random color, except the NOCOLOR.
     *
     * @return a random color, except the NOCOLOR
     */
    public static DiceColors getRandomColor(){
        Random random = new Random();
        DiceColors randomColor;

        do{
            randomColor = values()[random.nextInt(values().length)];
        } while (randomColor.equals(DiceColors.NOCOLOR));

        return randomColor;
    }

    /**
     * Returns a string representation in one letter of the color.
     *
     * @return a string representation in one letter of the color
     */
    public String toOneLetter(){
        switch(this) {
            case NOCOLOR: return "_";
            case RED: return "R";
            case YELLOW: return "Y";
            case GREEN: return "G";
            case BLUE: return "B";
            case PURPLE: return "P";
            default: throw new IllegalArgumentException();
        }
    }

    /**
     * Returns a string representation of the color.
     *
     * @return a string representation of the color
     */
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

}
