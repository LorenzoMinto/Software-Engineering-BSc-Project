package it.polimi.se2018.model;

import it.polimi.se2018.controller.BadFormattedPatternFileException;
import it.polimi.se2018.controller.NoPatternsFoundInFileSystemException;
import it.polimi.se2018.controller.WindowPatternManager;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static it.polimi.se2018.model.DiceColors.*;
import static org.junit.Assert.*;


/**
 * @author Jacopo Pio Gargano
 */
public class TestDiagonalsPublicObjectiveCard {

    private static WindowPatternManager windowPatternManager;

    private static WindowPattern diagonalsWP;
    private static WindowPattern rightDiagonalsWP;
    private static WindowPattern leftDiagonalsWP;
    private static WindowPattern twoDiceWP;
    private static WindowPattern nullWP;
    private static WindowPattern emptyWP;

    private static PublicObjectiveCard diagonalsPublicObjectiveCard;
    private static PublicObjectiveCard colorDiagonalsPublicObjectiveCard;

    private static int diagonalsScore;
    private static int rightDiagonalsScore;
    private static int leftDiagonalsScore;

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

            diagonalsWP = windowPatternManager.getPatterns(1).get(0);

            diagonalsWP.putDiceOnCell(new Dice(BLUE), 0, 0);
            diagonalsWP.putDiceOnCell(new Dice(RED), 0, 1);
            diagonalsWP.putDiceOnCell(new Dice(GREEN), 0, 2);
            diagonalsWP.putDiceOnCell(new Dice(YELLOW), 0, 3);

            diagonalsWP.putDiceOnCell(new Dice(RED), 1, 0);
            diagonalsWP.putDiceOnCell(new Dice(PURPLE), 1, 1);
            diagonalsWP.putDiceOnCell(new Dice(YELLOW), 1, 2);
            diagonalsWP.putDiceOnCell(new Dice(PURPLE), 1, 3);
            diagonalsWP.putDiceOnCell(new Dice(YELLOW), 1, 4);

            diagonalsWP.putDiceOnCell(new Dice(YELLOW), 2, 1);
            diagonalsWP.putDiceOnCell(new Dice(PURPLE), 2, 2);
            diagonalsWP.putDiceOnCell(new Dice(YELLOW), 2, 3);
            diagonalsWP.putDiceOnCell(new Dice(PURPLE), 2, 4);

            diagonalsWP.putDiceOnCell(new Dice(YELLOW), 3, 0);
            diagonalsWP.putDiceOnCell(new Dice(PURPLE), 3, 3);


            rightDiagonalsWP = windowPatternManager.getPatterns(1).get(0);

            rightDiagonalsWP.putDiceOnCell(new Dice(BLUE), 0, 0);
            rightDiagonalsWP.putDiceOnCell(new Dice(GREEN), 0, 2);
            rightDiagonalsWP.putDiceOnCell(new Dice(YELLOW), 0, 3);

            rightDiagonalsWP.putDiceOnCell(new Dice(RED), 1, 0);
            rightDiagonalsWP.putDiceOnCell(new Dice(PURPLE), 1, 1);
            rightDiagonalsWP.putDiceOnCell(new Dice(RED), 1, 3);
            rightDiagonalsWP.putDiceOnCell(new Dice(YELLOW), 1, 4);

            rightDiagonalsWP.putDiceOnCell(new Dice(YELLOW), 2, 1);
            rightDiagonalsWP.putDiceOnCell(new Dice(PURPLE), 2, 2);
            rightDiagonalsWP.putDiceOnCell(new Dice(RED), 2, 4);

            rightDiagonalsWP.putDiceOnCell(new Dice(YELLOW), 3, 2);
            rightDiagonalsWP.putDiceOnCell(new Dice(PURPLE), 3, 3);


            leftDiagonalsWP = windowPatternManager.getPatterns(1).get(0);

            leftDiagonalsWP.putDiceOnCell(new Dice(RED), 0, 0);
            leftDiagonalsWP.putDiceOnCell(new Dice(GREEN), 0, 2);
            leftDiagonalsWP.putDiceOnCell(new Dice(RED), 0, 3);
            leftDiagonalsWP.putDiceOnCell(new Dice(RED), 0, 4);

            leftDiagonalsWP.putDiceOnCell(new Dice(GREEN), 1, 1);
            leftDiagonalsWP.putDiceOnCell(new Dice(RED), 1, 2);
            leftDiagonalsWP.putDiceOnCell(new Dice(BLUE), 1, 3);
            leftDiagonalsWP.putDiceOnCell(new Dice(YELLOW), 1, 4);

            leftDiagonalsWP.putDiceOnCell(new Dice(GREEN), 2, 0);
            leftDiagonalsWP.putDiceOnCell(new Dice(RED), 2, 1);
            leftDiagonalsWP.putDiceOnCell(new Dice(YELLOW), 2, 3);
            leftDiagonalsWP.putDiceOnCell(new Dice(RED), 2, 4);

            leftDiagonalsWP.putDiceOnCell(new Dice(RED), 3, 0);


            twoDiceWP = windowPatternManager.getPatterns(1).get(0);

            twoDiceWP.putDiceOnCell(new Dice(RED), 0, 0);

            twoDiceWP.putDiceOnCell(new Dice(RED), 1, 1);




            nullWP = null;

            emptyWP = windowPatternManager.getPatterns(1).get(0);

        }catch (BadFormattedPatternFileException e){
            e.printStackTrace();
            fail();
        }
    }

    @BeforeClass
    public static void initializeCards(){
        diagonalsPublicObjectiveCard = DiagonalsPublicObjectiveCard.createTestInstance();

        colorDiagonalsPublicObjectiveCard = new DiagonalsPublicObjectiveCard(null,null,null,
                Dice::getColor);
    }

    @BeforeClass
    public static void initializeScore(){
        diagonalsScore = 13;
        rightDiagonalsScore = 9;
        leftDiagonalsScore = 9;
    }

    @Before
    public void resetScore(){
        testScore = 0;
    }

    @Test
    public void testCalculateScoreOfNullWindowPattern(){
        try {
            testScore = diagonalsPublicObjectiveCard.calculateScore(nullWP);
            fail();
        }catch (IllegalArgumentException e){
            assertNull(nullWP);
        }
    }

    @Test
    public void testCalculateScoreOfEmptyWindowPattern(){
        testScore = diagonalsPublicObjectiveCard.calculateScore(emptyWP);
        assertEquals(0, testScore);
    }

    @Test
    public void testCalculateScoreDiagonals(){
        testScore = colorDiagonalsPublicObjectiveCard.calculateScore(diagonalsWP);
        assertEquals(diagonalsScore, testScore);
    }

    @Test
    public void testCalculateScoreRightDiagonals(){
        testScore = colorDiagonalsPublicObjectiveCard.calculateScore(rightDiagonalsWP);
        assertEquals(rightDiagonalsScore, testScore);
    }

    @Test
    public void testCalculateScoreLeftDiagonals(){
        testScore = colorDiagonalsPublicObjectiveCard.calculateScore(leftDiagonalsWP);
        assertEquals(leftDiagonalsScore, testScore);
    }

    @Test
    public void testCalculateScoreCellPair(){
        testScore = colorDiagonalsPublicObjectiveCard.calculateScore(twoDiceWP);
        assertEquals(2, testScore);
    }

    @Test
    public void testCopy() {
        assertNotNull(diagonalsPublicObjectiveCard.copy());
    }

}