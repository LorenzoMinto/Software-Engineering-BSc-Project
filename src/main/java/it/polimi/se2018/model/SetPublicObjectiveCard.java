package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Represents a type of Public Objective Card that counts the number of specific
 * sets of dices in a window pattern. Each set is formed by the properties
 * (colors or values) specified in a set passed in the constructor.
 *
 * The Function, which gets the property of the dice, is passed in the constructor.
 *
 * @author Jacopo Pio Gargano
 */
public class SetPublicObjectiveCard extends PublicObjectiveCard {

    /**
     * Part of the toString representation of the SetPublicObjectiveCard. Contains content shown before multiplier
     */
    private static final String PRE_MULTIPLIER = "Multiplier: ";
    /**
     * Part of the toString representation of the SetPublicObjectiveCard. Contains content shown after multiplier
     */
    private static final String POST_MULTIPLIER = "";
    /**
     * The set of colors or values that form a set.
     */
    private HashSet<Object> items;

    /**
     * The score multiplier that is specific for each different set of colors or values.
     */
    private int multiplier;


    /**
     * @param title the title of the specific objective card
     * @param description the description of the specific objective card
     * @param imageURL the imageURL of the specific objective card
     * @param items the set of items to be checked when scoring
     * @param propertyFunction function of Dice used to get a certain property of it
     * @param multiplier the card multiplier used in the scoring process
     */
    public SetPublicObjectiveCard(String title, String description, String imageURL, Set<Object> items,
                                  Function<Dice, Object> propertyFunction, int multiplier) {
        super(title, description, imageURL, propertyFunction);
        this.items = (HashSet<Object>)items;
        this.multiplier = multiplier;
    }

    /**
     * Returns a new SetPublicObjectiveCard instance with same properties of this one.
     *
     * @return a new SetPublicObjectiveCard instance with same properties of this one
     */
    @Override
    public PublicObjectiveCard copy() {
        return new SetPublicObjectiveCard(super.getTitle(), super.getDescription(), super.getImageURL(),
                this.items, super.getPropertyFunction(), this.multiplier);
    }

    /**
     * Calculates the score of a given window pattern according this objective card.
     *
     * @param windowPattern the window pattern to be evaluated
     * @return the score of a given window pattern according this objective card
     */
    @Override
    public int calculateScore(WindowPattern windowPattern) {

        int numberOf1 = 0;
        int numberOf2 = 0;
        Object currentProperty;

        Cell[][] pattern = windowPattern.getPattern();
        List<Object> itemsList = new ArrayList<>(items);

        for(int i=0; i<windowPattern.getNumberOfRows(); i++){
            for(int j=0; j < windowPattern.getNumberOfColumns(); j++){

                currentProperty = getDiceProperty(pattern[i][j]);

                if(currentProperty!= null && currentProperty.equals(itemsList.get(0))){
                    numberOf1++;
                }else if(currentProperty!= null && currentProperty.equals(itemsList.get(1))){
                    numberOf2++;
                }
            }
        }

        if(numberOf1<numberOf2){
            return this.multiplier*numberOf1;
        }else{
            return this.multiplier*numberOf2;
        }
    }

    /**
     * Gets the dice property (color or value) if there is a dice on the cell otherwise returns null.
     *
     * @param cell the cell to check
     * @return the dice property (color or value) if there is a dice on the cell otherwise returns null
     */
    private Object getDiceProperty(Cell cell) {
        if(cell.hasDice()) {
            return super.getPropertyFunction().apply(cell.getDice());
        }else{
            return null;
        }
    }

    /**
     * Returns the String representation of the card.
     *
     * @return the String representation of the card.
     */
    @Override
    public String toString(){
        String s = super.toString();
        s = s.concat(PRE_MULTIPLIER + multiplier + POST_MULTIPLIER);
        s = s.concat(System.lineSeparator());
        return s;
    }
}