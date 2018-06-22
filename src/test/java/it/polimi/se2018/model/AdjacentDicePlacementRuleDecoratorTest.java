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
public class AdjacentDicePlacementRuleDecoratorTest {

    private static Cell[][] patternWithDiceOnCenter;
    private static Cell[][] patternWithDiceOnTopLeftCorner;

    private static PlacementRule rule;
    /**
     * Used to test the composite behavior. This rule is arbitrary among all the rules
     */
    private static PlacementRule decoratedRule;

    private WindowPattern windowPattern;

    private static Dice threeDice;
    private static Dice fourDice;
    private static Player player;

    /**
     * Initializes the variables needed in the tests
     */
    @BeforeClass
    public static void initializeVariables(){
        PlacementRule emptyRule = new EmptyPlacementRule();
        rule = new AdjacentDicePlacementRuleDecorator(emptyRule);
        decoratedRule = new AdjacentDicePlacementRuleDecorator(new ValuePlacementRuleDecorator(emptyRule));

        patternWithDiceOnCenter = new Cell[3][3];
        patternWithDiceOnTopLeftCorner = new Cell[3][3];
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                patternWithDiceOnCenter[i][j] = new Cell();
                patternWithDiceOnTopLeftCorner[i][j] = new Cell();
            }
        }
        patternWithDiceOnCenter[1][0] = new Cell(3, DiceColor.NOCOLOR);

        threeDice = new Dice(DiceColor.BLUE, 3);
        fourDice = new Dice(DiceColor.BLUE, 4);

        player = new Player("Sonny", new PrivateObjectiveCard("","","",RED));
    }

    /**
     * Initializes the player's windowPattern for the tests before each test
     */
    @Before
    public void initializeWindowPattern() {
        windowPattern = new WindowPattern("","", "",0, patternWithDiceOnCenter);
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
     * Tests that it is allowed to place a dice next to another one
     */
    @Test
    public void testCheckIfMoveIsAllowed() {
        assertTrue(decoratedRule.isMoveAllowed(windowPattern, fourDice, 1, 2));
    }

    /**
     * Tests that it is not allowed to place a dice on a generic {@link WindowPattern}
     * if it is not adjacent to any other dice and viceversa
     */
    @Test
    public void testCheckIfMoveIsAllowedOnGenericWindowPattern() {
        WindowPattern wp = new WindowPattern("", "","",0, patternWithDiceOnTopLeftCorner);
        player.setWindowPattern(wp);
        wp.putDiceOnCell(threeDice,0,0);

        Dice dice = new Dice(DiceColor.BLUE);
        assertTrue(rule.isMoveAllowed(wp, dice, 1,1));
        assertFalse(rule.isMoveAllowed(wp, dice, 1,2));
        assertFalse(rule.isMoveAllowed(wp, dice, 2,2));
    }

    /**
     * Tests that it is not allowed to place a dice on a certain cell because of the decorated rule
     */
    @Test
    public void testCheckIfMoveIsAllowedIfDecoratedNotAllowed() {
        assertFalse(decoratedRule.isMoveAllowed(windowPattern, fourDice, 1, 0));
    }

    /**
     * Tests that it is allowed to place a dice when there is a dice below it
     */
    @Test
    public void testCheckAdjacentDiceConstraintsBelow() {
        assertTrue(rule.isMoveAllowed(windowPattern, threeDice, 0,1));
    }

    /**
     * Tests that it is allowed to place a dice when there is a dice below on the right of it
     */
    @Test
    public void testCheckAdjacentDiceConstraintsRightBelow() {
        assertTrue(rule.isMoveAllowed(windowPattern, threeDice, 0,0));
    }

    /**
     * Tests that it is allowed to place a dice when there is a dice on the right of it
     */
    @Test
    public void testCheckAdjacentDiceConstraintsRight() {
        assertTrue(rule.isMoveAllowed(windowPattern, threeDice, 1,0));
    }

    /**
     * Tests that it is allowed to place a dice when there is a dice above on the right of it
     */
    @Test
    public void testCheckAdjacentDiceConstraintsRightAbove() {
        assertTrue(rule.isMoveAllowed(windowPattern, threeDice, 2,0));
    }

    /**
     * Tests that it is allowed to place a dice when there is a dice above it
     */
    @Test
    public void testCheckAdjacentDiceConstraintsAbove() {
        assertTrue(rule.isMoveAllowed(windowPattern, threeDice, 2,1));
    }

    /**
     * Tests that it is allowed to place a dice when there is a dice above on the left of it
     */
    @Test
    public void testCheckAdjacentDiceConstraintsLeftAbove() {
        assertTrue(rule.isMoveAllowed(windowPattern, threeDice, 2,2));
    }

    /**
     * Tests that it is allowed to place a dice when there is a dice on the left of it
     */
    @Test
    public void testCheckAdjacentDiceConstraintsLeft() {
        assertTrue(rule.isMoveAllowed(windowPattern, threeDice, 1,2));
    }

    /**
     * Tests that it is allowed to place a dice when there is a dice below on the left of it
     */
    @Test
    public void testCheckAdjacentDiceConstraintsLeftBelow() {
        assertTrue(rule.isMoveAllowed(windowPattern, threeDice, 0,2));
    }









}