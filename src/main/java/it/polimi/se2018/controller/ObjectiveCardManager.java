package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.ValueOutOfBoundsException;

import java.util.*;


/**Manages the creation and distribution of Objective Cards,
 * ensuring that the same card can't be created more than once
 *
 * @author Jacopo Pio Gargano
 */
public class ObjectiveCardManager {

    /**
     * String used as message of ValueOutOfBoundsException thrown in getPublicObjectiveCards()
     */
    private static final String ASKED_QUANTITY_GREATER_THAN_MAX = "The quantity of Public Objective Cards asked is greater than the number of Public Objective Cards.";

    /**
     * String used as message of BadBehaviourRuntimeException thrown in getPrivateObjectiveCard()
     */
    private static final String ALL_CARDS_ALREADY_CREATED = "Cannot get more than 5 Private Objective Card as all of them were already created.";
    /**
     * Keeps track of the already given cards' colors in order
     * to ensure that the same card can not be distributed
     * more than once
     */
    private Set<DiceColor> assignedColors = new HashSet<>();

    /**
     * Number of public objective cards that exists and can be created
     */
    private static final int NUMBER_OF_PUBLIC_OBJECTIVE_CARDS = 10;

    /**
     * Creates and returns a new {@link PrivateObjectiveCard}
     * @return new instance of a {@link PrivateObjectiveCard}
     */
    public PrivateObjectiveCard getPrivateObjectiveCard(){
        if(assignedColors.size() == DiceColor.values().length-1){ throw new BadBehaviourRuntimeException(ALL_CARDS_ALREADY_CREATED);}

        DiceColor color;

        do{
            color = DiceColor.getRandomColor();
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
    public List<PublicObjectiveCard> getPublicObjectiveCards(int quantity){

        if(quantity < 1 || quantity > NUMBER_OF_PUBLIC_OBJECTIVE_CARDS){
            throw new ValueOutOfBoundsException(ASKED_QUANTITY_GREATER_THAN_MAX);
        }

        Random r = new Random();
        int randomIndex;
        PublicObjectiveCard currentCard;
        List<PublicObjectiveCard> publicObjectiveCards = new ArrayList<>();
        List<Integer> usedIndexes = new ArrayList<>();

        for(int i=0; i<quantity; i++){
            //Choose randomly one of the cards
            do {
                randomIndex = r.nextInt(NUMBER_OF_PUBLIC_OBJECTIVE_CARDS);
                currentCard = ObjectiveCardFactory.getInstance().createPublicObjectiveCardCardByIndex(randomIndex);
            }while(usedIndexes.contains(randomIndex));
            usedIndexes.add(randomIndex);
            publicObjectiveCards.add(currentCard);
        }

        return publicObjectiveCards;
    }
}

