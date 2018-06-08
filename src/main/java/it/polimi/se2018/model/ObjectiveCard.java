package it.polimi.se2018.model;

import java.io.Serializable;

/**
 * Abstract class for Objective Cards. Each type is described by specific subclasses
 *
 * @author Jacopo Pio Gargano
 * @see DiagonalsPublicObjectiveCard
 * @see PrivateObjectiveCard
 * @see PublicObjectiveCard
 * @see RowsColumnsPublicObjectiveCard
 * @see SetPublicObjectiveCard
 */
public abstract class ObjectiveCard implements Serializable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -463196229987501893L;
    /**
     * The title of the Objective Card
     */
    private String title;

    /**
     * The description of the Objective Card
     */
    private String description;

    /**
     * The image url of the Objective Card
     */
    private String imageURL;

    /**
     * Constructor for a new Objective Card of given title, description and imageURL.
     * @param title the title of the Objective Card
     * @param description the description of the Objective Card
     * @param imageURL the image url of the Objective Card
     */
    public ObjectiveCard(String title, String description, String imageURL) {
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
    }

    /**
     * Constructor just for JUnit
     */
    protected ObjectiveCard(){}

    /**
     * Returns the title of the objective card.
     * @return the title of the objective card
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the description of the objective card.
     * @return the description of the objective card
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the image url of the objective card.
     * @return the image url of the objective card
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     * Returns a copy of the objective card.
     * @return a copy of the objective card
     */
    public abstract ObjectiveCard copy();

    /**
     * Returns the score of a given windowPattern according to this objective card criteria.
     * @param windowPattern the windowPattern to be evaluated
     * @return the score of a given windowPattern according to this objective card criteria
     */
    public abstract int calculateScore(WindowPattern windowPattern);

    /**
     * Returns the string representation of the card.
     * @return the string representation of the card
     */
    public String toString(){
        String s = title;
        s = s.concat(System.lineSeparator());
        s = s.concat(description);
        s = s.concat(System.lineSeparator());
        return s;
    }
}
