package it.polimi.se2018.model;

import it.polimi.se2018.utils.BadDiceReferenceException;
import it.polimi.se2018.utils.ValueOutOfBoundsException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.se2018.model.DiceColor.*;
import static org.junit.Assert.*;

/**
 * Test for {@link Track} class
 *
 * @author Lorenzo Minto
 * @author Jacopo Pio Gargano
 */
public class TrackTest {

    private Track track;
    private List<Dice> dices;
    private Dice dice1;

    /**
     * Initializes variables for the tests before each test
     */
    @Before
    public void setUp(){
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

        track = new Track();
        track.processDices(dices);
    }

    /**
     * Tests processing dices to the {@link Track}. The dices were processed in {@link TrackTest#setUp()}
     * @see Track#processDices(List)
     */
    @Test
    public void testProcessDices() {
        assertEquals(dices, track.getDicesFromSlotNumber(0));
    }

    /**
     * Tests the impossibility of processing null dices
     * @see Track#processDices(List)
     */
    @Test
    public void testProcessNullDices() {
        try{
            track.processDices(null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests the retrieval of dices from a certain {@link TrackSlot} number
     * @see Track#getDicesFromSlotNumber(int)
     */
    @Test
    public void testGetDicesFromSlotNumber() {
        assertEquals(dices, track.getDicesFromSlotNumber(0));
    }

    /**
     * Tests the impossibility of retrieving dices from a non existing {@link TrackSlot} number
     * @see Track#getDicesFromSlotNumber(int)
     */
    @Test
    public void testGetDicesFromOutOfBoundsSlotNumber() {
        try {
            track.getDicesFromSlotNumber(1);
            fail();
        }catch (ValueOutOfBoundsException e){}

        try {
            track.getDicesFromSlotNumber(-1);
            fail();
        }catch (ValueOutOfBoundsException e){}
    }

    /**
     * Tests taking a dice from a {@link TrackSlot}, which implies removing the {@link Dice} from the {@link TrackSlot}
     */
    @Test
    public void testTakeDice()  {
        try {
            track.takeDice(dice1, 0);
        } catch (BadDiceReferenceException e) {
            e.printStackTrace();
            fail();
        }
        assertFalse(track.getDicesFromSlotNumber(0).contains(dice1));
    }

    /**
     * Tests the impossibility of taking a {@link Dice} from a {@link TrackSlot} that does not contain it
     */
    @Test
    public void testTakeDiceNotInTrackSlot(){
        try {
            track.takeDice(dice1, 0);
        } catch (BadDiceReferenceException e) {
            e.printStackTrace();
            fail();
        }
        try {
            track.takeDice(dice1, 0);
            fail();
        } catch (BadDiceReferenceException e) {}
    }

    /**
     * Tests the impossibility of taking a {@link Dice} from a non existing {@link TrackSlot}
     */
    @Test
    public void testTakeDiceFromNonExistingTrackSlot() {
        try {
            track.takeDice(dice1, 2);
            fail();
        }catch (BadDiceReferenceException e){
            e.printStackTrace();
            fail();
        }catch (ValueOutOfBoundsException e) {}
    }

    /**
     * Tests putting a {@link Dice} in a {@link TrackSlot}
     */
    @Test
    public void testPutDice() {
        Dice dice6 = new Dice(DiceColor.RED, 6);
        track.putDice(dice6, 0);
        assertTrue(track.getDicesFromSlotNumber(0).contains(dice6));
    }

    /**
     * Tests putting a null {@link Dice} in a {@link TrackSlot}
     */
    @Test
    public void testPutNullDice() {
        try{
            track.putDice(null, 0);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests the impossibility of putting a {@link Dice} in a non existing {@link TrackSlot}
     */
    @Test
    public void testPutDiceInNonExistingTrackSlot() {
        try {
            track.putDice(dice1, 1);
        } catch (ValueOutOfBoundsException e) {}
    }
}