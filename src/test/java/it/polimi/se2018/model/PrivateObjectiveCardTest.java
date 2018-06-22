package it.polimi.se2018.model;

import it.polimi.se2018.controller.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static it.polimi.se2018.model.DiceColor.*;
import static org.junit.Assert.*;

/**
 * Test for {@link PrivateObjectiveCard} class
 *
 * @author Jacopo Pio Gargano
 */

public class PrivateObjectiveCardTest {

    private static WindowPattern windowPattern;
    private static WindowPattern oneDiceWindowPattern;
    private static WindowPattern emptyWP;

    private static PrivateObjectiveCard privateObjectiveCard;
    private static PrivateObjectiveCard redPrivateObjectiveCard;
    private static PrivateObjectiveCard yellowPrivateObjectiveCard;
    private static PrivateObjectiveCard greenPrivateObjectiveCard;
    private static PrivateObjectiveCard purplePrivateObjectiveCard;
    private static PrivateObjectiveCard bluePrivateObjectiveCard;
    private static PrivateObjectiveCard oneDicePatternPrivateObjectiveCard;

    private static final int redScore = 8;
    private static final int yellowScore = 12;
    private static final int greenScore = 6;
    private static final int blueScore = 5;
    private static final int purpleScore = 8;
    private static final int oneDiceWindowPatternScore = 3;

    private static final Dice uniqueDiceOnWindowPattern = new Dice(RED, 3);

    /**
     * Creates a new Window Pattern Manager and creates the specific patterns of the players for the test
     */
    @BeforeClass
    public static void buildWindowPatterns(){

        try{
            WindowPatternManager windowPatternManager = new WindowPatternManager();

            windowPattern = new ArrayList<>(windowPatternManager.getPairsOfPatterns(1)).get(0);

            Player player = new Player("", new PrivateObjectiveCard("","","",RED));
            player.setWindowPattern(windowPattern);
            windowPattern.setOwner(player);

            windowPattern.putDiceOnCell(new Dice(RED, 1), 0, 0);
            windowPattern.putDiceOnCell(new Dice(YELLOW, 1), 0, 1);
            windowPattern.putDiceOnCell(new Dice(PURPLE, 3), 0, 2);
            windowPattern.putDiceOnCell(new Dice(BLUE, 2), 0, 3);
            windowPattern.putDiceOnCell(new Dice(GREEN, 4), 0, 4);

            windowPattern.putDiceOnCell(new Dice(RED, 3), 1, 3);

            windowPattern.putDiceOnCell(new Dice(PURPLE, 5), 2, 0);
            windowPattern.putDiceOnCell(new Dice(YELLOW, 6), 2, 1);
            windowPattern.putDiceOnCell(new Dice(BLUE, 3), 2, 2);
            windowPattern.putDiceOnCell(new Dice(GREEN, 2), 2, 3);
            windowPattern.putDiceOnCell(new Dice(RED, 4), 2, 4);

            windowPattern.putDiceOnCell(new Dice(YELLOW, 5), 3, 3);


            oneDiceWindowPattern = new ArrayList<>(windowPatternManager.getPairsOfPatterns(1)).get(0);

            player.setWindowPattern(oneDiceWindowPattern);
            oneDiceWindowPattern.setOwner(player);

            oneDiceWindowPattern.putDiceOnCell(uniqueDiceOnWindowPattern,1,1);


            emptyWP = new ArrayList<>(windowPatternManager.getPairsOfPatterns(1)).get(0);

        }catch (BadFormattedPatternFileException | NoPatternsFoundInFileSystemException e){
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Creates the the instances of {@link PrivateObjectiveCard} used in the tests
     */
    @BeforeClass
    public static void initializeCards(){

        privateObjectiveCard = new PrivateObjectiveCard("","","",RED);

        redPrivateObjectiveCard = new PrivateObjectiveCard("","","", RED);
        yellowPrivateObjectiveCard = new PrivateObjectiveCard("","","", YELLOW);
        greenPrivateObjectiveCard = new PrivateObjectiveCard("","","", GREEN);
        purplePrivateObjectiveCard = new PrivateObjectiveCard("","","", PURPLE);
        bluePrivateObjectiveCard = new PrivateObjectiveCard("","","", BLUE);

        oneDicePatternPrivateObjectiveCard = new PrivateObjectiveCard
                (null,null,null, uniqueDiceOnWindowPattern.getColor());
    }

    /**
     * Tests the class constructor
     */
    @Test
    public void testConstructor(){
        assertNotNull(redPrivateObjectiveCard);
    }

    /**
     * Tests the impossibility of calculating the score of a null window pattern
     * @see PrivateObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreOfNullWindowPattern(){
        try {
            privateObjectiveCard.calculateScore(null);
            fail();
        }catch (NullPointerException e){}
    }

    /**
     * Tests the scoring of an empty window pattern. Score must be 0
     * @see PrivateObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreOfEmptyWindowPattern(){
        int score = privateObjectiveCard.calculateScore(emptyWP);
        assertEquals(0, score);
    }

    /**
     * Tests the scoring of a window pattern with one dice
     * @see PrivateObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreOfOneDice(){
        int score = oneDicePatternPrivateObjectiveCard.calculateScore(oneDiceWindowPattern);
        assertEquals(oneDiceWindowPatternScore, score);
    }

    /**
     * Tests the scoring of a generic window pattern by a Red {@link PrivateObjectiveCard}
     * @see PrivateObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreRed() {
        int score = redPrivateObjectiveCard.calculateScore(windowPattern);
        assertEquals(redScore, score);
    }

    /**
     * Tests the scoring of a generic window pattern by a Yellow {@link PrivateObjectiveCard}
     * @see PrivateObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreYellow() {
        int score = yellowPrivateObjectiveCard.calculateScore(windowPattern);
        assertEquals(yellowScore, score);
    }

    /**
     * Tests the scoring of a generic window pattern by a Green {@link PrivateObjectiveCard}
     * @see PrivateObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreGreen() {
        int score = greenPrivateObjectiveCard.calculateScore(windowPattern);
        assertEquals(greenScore, score);
    }

    /**
     * Tests the scoring of a generic window pattern by a Purple {@link PrivateObjectiveCard}
     * @see PrivateObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScorePurple() {
        int score = purplePrivateObjectiveCard.calculateScore(windowPattern);
        assertEquals(purpleScore, score);
    }

    /**
     * Tests the scoring of a generic window pattern by a Blue {@link PrivateObjectiveCard}
     * @see PrivateObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreBlue() {
        int score = bluePrivateObjectiveCard.calculateScore(windowPattern);
        assertEquals(blueScore, score);
    }

    /**
     * Tests the copy method of {@link PrivateObjectiveCard} (copy must not be null)
     * @see PrivateObjectiveCard#copy()
     */
    @Test
    public void testCopy(){
        assertNotNull(privateObjectiveCard.copy());
    }

}
