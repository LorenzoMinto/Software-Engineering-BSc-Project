package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Year;

import static it.polimi.se2018.model.DiceColors.*;
import static org.junit.Assert.*;

/**
 * @author Jacopo Pio Gargano
 */
public class TestWindowPattern {

    private WindowPattern windowPattern;

    private static final int rows = 4;
    private static final int cols = 5;

    private static Cell[][] pattern;
    private static Dice dice;


    @BeforeClass
    public static void initializeVariables(){
        pattern = new Cell[rows][cols];
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                pattern[i][j] = new Cell();
            }
        }

        dice = new Dice(RED);
    }

    @Before
    public void initializeWindowPattern(){
        windowPattern = new WindowPattern("title", 1, pattern);
    }

    @Test
    public void testConstructor(){
        windowPattern = new WindowPattern("title", 1, pattern);
        assertNotNull(windowPattern);
    }

    @Test
    public void testConstructorNullPattern(){
        try {
            windowPattern = new WindowPattern("title", 1, null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    @Test
    public void testGetTitle(){
        windowPattern = new WindowPattern("title", 1, pattern);
        assertEquals("title", windowPattern.getTitle());
    }

    @Test
    public void testGetDiceOnIllegalCell(){
        assertNull(windowPattern.getDiceOnCell( -1, 3));
    }

    @Test
    public void testPutDiceOnCell(){
        windowPattern.putDiceOnCell(dice, 2, 4);
        assertEquals(dice, windowPattern.getDiceOnCell(2,4));
    }

    @Test
    public void testPutNullDiceOnCell(){
        try {
            windowPattern.putDiceOnCell(null, 0,0);
            fail();
        }catch (IllegalArgumentException e ){}
    }

    @Test
    public void testPutDiceOnCellIllegalPosition(){
        assertFalse(windowPattern.putDiceOnCell(dice, rows, cols));
    }

    @Test
    public void testPutDiceOnCellWithDice(){
        windowPattern.putDiceOnCell(dice, 0,0);
        assertFalse(windowPattern.putDiceOnCell(new Dice(YELLOW), 0,0));
    }

    @Test
    public void testMoveDiceFromCellToCell(){
        windowPattern.putDiceOnCell(dice, 1, 1);
        assertTrue(windowPattern.moveDiceFromCellToCell(1,1,2,2));
        assertEquals(dice, windowPattern.getDiceOnCell(2,2));
        assertNull(windowPattern.getDiceOnCell(1,1));

    }

    @Test
    public void testMoveDiceFromCellWithoutDiceToCell(){
        assertFalse(windowPattern.moveDiceFromCellToCell(1,1, 1,2));
    }

    @Test
    public void testMoveDiceFromCellToCellWithDice(){
        windowPattern.putDiceOnCell(dice, 1, 1);
        windowPattern.putDiceOnCell(new Dice(YELLOW), 1, 2);
        assertFalse(windowPattern.moveDiceFromCellToCell(1,1, 1,2));
    }

    @Test
    public void testCopy(){
        assertNotNull(windowPattern.copy());
    }

    @Test
    public void testIsLegalPosition(){
        assertNull(windowPattern.getDiceOnCell( -1, 3));
        assertFalse(windowPattern.moveDiceFromCellToCell(1,1, 2,6));
        assertFalse(windowPattern.moveDiceFromCellToCell(-1,-1, 2,6));
        assertFalse(windowPattern.putDiceOnCell(dice, rows,2));
        assertFalse(windowPattern.putDiceOnCell(dice, 2,cols));
        assertFalse(windowPattern.putDiceOnCell(dice, rows,cols));
        assertTrue(windowPattern.putDiceOnCell(dice, 1, 4));
    }

    @Test
    public void testIsEmpty(){
        assertTrue(windowPattern.isEmpty());
    }


    @Test
    public void testToString(){
        Cell[][] smallPattern = new Cell[2][2];
        for(int i=0; i<2; i++){
            for(int j=0; j<2; j++){
                smallPattern[i][j] = new Cell();
            }
        }

        windowPattern = new WindowPattern("title", 5, smallPattern);

        String windowPatternToString = windowPattern.toString();
        String br = System.lineSeparator();
        String expectedString = "{title}"+br+"Difficulty: 5"+br+"(0:_)(0:_)"+br+"(0:_)(0:_)"+br;

        assertEquals(expectedString, windowPatternToString);

    }














}