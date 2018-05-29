package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValuePlacementRuleDecoratorTest {

    private static Cell[][] pattern;

    private PlacementRule rule;
    private PlacementRule decoratedRule;

    private WindowPattern windowPattern;

    private Dice threeDice;
    private Dice fourDice;

    @BeforeClass
    public static void setUpClass() throws Exception {
        pattern = new Cell[3][3];
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                pattern[i][j] = new Cell();
            }
        }
        pattern[1][1] = new Cell(3, DiceColor.RED);
    }

    @Before
    public void setUp() throws Exception {
        PlacementRule emptyRule = new EmptyPlacementRule();
        rule = new ValuePlacementRuleDecorator(emptyRule);
        decoratedRule = new ValuePlacementRuleDecorator(new AdjacentValuePlacementRuleDecorator(emptyRule));

        windowPattern = new WindowPattern("","", 0, pattern);
        threeDice = new Dice(DiceColor.RED, 3);
        fourDice = new Dice(DiceColor.BLUE, 4);
    }

    @Test
    public void testCheckIfMoveIsAllowedWhenMoveIsOnAlreadyPlacedDice() {
        windowPattern.putDiceOnCell(threeDice, 1,1);
        assertFalse(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 1,1));
    }

    @Test
    public void testCheckIfMoveIsAllowed() {
        assertTrue(decoratedRule.checkIfMoveIsAllowed(windowPattern, threeDice, 1, 1));
    }

    @Test
    public void testCheckIfMoveIsAllowedWhenNotAllowed() {
        assertFalse(decoratedRule.checkIfMoveIsAllowed(windowPattern, fourDice, 1, 1));
    }

    @Test
    public void testCheckIfMoveIsAllowedIfDecoratedNotAllowed() {
        windowPattern.putDiceOnCell(threeDice, 1,0);
        assertFalse(decoratedRule.checkIfMoveIsAllowed(windowPattern, threeDice, 1, 1));
    }
}