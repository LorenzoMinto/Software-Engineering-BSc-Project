package it.polimi.se2018.model;

import it.polimi.se2018.utils.BadDiceReferenceException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.se2018.model.DiceColor.*;
import static org.junit.Assert.*;

/**
 * Test for {@link TrackSlot} class
 *
 * @author Lorenzo Minto
 */
public class TrackSlotTest {

    private static List<Dice> dices;
    private static Dice dice1;
    private TrackSlot trackSlot;


    /**
     * Initializes variables for the tests
     */
    @BeforeClass
    public static void setUp(){
        dices = new ArrayList<>();

        dice1 = new Dice(BLUE, 5);
        Dice dice2 = new Dice(RED, 3);
        Dice dice3 = new Dice(GREEN, 2);
        Dice dice4 = new Dice(PURPLE, 1);
        Dice dice5 = new Dice(YELLOW, 4);

        dices.add(dice1);
        dices.add(dice2);
        dices.add(dice3);
        dices.add(dice4);
        dices.add(dice5);
    }
    
    @Before
    public void initializeTrackSlot(){
        trackSlot = new TrackSlot(new ArrayList<>()); 
    }

    /**
     * Tests adding a {@link Dice} to the {@link TrackSlot}
     * @see TrackSlot#addDice(Dice)
     */
    @Test
    public void testAddDice() {
        Dice dice = new Dice(DiceColor.BLUE, 2);

        trackSlot.addDice(dice);

        assertTrue(trackSlot.getDices().contains(dice));
    }

    /**
     * Tests the impossibility of adding a null {@link Dice} to the {@link TrackSlot}
     * @see TrackSlot#addDice(Dice)
     */
    @Test
    public void testAddNullDice() {
        try{
            trackSlot.addDice(null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests the removal of a {@link Dice} from a {@link TrackSlot}
     * @see TrackSlot#removeDice(Dice)
     */
    @Test
    public void testRemoveDice() {
        trackSlot = new TrackSlot(dices);

        try {
            trackSlot.removeDice(dice1);
        } catch (BadDiceReferenceException e) {
            e.printStackTrace();
            fail();
        }

        assertFalse(trackSlot.getDices().contains(dice1));
    }

    /**
     * Tests the impossibility of removing a null {@link Dice} from a {@link TrackSlot}
     * @see TrackSlot#removeDice(Dice)
     */
    @Test
    public void testRemoveNullDice() {
        trackSlot = new TrackSlot(dices);

        try {
            trackSlot.removeDice(null);
            fail();
        } catch (BadDiceReferenceException e) {
            e.printStackTrace();
            fail();
        } catch (IllegalArgumentException e){}
    }

    /**
     * Tests the impossibility of removing from a {@link TrackSlot} a {@link Dice} that is not in it
     * @see TrackSlot#removeDice(Dice)
     */
    @Test
    public void testRemoveDiceNotInTrackSlot() {
        trackSlot = new TrackSlot(dices);
        Dice dice = new Dice(DiceColor.BLUE, 2);

        try {
            trackSlot.removeDice(dice);
            fail();
        } catch (BadDiceReferenceException e) {}
    }

    /**
     * Tests the retrieval of the dices from the {@link TrackSlot}
     * @see TrackSlot#getDices()
     */
    @Test
    public void testGetDices() {
        trackSlot = new TrackSlot(dices);

        assertEquals(dices, trackSlot.getDices());
    }

    /**
     * Tests the retrieval of dices from a {@link TrackSlot} with no dice
     * @see TrackSlot#getDices()
     */
    @Test
    public void testGetDicesOfEmptyTrackSlot() {
        assertTrue(trackSlot.getDices().isEmpty());
    }

    /**
     * Tests the copy method of {@link TrackSlot}, should not return a null Object
     * @see TrackSlot#copy()
     */
    @Test
    public void testCopyTrackSlot(){
        assertNotNull(trackSlot.copy());
    }

    /**
     * Tests the toString method of {@link TrackSlot}, should return a String representation of the TrackSlot
     * @see TrackSlot#toString()
     */
    @Test
    public void testToString(){
        dices.clear();
        dices.add(dice1);
        trackSlot = new TrackSlot(dices);

        assertEquals(dices.toString(), trackSlot.toString());
    }
}