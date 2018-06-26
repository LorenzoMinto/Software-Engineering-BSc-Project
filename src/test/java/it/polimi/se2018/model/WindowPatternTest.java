package it.polimi.se2018.model;

import it.polimi.se2018.utils.ValueOutOfBoundsException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import static it.polimi.se2018.model.DiceColor.*;
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
    private static Player player;


    /**
     * Initializes pattern and dice
     */
    @BeforeClass
    public static void initializePatternAndDice(){

        player = new Player("", new PrivateObjectiveCard("","","",RED));


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
        windowPattern = new WindowPattern("id","title", "",1, pattern);
        player.setWindowPattern(windowPattern);
        windowPattern.setOwner(player);
    }


    /**
     * Tests the constructor with allowed parameters
     * @see WindowPattern#WindowPattern(String, String, String, int, Cell[][])
     */
    @Test
    public void testConstructor(){
        windowPattern = new WindowPattern("id","title", "",1, pattern);
        assertNotNull(windowPattern);
    }

    /**
     * Tests the impossibility of creating a window pattern with a null pattern
     * @see WindowPattern#WindowPattern(String, String, String, int, Cell[][])
     */
    @Test
    public void testConstructorNullPattern(){
        try {
            windowPattern = new WindowPattern("id","title", "",1, null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests the retrieval of the title of a window pattern
     * @see WindowPattern#getTitle()
     */
    @Test
    public void testGetTitle(){
        windowPattern = new WindowPattern("id","title", "",1, pattern);
        assertEquals("title", windowPattern.getTitle());
    }

    /**
     * Tests the retrieval of the id of a window pattern
     * @see WindowPattern#getID()
     */
    @Test
    public void testGetID(){
        windowPattern = new WindowPattern("id","title", "",1, pattern);
        assertEquals("id", windowPattern.getID());
    }

    /**
     * Tests the retrieval of the id of a window pattern
     * @see WindowPattern#getID()
     */
    @Test
    public void testGetImageURL(){
        windowPattern = new WindowPattern("id","title", "src/testPath.jpg",1, pattern);
        assertEquals("src/testPath.jpg", windowPattern.getImageURL());
    }

    /**
     * Tests the retrieval of the id of a window pattern
     * @see WindowPattern#getID()
     */
    @Test
    public void testGetDifficulty(){
        windowPattern = new WindowPattern("id","title", "",1, pattern);
        assertEquals(1, windowPattern.getDifficulty());
    }

    /**
     * Tests the impossibility of retrieving a dice from an illegal position
     * @see WindowPattern#getDiceOnCell(int, int)
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
     * @see WindowPattern#putDiceOnCell(Dice, int, int)
     */
    @Test
    public void testPutDiceOnCell(){
        windowPattern.putDiceOnCell(dice, 2, 4);
        assertEquals(dice, windowPattern.getDiceOnCell(2,4));
    }

    /**
     * Tests the impossibility of putting a null dice on a cell
     * @see WindowPattern#putDiceOnCell(Dice, int, int)
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
     * @see WindowPattern#putDiceOnCell(Dice, int, int)
     */
    @Test
    public void testPutDiceOnIllegalCell(){
        try{
            windowPattern.putDiceOnCell(dice, rows, cols);
        }catch (ValueOutOfBoundsException e){}
    }

    /**
     * Tests the impossibility of putting a dice on a cell that already has a dice
     * @see WindowPattern#putDiceOnCell(Dice, int, int)
     */
    @Test
    public void testPutDiceOnCellWithDice(){
        windowPattern.putDiceOnCell(dice, 0,0);
        assertFalse(windowPattern.putDiceOnCell(new Dice(YELLOW), 0,0));
    }

    /**
     * Tests moving a dice from a cell to another cell
     * @see WindowPattern#moveDiceFromCellToCell(int, int, int, int)
     */
    @Test
    public void testMoveDiceFromCellToCell(){
        windowPattern.putDiceOnCell(dice, 1, 1);
        assertTrue(windowPattern.moveDiceFromCellToCell(1,1,2,2));
        assertEquals(dice, windowPattern.getDiceOnCell(2,2));
        assertNull(windowPattern.getDiceOnCell(1,1));

    }

    /**
     * Tests the impossibility of moving a dice from a cell without a dice to a cell without a dice
     * @see WindowPattern#moveDiceFromCellToCell(int, int, int, int)
     */
    @Test
    public void testMoveDiceFromCellWithoutDiceToCellWithOutDice(){
        assertFalse(windowPattern.moveDiceFromCellToCell(1,1, 1,2));
    }

    /**
     * Tests the impossibility of moving a dice from a cell without a dice to a cell with a dice
     * @see WindowPattern#moveDiceFromCellToCell(int, int, int, int)
     */
    @Test
    public void testMoveDiceFromCellWithoutDiceToCellWithDice(){
        windowPattern.putDiceOnCell(dice,1,2);
        assertFalse(windowPattern.moveDiceFromCellToCell(1,1, 1,2));
    }

    /**
     * Tests the impossibility of moving a dice from a cell to another cell without a dice
     * @see WindowPattern#moveDiceFromCellToCell(int, int, int, int)
     */
    @Test
    public void testMoveDiceFromCellToCellWithDice(){
        windowPattern.putDiceOnCell(dice, 1, 1);
        windowPattern.putDiceOnCell(new Dice(YELLOW), 1, 2);
        assertFalse(windowPattern.moveDiceFromCellToCell(1,1, 1,2));
    }

    /**
     * Tests the legality of a position (cell) implicitly testing {@link WindowPattern#isIllegalPosition(int, int)}
     * @see WindowPattern#isIllegalPosition(int, int)
     */
    @Test
    public void testIsPositionIllegal(){
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
            windowPattern.putDiceOnCell(dice, rows, cols);
            fail();
        }catch (ValueOutOfBoundsException e){}

        assertTrue(windowPattern.putDiceOnCell(dice, 1, 4));
    }

    /**
     * Tests that an empty window pattern is actually empty
     * @see WindowPattern#isEmpty()
     */
    @Test
    public void testIsEmpty(){
        assertTrue(windowPattern.isEmpty());
    }

    /**
     * Tests the toString method of {@link WindowPattern}
     * @see WindowPattern#toString() ()
     */
    @Test
    public void testToString(){
        Cell[][] smallPattern = new Cell[2][2];
        for(int i=0; i<2; i++){
            for(int j=0; j<2; j++){
                smallPattern[i][j] = new Cell();
            }
        }

        windowPattern = new WindowPattern("id","title", "",5, smallPattern);

        String windowPatternToString = windowPattern.toString();
        String br = System.lineSeparator();
        String expectedString = "{title}"+br+"Difficulty: 5"+br+"(_::0)(_::0)"+br+"(_::0)(_::0)"+br;

        assertEquals(expectedString, windowPatternToString);

    }

    /**
     * Tests the copy method of {@link WindowPattern} (copy must not be null)
     * @see WindowPattern#copy()
     */
    @Test
    public void testCopy(){
        assertNotNull(windowPattern.copy());
    }














}