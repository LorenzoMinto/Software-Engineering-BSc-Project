package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.ValueOutOfBoundsException;

import java.util.*;


//TODO: set all imageURLs

/**
 * Creates all the types of Objective Cards: {@link PublicObjectiveCard} and {@link PrivateObjectiveCard}.
 *
 * This class is a SINGLETON.
 *
 * @author Jacopo Pio Gargano
 */
public class ObjectiveCardFactory {

    /**
     * Instance of the class in order to achieve the Singleton Pattern
     */
    private static ObjectiveCardFactory instance = null;

    /**
     * Private Costructor in order to prevent from multiple instantiation of the class
     */
    private ObjectiveCardFactory() {}

    /**
     * Gets the instance of the class (according to Singleton Pattern)
     * @return the instance of the class
     */
    public static ObjectiveCardFactory getInstance(){
        if(instance == null) {
            instance = new ObjectiveCardFactory();
        }
        return instance;
    }

    /**
     * Creates and returns a new {@link PrivateObjectiveCard} of the specified color.
     * @param color color of the card
     * @return the {@link PrivateObjectiveCard} of the specified color
     */
    protected PrivateObjectiveCard createPrivateObjectiveCard(DiceColors color) {
        if(color==DiceColors.NOCOLOR){ throw new IllegalArgumentException("ERROR: Cannot create a " +
                "Private Objective Card with no color"); }

        String title = "Shades of " + color.toString()+ " - Private";
        String description = "Sum of values on " + color.toString() + " dice";
        String imageURL = "assets/"+color+"privateobjectivecard.jpg";

        return new PrivateObjectiveCard(title, description, imageURL, color);

    }

    /**
     * Creates and returns a new {@link PublicObjectiveCard} of the type specified by index
     * @param index specifies the type of {@link PublicObjectiveCard} that is requested
     * @return the specified {@link PublicObjectiveCard}
     */
    protected PublicObjectiveCard createPublicObjectiveCardCardByIndex(int index) {
        HashSet<Object> items;
        switch (index){
            case 0:
                return createRowsColorPublicObjectiveCard();
            case 1:
                return createColumnsColorPublicObjectiveCard();
            case 2:
                return createRowsValuePublicObjectiveCard();
            case 3:
                return createColumnsValuePublicObjectiveCard();
            case 4:
                items = new HashSet<>();
                items.add(1);
                items.add(2);
                return createValueSetPublicObjectiveCard(items);
            case 5:
                items = new HashSet<>();
                items.add(3);
                items.add(4);
                return createValueSetPublicObjectiveCard(items);
            case 6:
                items = new HashSet<>();
                items.add(5);
                items.add(6);
                return createValueSetPublicObjectiveCard(items);
            case 7:
                items = new HashSet<>();
                for(int i=1; i <= 6; i++){
                    items.add(i);
                }
                return createValueSetPublicObjectiveCard(items);
            case 8:
                return createDiagonalsColorPublicObjectiveCard();
            case 9:
                items = new HashSet<>();
                for(DiceColors color: DiceColors.values()){
                    if(!color.equals(DiceColors.NOCOLOR)) {
                        items.add(color);
                    }
                }
                return createColorSetPublicObjectiveCard();
            default:
                throw new ValueOutOfBoundsException("The selected card does not exist. Index out of bounds.");
        }
    }


    /**
     * Creates and returns a new {@link SetPublicObjectiveCard}
     *
     * @return a {@link SetPublicObjectiveCard}
     */
    private PublicObjectiveCard createColorSetPublicObjectiveCard(){

        return new SetPublicObjectiveCard(
                "Color Variety",
                "Sets of one of each color anywhere",
                null,
                new HashSet<>(Arrays.asList(DiceColors.values())),
                Dice::getColor,
                4
        );
    }

