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
     * Constructor just for JUnit.
     */
    private SetPublicObjectiveCard(){}

    /**
     * Returns a new empty instance of the class just for tests in JUnit.
     *
     * @return a new empty instance of the class
     */
    public static PublicObjectiveCard createTestInstance() {
        return new SetPublicObjectiveCard();
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
     * @param windowPattern the windowpattern to be evaluated
     * @return the score of a given window pattern according this objective card
     */
    @Override
    public int calculateScore(WindowPattern windowPattern) {

        Cell[][] pattern = windowPattern.getPattern();

        //number of sets that contain the same elements (colors or values) as the 'items' set
        int numberOfCompletedSets;

        /* List of sets that can be formed with the dice of a WindowPattern.
           Not necessarily all sets of the list will be completed sets */
        List<HashSet<Object>> listOfSets = new ArrayList<>();
        Object currentProperty;

        listOfSets.add(new HashSet<>());

        for(int i=0; i<windowPattern.getNumberOfRows(); i++){
            for(int j=0; j < windowPattern.getNumberOfColumns(); j++){

                currentProperty = getDiceProperty(pattern[i][j]);

                updateSets(listOfSets, currentProperty);
            }
        }

        numberOfCompletedSets = countCompletedSets(listOfSets);

        return this.multiplier*numberOfCompletedSets;
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
     * Updates the sets adding the current property if it is different from null and it is contained in 'items'.
     *
     * @param listOfSets the sets that are or can become completed sets of properties
     * @param currentProperty the property of the dice that is being evaluated
     */
    private void updateSets(List<HashSet<Object>> listOfSets, Object currentProperty) {

        /*
        If there is not a dice on the current cell or if the property to be evaluated is not contained in 'items',
        no set can become a completed set
        */
        if(currentProperty == null || !items.contains(currentProperty)){
            return;
        }

        /*
        Otherwise, the property is necessarily contained in the 'items' set
        For each set, if the set does not already contain the current property, then add it to it
        If all sets already have the current property, then create a new set containing the current property
        */
        for (HashSet<Object> set: listOfSets) {

            if (!set.contains(currentProperty)){
                set.add(currentProperty);
                return;
            }

            else if(listOfSets.indexOf(set) == listOfSets.size()-1){
                set = new HashSet<>();
                set.add(currentProperty);
                listOfSets.add(set);
                return;
            }
        }
    }

    /**
     * Counts the number of sets that are equal to the requested set ('items') specified in the constructor of the card
     *
     * @param listOfSets the sets, both completed and not, that were formed after the execution of the algorithm
     * @return the number of sets that are equal to the requested set ('items') specified in the constructor of the card
     */
    private int countCompletedSets(List<HashSet<Object>> listOfSets) {

        int numberOfCompletedSets = 0;

        for (HashSet set: listOfSets) {
            if (set.equals(items)) {
                numberOfCompletedSets++;
            }
        }
        return numberOfCompletedSets;
    }

    /**
     * Returns the String representation of the card.
     *
     * @return the String representation of the card.
     */
    @Override
    public String toString(){
        String s = super.toString();
        s = s.concat("Multiplier: " + multiplier);
        s = s.concat(System.lineSeparator());
        return s;
    }
}