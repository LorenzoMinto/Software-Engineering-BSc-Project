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
    private ObjectiveCardManager manager = new ObjectiveCardManager();
    private PrivateObjectiveCard privateObjectiveCard;

    private static int redScore;
    private static int yellowScore;
    private static int greenScore;
    private static int blueScore;
    private static int purpleScore;

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

        }catch (BadFormattedPatternFileException e){
            fail();
            e.printStackTrace();
        }
    }



    //integration test between ObjectiveCardManager and CalculateScore. ObjectiveCardManager could return a card with
    //NOCOLOR as color. TODO: Must be run with ObjectiveCardManager Tests
    @Test
    public void calculateScore(){

        privateObjectiveCard = manager.getPrivateObjectiveCard();
        DiceColors cardColor = privateObjectiveCard.getColor();

        int score = privateObjectiveCard.calculateScore(wp);


        switch(cardColor){
            case RED:
                assertEquals("Score should be the same as redScore",
                        score, redScore);
                break;
            case YELLOW:
                assertEquals("Score should be the same as yellowScore",
                        score, yellowScore);
                break;
            case GREEN:
                assertEquals("Score should be the same as greenScore",
                        score, greenScore);
                break;
            case PURPLE:
                assertEquals("Score should be the same as purpleScore",
                        score, purpleScore);
                break;
            case BLUE:
                assertEquals("Score should be the same as blueScore",
                        score, blueScore);
                break;
            default:
                fail();
        }

    }

    @Test
    public void calculateScoreWithNullWindowPattern(){
        WindowPattern nullWindowPattern = null;
        privateObjectiveCard = manager.getPrivateObjectiveCard();
        try {
            privateObjectiveCard.calculateScore(nullWindowPattern);
            fail();
        }catch (IllegalArgumentException e){
            assertNull(nullWindowPattern);
        }


    }


    @Test
    public void calculateScoreRed() {
        privateObjectiveCard = new PrivateObjectiveCard(null,null,null,DiceColors.RED);

        assertThat(privateObjectiveCard.calculateScore(wp), is(redScore));

    }

    @Test
    public void calculateScoreYellow() {
        privateObjectiveCard = new PrivateObjectiveCard(null,null,null,DiceColors.YELLOW);
        assertThat(privateObjectiveCard.calculateScore(wp), is(yellowScore));

    }
    @Test
    public void calculateScoreGreen() {
        privateObjectiveCard = new PrivateObjectiveCard(null, null,null, DiceColors.GREEN);

        assertThat(privateObjectiveCard.calculateScore(wp), is(greenScore));

    }
    @Test
    public void calculateScorePurple() {
        privateObjectiveCard = new PrivateObjectiveCard(null, null,null, DiceColors.PURPLE);

        assertThat(privateObjectiveCard.calculateScore(wp), is(purpleScore));

    }
    @Test
    public void calculateScoreBlue() {
        privateObjectiveCard = new PrivateObjectiveCard(null, null,null, DiceColors.BLUE);

        assertThat(privateObjectiveCard.calculateScore(wp), is(blueScore));

    }


}

