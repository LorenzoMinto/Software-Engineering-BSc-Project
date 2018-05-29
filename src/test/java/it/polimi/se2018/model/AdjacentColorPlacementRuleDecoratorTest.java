package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class AdjacentColorPlacementRuleDecoratorTest {

    private static Cell[][] pattern;

    private PlacementRule rule;
    private PlacementRule decoratedRule;

    private WindowPattern windowPattern;

    private Dice redDice;
    private Dice blueDice;

    @BeforeClass
    public static void setUpClass() throws Exception {
        pattern = new Cell[3][3];
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                pattern[i][j] = new Cell();
            }
        }
        pattern[1][0] = new Cell(0, DiceColor.RED);
    }

    @Before
    public void setUp() throws Exception {
        PlacementRule emptyRule = new EmptyPlacementRule();
        rule = new AdjacentColorPlacementRuleDecorator(emptyRule);
        decoratedRule = new AdjacentColorPlacementRuleDecorator(new ColorPlacementRuleDecorator(emptyRule));

        windowPattern = new WindowPattern("","", 0, pattern);
        redDice = new Dice(DiceColor.RED);
        blueDice = new Dice(DiceColor.BLUE);
        windowPattern.putDiceOnCell(redDice, 1,1);
    }

    @Test
    public void testCheckIfMoveIsAllowed() {
        assertTrue(decoratedRule.checkIfMoveIsAllowed(windowPattern, blueDice, 1, 2));
    }

    @Test
    public void testCheckIfMoveIsAllowedWhenMoveIsOnAlreadyPlacedDice() {
        assertFalse(rule.checkIfMoveIsAllowed(windowPattern, redDice, 1,1));
    }

    @Test
    public void testCheckIfMoveIsAllowedIfDecoratedNotAllowed() {
        assertFalse(decoratedRule.checkIfMoveIsAllowed(windowPattern, blueDice, 1, 0));
    }

    @Test
    public void testCheckAdjacentColorConstraintsBelow() {
        assertFalse(rule.checkIfMoveIsAllowed(windowPattern, redDice, 0,1));
        assertTrue(rule.checkIfMoveIsAllowed(windowPattern, blueDice, 0,1));
    }

    @Test
    public void testCheckAdjacentColorConstraintsAbove() {
        assertFalse(rule.checkIfMoveIsAllowed(windowPattern, redDice, 2,1));
        assertTrue(rule.checkIfMoveIsAllowed(windowPattern, blueDice, 2,1));
    }

    @Test
    public void testCheckAdjacentColorConstraintsLeft() {
        assertFalse(rule.checkIfMoveIsAllowed(windowPattern, redDice, 1,2));
        assertTrue(rule.checkIfMoveIsAllowed(windowPattern, blueDice, 1,2));
    }

    @Test
    public void testCheckAdjacentColorConstraintsRight() {
        assertFalse(rule.checkIfMoveIsAllowed(windowPattern, redDice, 1,0));
        assertTrue(rule.checkIfMoveIsAllowed(windowPattern, blueDice, 1,0));
    }
}