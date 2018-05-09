package it.polimi.se2018.model;

import it.polimi.se2018.controller.*;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class RowsColumnsPublicObjectiveCardTest {

    private static WindowPatternManager windowPatternManager;
    private static WindowPattern wp;
    private PrivateObjectiveCard privateObjectiveCard;

    private static int scoreRowsColor;
    private static int scoreColumnsColor;
    private static int scoreRowsValue;
    private static int scoreColumnsValue;


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

        }catch (BadFormattedPatternFileException e){
            e.printStackTrace();
        }

    }

    @Test
    public void calculateScoreRowsColor() {
        PublicObjectiveCard publicObjectiveCard = new RowsColumnsPublicObjectiveCard(null,null,
                null, Dice::getColor, 6, true);

        int score = publicObjectiveCard.calculateScore(wp);

        assertEquals(score,scoreRowsColor);
    }

    @Test
    public void calculateScoreColumnsColor() {
        PublicObjectiveCard publicObjectiveCard = new RowsColumnsPublicObjectiveCard(null,null,null, Dice::getColor, 5, false);
        int score = publicObjectiveCard.calculateScore(wp);

        assertEquals(score,scoreColumnsColor);
    }

    @Test
    public void calculateScoreRowsValue() {
        PublicObjectiveCard publicObjectiveCard = new RowsColumnsPublicObjectiveCard(null,null,null, Dice::getValue, 5, true);
        int score = publicObjectiveCard.calculateScore(wp);

        assertEquals(score,scoreRowsValue);
    }

    @Test
    public void calculateScoreColumnsValue() {
        PublicObjectiveCard publicObjectiveCard = new RowsColumnsPublicObjectiveCard(null,null,null, Dice::getValue, 4, false);
        int score = publicObjectiveCard.calculateScore(wp);

        assertEquals(score,scoreColumnsValue);
    }
}