package it.polimi.se2018.controller;

import it.polimi.se2018.model.DiceColors;
import it.polimi.se2018.model.PrivateObjectiveCard;
import it.polimi.se2018.model.PublicObjectiveCard;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**Manages the creation and distribution of Objective Cards,
 * ensuring that the same card can't be created more than once
 *
 * @author Jacopo Pio Gargano
 */
public class ObjectiveCardManager {

    /**
     * Keeps track of the already given cards' colors in order
     * to ensure that the same card can not be distributed
     * more than once
     */
    private Set<DiceColors> assignedColors = new HashSet<>();

    /**
     * Number of public objective cards that exists and can be created
     */
    private static final int NUMBER_OF_PUBLIC_OBJECTIVE_CARDS = 10;

    /**
     * Creates and returns a new {@link PrivateObjectiveCard}
     * @return new instance of a {@link PrivateObjectiveCard}
     */
    public PrivateObjectiveCard getPrivateObjectiveCard(){
        DiceColors color;

        do{
            color = DiceColors.getRandomColor();
        }while (assignedColors.contains(color));

        assignedColors.add(color);

        return ObjectiveCardFactory.getInstance().createPrivateObjectiveCard(color);
    }

    /**
     * Creates and returns the specified number (quantity) of public objective cards
     *
     * @param quantity how many cards must be returned
     * @return the specified number (quantity) of public objective cards
     */
    public Set<PublicObjectiveCard> getPublicObjectiveCards(int quantity){

        if(quantity > NUMBER_OF_PUBLIC_OBJECTIVE_CARDS || quantity < 1){
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
                randomIndex = r.nextInt(NUMBER_OF_PUBLIC_OBJECTIVE_CARDS);
                currentCard = ObjectiveCardFactory.getInstance().createPublicObjectiveCardCardByIndex(randomIndex);
                publicObjectiveCards.add(currentCard);

            }while(publicObjectiveCards.contains(currentCard));

        }

        return publicObjectiveCards;
    }
}

