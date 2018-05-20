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

public class TestPrivateObjectiveCard {


    private static WindowPatternManager windowPatternManager;
    private static WindowPattern wp;
    private static WindowPattern oneDiceWindowPattern;
    private static WindowPattern nullWP;
    private static WindowPattern emptyWP;


    private static ObjectiveCardManager cardManager;
    private static PrivateObjectiveCard oneDicePrivateObjectiveCard;
    private static PrivateObjectiveCard privateObjectiveCard;
    private static PrivateObjectiveCard redPrivateObjectiveCard;
    private static PrivateObjectiveCard yellowPrivateObjectiveCard;
    private static PrivateObjectiveCard greenPrivateObjectiveCard;
    private static PrivateObjectiveCard purplePrivateObjectiveCard;
    private static PrivateObjectiveCard bluePrivateObjectiveCard;

    private static int redScore;
    private static int yellowScore;
    private static int greenScore;
    private static int blueScore;
    private static int purpleScore;
    private static int oneDiceWindowPatternScore;

    private static Dice uniqueDiceOnWindowPattern = new Dice(RED, 3);

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
            wp.putDiceOnCell(new Dice(YELLOW, 1), 0, 1);
            wp.putDiceOnCell(new Dice(PURPLE, 3), 0, 2);
            wp.putDiceOnCell(new Dice(BLUE, 2), 0, 3);
            wp.putDiceOnCell(new Dice(GREEN, 4), 0, 4);

            wp.putDiceOnCell(new Dice(RED, 3), 1, 3);

            wp.putDiceOnCell(new Dice(PURPLE, 5), 2, 0);
            wp.putDiceOnCell(new Dice(YELLOW, 6), 2, 1);
            wp.putDiceOnCell(new Dice(BLUE, 3), 2, 2);
            wp.putDiceOnCell(new Dice(GREEN, 2), 2, 3);
            wp.putDiceOnCell(new Dice(RED, 4), 2, 4);

            wp.putDiceOnCell(new Dice(YELLOW, 5), 3, 3);


            oneDiceWindowPattern = windowPatternManager.getPatterns(1).get(0);

            oneDiceWindowPattern.putDiceOnCell(uniqueDiceOnWindowPattern,1,1);


            nullWP = null;

            emptyWP = windowPatternManager.getPatterns(1).get(0);

        }catch (BadFormattedPatternFileException e){
            e.printStackTrace();
            fail();
        }
    }

    @BeforeClass
    public static void initializeCards(){

        cardManager = new ObjectiveCardManager();

        privateObjectiveCard = PrivateObjectiveCard.createTestInstance();

        redPrivateObjectiveCard = new PrivateObjectiveCard(null,null,null, RED);
        yellowPrivateObjectiveCard = new PrivateObjectiveCard(null,null,null, YELLOW);
        greenPrivateObjectiveCard = new PrivateObjectiveCard(null,null,null, GREEN);
        purplePrivateObjectiveCard = new PrivateObjectiveCard(null,null,null, PURPLE);
        bluePrivateObjectiveCard = new PrivateObjectiveCard(null,null,null, BLUE);

        oneDicePrivateObjectiveCard = new PrivateObjectiveCard
                (null,null,null, uniqueDiceOnWindowPattern.getColor());
    }

    @BeforeClass
    public static void initializeScores() {
        redScore = 8;
        yellowScore = 12;
        greenScore = 6;
        purpleScore = 8;
        blueScore = 5;

        oneDiceWindowPatternScore = uniqueDiceOnWindowPattern.getValue();
    }



    @Before
    public void resetScore(){
        testScore = 0;
    }



    @Test
    public void testCalculateScoreOfNullWindowPattern(){
        try {
            testScore = privateObjectiveCard.calculateScore(nullWP);
            fail();
        }catch (IllegalArgumentException e){
            assertNull(nullWP);
        }
    }

    @Test
    public void testCalculateScoreOfEmptyWindowPattern(){
        testScore = privateObjectiveCard.calculateScore(emptyWP);
        assertEquals(0, testScore);
    }

    @Test
    public void testCalculateScoreOfOneDice(){
        testScore = oneDicePrivateObjectiveCard.calculateScore(oneDiceWindowPattern);
        assertEquals(oneDiceWindowPatternScore, testScore);
    }

    @Test
    public void testCalculateScoreRed() {
        testScore = redPrivateObjectiveCard.calculateScore(wp);
        assertEquals(redScore, testScore);
    }

    @Test
    public void testCalculateScoreYellow() {
        testScore = yellowPrivateObjectiveCard.calculateScore(wp);
        assertEquals(yellowScore, testScore);
    }

    @Test
    public void testCalculateScoreGreen() {
        testScore = greenPrivateObjectiveCard.calculateScore(wp);
        assertEquals(greenScore, testScore);
    }

    @Test
    public void testCalculateScorePurple() {
        testScore = purplePrivateObjectiveCard.calculateScore(wp);
        assertEquals(purpleScore, testScore);
    }

    @Test
    public void testCalculateScoreBlue() {
        testScore = bluePrivateObjectiveCard.calculateScore(wp);
        assertEquals(blueScore, testScore);
    }

    //integration test between ObjectiveCardManager and CalculateScore. ObjectiveCardManager could return a card with
    //NOCOLOR as color. TODO: Must be run with ObjectiveCardManager Tests
    @Test
    public void testCalculateScoreWithCardFromCardManager() {

        privateObjectiveCard = cardManager.getPrivateObjectiveCard();
        DiceColors cardColor = privateObjectiveCard.getColor();

        testScore = privateObjectiveCard.calculateScore(wp);


        switch (cardColor) {
            case RED:
                assertEquals(redScore, testScore);
                break;
            case YELLOW:
                assertEquals(yellowScore, testScore);
                break;
            case GREEN:
                assertEquals(greenScore, testScore);
                break;
            case PURPLE:
                assertEquals(purpleScore, testScore);
                break;
            case BLUE:
                assertEquals(blueScore, testScore);
                break;
            default:
                fail();
        }

    }

    @Test
    public void testCopy(){
        assertNotNull(privateObjectiveCard.copy());
    }


}

