package it.polimi.se2018.model;

import it.polimi.se2018.controller.BadFormattedPatternFileException;
import it.polimi.se2018.controller.NoPatternsFoundInFileSystemException;
import it.polimi.se2018.controller.WindowPatternManager;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static it.polimi.se2018.model.DiceColor.*;
import static org.junit.Assert.*;

/**
 * Test for {@link SetPublicObjectiveCard} class
 *
 * @author Jacopo Pio Gargano
 */

public class SetPublicObjectiveCardTest {

    private static WindowPattern windowPattern;
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

    private static final int oneTwoSetScore = 2;
    private static final int threeFourSetScore = 6;
    private static final int fiveSixSetScore = 2;
    private static final int allValuesSetScore = 5;
    private static final int allColorsSetScore = 8;

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
            windowPattern.putDiceOnCell(new Dice(GREEN, 1), 3, 4);

            emptyWP = new ArrayList<>(windowPatternManager.getPairsOfPatterns(1)).get(0);



        }catch (BadFormattedPatternFileException | NoPatternsFoundInFileSystemException e){
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Initializes the sets that are passed as parameters to the constructors of {@link SetPublicObjectiveCard} 
     */
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
        for(DiceColor color: values()){
            if(!color.equals(NOCOLOR)) {
                allColorsSet.add(color);
            }
        }
    }

    /**
     * Creates the the instances of {@link SetPublicObjectiveCard} used in the tests
     * @see SetPublicObjectiveCard#SetPublicObjectiveCard(String, String, String, Set, Function, int)
     */
    @BeforeClass
    public static void initializeCards(){
        setPublicObjectiveCard = new SetPublicObjectiveCard(null,null, null, allColorsSet, Dice::getColor, 3);

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

    /**
     * Tests the class constructor
     */
    @Test
    public void testConstructor(){
        assertNotNull(oneTwoPublicObjectiveCard);
    }

    /**
     * Tests the impossibility of calculating the score of a null window pattern
     * @see SetPublicObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreOfNullWindowPattern(){
        try { 
            setPublicObjectiveCard.calculateScore(null);
            fail();
        }catch (NullPointerException e){}
    }


    /**
     * Tests the scoring of an empty window pattern. Score must be 0
     * @see SetPublicObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreOfEmptyWindowPattern(){
        int score = setPublicObjectiveCard.calculateScore(emptyWP);
        assertEquals(0, score);
    }


    /**
     * Tests the scoring of a window pattern by a OneTwo {@link SetPublicObjectiveCard}
     * @see SetPublicObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreOneTwoSet() {
        int score = oneTwoPublicObjectiveCard.calculateScore(windowPattern);
        assertEquals(oneTwoSetScore, score);
    }

    /**
     * Tests the scoring of a window pattern by a ThreeFour {@link SetPublicObjectiveCard}
     * @see SetPublicObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreThreeFourSet() {
        int score = threeFourPublicObjectiveCard.calculateScore(windowPattern);
        assertEquals(threeFourSetScore, score);
    }

    /**
     * Tests the scoring of a window pattern by a FiveSix {@link SetPublicObjectiveCard}
     * @see SetPublicObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreFiveSixSet() {
        int score = fiveSixPublicObjectiveCard.calculateScore(windowPattern);
        assertEquals(fiveSixSetScore, score);
    }

    /**
     * Tests the scoring of a window pattern by an AllValues {@link SetPublicObjectiveCard}
     * @see SetPublicObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreAllValuesSet() {
        int score = allValuesPublicObjectiveCard.calculateScore(windowPattern);
        assertEquals(allValuesSetScore, score);
    }

    /**
     * Tests the scoring of a window pattern by an AllColors {@link SetPublicObjectiveCard}
     * @see SetPublicObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreAllColorsSet() {
        int score = allColorsPublicObjectiveCard.calculateScore(windowPattern);
        assertEquals(allColorsSetScore, score);
    }

    /**
     * Tests the toString method of {@link SetPublicObjectiveCard}
     * @see PrivateObjectiveCard#toString()
     */
    @Test
    public void testToString(){
        setPublicObjectiveCard = new SetPublicObjectiveCard(
                "title", "description", null, allValuesSet, Dice::getValue, 5);
        String toString = setPublicObjectiveCard.toString();
        String expectedString = "title"+System.lineSeparator()+"description"+System.lineSeparator()+"Multiplier: 5"+System.lineSeparator();
        assertEquals(expectedString, toString);
    }

    /**
     * Tests the copy method of {@link SetPublicObjectiveCard} (copy must not be null)
     * @see SetPublicObjectiveCard#copy()
     */
    @Test
    public void testCopy(){
        assertNotNull(setPublicObjectiveCard.copy());
    }
}