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
        }while (color.equals(DiceColors.NOCOLOR) || assignedColors.contains(color));

        assignedColors.add(color);

        return createPrivateObjectiveCard(color);
    }

    private PrivateObjectiveCard createPrivateObjectiveCard(DiceColors color) {
        String title = "Shades of " + color.name().toLowerCase()+ " - Private";
        String description = "Sum of values on " + color.name().toLowerCase() + " dice";
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
                break;
        }

        return new PrivateObjectiveCard(title, description, imageURL, color);

    }


    public Set<PublicObjectiveCard> getPublicObjectiveCards(int quantity){
        Random r = new Random();
        int randomIndex;
        PublicObjectiveCard currentCard;
        Set<PublicObjectiveCard> publicObjectiveCards = new HashSet<>();

        //TODO Exception
        if(quantity > numberOfPublicObjectiveCards || quantity < 1) return new HashSet<>();

        for(int i=0; i<quantity; i++){
            //Choose randomly one of the cards
            do {
                randomIndex = r.nextInt(numberOfPublicObjectiveCards);
                currentCard = getPublicObjectiveCardCardByNumber(randomIndex);
                publicObjectiveCards.add(currentCard);

            }while(publicObjectiveCards.contains(currentCard));

        }

        return publicObjectiveCards;
    }

    private PublicObjectiveCard getPublicObjectiveCardCardByNumber(int randomIndex) {
        if(randomIndex > numberOfPublicObjectiveCards-1|| randomIndex<0) return null;

        Set<Object> items;
        switch (randomIndex){
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
                return null;
        }
    }

    //The card that now exists only has all 5 colors
    private PublicObjectiveCard createColorSetPublicObjectiveCard(Set<Object> colors){
        String title = "Color Variety";
        String description = "Sets of one ";
        int multiplier = colors.size();
        String imageURL = null;

        completeDescription(colors, description);

        if(colors.size() == 5){
            description = "Sets of one of each color anywhere";
            multiplier = 4;
            imageURL = null;
        }

        return new SetPublicObjectiveCard(title, description, imageURL, colors,Dice::getColor, multiplier);
    }

    private PublicObjectiveCard createValueSetPublicObjectiveCard(Set<Object> values){
        String title = null;
        String description = null;
        int multiplier = values.size();
        String imageURL = null;
        Integer firstValue;
        Integer secondValue;

        switch(values.size()){
            case 2:
                firstValue = 1;
                secondValue = 2;
                if(values.contains(firstValue) && values.contains(secondValue)){
                    title = "Light Shades";
                    description = "Sets of 1 & 2 values anywhere";
                    imageURL = null;
                }
                firstValue = 3;
                secondValue = 4;
                if(values.contains(firstValue) && values.contains(secondValue)){
                    title = "Medium Shades";
                    description = "Sets of 3 & 4 values anywhere";
                    imageURL = null;
                }
                firstValue = 5;
                secondValue = 6;
                if(values.contains(firstValue) && values.contains(secondValue)){
                    title = "Deep Shades";
                    description = "Sets of 5 & 6 values anywhere";
                    imageURL = null;
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
                break;
        }

        return new SetPublicObjectiveCard(title, description, imageURL, values,Dice::getValue, multiplier);
    }

    public PublicObjectiveCard createDiagonalsColorPublicObjectiveCard(){
        String title = "Color Diagonals";
        String description = "Count of diagonally adjacent same color dice";
        String imageURL = null;

        return new DiagonalsPublicObjectiveCard(title, description, imageURL, new ColorComparator());
    }

    //This card does not yet exist in the game
    private PublicObjectiveCard createDiagonalsValuePublicObjectiveCard(){
        String title = "Shade Diagonals";
        String description = "Count of diagonally adjacent same value dice";
        String imageURL = null;
        return new DiagonalsPublicObjectiveCard(title, description, imageURL, new ShadeComparator());
    }

    private PublicObjectiveCard createRowsColorPublicObjectiveCard() {
        String title = "Row Color Variety";
        String description = "Rows with no repeated colors";
        String imageURL = null;
        return new RowsColumnsPublicObjectiveCard(title, description, imageURL, new ColorComparator(), 6, true);
    }

    private PublicObjectiveCard createColumnsColorPublicObjectiveCard() {
        String title = "Column Color Variety";
        String description = "Columns with no repeated colors";
        String imageURL = null;
        return new RowsColumnsPublicObjectiveCard(title, description, imageURL, new ColorComparator(), 5, false);
    }

    private PublicObjectiveCard createRowsValuePublicObjectiveCard() {
        String title = "Row Shade Variety";
        String description = "Rows with no repeated values";
        String imageURL = null;
        return new RowsColumnsPublicObjectiveCard(title, description, imageURL, new ShadeComparator(), 5, true);
    }

    private PublicObjectiveCard createColumnsValuePublicObjectiveCard() {
        String title = "Column Shade Variety";
        String description = "Columns with no repeated values";
        String imageURL = null;
        return new RowsColumnsPublicObjectiveCard(title, description, imageURL, new ShadeComparator(), 4, false);
    }

    private void completeDescription(Set<Object> items, String description) {
        int i = 0;
        for (Object item :items) {
            i++;
            description = description.concat(item.toString());
            if(i!= items.size()){
                description = description.concat(", ");
            }
            description = description.concat(" dice anywhere");
        }
    }
}
