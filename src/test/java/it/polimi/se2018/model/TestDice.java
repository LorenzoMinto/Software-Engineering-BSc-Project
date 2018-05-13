package it.polimi.se2018.model;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestDice {

    private static Dice dice;

    @BeforeClass
    public static void testInit(){
        dice = new Dice(DiceColors.GREEN,2);
    }

    @Test
    public void testSetValue() {
        dice.setValue(5);
        assertEquals(5,dice.getValue());
    }

    @Test
    public void testRollOver() {
        dice.setValue(2);
        dice.rollOver();
        assertEquals(5,dice.getValue());
    }

    @Test
    public void testIncrementValue() {
        dice.setValue(5);
        dice.incrementValue();
        assertEquals(6,dice.getValue());
        assertFalse(dice.incrementValue());
    }

    @Test
    public void testDecrementValue() {
        dice.setValue(2);
        dice.decrementValue();
        assertEquals(1,dice.getValue());
        assertFalse(dice.decrementValue());
    }

    @Test
    public void testToString() {
        dice.setValue(2);
        assertEquals("2:G",dice.toString());
    }

    @Test
    public void testEquals() {
        assertTrue(dice.equals(dice));
        assertTrue(dice.equals(dice.copy()));
        assertFalse(dice.equals(new Cell()));
    }

    @Test
    public void testHashCode() {
    }
}