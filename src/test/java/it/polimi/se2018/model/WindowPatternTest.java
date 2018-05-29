package it.polimi.se2018.model;

import it.polimi.se2018.utils.ValueOutOfBoundsException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.bind.ValidationException;
import java.time.Year;

import static it.polimi.se2018.model.DiceColors.*;
import static org.junit.Assert.*;

/**
 * Test for {@link WindowPattern} class
 *
 * @author Jacopo Pio Gargano
 */
public class WindowPatternTest {

    private WindowPattern windowPattern;

    private static final int rows = 4;
    private static final int cols = 5;

    private static Cell[][] pattern;
    private static Dice dice;


    /**
     * Initializes pattern and dice
     */
    @BeforeClass
    public static void initializePatternAndDice(){
        pattern = new Cell[rows][cols];
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                pattern[i][j] = new Cell();
            }
        }

        dice = new Dice(RED);
    }

    /**
     * Initializes window pattern before each test in order to have an empty window pattern in each one of them
     */
    @Before
    public void initializeWindowPattern(){
        windowPattern = new WindowPattern("id","title", 1, pattern);
    }


    /**
     * Tests the constructor with allowed parameters
     */
    @Test
    public void testConstructor(){
        windowPattern = new WindowPattern("id","title", 1, pattern);
        assertNotNull(windowPattern);
    }

    /**
     * Tests the impossibility of creating a window pattern with a null pattern
     */
    @Test
    public void testConstructorNullPattern(){
        try {
            windowPattern = new WindowPattern("id","title", 1, null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests the retrieval of the title of a window pattern
     */
    @Test
    public void testGetTitle(){
        windowPattern = new WindowPattern("id","title", 1, pattern);
        assertEquals("title", windowPattern.getTitle());
    }

    /**
     * Tests the impossibility of retrieving a dice from an illegal position
     */
    @Test
    public void testGetDiceOnIllegalCell(){
        try {
            windowPattern.getDiceOnCell( -1, 3);
            fail();
        }catch (ValueOutOfBoundsException e){}

        try {
            windowPattern.getDiceOnCell( 1, -1);
            fail();
        }catch (ValueOutOfBoundsException e){}
    }

    /**
     * Tests putting a dice on a cell of the window pattern
     */
    @Test
    public void testPutDiceOnCell(){
        windowPattern.putDiceOnCell(dice, 2, 4);
        assertEquals(dice, windowPattern.getDiceOnCell(2,4));
    }

    /**
     * Tests the impossibility of putting a null dice on a cell
     */
    @Test
    public void testPutNullDiceOnCell(){
        try {
            windowPattern.putDiceOnCell(null, 1,2);
            fail();
        }catch (IllegalArgumentException e ){}
    }

    /**
     * Tests the impossibility of putting a dice on an illegal cell
     */
    @Test
    public void testPutDiceOnIllegalCell(){
        assertFalse(windowPattern.putDiceOnCell(dice, rows, cols));
    }

    /**
     * Tests the impossibility of putting a dice on a cell that already has a dice
     */
    @Test
    public void testPutDiceOnCellWithDice(){
        windowPattern.putDiceOnCell(dice, 0,0);
        assertFalse(windowPattern.putDiceOnCell(new Dice(YELLOW), 0,0));
    }

    /**
     * Tests moving a dice from a cell to another cell
     */
    @Test
    public void testMoveDiceFromCellToCell(){
        windowPattern.putDiceOnCell(dice, 1, 1);
        assertTrue(windowPattern.moveDiceFromCellToCell(1,1,2,2));
        assertEquals(dice, windowPattern.getDiceOnCell(2,2));
        assertNull(windowPattern.getDiceOnCell(1,1));

    }

    /**
     * Tests the impossibility of moving a dice from a cell without a dice to another cell
     */
    @Test
    public void testMoveDiceFromCellWithoutDiceToCell(){
        assertFalse(windowPattern.moveDiceFromCellToCell(1,1, 1,2));
    }

    /**
     * Tests the impossibility of moving a dice from a cell to another cell without a dice
     */
    @Test
    public void testMoveDiceFromCellToCellWithDice(){
        windowPattern.putDiceOnCell(dice, 1, 1);
        windowPattern.putDiceOnCell(new Dice(YELLOW), 1, 2);
        assertFalse(windowPattern.moveDiceFromCellToCell(1,1, 1,2));
    }

    /**
     * Tests the legality of a position (cell) implicitly testing {@link WindowPattern#isLegalPosition(int, int)}
     */
    @Test
    public void testIsLegalPosition(){
        try{
            windowPattern.getDiceOnCell( -1, 3);
            fail();
        }catch (ValueOutOfBoundsException e){}

        try{
            windowPattern.moveDiceFromCellToCell(1,1, 2,6);
            fail();
        }catch (ValueOutOfBoundsException e){}

        try{
            windowPattern.moveDiceFromCellToCell(-1,-1, 2,6);
            fail();
        }catch (ValueOutOfBoundsException e){}

        try {
            windowPattern.putDiceOnCell(dice, rows,2);
            fail();
        }catch (ValueOutOfBoundsException e){}

        try{
            windowPattern.putDiceOnCell(dice, 2,cols);
            fail();
        }catch (ValueOutOfBoundsException e){}

        try{
            windowPattern.putDiceOnCell(dice, rows,cols);
            fail();
        }catch (ValueOutOfBoundsException e){}

        assertTrue(windowPattern.putDiceOnCell(dice, 1, 4));
    }

    /**
     * Tests that an empty window pattern is actually empty
     */
    @Test
    public void testIsEmpty(){
        assertTrue(windowPattern.isEmpty());
    }

    /**
     * Tests the toString method of {@link WindowPattern}
     */
    @Test
    public void testToString(){
        Cell[][] smallPattern = new Cell[2][2];
        for(int i=0; i<2; i++){
            for(int j=0; j<2; j++){
                smallPattern[i][j] = new Cell();
            }
        }

        windowPattern = new WindowPattern("id","title", 5, smallPattern);

        String windowPatternToString = windowPattern.toString();
        String br = System.lineSeparator();
        String expectedString = "{title}"+br+"Difficulty: 5"+br+"(0:_)(0:_)"+br+"(0:_)(0:_)"+br;

        assertEquals(expectedString, windowPatternToString);

    }

    /**
     * Tests the copy method of {@link WindowPattern} (copy must not be null)
     */
    @Test
    public void testCopy(){
        assertNotNull(windowPattern.copy());
    }














}