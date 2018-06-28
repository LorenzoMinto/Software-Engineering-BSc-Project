package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.ValueOutOfBoundsException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Test for the {@link ObjectiveCardManager} class
 *
 * @author Jacopo Pio Gargano
 */

public class ObjectiveCardManagerTest {

    private ObjectiveCardManager manager;
    private PrivateObjectiveCard privateObjectiveCard;
    private PublicObjectiveCard publicObjectiveCard;
    private List<PublicObjectiveCard> publicObjectiveCards;

    private static final int numberOfPublicObjectiveCards = 10;

    //length -1 because NOCOLOR must not be considered
    private static final int numberOfPrivateObjectiveCards = DiceColor.values().length -1;


    /**
     * Gets a new instance of the ObjectiveCardManager in order to reset its assignedColors private field
     */
    @Before
    public void initializeManager(){
        manager = new ObjectiveCardManager();
    }

    /**
     * Sets the publicObjectiveCard and the privateObjectiveCard to null
     * Creates a new instance for publicObjectiveCards (list)
     */
    @Before
    public void initializeCards(){
        publicObjectiveCard = null;
        privateObjectiveCard = null;
        publicObjectiveCards = new ArrayList<>();
    }

    /**
     * Tests the retrieval of one {@link PrivateObjectiveCard} asserting the retrieved card is not null
     * @see ObjectiveCardManager#getPrivateObjectiveCard()
     */
    @Test
    public void testGetOnePrivateObjectiveCard(){
        privateObjectiveCard = manager.getPrivateObjectiveCard();
        assertNotNull(privateObjectiveCard);
    }

    /**
     * Tests the retrieval of all {@link PrivateObjectiveCard} asserting all the retrieved cards are not null
     * Fails if all PrivateObjectiveCards were already created
     * @see ObjectiveCardManager#getPrivateObjectiveCard()
     */
    @Test
    public void testGetAllPrivateObjectiveCards(){
        for(int i=0; i < numberOfPrivateObjectiveCards; i++){
            try{
                privateObjectiveCard = manager.getPrivateObjectiveCard();
                assertNotNull(privateObjectiveCard);
            } catch (BadBehaviourRuntimeException e ){
                fail();
            }
        }
    }

    /**
     * Tests the impossibility of getting more PrivateObjectiveCards than existing
     * @see ObjectiveCardManager#getPrivateObjectiveCard()
     */
    @Test
    public void testGetMorePrivateObjectiveCardsThanExisting(){
        //getting all private objective cards
        for(int i = 0; i < numberOfPrivateObjectiveCards; i++){
            privateObjectiveCard = manager.getPrivateObjectiveCard();
        }

        try{
            privateObjectiveCard = manager.getPrivateObjectiveCard();
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }

    /**
     * Tests that there are no PrivateObjectiveCards retrieved with no color
     * @see ObjectiveCardManager#getPrivateObjectiveCard()
     */
    @Test
    public void testPrivateObjectiveCardColorIsNotNoColor(){
        for(int i = 0; i < numberOfPrivateObjectiveCards; i++){
            privateObjectiveCard = manager.getPrivateObjectiveCard();
            assertNotNull(privateObjectiveCard);
            assertNotEquals(privateObjectiveCard.getColor(), DiceColor.NOCOLOR);
        }
        try{
            privateObjectiveCard = manager.getPrivateObjectiveCard();
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }

    /**
     * Tests the retrieval of one {@link PublicObjectiveCard} asserting the retrieved card is not null
     * @see ObjectiveCardManager#getPublicObjectiveCards(int)
     */
    @Test
    public void testGetOnePublicObjectiveCard(){
        publicObjectiveCard = manager.getPublicObjectiveCards(1).get(0);
        assertNotNull(publicObjectiveCard);
    }

    /**
     * Tests the retrieval of three {@link PublicObjectiveCard} asserting the retrieved cards are not null
     * @see ObjectiveCardManager#getPublicObjectiveCards(int)
     */
    @Test
    public void testGetThreePublicObjectiveCards(){
        try{
            publicObjectiveCards = manager.getPublicObjectiveCards(3);
        }catch (ValueOutOfBoundsException e){
            e.printStackTrace();
            fail();
        }
        assertEquals(3, publicObjectiveCards.size());
        for(PublicObjectiveCard card: publicObjectiveCards){
            assertNotNull(card);
        }
    }

    /**
     * Tests that all {@link PublicObjectiveCard} retrieved are different by comparing their titles
     * @see ObjectiveCardManager#getPublicObjectiveCards(int)
     */
    @Test
    public void testAllPublicObjectiveCardsRetrievedAreDifferent(){

        publicObjectiveCards = manager.getPublicObjectiveCards(numberOfPublicObjectiveCards);

        //compare each pair of cards to verify they are different
        for(int i=0; i < numberOfPublicObjectiveCards-1; i++){
            PublicObjectiveCard currentCard = publicObjectiveCards.get(i);
            assertNotNull(currentCard);
            for(int j=i+1; j < numberOfPublicObjectiveCards; j++){
                PublicObjectiveCard comparisonCard = publicObjectiveCards.get(j);
                if(currentCard.getTitle().equals(comparisonCard.getTitle())){
                    fail();
                }
            }
        }
    }

    /**
     * Tests the impossibility of getting more PublicObjectiveCards than existing or a negative quantity
     * @see ObjectiveCardManager#getPublicObjectiveCards(int)
     */
    @Test
    public void testGetMorePublicObjectiveCardsThanExisting(){
        try{
            publicObjectiveCards = manager.getPublicObjectiveCards(numberOfPublicObjectiveCards+1);
            fail();
        }catch (ValueOutOfBoundsException e){}

        try{
            publicObjectiveCards = manager.getPublicObjectiveCards(-1);
            fail();
        }catch (ValueOutOfBoundsException e){}

    }
}




