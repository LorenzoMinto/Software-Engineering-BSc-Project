package it.polimi.se2018.controller;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.DiceColors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private List<Integer> availableDices;

    /**
     * Creates a {@link DiceBag} containing specified number of dices of all colors ({@link DiceColors})
     * @param numberOfDicesPerColor how many dices for each color have to be created
     *
     * @see Dice
     * @see DiceColors
     */
    public DiceBag(int numberOfDicesPerColor) {
        availableDices = new ArrayList<>(DiceColors.values().length);
        for(int i=0; i<availableDices.size(); i++){
            availableDices.set(i,numberOfDicesPerColor);
        }
    }

    /**
     * Gets the specified quantity of dices from the DiceBag if there are enough availables.
     * If not, a RuntimeException is thrown
     * @param quantity how many dices have to be getted
     * @return a list of dices of lenght same of param quantity
     * @see Dice
     */
    //Takes "quantity" of dices out of DiceBag
    public List<Dice> getDices(int quantity) {
        List<Dice> drawnDices = new ArrayList<>();

        if( quantity > this.numberOfAvailableDices() ){
            throw new RuntimeException("Asked DiceBag to produce "+quantity+" of dices but only "+numberOfAvailableDices()+" dices are available");
        } else {
            for(int i=0; i<quantity; i++){

                //Looks for a color dice that can be created, and creates it
                int availableDicesForRandomColor;
                DiceColors randomColor;
                do {
                    randomColor = DiceColors.getRandomColor();
                    availableDicesForRandomColor = availableDices.get(Arrays.asList(DiceColors.values()).indexOf(randomColor));
                } while (availableDicesForRandomColor==0);

                drawnDices.add(new Dice(randomColor));
            }
        }

        return drawnDices;
    }

    /**
     * Adds the specified {@link Dice} to this {@link DiceBag}
     * @param dice the dice to be added to
     */
    public void addDice(Dice dice) {
        int index = Arrays.asList(DiceColors.values()).indexOf(dice.getColor());
        int availableDicesForColor = availableDices.get(index);
        availableDices.set(index, availableDicesForColor+1);
    }

    /**
     * Checks if this {@link DiceBag} has no dices left
     *
     * @return true if the {@link DiceBag} has no dices left
     */
    //Returns true if no more dices can be created. False in the other case.
    private boolean isEmpty(){
        return this.numberOfAvailableDices() == 0;
    }

    /**
     * Gets the number of available dices
     *
     * @return the number of available dices
     */
    private int numberOfAvailableDices(){
        return availableDices.stream().mapToInt(Integer::intValue).sum();
    }
}
