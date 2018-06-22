package it.polimi.se2018.model;

import java.util.function.Function;

/**
 * Represents Public Objective Cards as an abstract class that extends ObjectiveCard.
 * Each Public Objective card has a property function to get a certain property of the dice in the windowPattern.
 * @author Jacopo Pio Gargano
 */
public abstract class PublicObjectiveCard extends ObjectiveCard{

    /**
     * Function of Dice used to get a certain property of it.
     */
    private transient Function<Dice,Object> propertyFunction;

    /**
     * Constructor for PublicObjectiveCard.
     * @param title the title of the card
     * @param description the description of the card
     * @param imageURL the image url of the card
     * @param propertyFunction function of Dice used to get a certain property of it
     */
    public PublicObjectiveCard(String title, String description, String imageURL, Function<Dice,Object> propertyFunction) {
        super(title, description, imageURL);
        this.propertyFunction = propertyFunction;
    }

    /**
     * Protected constructor needed for JUnit tests.
     */
    protected PublicObjectiveCard() {}

    /**
     * Gets the property function of the public objective card.
     * @return the property function of the public objective card
     */
    protected Function<Dice, Object> getPropertyFunction() {
        return propertyFunction;
    }
}
