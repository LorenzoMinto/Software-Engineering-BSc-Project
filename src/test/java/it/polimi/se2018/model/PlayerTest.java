package it.polimi.se2018.model;

import it.polimi.se2018.controller.WindowPatternManager;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.se2018.model.DiceColor.RED;
import static org.junit.Assert.*;

/**
 * Test for {@link Player} Class
 *
 * @author Federico Haag
 * @author Jacopo Pio Gargano
 */
public class PlayerTest {

    private static Player player;
    private static WindowPattern windowPattern;

    /**
     * Initializes the needed windowPatterns for the test
     */
    @BeforeClass
    public static void initializeWindowPattern(){
        WindowPatternManager manager = new WindowPatternManager();
        List<WindowPattern> windowPatterns = new ArrayList<>(manager.getPairsOfPatterns(1));
        windowPattern = windowPatterns.get(0);
    }

    /**
     * Initializes the player before each test
     */
    @Before
    public void initializePlayer(){
        player = new Player("nickname", new PrivateObjectiveCard("","","",RED));
    }

    /**
     * Tests the constructor of the {@link Player} class
     *@see Player#Player(String, PrivateObjectiveCard)
     */
    @Test
    public void testConstructor(){
        assertNotNull(player);
    }

    /**
     * Tests the impossibility of creating a player with a null {@link PrivateObjectiveCard}
     *@see Player#Player(String, PrivateObjectiveCard)
     */
    @Test
    public void testConstructorWithNullCard(){
        try{
            player = new Player("nickname", null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests the retrieval of the player's favor tokens
     *@see Player#getFavorTokens()
     */
    @Test
    public void testGetFavorTokens() {
        assertEquals(0, player.getFavorTokens());
    }

    /**
     * Tests the retrieval of the player's favor token after the windowPattern is assigned
     *@see Player#decreaseTokens(int)
     */
    @Test
    public void testGetFavorTokensAfterWindowPatternAssigned(){
        int playerTokens = windowPattern. getDifficulty();
        player.decreaseTokens(playerTokens);
        assertEquals(0, player.getFavorTokens());
    }

    /**
     * Tests the retrieval of a player's ID
     *@see Player#getID()
     */
    @Test
    public void testGetID() {
        assertEquals("nickname", player.getID());
    }

    /**
     * Tests setting a player's windowPatter
     *@see Player#setWindowPattern(WindowPattern)
     */
    @Test
    public void setWindowPattern() {
        player.setWindowPattern(windowPattern);
        assertEquals(windowPattern, player.getWindowPattern());
    }

    /**
     * Tests the impossibility of setting a player's {@link WindowPattern} to null
     *@see Player#setWindowPattern(WindowPattern)
     */
    @Test
    public void setNullWindowPattern() {
        try {
            player.setWindowPattern(null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests the retrieval of a player's {@link PrivateObjectiveCard}
     *@see Player#getPrivateObjectiveCard()
     */
    @Test
    public void testGetPrivateObjectiveCard() {
        PrivateObjectiveCard privateObjectiveCard = new PrivateObjectiveCard("", "", "", DiceColor.RED);
        player = new Player("nickname", privateObjectiveCard);
        assertEquals(privateObjectiveCard, player.getPrivateObjectiveCard());
    }

    /**
     * Tests decreasing a player's tokens
     * @see Player#decreaseTokens(int)
     */
    @Test
    public void testDecreaseTokens() {
        assertFalse(player.decreaseTokens(2));

        player.setWindowPattern(windowPattern);

        int playerTokens = windowPattern. getDifficulty();
        player.decreaseTokens(playerTokens);
        assertEquals(0, player.getFavorTokens());
    }

    /**
     * Tests the impossibility of decreasing a player's tokens of a negative quantity
     * @see Player#decreaseTokens(int)
     */
    @Test
    public void testDecreaseTokensOfNegativeQuantity() {
        try{
            player.decreaseTokens(-3);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests the retrieval of a player's {@link WindowPattern}
     *@see Player#getWindowPattern()
     */
    @Test
    public void testGetWindowPattern() {
        assertNull(player.getWindowPattern());

        player.setWindowPattern(windowPattern);
        assertEquals(windowPattern, player.getWindowPattern());
    }

    /**
     * Tests the player's equals method
     * @see Player#equals(Object)
     */
    @Test
    public void testEquals() {
        assertTrue(player.equals(new Player("nickname", new PrivateObjectiveCard("","","",RED))));
    }

    /**
     * Tests the player's hashCode method
     * @see Player#hashCode()
     */
    @Test
    public void testHashCode() {
        assertNotNull(player.hashCode());
    }
}