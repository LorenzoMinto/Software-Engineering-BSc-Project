package it.polimi.se2018.model;

import it.polimi.se2018.controller.BadFormattedPatternFileException;
import it.polimi.se2018.controller.NoPatternsFoundInFileSystemException;
import it.polimi.se2018.controller.WindowPatternManager;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static it.polimi.se2018.model.DiceColor.*;
import static org.junit.Assert.*;


/**
 * Test for {@link DiagonalsPublicObjectiveCard} class
 *
 * @author Jacopo Pio Gargano
 */
public class DiagonalsPublicObjectiveCardTest {

    private static WindowPattern diagonalsWP;
    private static WindowPattern rightDiagonalsWP;
    private static WindowPattern leftDiagonalsWP;
    private static WindowPattern twoDiceWP;
    private static WindowPattern emptyWP;

    private PublicObjectiveCard diagonalsPublicObjectiveCard;
    private PublicObjectiveCard colorDiagonalsPublicObjectiveCard;

    private static final int diagonalsScore = 13;
    private static final int rightDiagonalsScore = 9;
    private static final int leftDiagonalsScore = 9;


    /**
     * Creates a new Window Pattern Manager and creates the specific windowPatterns of the players for the tests
     */
    @BeforeClass
    public static void buildWindowPatterns(){
        try {
            WindowPatternManager windowPatternManager = new WindowPatternManager();

            diagonalsWP = new ArrayList<>(windowPatternManager.getPairsOfPatterns(1)).get(0);

            Player player = new Player("", new PrivateObjectiveCard("","","",RED));
            player.setWindowPattern(diagonalsWP);
            diagonalsWP.setOwner(player);

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


            rightDiagonalsWP = new ArrayList<>(windowPatternManager.getPairsOfPatterns(1)).get(0);

            player.setWindowPattern(rightDiagonalsWP);
            rightDiagonalsWP.setOwner(player);

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


            leftDiagonalsWP = new ArrayList<>(windowPatternManager.getPairsOfPatterns(1)).get(0);

            player.setWindowPattern(leftDiagonalsWP);
            leftDiagonalsWP.setOwner(player);

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


            twoDiceWP = new ArrayList<>(windowPatternManager.getPairsOfPatterns(1)).get(0);

            player.setWindowPattern(twoDiceWP);
            twoDiceWP.setOwner(player);

            twoDiceWP.putDiceOnCell(new Dice(RED), 0, 0);

            twoDiceWP.putDiceOnCell(new Dice(RED), 1, 1);

            emptyWP = new ArrayList<>(windowPatternManager.getPairsOfPatterns(1)).get(0);

        }catch (BadFormattedPatternFileException | NoPatternsFoundInFileSystemException e){
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Creates the two instances of {@link DiagonalsPublicObjectiveCard} used in the tests
     * @see DiagonalsPublicObjectiveCard#DiagonalsPublicObjectiveCard(String, String, String, Function)
     */
    @Before
    public void initializeCards(){
        diagonalsPublicObjectiveCard = new DiagonalsPublicObjectiveCard(null,null,null, Dice::getColor);

        colorDiagonalsPublicObjectiveCard = new DiagonalsPublicObjectiveCard(null,null,null,
                Dice::getColor);
    }

    /**
     * Tests the class constructor
     */
    @Test
    public void testConstructor(){
        assertNotNull(colorDiagonalsPublicObjectiveCard);
    }

    /**
     * Tests the impossibility of calculating the score of a null window pattern
     * @see DiagonalsPublicObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreOfNullWindowPattern(){
        try {
            diagonalsPublicObjectiveCard.calculateScore(null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests the scoring of an empty window pattern. Score must be 0
     * @see DiagonalsPublicObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreOfEmptyWindowPattern(){
        int score = diagonalsPublicObjectiveCard.calculateScore(emptyWP);
        assertEquals(0, score);
    }

    /**
     * Tests the scoring of a generic window pattern
     * @see DiagonalsPublicObjectiveCard#calculateScore(WindowPattern)
     */
    @Test
    public void testCalculateScoreDiagonals(){
        int score = colorDiagonalsPublicObjectiveCard.calculateScore(diagonalsWP);
        assertEquals(diagonalsScore, score);
    }

    /**
     * Tests the scoring of a window pattern with left to right diagonals only
     * Implicitly tests getScoreLeftToRight method of {@link DiagonalsPublicObjectiveCard}
     * @see DiagonalsPublicObjectiveCard#getScoreLeftToRight(WindowPattern, List)
     */
    @Test
    public void testCalculateScoreLeftToRightDiagonals(){
        int score = colorDiagonalsPublicObjectiveCard.calculateScore(rightDiagonalsWP);
        assertEquals(rightDiagonalsScore, score);
    }

    /**
     * Tests the scoring of a window pattern with right to left diagonals only
     * Implicitly tests getScoreRightToLeft method of {@link DiagonalsPublicObjectiveCard}
     * @see DiagonalsPublicObjectiveCard#getScoreRightToLeft(WindowPattern, List) (WindowPattern, List)
     */
    @Test
    public void testCalculateScoreRighToLeftDiagonals(){
        int score = colorDiagonalsPublicObjectiveCard.calculateScore(leftDiagonalsWP);
        assertEquals(leftDiagonalsScore, score);
    }

    /**
     * Tests the scoring of a window pattern with two diagonally adjacent dice only
     * Implicitly tests getCellPairScore method of {@link DiagonalsPublicObjectiveCard}
     * @see DiagonalsPublicObjectiveCard#getCellPairScore(WindowPattern, List, int, int, int)
     */
    @Test
    public void testCalculateScoreCellPair(){
        int score = colorDiagonalsPublicObjectiveCard.calculateScore(twoDiceWP);
        assertEquals(2, score);
    }

    /**
     * Tests the copy method of {@link DiagonalsPublicObjectiveCard} (copy must not be null)
     * @see DiagonalsPublicObjectiveCard#copy()
     */
    @Test
    public void testCopy() {
        assertNotNull(diagonalsPublicObjectiveCard.copy());
    }

}