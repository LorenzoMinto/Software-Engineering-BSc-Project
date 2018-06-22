package it.polimi.se2018.model;

import it.polimi.se2018.controller.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.function.Function;

import static it.polimi.se2018.model.DiceColor.*;
import static org.junit.Assert.*;


/**
 * Test for {@link RowsColumnsPublicObjectiveCard} class
 *
 * @author Jacopo Pio Gargano
 */

public class RowsColumnsPublicObjectiveCardTest {

    private static WindowPattern windowPattern;
    private static WindowPattern emptyWP;

    private static PublicObjectiveCard rowsColumnsPublicObjectiveCard;
    private static PublicObjectiveCard rowsColorPublicObjectiveCard;
    private static PublicObjectiveCard columnsColorPublicObjectiveCard;
    private static PublicObjectiveCard rowsValuePublicObjectiveCard;
    private static PublicObjectiveCard columnsValuePublicObjectiveCard;

    private static final int scoreRowsColor = 12;
    private static final int scoreColumnsColor = 5;
    private static final int scoreRowsValue = 5;
    private static final int scoreColumnsValue = 4;

    /**
     * Creates a new Window Pattern Manager and creates the specific patterns of the players for the test
     */
    @BeforeClass
    public static void buildWindowPatterns(){

        try {

            WindowPatternManager windowPatternManager = new WindowPatternManager();

            windowPattern = new ArrayList<>(windowPatternManager.getPairsOfPatterns(1)).get(0);

            Player player = new Player("", new PrivateObjectiveCard("","","",RED));
            player.setWindowPattern(windowPattern);
            windowPattern.setOwner(player);

            windowPattern.putDiceOnCell(new Dice(RED, 1), 0, 0);
            windowPattern.putDiceOnCell(new Dice(YELLOW, 2), 0, 1);
            windowPattern.putDiceOnCell(new Dice(PURPLE, 3), 0, 2);
            windowPattern.putDiceOnCell(new Dice(BLUE, 5), 0, 3);
            windowPattern.putDiceOnCell(new Dice(GREEN, 4), 0, 4);

            windowPattern.putDiceOnCell(new Dice(YELLOW, 3), 1, 0);
            windowPattern.putDiceOnCell(new Dice(BLUE, 3), 1, 1);
            windowPattern.putDiceOnCell(new Dice(BLUE, 3), 1, 2);
            windowPattern.putDiceOnCell(new Dice(RED, 5), 1, 3);

            windowPattern.putDiceOnCell(new Dice(PURPLE, 5), 2, 0);
            windowPattern.putDiceOnCell(new Dice(YELLOW, 6), 2, 1);
            windowPattern.putDiceOnCell(new Dice(BLUE, 3), 2, 2);
            windowPattern.putDiceOnCell(new Dice(GREEN, 3), 2, 3);
            windowPattern.putDiceOnCell(new Dice(RED, 4), 2, 4);

            windowPattern.putDiceOnCell(new Dice(YELLOW, 4), 3, 0);
            windowPattern.putDiceOnCell(new Dice(YELLOW, 5), 3, 3);

            emptyWP = new ArrayList<>(windowPatternManager.getPairsOfPatterns(1)).get(0);

        }catch (BadFormattedPatternFileException | NoPatternsFoundInFileSystemException e){
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Creates the the instances of {@link RowsColumnsPublicObjectiveCard} used in the tests
     * @see RowsColumnsPublicObjectiveCard#RowsColumnsPublicObjectiveCard(String, String, String, Function, int, boolean)
     */
    @BeforeClass
    public static void initializeCards(){

        rowsColumnsPublicObjectiveCard = new RowsColumnsPublicObjectiveCard(null,null,null,Dice::getColor, 6, true);

        rowsColorPublicObjectiveCard = new RowsColumnsPublicObjectiveCard(null,null,
                null, Dice::getColor, 6, true);
        columnsColorPublicObjectiveCard = new RowsColumnsPublicObjectiveCard(null,null,
                null, Dice::getColor, 5, false);
        rowsValuePublicObjectiveCard = new RowsColumnsPublicObjectiveCard(null,null,
                null, Dice::getValue, 5, true);
        columnsValuePublicObjectiveCard = new RowsColumnsPublicObjectiveCard(null,null,
                null, Dice::getValue, 4, false);
    }

    /**
     * Tests the impossibility of calculating the score of a null window pattern
     * @see RowsColumnsPublicObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreOfNullWindowPattern(){
        try {
            rowsColumnsPublicObjectiveCard.calculateScore(null);
            fail();
        }catch (NullPointerException e){}
    }

    /**
     * Tests the scoring of an empty window pattern. Score must be 0
     * @see RowsColumnsPublicObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreOfEmptyWindowPattern(){
        int score = rowsColumnsPublicObjectiveCard.calculateScore(emptyWP);
        assertEquals(0, score);
    }

    /**
     * Tests the scoring of a generic window pattern with a RowsColor {@link RowsColumnsPublicObjectiveCard}
     * @see RowsColumnsPublicObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreRowsColor() {
        int score = rowsColorPublicObjectiveCard.calculateScore(windowPattern);
        assertEquals(scoreRowsColor, score);
    }

    /**
     * Tests the scoring of a generic window pattern with a ColumnsColor {@link RowsColumnsPublicObjectiveCard}
     * @see RowsColumnsPublicObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreColumnsColor() {
        int score = columnsColorPublicObjectiveCard.calculateScore(windowPattern);
        assertEquals(scoreColumnsColor, score);
    }

    /**
     * Tests the scoring of a generic window pattern with a RowsValue {@link RowsColumnsPublicObjectiveCard}
     * @see RowsColumnsPublicObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreRowsValue() {
        int score = rowsValuePublicObjectiveCard.calculateScore(windowPattern);
        assertEquals(scoreRowsValue, score);
    }

    /**
     * Tests the scoring of a generic window pattern with a ColumnsValue {@link RowsColumnsPublicObjectiveCard}
     * @see RowsColumnsPublicObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreColumnsValue() {
        int score = columnsValuePublicObjectiveCard.calculateScore(windowPattern);
        assertEquals(scoreColumnsValue, score);
    }

    /**
     * Tests the toString method of {@link RowsColumnsPublicObjectiveCard}
     * @see RowsColumnsPublicObjectiveCard#toString()
     */
    @Test
    public void testToString(){
        rowsColumnsPublicObjectiveCard = new RowsColumnsPublicObjectiveCard(
                "title", "description", null, Dice::getValue, 4, true);
        String toString = rowsColumnsPublicObjectiveCard.toString();
        String expectedString = "title"+System.lineSeparator()+"description"+System.lineSeparator()+"Multiplier: 4"+System.lineSeparator();
        assertEquals(expectedString, toString);
    }

    /**
     * Tests the copy method of {@link RowsColumnsPublicObjectiveCard} (copy must not be null)
     * @see RowsColumnsPublicObjectiveCard#copy()
     */
    @Test
    public void testCopy(){
        assertNotNull(rowsColorPublicObjectiveCard.copy());
    }
}