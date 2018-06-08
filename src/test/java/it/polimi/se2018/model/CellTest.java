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
        cell = new Cell(3, PURPLE);
    }

    /**
     * Tests the retrieval of the allowed value
     */
    @Test
    public void testGetAllowedValue() {
        assertEquals(3, cell.getAllowedValue());
    }

    /**
     * Tests the retrieval of the allowed color
     */
    @Test
    public void testGetAllowedColor() {
        assertEquals(PURPLE, cell.getAllowedColor());
    }

    /**
     * Tests setting a dice on a cell
     */
    @Test
    public void testSetDice(){
        cell.setDice(dice);

        assertTrue(cell.hasDice());
    }

    /**
     * Tests setting a null dice on a cell
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
     */
    @Test
    public void testRemoveDice() {
        cell.setDice(dice);
        Dice removedDice = cell.removeDice();
        assertEquals(dice, removedDice);
    }

    /**
     * Tests the impossibility of removing a dice from a cell with no dice
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
        assertEquals("(P::4)",cell.toString());
    }
}
