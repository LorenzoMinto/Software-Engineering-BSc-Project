package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static it.polimi.se2018.model.DiceColor.RED;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link AdjacentDicePlacementRuleDecorator} class
 * @see AdjacentDicePlacementRuleDecorator#isMoveAllowed(WindowPattern, Dice, int, int)
 * @author Lorenzo Minto
 */
public class AdjacentValuePlacementRuleDecoratorTest {

    private static Cell[][] pattern;

    private static PlacementRule rule;
    /**
     * Used to test the composite behavior. This rule is arbitrary among all the rules
     */
    private static PlacementRule decoratedRule;
    private static Player player;

    private WindowPattern windowPattern;

    private static Dice threeDice;
    private static Dice fourDice;

    /**
     * Initializes the player's windowPattern for the tests before each test
     */
    @BeforeClass
    public static void initializeVariables(){
        PlacementRule emptyRule = new EmptyPlacementRule();
        rule = new AdjacentValuePlacementRuleDecorator(emptyRule);
        decoratedRule = new AdjacentValuePlacementRuleDecorator(new ValuePlacementRuleDecorator(emptyRule));

        pattern = new Cell[3][3];
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                pattern[i][j] = new Cell();
            }
        }
        pattern[1][0] = new Cell(3, DiceColor.NOCOLOR);

        threeDice = new Dice(DiceColor.BLUE, 3);
        fourDice = new Dice(DiceColor.BLUE, 4);

        player = new Player("Sonny", new PrivateObjectiveCard(null, null, null, RED));
    }

    /**
     * Tests that it is allowed to place a dice next to a different value one
     */
    @Before
    public void initializeWindowPattern(){

        windowPattern = new WindowPattern("","", "",0, pattern);
        player.setWindowPattern(windowPattern);

        windowPattern.putDiceOnCell(threeDice, 1,1);
    }

    /**
     * Tests that it is not allowed to place a dice on a cell with a dice on it
     */
    @Test
    public void testCheckIfMoveIsAllowedWhenMoveIsOnCellWithAlreadyPlacedDice() {
        assertFalse(rule.isMoveAllowed(windowPattern, threeDice, 1,1));
    }

    /**
     * Tests that it is allowed to place a dice next to a different value one
     */
    @Test
    public void testCheckIfMoveIsAllowed() {
        assertTrue(decoratedRule.isMoveAllowed(windowPattern, fourDice, 1, 2));
    }

    /**
     * Tests that it is not allowed to place a dice on a certain cell because of the decorated rule
     */
    @Test
    public void testCheckIfMoveIsAllowedIfDecoratedNotAllowed() {
        assertFalse(decoratedRule.isMoveAllowed(windowPattern, fourDice, 1, 0));
    }

    /**
     * Tests that it is not allowed to place a dice when there is a dice of the same value below it
     * and that it is allowed to place a dice when there is a dice of a different value below it
     */
    @Test
    public void testCheckAdjacentValueConstraintsBelow() {
        assertFalse(rule.isMoveAllowed(windowPattern, threeDice, 0,1));
        assertTrue(rule.isMoveAllowed(windowPattern, fourDice, 0,1));
    }

    /**
     * Tests that it is not allowed to place a dice when there is a dice of the same value above it
     * and that it is allowed to place a dice when there is a dice of a different value above it
     */
    @Test
    public void testCheckAdjacentValueConstraintsAbove() {
        assertFalse(rule.isMoveAllowed(windowPattern, threeDice, 2,1));
        assertTrue(rule.isMoveAllowed(windowPattern, fourDice, 2,1));
    }

    /**
     * Tests that it is not allowed to place a dice when there is a dice of the same value on the left of it
     * and that it is allowed to place a dice when there is a dice of a different value on the left of it
     */
    @Test
    public void testCheckAdjacentValueConstraintsLeft() {
        assertFalse(rule.isMoveAllowed(windowPattern, threeDice, 1,2));
        assertTrue(rule.isMoveAllowed(windowPattern, fourDice, 1,2));
    }

    /**
     * Tests that it is not allowed to place a dice when there is a dice of the same value on the right of it
     * and that it is allowed to place a dice when there is a dice of a different value on the right of it
     */
    @Test
    public void testCheckAdjacentValueConstraintsRight() {
        assertFalse(rule.isMoveAllowed(windowPattern, threeDice, 1,0));
        assertTrue(rule.isMoveAllowed(windowPattern, fourDice, 1,0));
    }
}