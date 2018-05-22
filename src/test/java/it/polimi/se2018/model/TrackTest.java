package it.polimi.se2018.model;

import it.polimi.se2018.utils.ValueOutOfBoundsException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.se2018.model.DiceColors.*;
import static org.junit.Assert.*;

public class TrackTest {

    Track track;
    List<Dice> dices;
    Dice dice1;

    @Before
    public void setUp() throws Exception {
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

    @Test
    public void testProcessDices() {
        assertEquals(dices, track.getDicesFromSlotNumber(0));
    }

    @Test
    public void testGetDicesFromSlotNumber() {
        assertEquals(dices, track.getDicesFromSlotNumber(0));
    }

    @Test
    public void testGetDicesFromNonExistingSlotNumber() {

        try {
            track.getDicesFromSlotNumber(1);
            fail();
        } catch (ValueOutOfBoundsException e) {}
    }

    @Test
    public void testTakeDice() {
        track.takeDice(dice1, 0);
        assertFalse(track.getDicesFromSlotNumber(0).contains(dice1));
    }

    @Test
    public void testTakeDiceNotInTrackSlot() {
        track.takeDice(dice1, 0);
        try {
            track.takeDice(dice1, 0);
            fail();
            //TODO: invalidDiceException
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void testTakeDiceFromNonExistingTrackSlot() {
        try {
            track.takeDice(dice1, 2);
            fail();
        } catch (ValueOutOfBoundsException e) {}
    }

    @Test
    public void testPutDice() {
        Dice dice6 = new Dice(DiceColors.RED, 6);
        track.putDice(dice6, 0);
        assertTrue(track.getDicesFromSlotNumber(0).contains(dice6));
    }

    @Test
    public void testPutDiceInNonExistingTrackSlot() {
        try {
            track.putDice(dice1, 1);
        } catch (ValueOutOfBoundsException e) {}
    }
}