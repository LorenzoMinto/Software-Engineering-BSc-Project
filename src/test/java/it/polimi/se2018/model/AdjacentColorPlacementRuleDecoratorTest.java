package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class AdjacentColorPlacementRuleDecoratorTest {

    static Cell[][] pattern;

    PlacementRule rule;
    PlacementRule decoratedRule;

    WindowPattern windowPattern;

    Dice redDice;
    Dice blueDice;

    @BeforeClass
    public static void setUpClass() throws Exception {
        pattern = new Cell[3][3];
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                pattern[i][j] = new Cell();
            }
        }
        pattern[1][0] = new Cell(0, DiceColors.RED);
    }

    @Before
    public void setUp() throws Exception {
        PlacementRule emptyRule = new EmptyPlacementRule();
        rule = new AdjacentColorPlacementRuleDecorator(emptyRule);
        decoratedRule = new AdjacentColorPlacementRuleDecorator(new ColorPlacementRuleDecorator(emptyRule));

        windowPattern = new WindowPattern("", "",0, pattern);
        redDice = new Dice(DiceColors.RED);
        blueDice = new Dice(DiceColors.BLUE);
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