package it.polimi.se2018.model;

import it.polimi.se2018.controller.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class PrivateObjectiveCardTest {


    private static WindowPatternManager windowPatternManager;
    private static WindowPattern wp;
    private static WindowPattern oneDiceWindowPattern;
    private static WindowPattern nullWP = null;
    private static WindowPattern emptyWP;
    private static PrivateObjectiveCard oneDicePrivateObjectiveCard;

    private static ObjectiveCardManager cardManager;
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
    private static int scoreOfOneDiceWindowPattern;

    private int testScore = 0;

    @BeforeClass
    public static void buildPattern(){
        try {
            windowPatternManager = new WindowPatternManager();
        }catch (NoPatternsFoundInFileSystemException e){
            e.printStackTrace();
        }

        try {

            wp = windowPatternManager.getPatterns(1).get(0);

            wp.putDiceOnCell(new Dice(DiceColors.RED, 1), 0, 0);
            wp.putDiceOnCell(new Dice(DiceColors.YELLOW, 1), 0, 1);
            wp.putDiceOnCell(new Dice(DiceColors.PURPLE, 3), 0, 2);
            wp.putDiceOnCell(new Dice(DiceColors.BLUE, 2), 0, 3);
            wp.putDiceOnCell(new Dice(DiceColors.GREEN, 4), 0, 4);

            wp.putDiceOnCell(new Dice(DiceColors.RED, 3), 1, 3);

            wp.putDiceOnCell(new Dice(DiceColors.PURPLE, 5), 2, 0);
            wp.putDiceOnCell(new Dice(DiceColors.YELLOW, 6), 2, 1);
            wp.putDiceOnCell(new Dice(DiceColors.BLUE, 3), 2, 2);
            wp.putDiceOnCell(new Dice(DiceColors.GREEN, 2), 2, 3);
            wp.putDiceOnCell(new Dice(DiceColors.RED, 4), 2, 4);

            wp.putDiceOnCell(new Dice(DiceColors.YELLOW, 5), 3, 3);

            redScore = 8;
            yellowScore = 12;
            greenScore = 6;
            purpleScore = 8;
            blueScore = 5;


            oneDiceWindowPattern = windowPatternManager.getPatterns(1).get(0);

            Dice uniqueDiceOnWindowPattern = new Dice(DiceColors.RED, 3);
            oneDiceWindowPattern.putDiceOnCell(uniqueDiceOnWindowPattern,1,1);

            scoreOfOneDiceWindowPattern = uniqueDiceOnWindowPattern.getValue();
            oneDicePrivateObjectiveCard = new PrivateObjectiveCard
                    (null,null,null, uniqueDiceOnWindowPattern.getColor());


            emptyWP = windowPatternManager.getPatterns(1).get(0);


            cardManager = new ObjectiveCardManager();

            privateObjectiveCard = new PrivateObjectiveCard();
            redPrivateObjectiveCard = new PrivateObjectiveCard(null,null,null,DiceColors.RED);
            yellowPrivateObjectiveCard = new PrivateObjectiveCard(null,null,null,DiceColors.YELLOW);
            greenPrivateObjectiveCard = new PrivateObjectiveCard(null,null,null,DiceColors.GREEN);
            purplePrivateObjectiveCard = new PrivateObjectiveCard(null,null,null,DiceColors.PURPLE);
            bluePrivateObjectiveCard = new PrivateObjectiveCard(null,null,null,DiceColors.BLUE);

        }catch (BadFormattedPatternFileException e){
            fail();
            e.printStackTrace();
        }
    }


    @Before
    public void resetScore(){
        testScore = 0;
    }


    //integration test between ObjectiveCardManager and CalculateScore. ObjectiveCardManager could return a card with
    //NOCOLOR as color. TODO: Must be run with ObjectiveCardManager Tests
    @Test
    public void calculateScore(){

        privateObjectiveCard = cardManager.getPrivateObjectiveCard();
        DiceColors cardColor = privateObjectiveCard.getColor();

        int score = privateObjectiveCard.calculateScore(wp);


        switch(cardColor){
            case RED:
                assertEquals(score, redScore);
                break;
            case YELLOW:
                assertEquals(score, yellowScore);
                break;
            case GREEN:
                assertEquals(score, greenScore);
                break;
            case PURPLE:
                assertEquals(score, purpleScore);
                break;
            case BLUE:
                assertEquals(score, blueScore);
                break;
            default:
                fail();
        }

    }

    @Test
    public void calculateScoreOfNullWindowPattern(){
        try {
            privateObjectiveCard.calculateScore(nullWP);
            fail();
        }catch (IllegalArgumentException e){
            assertNull(nullWP);
        }
    }

    @Test
    public void calculateScoreOfEmptyWindowPattern(){
        testScore = privateObjectiveCard.calculateScore(emptyWP);
        assertEquals(testScore, 0);
    }

    @Test
    public void calculateScoreOfOneRedDice(){
        testScore = oneDicePrivateObjectiveCard.calculateScore(oneDiceWindowPattern);
        assertEquals(testScore, scoreOfOneDiceWindowPattern);
    }

    @Test
    public void calculateScoreRed() {
        testScore = redPrivateObjectiveCard.calculateScore(wp);
        assertEquals(testScore, redScore);
    }

    @Test
    public void calculateScoreYellow() {
        testScore = yellowPrivateObjectiveCard.calculateScore(wp);
        assertEquals(testScore, yellowScore);
    }

    @Test
    public void calculateScoreGreen() {
        testScore = greenPrivateObjectiveCard.calculateScore(wp);
        assertEquals(testScore, greenScore);
    }

    @Test
    public void calculateScorePurple() {
        testScore = purplePrivateObjectiveCard.calculateScore(wp);
        assertEquals(testScore, purpleScore);
    }

    @Test
    public void calculateScoreBlue() {
        testScore = bluePrivateObjectiveCard.calculateScore(wp);
        assertEquals(testScore, blueScore);
    }

}

