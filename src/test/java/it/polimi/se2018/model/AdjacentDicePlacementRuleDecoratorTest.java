package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AdjacentDicePlacementRuleDecoratorTest {

    static Cell[][] pattern;
    static Cell[][] pattern2;

    PlacementRule rule;
    PlacementRule decoratedRule;

    WindowPattern windowPattern;

    Dice threeDice;
    Dice fourDice;

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
        pattern[1][0] = new Cell(3, DiceColors.NOCOLOR);
    }

    @Before
    public void setUp() throws Exception {
        PlacementRule emptyRule = new EmptyPlacementRule();
        rule = new AdjacentDicePlacementRuleDecorator(emptyRule);
        decoratedRule = new AdjacentDicePlacementRuleDecorator(new ValuePlacementRuleDecorator(emptyRule));

        windowPattern = new WindowPattern("", 0, pattern);
        threeDice = new Dice(DiceColors.BLUE, 3);
        fourDice = new Dice(DiceColors.BLUE, 4);
        windowPattern.putDiceOnCell(threeDice, 1,1);
    }

    @Test
    public void testCheckIfMoveIsAllowedWhenMoveIsOnAlreadyPlacedDice() {
        assertFalse(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 1,1));
    }

    @Test
    public void testCheckIfMoveIsAllowed() {
        assertTrue(decoratedRule.checkIfMoveIsAllowed(windowPattern, fourDice, 1, 2));
    }

    @Test
    public void testCheckIfMoveIsAllowedWhenNotAllowed() {
        WindowPattern wp = new WindowPattern("",0, pattern2);
        Dice dice = new Dice(DiceColors.BLUE);
        assertFalse(rule.checkIfMoveIsAllowed(wp, dice, 1,1));
        assertFalse(rule.checkIfMoveIsAllowed(wp, dice, 1,2));
        assertFalse(rule.checkIfMoveIsAllowed(wp, dice, 0,0));
    }

    @Test
    public void testCheckIfMoveIsAllowedIfDecoratedNotAllowed() {
        assertFalse(decoratedRule.checkIfMoveIsAllowed(windowPattern, fourDice, 1, 0));
    }
    @Test
    public void testCheckAdjacentDiceConstraintsBelow() {
        assertTrue(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 0,1));
    }

    @Test
    public void testCheckAdjacentDiceConstraintsRightBelow() {
        assertTrue(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 0,0));
    }

    @Test
    public void testCheckAdjacentDiceConstraintsLeftBelow() {
        assertTrue(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 0,2));
    }

    @Test
    public void testCheckAdjacentDiceConstraintsAbove() {
        assertTrue(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 2,1));
    }

    @Test
    public void testCheckAdjacentDiceConstraintsRightAbove() {
        assertTrue(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 2,0));
    }

    @Test
    public void testCheckAdjacentDiceConstraintsLeftAbove() {
        assertTrue(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 2,2));
    }

    @Test
    public void testCheckAdjacentDiceConstraintsLeft() {
        assertTrue(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 1,2));
    }

    @Test
    public void testCheckAdjacentDiceConstraintsRight() {
        assertTrue(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 1,0));
    }
}