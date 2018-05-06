package it.polimi.se2018.controller;

import it.polimi.se2018.model.DiceColors;
import it.polimi.se2018.model.PrivateObjectiveCard;
import it.polimi.se2018.model.PublicObjectiveCard;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ObjectiveCardManager {

    private Set<DiceColors> assignedColors = new HashSet<>();
    private int numberOfPublicObjectiveCards = 10;

    public PrivateObjectiveCard getPrivateObjectiveCard(){
        DiceColors color;

        do{
            color = DiceColors.getRandomColor();
        }while (assignedColors.contains(color));

        assignedColors.add(color);

        return ObjectiveCardFactory.getInstance().createPrivateObjectiveCard(color);
    }

    public Set<PublicObjectiveCard> getPublicObjectiveCards(int quantity){

        if(quantity > numberOfPublicObjectiveCards || quantity < 1){
            throw new RuntimeException("ERROR: The quantity of Public Objective Cards asked is greater" +
                    "than the number of Public Objective Cards.");
        }

        Random r = new Random();
        int randomIndex;
        PublicObjectiveCard currentCard;
        Set<PublicObjectiveCard> publicObjectiveCards = new HashSet<>();

        for(int i=0; i<quantity; i++){
            //Choose randomly one of the cards
            do {
                randomIndex = r.nextInt(numberOfPublicObjectiveCards);
                currentCard = ObjectiveCardFactory.getInstance().createPublicObjectiveCardCardByIndex(randomIndex);
                publicObjectiveCards.add(currentCard);

            }while(publicObjectiveCards.contains(currentCard));

        }

        return publicObjectiveCards;
    }
}

