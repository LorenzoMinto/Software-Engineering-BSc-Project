package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static it.polimi.se2018.model.DiceColor.RED;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link BorderPlacementRuleDecorator} class
 * @see BorderPlacementRuleDecorator#isMoveAllowed(WindowPattern, Dice, int, int)
 * @author Lorenzo Minto
 */
public class BorderPlacementRuleDecoratorTest {

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
        rule = new BorderPlacementRuleDecorator(emptyRule);
        decoratedRule = new BorderPlacementRuleDecorator(new ColorPlacementRuleDecorator(emptyRule));

        pattern = new Cell[3][3];
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                pattern[i][j] = new Cell();
            }
        }
        pattern[1][0] = new Cell(0, DiceColor.RED);

        redDice = new Dice(DiceColor.RED);
        blueDice = new Dice(DiceColor.BLUE);

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
    public void testCheckIfMoveIsAllowedWhenMoveIsOnAlreadyPlacedDice() {
        windowPattern.putDiceOnCell(redDice, 1,1);
        assertFalse(rule.isMoveAllowed(windowPattern, redDice, 1,1));
    }

    /**
     * Tests that it is allowed to place a dice on the border of a windowPattern
     */
    @Test
    public void testCheckIfMoveIsAllowed() {
        assertTrue(decoratedRule.isMoveAllowed(windowPattern, redDice, 1, 0));
        assertTrue(decoratedRule.isMoveAllowed(windowPattern, redDice, 1, 2));
        assertTrue(decoratedRule.isMoveAllowed(windowPattern, redDice, 2, 1));
        assertTrue(decoratedRule.isMoveAllowed(windowPattern, redDice, 0, 1));
    }

    /**
     * Tests that it is not allowed to place a dice not on the border of a windowPattern
     */
    @Test
    public void testCheckIfMoveIsAllowedWhenTryToPlaceNotOnBorder() {
        assertFalse(decoratedRule.isMoveAllowed(windowPattern, blueDice, 1, 1));
    }

    /**
     * Tests that it is not allowed to place a dice on a certain cell because of the decorated rule
     */
    @Test
    public void testCheckIfMoveIsAllowedIfDecoratedNotAllowed() {
        assertFalse(decoratedRule.isMoveAllowed(windowPattern, blueDice, 1, 0));
    }
}