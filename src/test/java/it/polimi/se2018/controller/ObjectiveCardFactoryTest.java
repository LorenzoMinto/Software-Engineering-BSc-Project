package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.ValueOutOfBoundsException;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for {@link ObjectiveCardFactory} class
 *
 * @author Jacopo Pio Gargano
 */

public class ObjectiveCardFactoryTest {

    private static ObjectiveCardFactory factory;
    private PrivateObjectiveCard privateObjectiveCard;
    private PublicObjectiveCard publicObjectiveCard;

    private static final int indexOfRowsColorPublicObjectiveCard = 0;
    private static final int indexOfColumnsColorPublicObjectiveCard = 1;
    private static final int indexOfRowsValuePublicObjectiveCard = 2;
    private static final int indexOfColumnsValuePublicObjectiveCard = 3;
    private static final int indexOfOneTwoSetPublicObjectiveCard = 4;
    private static final int indexOfThreeFourSetPublicObjectiveCard = 5;
    private static final int indexOfFiveSixSetPublicObjectiveCard = 6;
    private static final int indexOfAllValuesSetPublicObjectiveCard = 7;
    private static final int indexOfDiagonalsColorPublicObjectiveCard = 8;
    private static final int indexOfAllColorsSetPublicObjectiveCard = 9;
    private static final int wrongIndex = 14;

    private static final int numberOfPublicObjectiveCards = 10;


    /**
     * Retrieves the singleton instance of the {@link ObjectiveCardFactory}
     */
    @BeforeClass
    public static void getSingleton(){
        factory = ObjectiveCardFactory.getInstance();
    }

    /**
     * Tests the singleton getInstance method does not return null, even if called multiple times
     * @see ObjectiveCardFactory#getInstance()
     */
    @Test
    public void testSingletonInstanceIsNotNull(){
        assertNotNull(ObjectiveCardFactory.getInstance());
        assertNotNull(ObjectiveCardFactory.getInstance());
        assertNotNull(ObjectiveCardFactory.getInstance());
    }

    /**
     * Tests that the two instances of the singleton are the same instance
     * @see ObjectiveCardFactory#getInstance()
     */
    @Test
    public void testSingletonInstance(){
        ObjectiveCardFactory factory1 = ObjectiveCardFactory.getInstance();
        ObjectiveCardFactory factory2 = ObjectiveCardFactory.getInstance();
        assertEquals(factory1, factory2);
    }

    /**
     * Tests the creation of a Red private objective card
     * @see ObjectiveCardFactory#createPrivateObjectiveCard(DiceColor)
     */
    @Test
    public void testCreateRedPrivateObjectiveCard(){
        privateObjectiveCard = factory.createPrivateObjectiveCard(DiceColor.RED);
        assertNotNull(privateObjectiveCard);
        assertEquals(DiceColor.RED, privateObjectiveCard.getColor());
    }

    /**
     * Tests the creation of a Yellow private objective card
     * @see ObjectiveCardFactory#createPrivateObjectiveCard(DiceColor)
     */
    @Test
    public void testCreateYellowPrivateObjectiveCard(){
        privateObjectiveCard = factory.createPrivateObjectiveCard(DiceColor.YELLOW);
        assertNotNull(privateObjectiveCard);
        assertEquals(DiceColor.YELLOW, privateObjectiveCard.getColor());
    }

    /**
     * Tests the creation of a Green private objective card
     * @see ObjectiveCardFactory#createPrivateObjectiveCard(DiceColor)
     */
    @Test
    public void testCreateGreenPrivateObjectiveCard(){
        privateObjectiveCard = factory.createPrivateObjectiveCard(DiceColor.GREEN);
        assertNotNull(privateObjectiveCard);
        assertEquals(DiceColor.GREEN, privateObjectiveCard.getColor());
    }

    /**
     * Tests the creation of a Blue private objective card
     * @see ObjectiveCardFactory#createPrivateObjectiveCard(DiceColor)
     */
    @Test
    public void testCreateBluePrivateObjectiveCard(){
        privateObjectiveCard = factory.createPrivateObjectiveCard(DiceColor.BLUE);
        assertNotNull(privateObjectiveCard);
        assertEquals(DiceColor.BLUE, privateObjectiveCard.getColor());
    }

    /**
     * Tests the creation of a Purple private objective card
     * @see ObjectiveCardFactory#createPrivateObjectiveCard(DiceColor)
     */
    @Test
    public void testCreatePurplePrivateObjectiveCard(){
        privateObjectiveCard = factory.createPrivateObjectiveCard(DiceColor.PURPLE);
        assertNotNull(privateObjectiveCard);
        assertEquals(DiceColor.PURPLE, privateObjectiveCard.getColor());
    }

    /**
     * Tests the impossibility of creating a no color private objective card
     * @see ObjectiveCardFactory#createPrivateObjectiveCard(DiceColor)
     */
    @Test
    public void testCreateNoColorPrivateObjectiveCard(){
        try {
            privateObjectiveCard = factory.createPrivateObjectiveCard(DiceColor.NOCOLOR);
            fail();
        }catch(IllegalArgumentException e){
            assertNull(privateObjectiveCard);
        }
    }

    /**
     * Tests the creation of a RowsColor PublicObjectiveCard
     * @see ObjectiveCardFactory#createPublicObjectiveCardCardByIndex(int)
     */
    @Test
    public void testCreateRowsColorPublicObjectiveCard(){
        publicObjectiveCard = factory.createPublicObjectiveCardCardByIndex(indexOfRowsColorPublicObjectiveCard);
        assertTrue(publicObjectiveCard instanceof RowsColumnsPublicObjectiveCard);
    }

