package it.polimi.se2018.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestCell {

    @Test
    public void testGetAllowedValue() {
        Cell cell = new Cell(3,DiceColors.PURPLE);
        assertEquals(3,cell.getAllowedValue());
    }

    @Test
    public void testGetAllowedColor() {
        Cell cell = new Cell(3,DiceColors.PURPLE);
        assertEquals(DiceColors.PURPLE,cell.getAllowedColor());
    }

    @Test
    public void testRemoveDice() {
        Cell cell = new Cell(3,DiceColors.PURPLE);
        Dice dice = new Dice(DiceColors.GREEN,4);
        cell.setDice(dice);
        Dice diceRemoved = cell.removeDice();
        assertTrue(diceRemoved.equals(dice));
    }

    @Test
    public void testToString() {
        Cell cell = new Cell(3,DiceColors.PURPLE);
        Dice dice = new Dice(DiceColors.GREEN,4);
        cell.setDice(dice);
        assertEquals("(4:G)",cell.toString());
        cell.removeDice();
        assertEquals("(3:P)",cell.toString());
    }
}