package it.polimi.se2018.model;

import it.polimi.se2018.controller.BadFormattedPatternFileException;
import it.polimi.se2018.controller.NoPatternsFoundInFileSystemException;
import it.polimi.se2018.controller.WindowPatternManager;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static it.polimi.se2018.model.DiceColors.*;
import static org.junit.Assert.*;

/**
 * @author Jacopo Pio Gargano
 */

public class SetPublicObjectiveCardTest {

    private static WindowPatternManager windowPatternManager;

    private static WindowPattern wp;
    private static WindowPattern nullWP;
    private static WindowPattern emptyWP;

    private static PublicObjectiveCard setPublicObjectiveCard;
    private static PublicObjectiveCard oneTwoPublicObjectiveCard;
    private static PublicObjectiveCard threeFourPublicObjectiveCard;
    private static PublicObjectiveCard fiveSixPublicObjectiveCard;
    private static PublicObjectiveCard allValuesPublicObjectiveCard;
    private static PublicObjectiveCard allColorsPublicObjectiveCard;

    private static HashSet<Object> oneTwoSet;
    private static HashSet<Object> threeFourSet;
    private static HashSet<Object> fiveSixSet;
    private static HashSet<Object> allValuesSet;
    private static HashSet<Object> allColorsSet;

    private static int oneTwoSetScore;
    private static int threeFourSetScore;
    private static int fiveSixSetScore;
    private static int allValuesSetScore;
    private static int allColorsSetScore;

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

            wp = new ArrayList<>(windowPatternManager.getCouplesOfPatterns(1)).get(0);

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

            emptyWP = new ArrayList<>(windowPatternManager.getCouplesOfPatterns(1)).get(0);

        }catch (BadFormattedPatternFileException e){
            e.printStackTrace();
            fail();
        }
    }

    @BeforeClass
    public static void initializeSets(){

        oneTwoSet = new HashSet<>();
        oneTwoSet.add(1);
        oneTwoSet.add(2);

        threeFourSet = new HashSet<>();
        threeFourSet.add(3);
        threeFourSet.add(4);

        fiveSixSet = new HashSet<>();
        fiveSixSet.add(5);
        fiveSixSet.add(6);

        allValuesSet = new HashSet<>();
        for(int i=1; i <= 6; i++){
            allValuesSet.add(i);
        }

        allColorsSet = new HashSet<>();
        for(DiceColors color: values()){
            if(!color.equals(NOCOLOR)) {
                allColorsSet.add(color);
            }
        }
    }

    @BeforeClass
    public static void initializeCards(){
        setPublicObjectiveCard = SetPublicObjectiveCard.createTestInstance();

        oneTwoPublicObjectiveCard = new SetPublicObjectiveCard(null,null,null,
                oneTwoSet, Dice::getValue,2);
        threeFourPublicObjectiveCard = new SetPublicObjectiveCard(null,null,null,
                threeFourSet, Dice::getValue,2);
        fiveSixPublicObjectiveCard =  new SetPublicObjectiveCard(null,null,null,
                fiveSixSet, Dice::getValue,2);
        allValuesPublicObjectiveCard = new SetPublicObjectiveCard(null,null,null,
                allValuesSet, Dice::getValue,5);
        allColorsPublicObjectiveCard = new SetPublicObjectiveCard(null,null,null,
                allColorsSet, Dice::getColor,4);
    }

    @BeforeClass
    public static void initializeScores() {
        oneTwoSetScore = 2 * 1;
        threeFourSetScore = 2 * 3;
        fiveSixSetScore = 2 * 1;
        allValuesSetScore = 5 * 1;
        allColorsSetScore = 4 * 2;
    }

    @Before
    public void resetScore(){
        testScore = 0;
    }




    @Test
    public void testCalculateScoreOfNullWindowPattern(){
        try {
            testScore = setPublicObjectiveCard.calculateScore(nullWP);
            fail();
        }catch (NullPointerException e){}
    }

    @Test
    public void testCalculateScoreOfEmptyWindowPattern(){
        testScore = setPublicObjectiveCard.calculateScore(emptyWP);
        assertEquals(0, testScore);
    }


    @Test
    public void testCalculateScoreOneTwoSet() {
        testScore = oneTwoPublicObjectiveCard.calculateScore(wp);
        assertEquals(oneTwoSetScore, testScore);
    }

    @Test
    public void testCalculateScoreThreeFourSet() {
        testScore = threeFourPublicObjectiveCard.calculateScore(wp);
        assertEquals(threeFourSetScore, testScore);
    }

    @Test
    public void testCalculateScoreFiveSixSet() {
        testScore = fiveSixPublicObjectiveCard.calculateScore(wp);
        assertEquals(fiveSixSetScore, testScore);
    }

    @Test
    public void testCalculateScoreAllValuesSet() {
        testScore = allValuesPublicObjectiveCard.calculateScore(wp);
        assertEquals(allValuesSetScore, testScore);
    }

    @Test
    public void testCalculateScoreAllColorsSet() {
        testScore = allColorsPublicObjectiveCard.calculateScore(wp);
        assertEquals(allColorsSetScore, testScore);
    }

    @Test
    public void testToString(){
        setPublicObjectiveCard = new SetPublicObjectiveCard(
                "title", "description", null, allValuesSet, Dice::getValue, 5);
        String toString = setPublicObjectiveCard.toString();
        String expectedString = "title"+System.lineSeparator()+"description"+System.lineSeparator()+"Multiplier: 5"+System.lineSeparator();
        assertEquals(expectedString, toString);
    }

    @Test
    public void testCopy(){
        assertNotNull(setPublicObjectiveCard.copy());
    }
}