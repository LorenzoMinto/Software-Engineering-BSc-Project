package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;

import java.util.*;


//TODO: set all imageURLs

public class ObjectiveCardFactory {

    private static ObjectiveCardFactory instance = null;

    private ObjectiveCardFactory() {}

    public static ObjectiveCardFactory getInstance(){
        if(instance == null) {
            instance = new ObjectiveCardFactory();
        }
        return instance;
    }

    public PrivateObjectiveCard createPrivateObjectiveCard(DiceColors color) {
        if(color==DiceColors.NOCOLOR){ throw new IllegalArgumentException("ERROR: Cannot create a " +
                "Private Objective Card with no color"); }

        String title = "Shades of " + color.toString()+ " - Private";
        String description = "Sum of values on " + color.toString() + " dice";
        String imageURL = "assets/"+color+"privateobjectivecard.jpg";

        return new PrivateObjectiveCard(title, description, imageURL, color);

    }

    public PublicObjectiveCard createPublicObjectiveCardCardByIndex(int index) {
        Set<Object> items;
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
                return createColorSetPublicObjectiveCard(items);
            default:
                throw new RuntimeException("The selected (by index) card does not exist.");
        }
    }

    //The card that now exists only has all 5 colors
    private PublicObjectiveCard createColorSetPublicObjectiveCard(Set<Object> colors){
        if(colors == null){ throw new IllegalArgumentException("Cannot create a ColorSetPublicObjectiveCard with no colors specified"); }

        String title = "Color Variety";
        String description;
        int multiplier;
        String imageURL;

        if(colors.size() == DiceColors.values().length - 1){
            description = "Sets of one of each color anywhere";
            multiplier = 4;
            imageURL = null;
        }else{
            throw new RuntimeException("ColorSetPublicObjectiveCard can be created only with all of the existing colors.");
        }

        return new SetPublicObjectiveCard(title, description, imageURL, colors, Dice::getColor, multiplier);
    }

    private PublicObjectiveCard createValueSetPublicObjectiveCard(Set<Object> values){
        if(values == null){ throw new IllegalArgumentException("ERROR: Value set cannot be null."); }

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
                    throw new RuntimeException("ERROR: The Value Set Public Objective Card " +
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
                throw new RuntimeException("ERROR: The Value Set Public Objective Card " +
                        "cannot be created with the values passed in the constructor. The values passed are:" +
                        values + " .");
        }

        return new SetPublicObjectiveCard(title, description, imageURL, values, Dice::getValue, multiplier);
    }

    private PublicObjectiveCard createDiagonalsColorPublicObjectiveCard(){
        String title = "Color Diagonals";
        String description = "Count of diagonally adjacent same color dice";
        String imageURL = null;

        return new DiagonalsPublicObjectiveCard(title, description, imageURL, Dice::getColor);
    }

    private PublicObjectiveCard createRowsColorPublicObjectiveCard() {
        String title = "Row Color Variety";
        String description = "Rows with no repeated colors";
        String imageURL = null;
        return new RowsColumnsPublicObjectiveCard(title, description, imageURL, Dice::getColor, 6, true);
    }

    private PublicObjectiveCard createColumnsColorPublicObjectiveCard() {
        String title = "Column Color Variety";
        String description = "Columns with no repeated colors";
        String imageURL = null;
        return new RowsColumnsPublicObjectiveCard(title, description, imageURL, Dice::getColor, 5, false);
    }

    private PublicObjectiveCard createRowsValuePublicObjectiveCard() {
        String title = "Row Shade Variety";
        String description = "Rows with no repeated values";
        String imageURL = null;
        return new RowsColumnsPublicObjectiveCard(title, description, imageURL, Dice::getValue, 5, true);
    }

    private PublicObjectiveCard createColumnsValuePublicObjectiveCard() {
        String title = "Column Shade Variety";
        String description = "Columns with no repeated values";
        String imageURL = null;
        return new RowsColumnsPublicObjectiveCard(title, description, imageURL, Dice::getValue, 4, false);
    }

}
