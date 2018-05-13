package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestCell {

    private Cell cell;
    private static Dice dice;

    @BeforeClass
    public static void initializeVariables(){
        dice = new Dice(DiceColors.GREEN,4);
    }

    @Before
    public void initializeCell(){
        cell = new Cell(3,DiceColors.PURPLE);
    }

    @Test
    public void testGetAllowedValue() {
        int allowedValue = 3;

        cell = new Cell(allowedValue, DiceColors.PURPLE);

        assertEquals(allowedValue, cell.getAllowedValue());
    }

    @Test
    public void testGetAllowedColor() {
        DiceColors allowedColor = DiceColors.PURPLE;

        cell = new Cell(3, allowedColor);

        assertEquals(allowedColor, cell.getAllowedColor());
    }

    @Test
    public void testSetDice(){
        cell.setDice(dice);

        assertTrue(cell.hasDice());
    }

    @Test
    public void testSetNullDice(){
        try{
            cell.setDice(null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    @Test
    public void testRemoveDice() {
        cell.setDice(dice);
        Dice removedDice = cell.removeDice();
        assertEquals(dice, removedDice);
    }

    @Test
    public void testRemoveDiceIfCellHasNoDice(){
        assertFalse(cell.hasDice());
        Dice removedDice = cell.removeDice();
        assertNull(removedDice);
    }

    @Test
    public void testToString() {
        cell.setDice(dice);
        assertEquals("(4:G)",cell.toString());
        cell.removeDice();
        assertEquals("(3:P)",cell.toString());
    }
}