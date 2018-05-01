package it.polimi.se2018.model;

import java.util.*;


//TODO: set all imageURLs

public class ObjectiveCardFactory {
    private int numberOfPublicObjectiveCards = 10;
    private Set<DiceColors> assignedColors = new HashSet<>();

    public PrivateObjectiveCard getPrivateObjectiveCard(){
        DiceColors color;

        do{
            color = DiceColors.getRandomColor();
        }while (assignedColors.contains(color));

        assignedColors.add(color);

        return createPrivateObjectiveCard(color);
    }

    private PrivateObjectiveCard createPrivateObjectiveCard(DiceColors color) {
        if(color==DiceColors.NOCOLOR){ throw new IllegalArgumentException("ERROR: Cannot create a " +
                "Private Objective Card with no color"); }

        String title = "Shades of " + color.toString()+ " - Private";
        String description = "Sum of values on " + color.toString() + " dice";
        String imageURL = null;

        switch (color){
            case RED:
                imageURL = null;
                break;
            case YELLOW:
                imageURL = null;
                break;
            case GREEN:
                imageURL = null;
                break;
            case BLUE:
                imageURL = null;
                break;
            case PURPLE:
                imageURL = null;
                break;
            default:
                RuntimeException e = new RuntimeException("ERROR: The selected color does not exist.");
                e.printStackTrace();
                break;
        }

        return new PrivateObjectiveCard(title, description, imageURL, color);

    }


    public Set<PublicObjectiveCard> getPublicObjectiveCards(int quantity){

        if(quantity > numberOfPublicObjectiveCards || quantity < 1){
            RuntimeException e = new RuntimeException("ERROR: The quantity of Public Objective Cards asked is greater" +
                    "than the number of Public Objective Cards.");
            e.printStackTrace();
        }

        Random r = new Random();
        int randomIndex;
        PublicObjectiveCard currentCard;
        Set<PublicObjectiveCard> publicObjectiveCards = new HashSet<>();

        for(int i=0; i<quantity; i++){
            //Choose randomly one of the cards
            do {
                randomIndex = r.nextInt(numberOfPublicObjectiveCards);
                currentCard = getPublicObjectiveCardCardByIndex(randomIndex);
                publicObjectiveCards.add(currentCard);

            }while(publicObjectiveCards.contains(currentCard));

        }

        return publicObjectiveCards;
    }

    private PublicObjectiveCard getPublicObjectiveCardCardByIndex(int index) {
        if(index > numberOfPublicObjectiveCards-1|| index<0) { throw new IllegalArgumentException("ERROR: " +
                "Cannot get a Public Objective card with index not in range " +
                "[0,"+(numberOfPublicObjectiveCards-1)+"]."); }

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
                RuntimeException e = new RuntimeException("ERROR: The selected card by index does not exist.");
                e.printStackTrace();
                return null;
        }
    }

    //The card that now exists only has all 5 colors
    private PublicObjectiveCard createColorSetPublicObjectiveCard(Set<Object> colors){
        if(colors == null){ throw new IllegalArgumentException("ERROR: Color set cannot be null."); }

        String title = "Color Variety";
        String description = "Sets of one ";
        int multiplier = colors.size();
        String imageURL = null;

        if(colors.size() == 5){
            description = "Sets of one of each color anywhere";
            multiplier = 4;
            imageURL = null;
        }else{
            RuntimeException e = new RuntimeException("ERROR: The Color Set Public Objective Card " +
                    "can only be created with all of the existing colors.");
            e.printStackTrace();
        }

        return new SetPublicObjectiveCard(title, description, imageURL, colors,Dice::getColor, multiplier);
    }

    private PublicObjectiveCard createValueSetPublicObjectiveCard(Set<Object> values){
        if(values == null){ throw new IllegalArgumentException("ERROR: Value set cannot be null."); }

        String title = null;
        String description = null;
        int multiplier = values.size();
        String imageURL = null;

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
                    RuntimeException e = new RuntimeException("ERROR: The Value Set Public Objective Card " +
                            "cannot be created with couples of two different from the following: (1,2) (3,4) (5,6).");
                    e.printStackTrace();
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
                RuntimeException e = new RuntimeException("ERROR: The Value Set Public Objective Card " +
                        "cannot be created with the values passed in the constructor. The values passed are:" +
                        values + " .");
                e.printStackTrace();
                break;
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
