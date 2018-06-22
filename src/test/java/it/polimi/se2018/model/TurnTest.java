package it.polimi.se2018.model;

import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.ValueOutOfBoundsException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

import static it.polimi.se2018.model.DiceColor.*;
import static org.junit.Assert.*;

/**
 * Test for {@link Turn} class
 *
 * @author Jacopo Pio Gargano
 */

public class TurnTest {

    private Turn turn;

    private static PrivateObjectiveCard privateObjectiveCard;
    private static Player player;
    private static Dice dice;
    private static ToolCard toolCard;
    private static String playerName;


    /**
     * Initializes variables for the tests
     */
    @BeforeClass
    public static void setUp() {
        privateObjectiveCard = new PrivateObjectiveCard("","","", RED);
        playerName = "player";
        player = new Player(playerName, privateObjectiveCard);
        dice = new Dice(RED);

        Properties properties = new Properties();

        properties.put("id","ID1");
        properties.put("title","title1");
        properties.put("description","description1");
        properties.put("imageURL","imageURL1");
        properties.put("neededTokens", "1");
        properties.put("tokensUsageMultiplier", "2");

        toolCard = new ToolCard(properties, new HashMap<>(), null, new HashSet<>());

    }

    /**
     * Initializes the turn
     */
    @Before
    public void initializeDefaultTurnWithPlayer(){
        turn = new Turn(0, player);
    }


    /**
     * Tests the class constructor
     * @see Turn#Turn(int, Player)
     */
    @Test
    public void testConstructor(){
        turn = new Turn(0,player);
        assertNotNull(turn);
    }

    /**
     * Tests the impossibility of creating a Turn with a negative number
     * @see Turn#Turn(int, Player)
     */
    @Test
    public void testConstructorWithNegativeTurnNumber(){
        try{
            turn = new Turn(-1, player);
            fail();
        }catch (ValueOutOfBoundsException e){}
    }

