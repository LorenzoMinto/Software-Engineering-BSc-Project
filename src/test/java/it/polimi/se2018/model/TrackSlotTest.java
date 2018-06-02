package it.polimi.se2018.model;

import it.polimi.se2018.utils.BadDiceReferenceException;
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

    /**
     * Tests adding a {@link Dice} to the {@link TrackSlot}
     */
    @Test
    public void testAddDice() {
        Dice dice = new Dice(DiceColor.BLUE, 2);
        TrackSlot slot = new TrackSlot(new ArrayList<>());

        slot.addDice(dice);

        assertTrue(slot.getDices().contains(dice));
    }

    /**
     * Tests the impossibility of adding a null {@link Dice} to the {@link TrackSlot}
     */
    @Test
    public void testAddNullDice() {
        TrackSlot slot = new TrackSlot(new ArrayList<>());

        try{
            slot.addDice(null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests the removal of a {@link Dice} from a {@link TrackSlot}
     */
    @Test
    public void testRemoveDice() {
        TrackSlot slot = new TrackSlot(dices);

        try {
            slot.removeDice(dice1);
        } catch (BadDiceReferenceException e) {
            e.printStackTrace();
            fail();
        }

        assertFalse(slot.getDices().contains(dice1));
    }

    /**
     * Tests the impossibility of removing a null {@link Dice} from a {@link TrackSlot}
     */
    @Test
    public void testRemoveNullDice() {
        TrackSlot slot = new TrackSlot(dices);

        try {
            slot.removeDice(null);
            fail();
        } catch (BadDiceReferenceException e) {
            e.printStackTrace();
            fail();
        } catch (IllegalArgumentException e){}
    }

    /**
     * Tests the impossibility of removing a {@link Dice} that is not in the {@link TrackSlot} from it
     */
    @Test
    public void testRemoveDiceNotInTrackSlot() {
        TrackSlot slot = new TrackSlot(dices);
        Dice dice = new Dice(DiceColor.BLUE, 2);

        try {
            slot.removeDice(dice);
            fail();
        } catch (BadDiceReferenceException e) {}
    }

    /**
     * Tests the retrieval of the dices from the {@link TrackSlot}
     */
    @Test
    public void testGetDices() {
        TrackSlot slot = new TrackSlot(dices);

        assertEquals(dices, slot.getDices());
    }
}