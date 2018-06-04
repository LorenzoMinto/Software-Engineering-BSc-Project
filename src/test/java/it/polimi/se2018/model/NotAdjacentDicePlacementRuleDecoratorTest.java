package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NotAdjacentDicePlacementRuleDecoratorTest {

    private static Cell[][] pattern;
    private static Cell[][] pattern2;

    private PlacementRule rule;
    private PlacementRule decoratedRule;

    private WindowPattern windowPattern;
    private WindowPattern windowPattern2;

    private Dice threeDice;
    private Dice fourDice;

    @BeforeClass
    public static void setUpClass() throws Exception {
        pattern = new Cell[3][3];
        pattern2 = new Cell[3][3];
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                pattern[i][j] = new Cell();
                pattern2[i][j] = new Cell();
            }
        }
        pattern[1][0] = new Cell(3, DiceColor.NOCOLOR);
        pattern2[1][0] = new Cell(3, DiceColor.NOCOLOR);
    }

    @Before
    public void setUp() throws Exception {
        PlacementRule emptyRule = new EmptyPlacementRule();
        rule = new NotAdjacentDicePlacementRuleDecorator(emptyRule);
        decoratedRule = new NotAdjacentDicePlacementRuleDecorator(new ValuePlacementRuleDecorator(emptyRule));

        windowPattern = new WindowPattern("", "","",0, pattern);
        threeDice = new Dice(DiceColor.BLUE, 3);
        fourDice = new Dice(DiceColor.BLUE, 4);
        windowPattern.putDiceOnCell(threeDice, 1,1);

        windowPattern2 = new WindowPattern("","","",0, pattern2);
    }

    @Test
    public void testCheckIfMoveIsAllowedWhenMoveIsOnAlreadyPlacedDice() {
        assertFalse(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 1,1));
    }

    @Test
    public void testCheckIfMoveIsAllowed() {
        assertTrue(decoratedRule.checkIfMoveIsAllowed(windowPattern2, fourDice, 1, 1));
        assertTrue(decoratedRule.checkIfMoveIsAllowed(windowPattern2, fourDice, 0, 0));
        assertTrue(decoratedRule.checkIfMoveIsAllowed(windowPattern2, fourDice, 1, 2));
    }

    @Test
    public void testCheckIfMoveIsAllowedIfDecoratedNotAllowed() {
        assertFalse(decoratedRule.checkIfMoveIsAllowed(windowPattern2, fourDice, 1, 0));
    }


    @Test
    public void testCheckAdjacentDiceConstraintsBelow() {
        assertFalse(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 0,1));
    }

    @Test
    public void testCheckAdjacentDiceConstraintsRightBelow() {
        assertFalse(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 0,0));
    }

    @Test
    public void testCheckAdjacentDiceConstraintsLeftBelow() {
        assertFalse(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 0,2));
    }

    @Test
    public void testCheckAdjacentDiceConstraintsAbove() {
        assertFalse(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 2,1));
    }

    @Test
    public void testCheckAdjacentDiceConstraintsRightAbove() {
        assertFalse(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 2,0));
    }

    @Test
    public void testCheckAdjacentDiceConstraintsLeftAbove() {
        assertFalse(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 2,2));
    }

    @Test
    public void testCheckAdjacentDiceConstraintsLeft() {
        assertFalse(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 1,2));
    }

    @Test
    public void testCheckAdjacentDiceConstraintsRight() {
        assertFalse(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 1,0));
    }
}