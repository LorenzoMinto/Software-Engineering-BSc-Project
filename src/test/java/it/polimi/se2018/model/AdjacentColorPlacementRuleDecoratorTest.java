package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static it.polimi.se2018.model.DiceColor.*;
import static org.junit.Assert.*;

/**
 * Test for {@link AdjacentColorPlacementRuleDecorator} class
 *
 * @author Lorenzo Minto
 */
public class AdjacentColorPlacementRuleDecoratorTest {

    private static Cell[][] pattern;

    private static PlacementRule rule;

    /**
     * Used to test the composite behavior. This rule is arbitrary among all the rules
     */
    private static PlacementRule decoratedRule;
    private static Player player;

    private WindowPattern windowPattern;

    private static Dice redDice;
    private static Dice blueDice;

    /**
     * Initializes the variables needed in the tests
     */
    @BeforeClass
    public static void initializeVariables(){
        PlacementRule emptyRule = new EmptyPlacementRule();
        rule = new AdjacentColorPlacementRuleDecorator(emptyRule);
        decoratedRule = new AdjacentColorPlacementRuleDecorator(new ColorPlacementRuleDecorator(emptyRule));

        pattern = new Cell[3][3];
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                pattern[i][j] = new Cell();
            }
        }
        pattern[1][0] = new Cell(0, RED);

        redDice = new Dice(RED);
        blueDice = new Dice(BLUE);

        player = new Player("Sonny", PrivateObjectiveCard.createTestInstance());
    }

    /**
     * Initializes the player's windowPattern for the tests before each test
     */
    @Before
    public void initializeWindowPattern(){

        windowPattern = new WindowPattern("","", "",0, pattern);
        player.setWindowPattern(windowPattern);

        windowPattern.putDiceOnCell(redDice, 1,1);
    }

    /**
     * Tests that it is allowed to move a dice next to a different color one
     */
    @Test
    public void testCheckIfMoveIsAllowed() {
        assertTrue(decoratedRule.isMoveAllowed(windowPattern, blueDice, 1, 2));
    }

    /**
     * Tests that it is not allowed to move a dice on a cell with a dice on it
     */
    @Test
    public void testCheckIfMoveIsAllowedWhenMoveIsOnCellWithAlreadyPlacedDice() {
        assertFalse(rule.isMoveAllowed(windowPattern, redDice, 1,1));
    }

    /**
     * Tests that the decorated rule checks that it is not allowed to place a dice on a certain
     * cell because of color constraints
     */
    @Test
    public void testCheckIfMoveIsAllowedIfDecoratedNotAllowed() {
        assertFalse(decoratedRule.isMoveAllowed(windowPattern, blueDice, 1, 0));
    }

    /**
     * Tests that it is not allowed to place a dice just below a dice of the same color
     * and that it is allowed to place a dice just below a dice of a different color
     */
    @Test
    public void testCheckAdjacentColorConstraintsBelow() {
        assertFalse(rule.isMoveAllowed(windowPattern, redDice, 0,1));
        assertTrue(rule.isMoveAllowed(windowPattern, blueDice, 0,1));
    }

    /**
     * Tests that it is not allowed to place a dice just above a dice of the same color
     * and that it is allowed to place a dice just above a dice of a different color
     */
    @Test
    public void testCheckAdjacentColorConstraintsAbove() {
        assertFalse(rule.isMoveAllowed(windowPattern, redDice, 2,1));
        assertTrue(rule.isMoveAllowed(windowPattern, blueDice, 2,1));
    }

    /**
     * Tests that it is not allowed to place a dice just on the left of a dice of the same color
     * and that it is allowed to place a dice just on the left of a dice of a different color
     */
    @Test
    public void testCheckAdjacentColorConstraintsLeft() {
        assertFalse(rule.isMoveAllowed(windowPattern, redDice, 1,2));
        assertTrue(rule.isMoveAllowed(windowPattern, blueDice, 1,2));
    }

    /**
     * Tests that it is not allowed to place a dice just on the right of a dice of the same color
     * and that it is allowed to place a dice just on the right of a dice of a different color
     */
    @Test
    public void testCheckAdjacentColorConstraintsRight() {
        assertFalse(rule.isMoveAllowed(windowPattern, redDice, 1,0));
        assertTrue(rule.isMoveAllowed(windowPattern, blueDice, 1,0));
    }
}