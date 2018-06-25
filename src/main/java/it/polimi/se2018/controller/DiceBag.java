package it.polimi.se2018.controller;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.DiceColor;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;

import java.util.*;

/**
 * Contains all the dices that can be drafted and used.
 * Conceptual representation of a real Bag of Dices.
 * The important difference respecting a "real" Dice Bag is that
 * dices do not actually exist until they are requested.
 *
 * @author Federico Haag
 * @see Dice
 */
public class DiceBag {

    /**
     * String used as message of IllegalArgumentException in creation of DiceBag
     */
    private static final String NEGATIVE_NUMBER_OF_DICES = "Can't get a negative number of dices.";

    /**
     * String used as message of IllegalArgumentException in getDices()
     */
    private static final String ASKED_TOO_MUCH_DICES = "Asked DiceBag to get more dices than available: ";

    /**
     * Maps the quantity of available dices of each color
     */
    private Map<DiceColor,Integer> availableDices;

    /**
     * Creates a {@link DiceBag} containing specified number of dices of all colors ({@link DiceColor})
     * @param numberOfDicesPerColor how many dices for each color have to be created
     *
     * @see Dice
     * @see DiceColor
     */
    public DiceBag(int numberOfDicesPerColor) {
        if(numberOfDicesPerColor <0){ throw new IllegalArgumentException(NEGATIVE_NUMBER_OF_DICES);}

        this.availableDices = new EnumMap<>(DiceColor.class);
        for(int i = 0; i < DiceColor.values().length-1; i++){
            this.availableDices.put(DiceColor.values()[i],numberOfDicesPerColor);
        }
    }

    /**
     * Gets the specified quantity of dices from the DiceBag if there are enough availables.
     * If not, a RuntimeException is thrown
     * @param quantity how many dices have to be getted
     * @return a list of dices of length same of param quantity
     * @see Dice
     */
    public List<Dice> getDices(int quantity) {
        List<Dice> drawnDices = new ArrayList<>();

        if(quantity <0){ throw new IllegalArgumentException(NEGATIVE_NUMBER_OF_DICES);}

        if( quantity > this.getNumberOfAvailableDices() ){
            throw new BadBehaviourRuntimeException(ASKED_TOO_MUCH_DICES +quantity + "/" + getNumberOfAvailableDices());
        } else {
            for(int i=0; i<quantity; i++){

                //Looks for a color dice that can be created, and creates it
                int availableDicesForRandomColor;
                DiceColor randomColor;
                do {
                    randomColor = DiceColor.getRandomColor();
                    availableDicesForRandomColor = availableDices.get(randomColor);
                } while (availableDicesForRandomColor<=0);

                drawnDices.add(new Dice(randomColor));
                availableDices.put(randomColor,availableDicesForRandomColor-1);
            }
        }

        return drawnDices;
    }

    /**
     * Adds the specified {@link Dice} to this {@link DiceBag}
     * @param dice the dice to be added to
     */
    public void addDice(Dice dice) {
        DiceColor diceColor = dice.getColor();

        int availableDicesForColor = availableDices.get(diceColor);
        availableDices.put(diceColor, availableDicesForColor+1);
    }

    /**
     * Gets the number of available dices summing the available dices for each Dice Color in availableDices (map)
     *
     * @return the number of available dices
     */
    private int getNumberOfAvailableDices(){
        int sum = 0;
        for(Integer value : availableDices.values()){
            sum += value;
        }
        return sum;
    }
}
