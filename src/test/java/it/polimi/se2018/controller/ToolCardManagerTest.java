package it.polimi.se2018.controller;

import it.polimi.se2018.model.EmptyPlacementRule;
import it.polimi.se2018.model.PlacementRule;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test for {@link ToolCardManager} classD
 *
 * @author Jacopo Pio Gargano
 */
public class ToolCardManagerTest {


    private static ToolCardManager manager;

    /**
     * Initializes the toolCardManager for the test
     */
    @BeforeClass
    public static void initializeManager(){
        manager = new ToolCardManager(new EmptyPlacementRule());
    }

    /**
     * Tests the constructor for {@link ToolCardManager} classD
     * @see ToolCardManager#ToolCardManager(PlacementRule)
     */
    @Test
    public void testConstructor(){
        assertNotNull(manager);
    }

    /**
     * Tests the impossibility of retrieving a negative number of toolCards
     * @see ToolCardManager#getRandomToolCards(int)
     */
    @Test
    public void testGetNegativeQuantityOfToolCards(){
        try{
            manager.getRandomToolCards(-3);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests the impossibility of retrieving more toolCards than available
     * @see ToolCardManager#getRandomToolCards(int)
     */
    @Test
    public void testGetMoreToolCardsThanExisting(){
        try{
            manager.getRandomToolCards(50);
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }

    /**
     * Tests the retrieval of two toolCards and that they are different
     * @see ToolCardManager#getRandomToolCards(int)
     */
    @Test
    public void testGetToolCards(){
        List<ToolCard> toolCards = manager.getRandomToolCards(2);
        assertNotEquals(toolCards.get(0), toolCards.get(1));
    }

}