    /**
     * Tests the creation of a ColumnsColor PublicObjectiveCard
     * @see ObjectiveCardFactory#createPublicObjectiveCardCardByIndex(int)
     */
    @Test
    public void testCreateColumnsColorPublicObjectiveCard(){
        publicObjectiveCard = factory.createPublicObjectiveCardCardByIndex(indexOfColumnsColorPublicObjectiveCard);
        assertTrue(publicObjectiveCard instanceof RowsColumnsPublicObjectiveCard);
    }

    /**
     * Tests the creation of a RowsValue PublicObjectiveCard
     * @see ObjectiveCardFactory#createPublicObjectiveCardCardByIndex(int)
     */
    @Test
    public void testCreateRowsValuePublicObjectiveCard(){
        publicObjectiveCard = factory.createPublicObjectiveCardCardByIndex(indexOfRowsValuePublicObjectiveCard);
        assertTrue(publicObjectiveCard instanceof RowsColumnsPublicObjectiveCard);
    }

    /**
     * Tests the creation of a ColumnsValue PublicObjectiveCard
     * @see ObjectiveCardFactory#createPublicObjectiveCardCardByIndex(int)
     */
    @Test
    public void testCreateColumnsValuePublicObjectiveCard(){
        publicObjectiveCard = factory.createPublicObjectiveCardCardByIndex(indexOfColumnsValuePublicObjectiveCard);
        assertTrue(publicObjectiveCard instanceof RowsColumnsPublicObjectiveCard);
    }

    /**
     * Tests the creation of a OneTwoSet PublicObjectiveCard
     * @see ObjectiveCardFactory#createPublicObjectiveCardCardByIndex(int)
     */
    @Test
    public void testCreateOneTwoSetPublicObjectiveCard(){
        publicObjectiveCard = factory.createPublicObjectiveCardCardByIndex(indexOfOneTwoSetPublicObjectiveCard);
        assertTrue(publicObjectiveCard instanceof SetPublicObjectiveCard);
    }

    /**
     * Tests the creation of a ThreeFourSet PublicObjectiveCard
     * @see ObjectiveCardFactory#createPublicObjectiveCardCardByIndex(int)
     */
    @Test
    public void testCreateThreeFourSetPublicObjectiveCard(){
        publicObjectiveCard = factory.createPublicObjectiveCardCardByIndex(indexOfThreeFourSetPublicObjectiveCard);
        assertTrue(publicObjectiveCard instanceof SetPublicObjectiveCard);
    }

    /**
     * Tests the creation of a FiveSixSet PublicObjectiveCard
     * @see ObjectiveCardFactory#createPublicObjectiveCardCardByIndex(int)
     */
    @Test
    public void testCreateFiveSixSetPublicObjectiveCard(){
        publicObjectiveCard = factory.createPublicObjectiveCardCardByIndex(indexOfFiveSixSetPublicObjectiveCard);
        assertTrue(publicObjectiveCard instanceof SetPublicObjectiveCard);
    }

    /**
     * Tests the creation of a AllValuesSet PublicObjectiveCard
     * @see ObjectiveCardFactory#createPublicObjectiveCardCardByIndex(int)
     */
    @Test
    public void testCreateAllValuesSetPublicObjectiveCard(){
        publicObjectiveCard = factory.createPublicObjectiveCardCardByIndex(indexOfAllValuesSetPublicObjectiveCard);
        assertTrue(publicObjectiveCard instanceof SetPublicObjectiveCard);
    }

    /**
     * Tests the creation of a Diagonals PublicObjectiveCard
     * @see ObjectiveCardFactory#createPublicObjectiveCardCardByIndex(int)
     */
    @Test
    public void testCreateDiagonalsPublicObjectiveCard(){
        publicObjectiveCard = factory.createPublicObjectiveCardCardByIndex(indexOfDiagonalsColorPublicObjectiveCard);
        assertTrue(publicObjectiveCard instanceof DiagonalsPublicObjectiveCard);
    }

    /**
     * Tests the creation of a AllColorsSet PublicObjectiveCard
     * @see ObjectiveCardFactory#createPublicObjectiveCardCardByIndex(int)
     */
    @Test
    public void testCreateAllColorsSetPublicObjectiveCard(){
        publicObjectiveCard = factory.createPublicObjectiveCardCardByIndex(indexOfAllColorsSetPublicObjectiveCard);
        assertTrue(publicObjectiveCard instanceof SetPublicObjectiveCard);
    }

    /**
     * Tests the impossibility of creating a PublicObjectiveCard whose index does not exist yet
     * @see ObjectiveCardFactory#createPublicObjectiveCardCardByIndex(int)
     */
    @Test
    public void testCreateWrongIndexPublicObjectiveCard(){
        try{
            publicObjectiveCard = factory.createPublicObjectiveCardCardByIndex(wrongIndex);
            fail();
        }catch (ValueOutOfBoundsException e){}
    }

    /**
     * Tests that all PublicObjectiveCards titles are different
     * By testing this, it is assumed that all PublicObjectiveCards are different
     * @see ObjectiveCardFactory#createPublicObjectiveCardCardByIndex(int)
     */
    @Test
    public void testAllPublicObjectiveCardsTitlesAreDifferent(){
        for(int i=0; i < numberOfPublicObjectiveCards-1; i++){
            PublicObjectiveCard currentCard = factory.createPublicObjectiveCardCardByIndex(i);
            for(int j=i+1; j < numberOfPublicObjectiveCards; j++){
                PublicObjectiveCard comparisonCard = factory.createPublicObjectiveCardCardByIndex(j);
                if(currentCard.getTitle().equals(comparisonCard.getTitle())){
                    fail();
                }
            }
        }
    }

}
















