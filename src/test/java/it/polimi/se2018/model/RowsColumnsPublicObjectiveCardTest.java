package it.polimi.se2018.model;

import it.polimi.se2018.controller.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;


//TODO: tests with factory
public class RowsColumnsPublicObjectiveCardTest {

    private static WindowPatternManager windowPatternManager;

    private static WindowPattern wp;
    private static WindowPattern nullWP = null;
    private static WindowPattern empytyWP= null;

    private static PublicObjectiveCard rowsColumnsPublicObjectiveCard;
    private static PublicObjectiveCard rowsColorPublicObjectiveCard;
    private static PublicObjectiveCard columnsColorPublicObjectiveCard;
    private static PublicObjectiveCard rowsValuePublicObjectiveCard;
    private static PublicObjectiveCard columnsValuePublicObjectiveCard;


    private static int scoreRowsColor;
    private static int scoreColumnsColor;
    private static int scoreRowsValue;
    private static int scoreColumnsValue;

    private int testScore = 0;


    @BeforeClass
    public static void buildWindowPattern(){
        try {
            windowPatternManager = new WindowPatternManager();
        }catch (NoPatternsFoundInFileSystemException e){
            e.printStackTrace();
        }

        try {

            wp = windowPatternManager.getPatterns(1).get(0);

            wp.putDiceOnCell(new Dice(DiceColors.RED, 1), 0, 0);
            wp.putDiceOnCell(new Dice(DiceColors.YELLOW, 2), 0, 1);
            wp.putDiceOnCell(new Dice(DiceColors.PURPLE, 3), 0, 2);
            wp.putDiceOnCell(new Dice(DiceColors.BLUE, 5), 0, 3);
            wp.putDiceOnCell(new Dice(DiceColors.GREEN, 4), 0, 4);

            wp.putDiceOnCell(new Dice(DiceColors.YELLOW, 3), 1, 0);
            wp.putDiceOnCell(new Dice(DiceColors.BLUE, 3), 1, 1);
            wp.putDiceOnCell(new Dice(DiceColors.BLUE, 3), 1, 2);
            wp.putDiceOnCell(new Dice(DiceColors.RED, 5), 1, 3);

            wp.putDiceOnCell(new Dice(DiceColors.PURPLE, 5), 2, 0);
            wp.putDiceOnCell(new Dice(DiceColors.YELLOW, 6), 2, 1);
            wp.putDiceOnCell(new Dice(DiceColors.BLUE, 3), 2, 2);
            wp.putDiceOnCell(new Dice(DiceColors.GREEN, 3), 2, 3);
            wp.putDiceOnCell(new Dice(DiceColors.RED, 4), 2, 4);

            wp.putDiceOnCell(new Dice(DiceColors.YELLOW, 4), 3, 0);
            wp.putDiceOnCell(new Dice(DiceColors.YELLOW, 5), 3, 3);

            scoreRowsColor = 12;
            scoreColumnsColor = 5;
            scoreRowsValue = 5;
            scoreColumnsValue = 4;


            empytyWP = windowPatternManager.getPatterns(1).get(0);


            rowsColumnsPublicObjectiveCard = new RowsColumnsPublicObjectiveCard();
            rowsColorPublicObjectiveCard = new RowsColumnsPublicObjectiveCard(null,null,
                    null, Dice::getColor, 6, true);
            columnsColorPublicObjectiveCard = new RowsColumnsPublicObjectiveCard(null,null,
                    null, Dice::getColor, 5, false);
            rowsValuePublicObjectiveCard = new RowsColumnsPublicObjectiveCard(null,null,
                    null, Dice::getValue, 5, true);
            columnsValuePublicObjectiveCard = new RowsColumnsPublicObjectiveCard(null,null,
                    null, Dice::getValue, 4, false);

        }catch (BadFormattedPatternFileException e){
            e.printStackTrace();
        }
    }

    @Before
    public void resetScore(){
        testScore = 0;
    }

    @Test
    public void calculateScoreOfNullWindowPattern(){
        try {
            rowsColumnsPublicObjectiveCard.calculateScore(nullWP);
            fail();
        }catch (IllegalArgumentException e){
            assertNull(nullWP);
        }
    }

    @Test
    public void calculateScoreOfEmptyWindowPattern(){
        testScore = rowsColumnsPublicObjectiveCard.calculateScore(empytyWP);
        assertEquals(testScore, 0);
    }

    @Test
    public void calculateScoreRowsColor() {
        testScore = rowsColorPublicObjectiveCard.calculateScore(wp);
        assertEquals(testScore, scoreRowsColor);
    }

    @Test
    public void calculateScoreColumnsColor() {
        testScore = columnsColorPublicObjectiveCard.calculateScore(wp);
        assertEquals(testScore, scoreColumnsColor);
    }

    @Test
    public void calculateScoreRowsValue() {
        testScore = rowsValuePublicObjectiveCard.calculateScore(wp);
        assertEquals(testScore, scoreRowsValue);
    }

    @Test
    public void calculateScoreColumnsValue() {
        testScore = columnsValuePublicObjectiveCard.calculateScore(wp);
        assertEquals(testScore, scoreColumnsValue);
    }
}