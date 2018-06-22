package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static it.polimi.se2018.model.DiceColor.RED;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link NotAdjacentDicePlacementRuleDecorator} class
 * @see NotAdjacentDicePlacementRuleDecorator#isMoveAllowed(WindowPattern, Dice, int, int)
 * @author Lorenzo Minto
 */
public class NotAdjacentDicePlacementRuleDecoratorTest {

    private static Cell[][] patternWithDiceOnCenter;
    private static Cell[][] patternWithDiceOnTopLeftCorner;

    private static PlacementRule rule;
    /**
     * Used to test the composite behavior. This rule is arbitrary among all the rules
     */
    private static PlacementRule decoratedRule;
    private static Player player1;
    private static Player player2;

    private WindowPattern windowPatternWithDiceOnCenter;
    private WindowPattern windowPatternWithDiceOnTopLeftCorner;

    private static Dice threeDice;
    private static Dice fourDice;

    /**
     * Initializes the variables needed in the tests
     */
    @BeforeClass
    public static void initializeVariables(){
        PlacementRule emptyRule = new EmptyPlacementRule();
        rule = new NotAdjacentDicePlacementRuleDecorator(emptyRule);
        decoratedRule = new NotAdjacentDicePlacementRuleDecorator(new ValuePlacementRuleDecorator(emptyRule));

        patternWithDiceOnCenter = new Cell[3][3];
        patternWithDiceOnTopLeftCorner = new Cell[3][3];
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                patternWithDiceOnCenter[i][j] = new Cell();
                patternWithDiceOnTopLeftCorner[i][j] = new Cell();
            }
        }
        patternWithDiceOnCenter[1][0] = new Cell(3, DiceColor.NOCOLOR);
        patternWithDiceOnTopLeftCorner[1][0] = new Cell(3, DiceColor.NOCOLOR);

        threeDice = new Dice(DiceColor.BLUE, 3);
        fourDice = new Dice(DiceColor.BLUE, 4);

        player1 = new Player("Sonny", new PrivateObjectiveCard("","","",RED));
        player2 = new Player("Johnny", new PrivateObjectiveCard("","","",RED));
    }

    /**
     * Initializes the players' windowPatterns for the tests before each test
     */
    @Before
    public void initializeWindowPatterns(){
        windowPatternWithDiceOnCenter = new WindowPattern("","", "",0, patternWithDiceOnCenter);
        player1.setWindowPattern(windowPatternWithDiceOnCenter);
        windowPatternWithDiceOnCenter.putDiceOnCell(threeDice, 1,1);

        windowPatternWithDiceOnTopLeftCorner = new WindowPattern("","","",0, patternWithDiceOnTopLeftCorner);
        player2.setWindowPattern(windowPatternWithDiceOnTopLeftCorner);
        windowPatternWithDiceOnTopLeftCorner.putDiceOnCell(threeDice,0,0);
    }

    /**
     * Tests that it is not allowed to place a dice on a cell with a dice on it
     */
    @Test
    public void testCheckIfMoveIsAllowedWhenMoveIsOnCellWithAlreadyPlacedDice() {
        assertFalse(rule.isMoveAllowed(windowPatternWithDiceOnCenter, threeDice, 1,1));
    }

    /**
     * Tests that it is not allowed to place a dice on a generic {@link WindowPattern}
     * if it is adjacent to any other dice and viceversa
     */
    @Test
    public void testCheckIfMoveIsAllowedOnGenericWindowPattern() {
        assertFalse(decoratedRule.isMoveAllowed(windowPatternWithDiceOnTopLeftCorner, fourDice, 1, 1));
        assertTrue(decoratedRule.isMoveAllowed(windowPatternWithDiceOnTopLeftCorner, fourDice, 1, 2));
        assertTrue(decoratedRule.isMoveAllowed(windowPatternWithDiceOnTopLeftCorner, fourDice, 2,2));
    }

    /**
     * Tests that it is not allowed to place a dice on a certain cell because of the decorated rule
     */
    @Test
    public void testCheckIfMoveIsAllowedIfDecoratedNotAllowed() {
        assertFalse(decoratedRule.isMoveAllowed(windowPatternWithDiceOnTopLeftCorner, fourDice, 1, 0));
    }

    /**
     * Tests that it is not allowed to place a dice when there is a dice below it
     */
    @Test
    public void testCheckAdjacentDiceConstraintsBelow() {
        assertFalse(rule.isMoveAllowed(windowPatternWithDiceOnCenter, threeDice, 0,1));
    }

    /**
     * Tests that it is not allowed to place a dice when there is a dice above on the left of it
     */
    @Test
    public void testCheckAdjacentDiceConstraintsRightBelow() {
        assertFalse(rule.isMoveAllowed(windowPatternWithDiceOnCenter, threeDice, 0,0));
    }

    /**
     * Tests that it is not allowed to place a dice when there is a dice on the right of it
     */
    @Test
    public void testCheckAdjacentDiceConstraintsRight() {
        assertFalse(rule.isMoveAllowed(windowPatternWithDiceOnCenter, threeDice, 1,0));
    }

    /**
     * Tests that it is not allowed to place a dice when there is a dice above on the right of it
     */
    @Test
    public void testCheckAdjacentDiceConstraintsRightAbove() {
        assertFalse(rule.isMoveAllowed(windowPatternWithDiceOnCenter, threeDice, 2,0));
    }

    /**
     * Tests that it is not allowed to place a dice when there is a dice above it
     */
    @Test
    public void testCheckAdjacentDiceConstraintsAbove() {
        assertFalse(rule.isMoveAllowed(windowPatternWithDiceOnCenter, threeDice, 2,1));
    }

    /**
     * Tests that it is not allowed to place a dice when there is a dice above on the left of it
     */
    @Test
    public void testCheckAdjacentDiceConstraintsLeftAbove() {
        assertFalse(rule.isMoveAllowed(windowPatternWithDiceOnCenter, threeDice, 2,2));
    }

    /**
     * Tests that it is not allowed to place a dice when there is a dice on the left of it
     */
    @Test
    public void testCheckAdjacentDiceConstraintsLeft() {
        assertFalse(rule.isMoveAllowed(windowPatternWithDiceOnCenter, threeDice, 1,2));
    }

    /**
     * Tests that it is not allowed to place a dice when there is a dice below on the left of it
     */
    @Test
    public void testCheckAdjacentDiceConstraintsLeftBelow() {
        assertFalse(rule.isMoveAllowed(windowPatternWithDiceOnCenter, threeDice, 0,2));
    }
}
