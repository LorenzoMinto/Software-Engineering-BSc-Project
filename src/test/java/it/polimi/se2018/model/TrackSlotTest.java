package it.polimi.se2018.model;

import org.junit.After;
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
    public void addDice() {
        Dice dice = new Dice(BLUE, 2);
        TrackSlot slot = new TrackSlot(new ArrayList<>());

        slot.addDice(dice);

        assertTrue(slot.getDices().contains(dice));
    }

    @Test
    public void removeDice() {
        TrackSlot slot = new TrackSlot(dices);

        slot.removeDice(dice1);

        assertFalse(slot.getDices().contains(dice1));
    }

    @Test
    public void getDices() {
        TrackSlot slot = new TrackSlot(this.dices);

        assertEquals(this.dices, slot.getDices());
    }
}