    /**
     * Creates and returns a new {@link SetPublicObjectiveCard} based on the specified values
     *
     * @param values A group of dices is considered a valid set if it contains at least one
     *               dice per color specified in this list
     * @return a {@link SetPublicObjectiveCard} evaluating the specified dices' values
     */
    private PublicObjectiveCard createValueSetPublicObjectiveCard(HashSet<Object> values){

        String title;
        String description;
        String imageURL;
        int multiplier;


        switch(values.size()){
            case 2:

                if(values.contains(Integer.valueOf(1)) && values.contains(Integer.valueOf(2))){
                    title = "Light Shades";
                    description = "Sets of 1 & 2 values anywhere";
                    imageURL = null;
                }else if(values.contains(Integer.valueOf(3)) && values.contains(Integer.valueOf(4))){
                    title = "Medium Shades";
                    description = "Sets of 3 & 4 values anywhere";
                    imageURL = null;
                }else if(values.contains(Integer.valueOf(5)) && values.contains(Integer.valueOf(6))){
                    title = "Deep Shades";
                    description = "Sets of 5 & 6 values anywhere";
                    imageURL = null;
                }else{
                    throw new BadBehaviourRuntimeException("ERROR: The Value Set Public Objective Card " +
                            "cannot be created with couples of two different from the following: (1,2) (3,4) (5,6).");
                }
                multiplier = 2;
                break;
            case 6:
                title = "Shade Variety";
                description = "Sets of one of each value anywhere";
                multiplier = 5;
                imageURL = null;
                break;
            default:
                throw new BadBehaviourRuntimeException("ERROR: The Value Set Public Objective Card " +
                        "cannot be created with the values passed in the constructor. The values passed are:" +
                        values + " .");
        }

        return new SetPublicObjectiveCard(title, description, imageURL, values, Dice::getValue, multiplier);
    }

    /**
     * Factory method for Diagonals Color Public Objective Cards
     * @return a new instance of Diagonals Color Public Objective Card
     */
    private PublicObjectiveCard createDiagonalsColorPublicObjectiveCard(){
        String title = "Color Diagonals";
        String description = "Count of diagonally adjacent same color dice";
        String imageURL = null;

        return new DiagonalsPublicObjectiveCard(title, description, imageURL, Dice::getColor);
    }

    /**
     * Factory method for Rows Color Public Objective Cards
     * @return a new instance of Rows Color Public Objective Card
     */
    private PublicObjectiveCard createRowsColorPublicObjectiveCard() {
        String title = "Row Color Variety";
        String description = "Rows with no repeated colors";
        String imageURL = null;
        return new RowsColumnsPublicObjectiveCard(title, description, imageURL,
                Dice::getColor, 6, true);
    }

    /**
     * Factory method for Columns Color Public Objective Card
     * @return a new instance of Columns Color Public Objective Card
     */
    private PublicObjectiveCard createColumnsColorPublicObjectiveCard() {
        String title = "Column Color Variety";
        String description = "Columns with no repeated colors";
        String imageURL = null;
        return new RowsColumnsPublicObjectiveCard(title, description, imageURL,
                Dice::getColor, 5, false);
    }

    /**
     * Factory method for Rows Values Public Objective Cards
     * @return a new instance of Rows Values Public Objective Card
     */
    private PublicObjectiveCard createRowsValuePublicObjectiveCard() {
        String title = "Row Shade Variety";
        String description = "Rows with no repeated values";
        String imageURL = null;
        return new RowsColumnsPublicObjectiveCard(title, description, imageURL,
                Dice::getValue, 5, true);
    }

    /**
     * Factory method for Columns Values Public Objective Cards
     * @return a new instance of Columns Values Public Objective Card
     */
    private PublicObjectiveCard createColumnsValuePublicObjectiveCard() {
        String title = "Column Shade Variety";
        String description = "Columns with no repeated values";
        String imageURL = null;
        return new RowsColumnsPublicObjectiveCard(title, description, imageURL,
                Dice::getValue, 4, false);
    }

}
