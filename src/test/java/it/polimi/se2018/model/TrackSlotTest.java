package it.polimi.se2018.model;

import it.polimi.se2018.utils.BadDiceReferenceException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.se2018.model.DiceColors.*;
import static org.junit.Assert.*;

public class TrackSlotTest {

    private List<Dice> dices;
    private Dice dice1;


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
    }

    @Test
    public void testAddDice() {
        Dice dice = new Dice(DiceColors.BLUE, 2);
        TrackSlot slot = new TrackSlot(new ArrayList<>());

        slot.addDice(dice);

        assertTrue(slot.getDices().contains(dice));
    }

    @Test
    public void testRemoveDice() {
        TrackSlot slot = new TrackSlot(dices);

        try {
            slot.removeDice(dice1);
        } catch (BadDiceReferenceException e) {
            fail();
        }

        assertFalse(slot.getDices().contains(dice1));
    }

    @Test
    public void testRemoveDiceNotInTrackSlot() {
        TrackSlot slot = new TrackSlot(dices);
        Dice dice = new Dice(DiceColors.BLUE, 2);

        try {
            slot.removeDice(dice);
            fail();
        } catch (BadDiceReferenceException e) {}
    }

    @Test
    public void testGetDices() {
        TrackSlot slot = new TrackSlot(this.dices);

        assertEquals(this.dices, slot.getDices());
    }
}