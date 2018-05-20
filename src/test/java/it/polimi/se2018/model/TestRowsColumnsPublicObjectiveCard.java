package it.polimi.se2018.model;

import it.polimi.se2018.controller.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static it.polimi.se2018.model.DiceColors.*;
import static org.junit.Assert.*;


/**
 * @author Jacopo Pio Gargano
 */

//TODO: tests with factory
public class TestRowsColumnsPublicObjectiveCard {

    private static WindowPatternManager windowPatternManager;

    private static WindowPattern wp;
    private static WindowPattern nullWP;
    private static WindowPattern emptyWP;

    private static PublicObjectiveCard rowsColumnsPublicObjectiveCard;
    private static PublicObjectiveCard rowsColorPublicObjectiveCard;
    private static PublicObjectiveCard columnsColorPublicObjectiveCard;
    private static PublicObjectiveCard rowsValuePublicObjectiveCard;
    private static PublicObjectiveCard columnsValuePublicObjectiveCard;


    private static int scoreRowsColor;
    private static int scoreColumnsColor;
    private static int scoreRowsValue;
    private static int scoreColumnsValue;

    private int testScore;


    @BeforeClass
    public static void buildWindowPatterns(){
        try {
            windowPatternManager = new WindowPatternManager();
        }catch (NoPatternsFoundInFileSystemException e){
            e.printStackTrace();
            fail();
        }

        try {

            wp = windowPatternManager.getPatterns(1).get(0);

            wp.putDiceOnCell(new Dice(RED, 1), 0, 0);
            wp.putDiceOnCell(new Dice(YELLOW, 2), 0, 1);
            wp.putDiceOnCell(new Dice(PURPLE, 3), 0, 2);
            wp.putDiceOnCell(new Dice(BLUE, 5), 0, 3);
            wp.putDiceOnCell(new Dice(GREEN, 4), 0, 4);

            wp.putDiceOnCell(new Dice(YELLOW, 3), 1, 0);
            wp.putDiceOnCell(new Dice(BLUE, 3), 1, 1);
            wp.putDiceOnCell(new Dice(BLUE, 3), 1, 2);
            wp.putDiceOnCell(new Dice(RED, 5), 1, 3);

            wp.putDiceOnCell(new Dice(PURPLE, 5), 2, 0);
            wp.putDiceOnCell(new Dice(YELLOW, 6), 2, 1);
            wp.putDiceOnCell(new Dice(BLUE, 3), 2, 2);
            wp.putDiceOnCell(new Dice(GREEN, 3), 2, 3);
            wp.putDiceOnCell(new Dice(RED, 4), 2, 4);

            wp.putDiceOnCell(new Dice(YELLOW, 4), 3, 0);
            wp.putDiceOnCell(new Dice(YELLOW, 5), 3, 3);


            nullWP = null;

            emptyWP = windowPatternManager.getPatterns(1).get(0);

        }catch (BadFormattedPatternFileException e){
            e.printStackTrace();
            fail();
        }
    }


    @BeforeClass
    public static void initializeScores(){
        scoreRowsColor = 12;
        scoreColumnsColor = 5;
        scoreRowsValue = 5;
        scoreColumnsValue = 4;
    }

    @Before
    public void initializeCards(){

        rowsColumnsPublicObjectiveCard = RowsColumnsPublicObjectiveCard.createTestInstance();

        rowsColorPublicObjectiveCard = new RowsColumnsPublicObjectiveCard(null,null,
                null, Dice::getColor, 6, true);
        columnsColorPublicObjectiveCard = new RowsColumnsPublicObjectiveCard(null,null,
                null, Dice::getColor, 5, false);
        rowsValuePublicObjectiveCard = new RowsColumnsPublicObjectiveCard(null,null,
                null, Dice::getValue, 5, true);
        columnsValuePublicObjectiveCard = new RowsColumnsPublicObjectiveCard(null,null,
                null, Dice::getValue, 4, false);
    }

    @Before
    public void resetScore(){
        testScore = 0;
    }




    @Test
    public void testCalculateScoreOfNullWindowPattern(){
        try {
            testScore = rowsColumnsPublicObjectiveCard.calculateScore(nullWP);
            fail();
        }catch (IllegalArgumentException e){
            assertNull(nullWP);
        }
    }

    @Test
    public void testCalculateScoreOfEmptyWindowPattern(){
        testScore = rowsColumnsPublicObjectiveCard.calculateScore(emptyWP);
        assertEquals(0, testScore);
    }

    @Test
    public void testCalculateScoreRowsColor() {
        testScore = rowsColorPublicObjectiveCard.calculateScore(wp);
        assertEquals(scoreRowsColor, testScore);
    }

    @Test
    public void testCalculateScoreColumnsColor() {
        testScore = columnsColorPublicObjectiveCard.calculateScore(wp);
        assertEquals(scoreColumnsColor, testScore);
    }

    @Test
    public void testCalculateScoreRowsValue() {
        testScore = rowsValuePublicObjectiveCard.calculateScore(wp);
        assertEquals(scoreRowsValue, testScore);
    }

    @Test
    public void testCalculateScoreColumnsValue() {
        testScore = columnsValuePublicObjectiveCard.calculateScore(wp);
        assertEquals(scoreColumnsValue, testScore);
    }

    @Test
    public void testToString(){
        rowsColorPublicObjectiveCard = new RowsColumnsPublicObjectiveCard(
                "title", "description", null, Dice::getValue, 4, true);
        String toString = rowsColorPublicObjectiveCard.toString();
        String expectedString = "title"+System.lineSeparator()+"description"+System.lineSeparator()+"Multiplier: 4"+System.lineSeparator();
        assertEquals(expectedString, toString);
    }

    @Test
    public void testCopy(){
        assertNotNull(rowsColorPublicObjectiveCard.copy());
    }
}