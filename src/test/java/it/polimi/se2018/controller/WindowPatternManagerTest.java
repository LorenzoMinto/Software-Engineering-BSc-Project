package it.polimi.se2018.controller;

import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * Test for {@link WindowPatternManager} class
 *
 * @author Jacopo Pio Gargano
 */
public class WindowPatternManagerTest {

    private static WindowPatternManager manager;

    /**
     * Initializes the manager for the tests
     */
    @BeforeClass
    public static void initializeManager(){
        manager = new WindowPatternManager();
    }

    /**
     * Tests the constructor for the class
     * @see WindowPatternManager#WindowPatternManager()
     */
    @Test
    public void testConstructor(){
        assertNotNull(manager);
    }

    /**
     * Tests the impossibility of retrieving a negative pair of {@link it.polimi.se2018.model.WindowPattern}
     * @see WindowPatternManager#getPairsOfPatterns(int)
     */
    @Test
    public void testGetNegativePairsOfPatterns(){
        try{
            manager.getPairsOfPatterns(-1);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests the impossibility of retrieving more windowPatterns than available
     * @see WindowPatternManager#getPairsOfPatterns(int)
     */
    @Test
    public void testGetMorePatternsThanAvailable(){
        try {
            manager.getPairsOfPatterns(60);
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }

    /**
     * Tests the retrieval of a pair of {@link it.polimi.se2018.model.WindowPattern}
     * @see WindowPatternManager#getPairsOfPatterns(int)
     */
    @Test
    public void testGetAPairOfWindowPatterns(){
        HashSet windowPatterns = (HashSet)manager.getPairsOfPatterns(1);
        assertEquals(2, windowPatterns.size());
    }

}