package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

/**
 * @author Jacopo Pio Gargano
 */

public class TestObjectiveCardManager {

    private ObjectiveCardManager manager;
    private PrivateObjectiveCard privateObjectiveCard;
    private PublicObjectiveCard publicObjectiveCard;
    private List<PublicObjectiveCard> publicObjectiveCards;

    private static int numberOfPublicObjectiveCards = 10;

    //length -1 because NOCOLOR must not be considered
    private static int numberOfPrivateObjectiveCards = DiceColors.values().length -1;


    //done to reset assignedColors private field of manager
    @Before
    public void initializeManager(){
        manager = new ObjectiveCardManager();
    }

    @Before
    public void initializeCards(){
        publicObjectiveCard = null;
        privateObjectiveCard = null;
        publicObjectiveCards = new ArrayList<>();
    }

    @Test
    public void testGetOnePrivateObjectiveCard(){
        privateObjectiveCard = manager.getPrivateObjectiveCard();
        assertNotNull(privateObjectiveCard);
    }

    @Test
    public void testGetAllPrivateObjectiveCards(){
        for(int i=0; i < numberOfPrivateObjectiveCards; i++){
            try{
                privateObjectiveCard = manager.getPrivateObjectiveCard();
            } catch (RuntimeException e ){
                fail();
            }
        }
    }

    @Test
    public void testGetMorePrivateObjectiveCardsThanExisting(){
        //getting all private objective cards
        for(int i = 0; i < numberOfPrivateObjectiveCards; i++){
            privateObjectiveCard = manager.getPrivateObjectiveCard();
        }

        try{
            privateObjectiveCard = manager.getPrivateObjectiveCard();
            fail();
        }catch (RuntimeException e){}
    }

    //TODO: to be run with testGetAllPrivateObjectiveCards
    @Test
    public void testPrivateObjectiveCardColorIsNotNoColor(){
        for(int i = 0; i < numberOfPrivateObjectiveCards; i++){
            privateObjectiveCard = manager.getPrivateObjectiveCard();
            assertNotEquals(privateObjectiveCard.getColor(), DiceColors.NOCOLOR);
        }
    }

    @Test
    public void testGetOnePublicObjectiveCard(){
        publicObjectiveCard = manager.getPublicObjectiveCards(1).get(0);
        assertNotNull(publicObjectiveCard);
    }

    @Test
    public void testGetThreePublicObjectiveCards(){
        publicObjectiveCards = manager.getPublicObjectiveCards(3);
        assertEquals(3, publicObjectiveCards.size());
        for(PublicObjectiveCard card: publicObjectiveCards){
            assertNotNull(card);
        }
    }

    //TODO: to be run with testAllPublicObjectiveCardsTitlesAreDifferent of TestObjectiveCardFactory
    @Test
    public void testAllPublicObjectiveCardsRetrievedAreDifferent(){

        publicObjectiveCards = manager.getPublicObjectiveCards(numberOfPublicObjectiveCards);

        //compare each pair of cards to verify they are different
        for(int i=0; i < numberOfPublicObjectiveCards-1; i++){
            PublicObjectiveCard currentCard = publicObjectiveCards.get(i);
            for(int j=i+1; j < numberOfPublicObjectiveCards; j++){
                PublicObjectiveCard comparisonCard = publicObjectiveCards.get(j);
                if(currentCard.getTitle().equals(comparisonCard.getTitle())){
                    fail();
                }
            }
        }
    }

    @Test
    public void testGetMorePublicObjectiveCardsThanExisting(){
        try{
            publicObjectiveCards = manager.getPublicObjectiveCards(numberOfPublicObjectiveCards+1);
            fail();
        }catch (RuntimeException e){}
    }

    @Test
    public void testGetNegativeQuantityOfPublicObjectiveCards(){
        try{
            publicObjectiveCards = manager.getPublicObjectiveCards(-1);
            fail();
        }catch (RuntimeException e){}
    }

}




