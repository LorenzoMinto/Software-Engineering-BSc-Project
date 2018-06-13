package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.Test;

import static it.polimi.se2018.model.DiceColor.*;
import static org.junit.Assert.*;


/**
 * Test for {@link Cell} class
 *
 * @author Federico Haag
 * @author Jacopo Pio Gargano
 */
public class CellTest {

    private Cell cell;
    private Dice dice = new Dice(GREEN,4);

    /**
     * Initializes the cell for the tests
     */
    @Before
    public void initializeCell(){
        cell = new Cell(3, NOCOLOR);
    }

    /**
     * Tests the retrieval of the allowed value
     * @see Cell#getAllowedValue()
     */
    @Test
    public void testGetAllowedValue() {
        assertEquals(3, cell.getAllowedValue());
    }

    /**
     * Tests the retrieval of the allowed color
     * @see Cell#getAllowedColor()
     */
    @Test
    public void testGetAllowedColor() {
        assertEquals(NOCOLOR, cell.getAllowedColor());
    }

    /**
     * Tests setting a dice on a cell
     * @see Cell#hasDice()
     */
    @Test
    public void testSetDice(){
        cell.setDice(dice);

        assertTrue(cell.hasDice());
    }

    /**
     * Tests setting a null dice on a cell
     * @see Cell#setDice(Dice)
     */
    @Test
    public void testSetNullDice(){
        try{
            cell.setDice(null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests the removal of a dice from a cell with a dice
     * @see Cell#removeDice()
     */
    @Test
    public void testRemoveDice() {
        cell.setDice(dice);
        Dice removedDice = cell.removeDice();
        assertEquals(dice, removedDice);
    }

    /**
     * Tests the impossibility of removing a dice from a cell with no dice
     * @see Cell#removeDice()
     */
    @Test
    public void testRemoveDiceIfCellHasNoDice(){
        assertFalse(cell.hasDice());
        Dice removedDice = cell.removeDice();
        assertNull(removedDice);
    }

    /**
     * Tests the toString method of {@link Cell}
     * @see Cell#toString()
     */
    @Test
    public void testToString() {
        cell.setDice(dice);
        assertEquals("( G4 )",cell.toString());
        cell.removeDice();
        assertEquals("(_::3)",cell.toString());
    }

    /**
     * Tests the getCellConstraintsToString method of {@link Cell}, used for retrieving the image of the constraint
     * @see Cell#getCellConstraintsToString()
     */
    @Test
    public void testGetCellConstraintsToString(){
        cell = new Cell(3, NOCOLOR);
        assertEquals("3", cell.getCellConstraintsToString());

        cell = new Cell(0, NOCOLOR);
        assertEquals("X", cell.getCellConstraintsToString());

        cell = new Cell(0, RED);
        assertEquals("R", cell.getCellConstraintsToString());
    }
}