    /**
     * Tests the impossibility of creating a Turn with a null {@link Player}
     * @see Turn#Turn(int, Player)
     */
    @Test
    public void testConstructorWithNullPlayer(){
        try{
            turn = new Turn(0, null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests setting the drafted {@link Dice} of a turn
     * @see Turn#setDraftedDice(Dice)
     */
    @Test
    public void testHasActuallyDrafted(){
        turn.setDraftedDice(dice);
        assertTrue(turn.hasDrafted());
    }

    /**
     * Tests that a turn with no drafted {@link Dice} has not drafted yet
     * @see Turn#getDraftedDice()
     * @see Turn#hasDrafted()
     */
    @Test
    public void testHasNotDrafted(){
        assertNull(turn.getDraftedDice());
        assertFalse(turn.hasDrafted());
    }

    /**
     * Tests setting the draftedAndPlaced property of {@link Turn} after drafting
     * @see Turn#setDraftedDice(Dice)
     * @see Turn#setDraftedAndPlaced()
     */
    @Test
    public void testHasDraftedAndPlaced(){
        turn.setDraftedDice(dice);
        turn.setDraftedAndPlaced();
        assertTrue(turn.hasDraftedAndPlaced());
    }

    /**
     * Tests the impossibility of setting the draftedAndPlaced property of {@link Turn} if has not drafted
     * @see Turn#setDraftedAndPlaced()
     */
    @Test
    public void testHasDraftedAndPlacedWithoutDrafting(){
        try{
            assertNull(turn.getDraftedDice());
            turn.setDraftedAndPlaced();
            fail();
        }catch (IllegalStateException e){}
    }

    /**
     * Tests that a {@link ToolCard} was used in the {@link Turn} after the usedToolCard property of {@link Turn} was set
     * @see Turn#setUsedToolCard(ToolCard)
     * @see Turn#hasUsedToolCard()
     */
    @Test
    public void testHasActuallyUsedToolCard(){
        turn.setUsedToolCard(toolCard);
        assertTrue(turn.hasUsedToolCard());
    }

    /**
     * Tests that a {@link Turn} has not used a {@link ToolCard} if no usedToolCard of {@link Turn} was set
     * @see Turn#hasUsedToolCard()
     */
    @Test
    public void testHasNotUsedToolCard(){
        assertFalse(turn.hasUsedToolCard());
    }

    /**
     * Tests the impossibility of setting the usedToolCard of {@link Turn} to null
     * @see Turn#setUsedToolCard(ToolCard)
     */
    @Test
    public void testSetNullUsedToolCard(){
        try {
            turn.setUsedToolCard(null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests the impossibility of retrieving the number of {@link TrackSlot} of the {@link Dice} chosen on the {@link Track} if it was not set
     * @see Turn#getSlotOfTrackChosenDice()
     */
    @Test
    public void getNotSetSlotOfTrackChosenDice(){
        try {
            turn.getSlotOfTrackChosenDice();
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }

    /**
     * Tests setting and retrieving the number of {@link TrackSlot} of the {@link Dice} chosen on the {@link Track}
     * @see Turn#getSlotOfTrackChosenDice()
     */
    @Test
    public void testSetSlotOfTrackChosenDice(){
        turn.setSlotOfTrackChosenDice(1);
        assertEquals(1,turn.getSlotOfTrackChosenDice());
    }

    /**
     * Tests the impossibility of setting the number of {@link TrackSlot} of the {@link Dice} chosen on the {@link Track} to a negative number
     * @see Turn#setSlotOfTrackChosenDice(int)
     */
    @Test
    public void testSetSlotOfTrackChosenDiceWithNegativeValue(){
        try{
            turn.setSlotOfTrackChosenDice(-1);
            fail();
        }catch (ValueOutOfBoundsException e){}
    }

    /**
     * Tests setting the drafted dice
     * @see Turn#setDraftedDice(Dice)
     */
    @Test
    public void testSetDraftedDice(){
        turn.setDraftedDice(dice);
        assertNotNull(turn.getDraftedDice());
    }

    /**
     * Tests the impossibility of setting the drafted {@link Dice} to a null dice
     * @see Turn#setDraftedDice(Dice)
     */
    @Test
    public void testSetNullDraftedDice(){
        try {
            turn.setDraftedDice(null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests setting the {@link Dice} chosen on the {@link Track}
     * @see Turn#setTrackChosenDice(Dice)
     * @see Turn#getTrackChosenDice()
     */
    @Test
    public void testSetTrackChosenDice(){
        turn.setTrackChosenDice(dice);
        assertNotNull(turn.getTrackChosenDice());
    }

    /**
     * Tests the impossibility of setting the {@link Dice} chosen on the {@link Track} to a null dice
     * @see Turn#setTrackChosenDice(Dice)
     */
    @Test
    public void testSetNullTrackChosenDice(){
        try {
            turn.setTrackChosenDice(null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests that the {@link Player} of the current {@link Turn} is the current player
     * @see Turn#isCurrentPlayer(String)
     */
    @Test
    public void testIsCurrentPlayer(){
        Player playerTest = turn.getPlayer();
        assertTrue(turn.isCurrentPlayer(playerTest.getID()));
    }

    /**
     * Tests that a {@link Player} with the same name of the player of the current{@link Turn} is the current player
     * @see Turn#isCurrentPlayer(String)
     */
    @Test
    public void testIsCurrentPlayerSameName(){
        Player playerTest = new Player(playerName, privateObjectiveCard);
        assertTrue(turn.isCurrentPlayer(playerTest.getID()));
    }

    /**
     * Tests that a different {@link Player} from the current player is not the current player
     * @see Turn#isCurrentPlayer(String)
     */
    @Test
    public void testDifferentPlayerIsCurrentPlayer(){
        Player playerTest = new Player( "differentName", privateObjectiveCard);
        assertFalse(turn.isCurrentPlayer(playerTest.getID()));
    }

    /**
     * Tests the impossibility of checking that a null {@link Player} is the current player
     * @see Turn#isCurrentPlayer(String)
     */
    @Test
    public void testNullPlayerIsCurrentPlayer(){
        try{
            turn.isCurrentPlayer(null);
            fail();
        }catch (IllegalArgumentException e){}
    }



}