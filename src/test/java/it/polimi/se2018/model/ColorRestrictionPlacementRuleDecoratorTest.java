package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static it.polimi.se2018.model.DiceColor.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link ColorRestrictionPlacementRuleDecorator} class
 * @see ColorRestrictionPlacementRuleDecorator#isMoveAllowed(WindowPattern, Dice, int, int)
 * @author Lorenzo Minto
 */
public class ColorRestrictionPlacementRuleDecoratorTest {

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
        rule = new ColorRestrictionPlacementRuleDecorator(emptyRule, BLUE);
        decoratedRule = new ColorRestrictionPlacementRuleDecorator(new AdjacentColorPlacementRuleDecorator(emptyRule),BLUE);

        pattern = new Cell[3][3];
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                pattern[i][j] = new Cell();
            }
        }
        pattern[1][1] = new Cell(0, BLUE);

        redDice = new Dice(RED);
        blueDice = new Dice(BLUE);

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
     * Tests that it is not allowed to place a dice on a cell with a dice on it
     */
    @Test
    public void testCheckIfMoveIsAllowedWhenMoveIsOnCellWithAlreadyPlacedDice() {
        windowPattern.putDiceOnCell(blueDice, 1,1);
        assertFalse(rule.isMoveAllowed(windowPattern, blueDice, 1,1));
    }

    /**
     * Tests that it is not allowed to move a dice of a different color than the allowed one (blue)
     */
    @Test
    public void testCheckIfMoveIsAllowedIfDifferentColorThanAllowedInDecorator() {
        assertFalse(rule.isMoveAllowed(windowPattern, redDice, 1, 1));
    }

    /**
     * Tests that it is allowed to move a dice of the same color as specified in the decorator (blue)
     */
    @Test
    public void testCheckIfMoveIsAllowed() {
        assertNull(windowPattern.getDiceOnCell(1,1));
        assertTrue(rule.isMoveAllowed(windowPattern, blueDice, 1, 1));
    }

    /**
     * Tests that it is not allowed to place a dice on a certain cell because of the decorated rule
     */
    @Test
    public void testCheckIfMoveIsAllowedIfDecoratedNotAllowed() {
        windowPattern.putDiceOnCell(blueDice, 1,0);
        assertFalse(decoratedRule.isMoveAllowed(windowPattern, blueDice, 1, 1));
    }
}