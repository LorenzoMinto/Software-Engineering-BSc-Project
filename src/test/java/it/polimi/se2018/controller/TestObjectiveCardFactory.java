package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestObjectiveCardFactory {

    private static ObjectiveCardFactory factory;
    private PrivateObjectiveCard privateObjectiveCard;
    private PublicObjectiveCard publicObjectiveCard;

    private static int indexOfRowsColorPublicObjectiveCard;
    private static int indexOfColumnsColorPublicObjectiveCard;
    private static int indexOfRowsValuePublicObjectiveCard;
    private static int indexOfColumnsValuePublicObjectiveCard;
    private static int indexOfOneTwoSetPublicObjectiveCard;
    private static int indexOfThreeFourSetPublicObjectiveCard;
    private static int indexOfFiveSixSetPublicObjectiveCard;
    private static int indexOfAllValuesSetPublicObjectiveCard;
    private static int indexOfDiagonalsColorPublicObjectiveCard;
    private static int indexOfAllColorsSetPublicObjectiveCard;
    private static int wrongIndex;



    @BeforeClass
    public static void getSingleton(){
        factory = ObjectiveCardFactory.getInstance();
    }

    @BeforeClass
    public static void initializeIndexesOfPublicObjectiveCards(){
        indexOfRowsColorPublicObjectiveCard = 0;
        indexOfColumnsColorPublicObjectiveCard = 1;
        indexOfRowsValuePublicObjectiveCard = 2;
        indexOfColumnsValuePublicObjectiveCard = 3;
        indexOfOneTwoSetPublicObjectiveCard = 4;
        indexOfThreeFourSetPublicObjectiveCard = 5;
        indexOfFiveSixSetPublicObjectiveCard = 6;
        indexOfAllValuesSetPublicObjectiveCard = 7;
        indexOfDiagonalsColorPublicObjectiveCard = 8;
        indexOfAllColorsSetPublicObjectiveCard = 9;

        wrongIndex = 14;
    }


    @Test
    public void testSingletonInstanceIsNotNull(){
        assertNotNull(ObjectiveCardFactory.getInstance());
    }

    @Test
    public void testCreateRedPrivateObjectiveCard(){
        privateObjectiveCard = factory.createPrivateObjectiveCard(DiceColors.RED);
        assertTrue(privateObjectiveCard instanceof PrivateObjectiveCard);
        assertEquals(DiceColors.RED, privateObjectiveCard.getColor());
    }

    @Test
    public void testCreateYellowPrivateObjectiveCard(){
        privateObjectiveCard = factory.createPrivateObjectiveCard(DiceColors.YELLOW);
        assertTrue(privateObjectiveCard instanceof PrivateObjectiveCard);
        assertEquals(DiceColors.YELLOW, privateObjectiveCard.getColor());
    }

    @Test
    public void testCreateGreenPrivateObjectiveCard(){
        privateObjectiveCard = factory.createPrivateObjectiveCard(DiceColors.GREEN);
        assertTrue(privateObjectiveCard instanceof PrivateObjectiveCard);
        assertEquals(DiceColors.GREEN, privateObjectiveCard.getColor());
    }

    @Test
    public void testCreateBluePrivateObjectiveCard(){
        privateObjectiveCard = factory.createPrivateObjectiveCard(DiceColors.BLUE);
        assertTrue(privateObjectiveCard instanceof PrivateObjectiveCard);
        assertEquals(DiceColors.BLUE, privateObjectiveCard.getColor());
    }

    @Test
    public void testCreatePurplePrivateObjectiveCard(){
        privateObjectiveCard = factory.createPrivateObjectiveCard(DiceColors.PURPLE);
        assertTrue(privateObjectiveCard instanceof PrivateObjectiveCard);
        assertEquals(DiceColors.PURPLE, privateObjectiveCard.getColor());
    }

    @Test
    public void testCreateNoColorPrivateObjectiveCard(){
        try {
            privateObjectiveCard = factory.createPrivateObjectiveCard(DiceColors.NOCOLOR);
            fail();
        }catch(IllegalArgumentException e){
            assertNull(privateObjectiveCard);
        }
    }


    @Test
    public void testCreateRowsColorPublicObjectiveCard(){
        publicObjectiveCard = factory.createPublicObjectiveCardCardByIndex(indexOfRowsColorPublicObjectiveCard);
        assertTrue(publicObjectiveCard instanceof RowsColumnsPublicObjectiveCard);
    }

    @Test
    public void testCreateColumnsColorPublicObjectiveCard(){
        publicObjectiveCard = factory.createPublicObjectiveCardCardByIndex(indexOfColumnsColorPublicObjectiveCard);
        assertTrue(publicObjectiveCard instanceof RowsColumnsPublicObjectiveCard);
    }

    @Test
    public void testCreateRowsValuePublicObjectiveCard(){
        publicObjectiveCard = factory.createPublicObjectiveCardCardByIndex(indexOfRowsValuePublicObjectiveCard);
        assertTrue(publicObjectiveCard instanceof RowsColumnsPublicObjectiveCard);
    }

    @Test
    public void testCreateColumnsValuePublicObjectiveCard(){
        publicObjectiveCard = factory.createPublicObjectiveCardCardByIndex(indexOfColumnsValuePublicObjectiveCard);
        assertTrue(publicObjectiveCard instanceof RowsColumnsPublicObjectiveCard);
    }

    @Test
    public void testCreateOneTwoSetPublicObjectiveCard(){
        publicObjectiveCard = factory.createPublicObjectiveCardCardByIndex(indexOfOneTwoSetPublicObjectiveCard);
        assertTrue(publicObjectiveCard instanceof SetPublicObjectiveCard);
    }

    @Test
    public void testCreateThreeFourSetPublicObjectiveCard(){
        publicObjectiveCard = factory.createPublicObjectiveCardCardByIndex(indexOfThreeFourSetPublicObjectiveCard);
        assertTrue(publicObjectiveCard instanceof SetPublicObjectiveCard);
    }

    @Test
    public void testCreateFiveSixSetPublicObjectiveCard(){
        publicObjectiveCard = factory.createPublicObjectiveCardCardByIndex(indexOfFiveSixSetPublicObjectiveCard);
        assertTrue(publicObjectiveCard instanceof SetPublicObjectiveCard);
    }

    @Test
    public void testCreateAllValuesSetPublicObjectiveCard(){
        publicObjectiveCard = factory.createPublicObjectiveCardCardByIndex(indexOfAllValuesSetPublicObjectiveCard);
        assertTrue(publicObjectiveCard instanceof SetPublicObjectiveCard);
    }

    @Test
    public void testCreateDiagonalsPublicObjectiveCard(){
        publicObjectiveCard = factory.createPublicObjectiveCardCardByIndex(indexOfDiagonalsColorPublicObjectiveCard);
        assertTrue(publicObjectiveCard instanceof DiagonalsPublicObjectiveCard);
    }

    @Test
    public void testCreateAllColorsSetPublicObjectiveCard(){
        publicObjectiveCard = factory.createPublicObjectiveCardCardByIndex(indexOfAllColorsSetPublicObjectiveCard);
        assertTrue(publicObjectiveCard instanceof SetPublicObjectiveCard);
    }

    @Test
    public void testCreateWrongIndexPublicObjectiveCard(){
        try{
            publicObjectiveCard = factory.createPublicObjectiveCardCardByIndex(wrongIndex);
            fail();
        }catch (RuntimeException e){
            assertTrue(wrongIndex<0 || wrongIndex>9);
        }
    }



}
















