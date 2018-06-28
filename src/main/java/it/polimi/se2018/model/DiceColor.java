package it.polimi.se2018.model;

import java.util.Random;

/**
 * Enum of Dice colors. It is included also a placeholder color named "NOCOLOR"
 * to better handling when a {@link Cell} has not a specified color constraint.
 * @author Federico Haag
 */
public enum DiceColor {
    RED ("R"),
    YELLOW ("Y"),
    GREEN ("G"),
    BLUE ("B"),
    PURPLE ("P"),
    NOCOLOR ("_");

    private final String oneLetterRepresentation;

    DiceColor(String s){ this.oneLetterRepresentation = s; }

    /**
     * Returns a random color, except the NOCOLOR.
     * @return a random color
     */
    public static DiceColor getRandomColor(){
        Random random = new Random();
        DiceColor randomColor;

        do{
            randomColor = values()[random.nextInt(values().length)];
        } while (randomColor.equals(DiceColor.NOCOLOR));

        return randomColor;
    }

    /**
     * Returns a string representation in one letter of the color.
     * @return a string representation in one letter of the color
     */
    public String toOneLetter(){
        return this.oneLetterRepresentation;
    }

    /**
     * Returns a string representation of the color.
     * @return a string representation of the color
     */
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

}
