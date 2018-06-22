package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static it.polimi.se2018.model.DiceColor.RED;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link EmptyPlacementRule} class
 * @see EmptyPlacementRule#isMoveAllowed(WindowPattern, Dice, int, int)
 * @author Lorenzo Minto
 */
public class EmptyPlacementRuleTest {

    private static Cell[][] pattern;

    private static PlacementRule rule;

    private WindowPattern windowPattern;

    private static Dice redDice;
    private static Dice blueDice;

    private static Player player;

    /**
     * Initializes the variables needed in the tests
     */
    @BeforeClass
    public static void initializeVariables(){
        rule = new EmptyPlacementRule();

        pattern = new Cell[3][3];
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                pattern[i][j] = new Cell();
            }
        }
        pattern[1][1] = new Cell(0, DiceColor.RED);

        redDice = new Dice(DiceColor.RED);
        blueDice = new Dice(DiceColor.BLUE);

        player = new Player("Sonny", new PrivateObjectiveCard("","","",RED));
    }

    /**
     * Initializes the player's windowPattern for the tests before each test
     */
    @Before
    public void initializeWindowPattern(){
        windowPattern = new WindowPattern("","", "",0, pattern);
        player.setWindowPattern(windowPattern);
    }

    /**
     * Tests that it is allowed to place a dice on a cell with no dice
     * @see EmptyPlacementRule#isMoveAllowed(WindowPattern, Dice, int, int)
     */
    @Test
    public void testCheckIfMoveIsAllowed() {
        assertNull(windowPattern.getDiceOnCell(1,1));
        assertTrue(rule.isMoveAllowed(windowPattern, redDice, 1, 1));
    }

    /**
     * Tests the impossibility of moving a dice on a cell that already has a dice
     * @see EmptyPlacementRule#isMoveAllowed(WindowPattern, Dice, int, int)
     */
    @Test
    public void testCheckIfMoveIsAllowedWhenThereIsAlreadyADiceOnCell() {
        windowPattern.putDiceOnCell(redDice, 1,1);
        assertFalse(rule.isMoveAllowed(windowPattern, blueDice, 1, 1));
    }
}