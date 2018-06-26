package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.EmptyListException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static it.polimi.se2018.model.DiceColor.*;
import static org.junit.Assert.*;

/**
 * Test for {@link Scorer} Class
 * The tests are run with {@link RowsColumnsPublicObjectiveCardTest}, {@link DiagonalsPublicObjectiveCardTest},
 * {@link SetPublicObjectiveCardTest} and {@link PrivateObjectiveCardTest} since the scorer uses these cards and
 * the scores strongly depend on the scoring processo of the {@link PublicObjectiveCard}.
 *
 * @author Jacopo Pio Gargano
 */



public class ScorerTest {
    private static Scorer scorer;

    //generic patterns
    private static WindowPattern wp1;
    private static WindowPattern wp2;
    private static WindowPattern wp3;
    private static WindowPattern wp4;

    //patterns with same total score
    private static WindowPattern wpSameScore1;
    private static WindowPattern wpSameScore2;
    private static WindowPattern wpSameScore3;
    private static WindowPattern wpSameScore4;

    //patterns with same private objective card score
    private static WindowPattern wpSamePrivateScore1;
    private static WindowPattern wpSamePrivateScore2;
    private static WindowPattern wpSamePrivateScore3;
    private static WindowPattern wpSamePrivateScore4;

    //patterns with same total score and same private objective card score
    private static WindowPattern wpSame1;
    private static WindowPattern wpSame2;
    private static WindowPattern wpSame3;
    private static WindowPattern wpSame4;

    //players used in the test
    private static Player p1;
    private static Player p2;
    private static Player p3;
    private static Player p4;

    //list of players to be completed with the players of the last round

    private int p1Score;
    private int p2Score;
    private int p3Score;
    private int p4Score;


    //players' private objective cards
    private static final PrivateObjectiveCard card1 = new PrivateObjectiveCard("","","",RED);
    private static final PrivateObjectiveCard card2 = new PrivateObjectiveCard("","","",GREEN);
    private static final PrivateObjectiveCard card3 = new PrivateObjectiveCard("","","",PURPLE);
    private static final PrivateObjectiveCard card4 = new PrivateObjectiveCard("","","",YELLOW);

    private static Set<PublicObjectiveCard> publicObjectiveCards;

    /**
     * Getting the singleton instance of scorer
     */
    @BeforeClass
    public static void getSingleton(){
        scorer = Scorer.getInstance();
    }

    /**
     * Giving private objective cards to players and setting the public objective cards for the tests
     */
    @BeforeClass
    public static void initializePlayersAndCards(){

        PublicObjectiveCard rowsColorPublicObjectiveCard = new RowsColumnsPublicObjectiveCard(
                null, null, null, Dice::getColor, 6, true);
        PublicObjectiveCard columnsValuePublicObjectiveCard = new RowsColumnsPublicObjectiveCard(
                null, null, null, Dice::getValue, 4, false);
        DiagonalsPublicObjectiveCard diagonalsPublicObjectiveCard = new DiagonalsPublicObjectiveCard(
                null, null, null, Dice::getColor);

        publicObjectiveCards = new HashSet<>();
        publicObjectiveCards.add(columnsValuePublicObjectiveCard);
        publicObjectiveCards.add(rowsColorPublicObjectiveCard);
        publicObjectiveCards.add(diagonalsPublicObjectiveCard);
    }

    /**
     * Creates a new Window Pattern Manager and creates the specific patterns of the players for the test
     */
    @BeforeClass
    public static void buildWindowPatterns(){

        p1 = new Player("p1", card1);
        p2 = new Player("p2", card2);
        p3 = new Player("p3", card3);
        p4 = new Player("p4", card4);

        try {
            WindowPatternManager windowPatternManager = new WindowPatternManager();
            List<WindowPattern> windowPatterns = new ArrayList<>(windowPatternManager.getPairsOfPatterns(1));
            WindowPattern genericWP = windowPatterns.get(0);


            wp1 = genericWP.copy();


            p1.setWindowPattern(wp1);

            wp1.putDiceOnCell(new Dice(BLUE, 1), 0, 0);
            wp1.putDiceOnCell(new Dice(YELLOW, 2), 0, 1);
            wp1.putDiceOnCell(new Dice(PURPLE, 4), 0, 2);
            wp1.putDiceOnCell(new Dice(BLUE, 5), 0, 3);
            wp1.putDiceOnCell(new Dice(GREEN, 4), 0, 4);

            wp1.putDiceOnCell(new Dice(YELLOW, 3), 1, 0);
            wp1.putDiceOnCell(new Dice(BLUE, 3), 1, 1);
            wp1.putDiceOnCell(new Dice(BLUE, 3), 1, 2);
            wp1.putDiceOnCell(new Dice(RED, 5), 1, 3);

            wp1.putDiceOnCell(new Dice(PURPLE, 5), 2, 0);
            wp1.putDiceOnCell(new Dice(YELLOW, 6), 2, 1);
            wp1.putDiceOnCell(new Dice(BLUE, 1), 2, 2);
            wp1.putDiceOnCell(new Dice(GREEN, 3), 2, 3);
            wp1.putDiceOnCell(new Dice(RED, 4), 2, 4);

            wp1.putDiceOnCell(new Dice(YELLOW, 4), 3, 0);
            wp1.putDiceOnCell(new Dice(YELLOW, 2), 3, 2);
            wp1.putDiceOnCell(new Dice(YELLOW, 5), 3, 3);


            wp2 = genericWP.copy();

            p2.setWindowPattern(wp2);

            wp2.putDiceOnCell(new Dice(BLUE, 1), 0, 0);
            wp2.putDiceOnCell(new Dice(YELLOW, 2), 0, 1);
            wp2.putDiceOnCell(new Dice(PURPLE, 3), 0, 2);
            wp2.putDiceOnCell(new Dice(GREEN, 5), 0, 3);
            wp2.putDiceOnCell(new Dice(GREEN, 4), 0, 4);

            wp2.putDiceOnCell(new Dice(YELLOW, 3), 1, 0);
            wp2.putDiceOnCell(new Dice(RED, 3), 1, 1);
            wp2.putDiceOnCell(new Dice(BLUE, 3), 1, 2);
            wp2.putDiceOnCell(new Dice(RED, 5), 1, 3);

            wp2.putDiceOnCell(new Dice(PURPLE, 5), 2, 0);
            wp2.putDiceOnCell(new Dice(YELLOW, 6), 2, 1);
            wp2.putDiceOnCell(new Dice(BLUE, 3), 2, 2);
            wp2.putDiceOnCell(new Dice(YELLOW, 3), 2, 3);
            wp2.putDiceOnCell(new Dice(RED, 4), 2, 4);

            wp2.putDiceOnCell(new Dice(YELLOW, 4), 3, 0);
            wp2.putDiceOnCell(new Dice(GREEN, 3), 3, 2);
            wp2.putDiceOnCell(new Dice(YELLOW, 5), 3, 3);


            wp3 = genericWP.copy();

            p3.setWindowPattern(wp3);

            wp3.putDiceOnCell(new Dice(BLUE, 1), 0, 0);
            wp3.putDiceOnCell(new Dice(YELLOW, 2), 0, 1);
            wp3.putDiceOnCell(new Dice(PURPLE, 3), 0, 2);
            wp3.putDiceOnCell(new Dice(PURPLE, 5), 0, 3);
            wp3.putDiceOnCell(new Dice(GREEN, 2), 0, 4);

            wp3.putDiceOnCell(new Dice(YELLOW, 3), 1, 0);
            wp3.putDiceOnCell(new Dice(RED, 3), 1, 1);
            wp3.putDiceOnCell(new Dice(BLUE, 3), 1, 2);
            wp3.putDiceOnCell(new Dice(GREEN, 5), 1, 3);
            wp3.putDiceOnCell(new Dice(GREEN, 1), 1, 4);

            wp3.putDiceOnCell(new Dice(PURPLE, 5), 2, 0);
            wp3.putDiceOnCell(new Dice(YELLOW, 6), 2, 1);
            wp3.putDiceOnCell(new Dice(BLUE, 3), 2, 2);
            wp3.putDiceOnCell(new Dice(GREEN, 3), 2, 3);
            wp3.putDiceOnCell(new Dice(RED, 4), 2, 4);

            wp3.putDiceOnCell(new Dice(YELLOW, 4), 3, 0);
            wp3.putDiceOnCell(new Dice(GREEN, 5), 3, 4);


            wp4 = genericWP.copy();

            p4.setWindowPattern(wp4);

            wp4.putDiceOnCell(new Dice(BLUE, 1), 0, 0);
            wp4.putDiceOnCell(new Dice(YELLOW, 2), 0, 1);
            wp4.putDiceOnCell(new Dice(GREEN, 3), 0, 2);
            wp4.putDiceOnCell(new Dice(BLUE, 5), 0, 3);
            wp4.putDiceOnCell(new Dice(GREEN, 4), 0, 4);

            wp4.putDiceOnCell(new Dice(YELLOW, 3), 1, 0);
            wp4.putDiceOnCell(new Dice(BLUE, 3), 1, 1);
            wp4.putDiceOnCell(new Dice(GREEN, 3), 1, 2);
            wp4.putDiceOnCell(new Dice(RED, 2), 1, 4);

            wp4.putDiceOnCell(new Dice(PURPLE, 5), 2, 0);
            wp4.putDiceOnCell(new Dice(BLUE, 6), 2, 1);
            wp4.putDiceOnCell(new Dice(BLUE, 3), 2, 2);
            wp4.putDiceOnCell(new Dice(GREEN, 3), 2, 3);
            wp4.putDiceOnCell(new Dice(RED, 3), 2, 4);

            wp4.putDiceOnCell(new Dice(RED, 4), 3, 0);
            wp4.putDiceOnCell(new Dice(PURPLE, 5), 3, 3);
            wp4.putDiceOnCell(new Dice(RED, 1), 3, 4);


            wpSameScore1 = genericWP.copy();

            p1.setWindowPattern(wpSameScore1);

            wpSameScore1.putDiceOnCell(new Dice(RED,5),0,0);
            wpSameScore1.putDiceOnCell(new Dice(BLUE,1),0,1);
            wpSameScore1.putDiceOnCell(new Dice(YELLOW,1),0,2);
            wpSameScore1.putDiceOnCell(new Dice(PURPLE,1),0,3);
            wpSameScore1.putDiceOnCell(new Dice(GREEN,1),0,4);

            wpSameScore1.putDiceOnCell(new Dice(RED,3),1,1);

            wpSameScore1.putDiceOnCell(new Dice(RED,2),2,2);


            wpSameScore2 = genericWP.copy();

            p2.setWindowPattern(wpSameScore2);

            wpSameScore2.putDiceOnCell(new Dice(RED,5),0,0);
            wpSameScore2.putDiceOnCell(new Dice(BLUE,1),0,1);
            wpSameScore2.putDiceOnCell(new Dice(YELLOW,1),0,2);
            wpSameScore2.putDiceOnCell(new Dice(PURPLE,1),0,3);
            wpSameScore2.putDiceOnCell(new Dice(GREEN,3),0,4);

            wpSameScore2.putDiceOnCell(new Dice(RED,5),1,2);

            wpSameScore2.putDiceOnCell(new Dice(RED,3),2,3);

            wpSameScore2.putDiceOnCell(new Dice(GREEN,6),3,0);
            wpSameScore2.putDiceOnCell(new Dice(RED,4),3,2);


            wpSameScore3 = genericWP.copy();

            p3.setWindowPattern(wpSameScore3);

            wpSameScore3.putDiceOnCell(new Dice(PURPLE,1),0,0);
            wpSameScore3.putDiceOnCell(new Dice(RED,5),0,1);
            wpSameScore3.putDiceOnCell(new Dice(PURPLE,1),0,2);

            wpSameScore3.putDiceOnCell(new Dice(YELLOW,2),1,0);
            wpSameScore3.putDiceOnCell(new Dice(RED,2),1,2);

            wpSameScore3.putDiceOnCell(new Dice(RED,3),2,0);
            wpSameScore3.putDiceOnCell(new Dice(YELLOW,3),2,2);

            wpSameScore3.putDiceOnCell(new Dice(GREEN,4),3,0);
            wpSameScore3.putDiceOnCell(new Dice(GREEN,4),3,2);
            wpSameScore3.putDiceOnCell(new Dice(PURPLE,4),3,4);


            wpSameScore4 = genericWP.copy();

            p4.setWindowPattern(wpSameScore4);

            wpSameScore4.putDiceOnCell(new Dice(RED, 4),0,0);
            wpSameScore4.putDiceOnCell(new Dice(YELLOW, 3),0,4);

            wpSameScore4.putDiceOnCell(new Dice(RED, 5),1,1);

            wpSameScore4.putDiceOnCell(new Dice(RED, 3),2,2);

            wpSameScore4.putDiceOnCell(new Dice(RED,5),3,0);
            wpSameScore4.putDiceOnCell(new Dice(BLUE,1),3,1);
            wpSameScore4.putDiceOnCell(new Dice(YELLOW,5),3,2);
            wpSameScore4.putDiceOnCell(new Dice(PURPLE,1),3,3);
            wpSameScore4.putDiceOnCell(new Dice(GREEN,1),3,4);



            wpSamePrivateScore1 = genericWP.copy();

            p1.setWindowPattern(wpSamePrivateScore1);

            wpSamePrivateScore1.putDiceOnCell(new Dice(RED,5),0,0);
            wpSamePrivateScore1.putDiceOnCell(new Dice(BLUE,1),0,1);
            wpSamePrivateScore1.putDiceOnCell(new Dice(YELLOW,1),0,2);
            wpSamePrivateScore1.putDiceOnCell(new Dice(PURPLE,1),0,3);
            wpSamePrivateScore1.putDiceOnCell(new Dice(GREEN,1),0,4);

            wpSamePrivateScore1.putDiceOnCell(new Dice(RED,3),1,1);
            wpSamePrivateScore1.putDiceOnCell(new Dice(YELLOW,1),1,3);

            wpSamePrivateScore1.putDiceOnCell(new Dice(RED,2),2,2);

            wpSamePrivateScore1.putDiceOnCell(new Dice(RED,1),3,3);


            wpSamePrivateScore2 = genericWP.copy();

            p2.setWindowPattern(wpSamePrivateScore2);

            wpSamePrivateScore2.putDiceOnCell(new Dice(BLUE,1),0,0);
            wpSamePrivateScore2.putDiceOnCell(new Dice(RED,5),0,1);
            wpSamePrivateScore2.putDiceOnCell(new Dice(YELLOW,1),0,2);
            wpSamePrivateScore2.putDiceOnCell(new Dice(PURPLE,1),0,3);
            wpSamePrivateScore2.putDiceOnCell(new Dice(GREEN,5),0,4);

            wpSamePrivateScore2.putDiceOnCell(new Dice(RED,5),1,2);
            wpSamePrivateScore2.putDiceOnCell(new Dice(RED,1),1,4);

            wpSamePrivateScore2.putDiceOnCell(new Dice(RED,3),2,3);

            wpSamePrivateScore2.putDiceOnCell(new Dice(GREEN,6),3,0);
            wpSamePrivateScore2.putDiceOnCell(new Dice(RED,4),3,2);
            wpSamePrivateScore2.putDiceOnCell(new Dice(RED,3),3,4);


            wpSamePrivateScore3 = genericWP.copy();

            p3.setWindowPattern(wpSamePrivateScore3);

            wpSamePrivateScore3.putDiceOnCell(new Dice(PURPLE,5),0,0);
            wpSamePrivateScore3.putDiceOnCell(new Dice(RED,5),0,1);
            wpSamePrivateScore3.putDiceOnCell(new Dice(PURPLE,6),0,2);

            wpSamePrivateScore3.putDiceOnCell(new Dice(RED,3),1,0);
            wpSamePrivateScore3.putDiceOnCell(new Dice(RED,2),1,2);

            wpSamePrivateScore3.putDiceOnCell(new Dice(YELLOW,2),2,0);
            wpSamePrivateScore3.putDiceOnCell(new Dice(YELLOW,3),2,2);
            wpSamePrivateScore3.putDiceOnCell(new Dice(RED,3),2,3);

            wpSamePrivateScore3.putDiceOnCell(new Dice(GREEN,4),3,0);
            wpSamePrivateScore3.putDiceOnCell(new Dice(GREEN,4),3,2);


            wpSamePrivateScore4 = genericWP.copy();

            p4.setWindowPattern(wpSamePrivateScore4);

            wpSamePrivateScore4.putDiceOnCell(new Dice(YELLOW, 5),0,4);

            wpSamePrivateScore4.putDiceOnCell(new Dice(RED, 5),1,1);

            wpSamePrivateScore4.putDiceOnCell(new Dice(RED, 3),2,2);
            wpSamePrivateScore4.putDiceOnCell(new Dice(YELLOW, 1),2,3);

            wpSamePrivateScore4.putDiceOnCell(new Dice(BLUE,1),3,0);
            wpSamePrivateScore4.putDiceOnCell(new Dice(RED,5),3,1);
            wpSamePrivateScore4.putDiceOnCell(new Dice(YELLOW,5),3,2);
            wpSamePrivateScore4.putDiceOnCell(new Dice(PURPLE,1),3,3);
            wpSamePrivateScore4.putDiceOnCell(new Dice(GREEN,1),3,4);


            wpSame1 = genericWP.copy();

            p1.setWindowPattern(wpSame1);

            wpSame1.putDiceOnCell(new Dice(RED, 5), 0,0);
            wpSame1.putDiceOnCell(new Dice(BLUE, 1), 0,1);
            wpSame1.putDiceOnCell(new Dice(YELLOW, 1), 0,2);
            wpSame1.putDiceOnCell(new Dice(PURPLE, 1), 0,3);
            wpSame1.putDiceOnCell(new Dice(GREEN, 1), 0,4);

            wpSame1.putDiceOnCell(new Dice(RED, 3), 1,1);
            wpSame1.putDiceOnCell(new Dice(YELLOW, 1), 1,3);

            wpSame1.putDiceOnCell(new Dice(RED, 2), 2,2);

            wpSame1.putDiceOnCell(new Dice(RED, 1), 3,3);


            wpSame2 = genericWP.copy();

            p2.setWindowPattern(wpSame2);

            wpSame2.putDiceOnCell(new Dice(BLUE, 1), 0,0);
            wpSame2.putDiceOnCell(new Dice(RED, 5), 0,1);
            wpSame2.putDiceOnCell(new Dice(YELLOW, 1), 0,2);
            wpSame2.putDiceOnCell(new Dice(PURPLE, 1), 0,3);
            wpSame2.putDiceOnCell(new Dice(GREEN, 5), 0,4);

            wpSame2.putDiceOnCell(new Dice(RED, 5), 1,2);

            wpSame2.putDiceOnCell(new Dice(RED, 3), 2,3);

            wpSame2.putDiceOnCell(new Dice(GREEN, 6), 3,0);
            wpSame2.putDiceOnCell(new Dice(RED, 4), 3,2);
            wpSame2.putDiceOnCell(new Dice(RED, 1), 3,4);


            wpSame3 = genericWP.copy();

            p3.setWindowPattern(wpSame3);

            wpSame3.putDiceOnCell(new Dice(YELLOW, 2), 0,0);
            wpSame3.putDiceOnCell(new Dice(PURPLE, 1), 0,1);
            wpSame3.putDiceOnCell(new Dice(RED, 2), 0,2);

            wpSame3.putDiceOnCell(new Dice(PURPLE, 1), 1,0);
            wpSame3.putDiceOnCell(new Dice(PURPLE, 5), 1,2);

            wpSame3.putDiceOnCell(new Dice(RED, 3), 2,0);
            wpSame3.putDiceOnCell(new Dice(YELLOW, 3), 2,2);

            wpSame3.putDiceOnCell(new Dice(GREEN, 4), 3,0);
            wpSame3.putDiceOnCell(new Dice(GREEN, 4), 3,2);
            wpSame3.putDiceOnCell(new Dice(PURPLE, 4), 3,4);


            wpSame4 = genericWP.copy();

            p4.setWindowPattern(wpSame4);

            wpSame4.putDiceOnCell(new Dice(RED, 4), 0,0);
            wpSame4.putDiceOnCell(new Dice(RED, 1), 0,2);
            wpSame4.putDiceOnCell(new Dice(YELLOW, 5), 0,4);

            wpSame4.putDiceOnCell(new Dice(RED, 5), 1,1);

            wpSame4.putDiceOnCell(new Dice(RED, 3), 2,2);

            wpSame4.putDiceOnCell(new Dice(BLUE, 1), 3,0);
            wpSame4.putDiceOnCell(new Dice(RED, 5), 3,1);
            wpSame4.putDiceOnCell(new Dice(YELLOW, 6), 3,2);
            wpSame4.putDiceOnCell(new Dice(PURPLE, 1), 3,3);
            wpSame4.putDiceOnCell(new Dice(GREEN, 1), 3,4);

        }catch (BadFormattedPatternFileException | NoPatternsFoundInFileSystemException e){
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Tests the singleton getInstance method does not return null, even if called multiple times
     * @see Scorer#getInstance()
     */
    @Test
    public void testSingletonInstanceIsNotNull(){
        assertNotNull(Scorer.getInstance());
        assertNotNull(Scorer.getInstance());
        assertNotNull(Scorer.getInstance());
    }

    /**
     * Tests that the two instances of the singleton are the same instance
     * @see Scorer#getInstance()
     */
    @Test
    public void testSingletonInstance(){
        Scorer scorer1 = Scorer.getInstance();
        Scorer scorer2 = Scorer.getInstance();
        assertEquals(scorer1, scorer2);
    }

    /**
     * Tests that if a game has only one player, then that player is the winner
     * @see Scorer#getRankings(Set, Set, Set)
     */
    @Test
    public void testSinglePlayerIsWinner(){
        p1.setWindowPattern(wp1);

        Set<Player> playersOfLastRound = new HashSet<>();
        playersOfLastRound.add(p1);

        Map<Player, Integer> rankings = scorer.getRankings(playersOfLastRound, new HashSet<>(), publicObjectiveCards);
        Player winner = scorer.getWinner(rankings);
        assertEquals(p1, winner);
    }

    /**
     * Tests the rankings of a game with four players, all with different scores
     * @see ScorerTest#initializeDefaultGame()
     * @see Scorer#getRankings(Set, Set, Set)
     */
    @Test
    public void testGetRankings(){
        Set<Player> playersOfLastRound = initializeDefaultGame();

        Map<Player, Integer> expectedRankings = new LinkedHashMap<>();
        expectedRankings.put(p3,p3Score);
        expectedRankings.put(p1,p1Score);
        expectedRankings.put(p2,p2Score);
        expectedRankings.put(p4,p4Score);

        Map<Player, Integer> rankings = scorer.getRankings(playersOfLastRound, new HashSet<>(), publicObjectiveCards);

        assertEquals(expectedRankings, rankings);
    }

    /**
     * Tests the throwing of a NullPointerException if playersOfLastRound parameter is null
     * @see Scorer#getRankings(Set, Set, Set)
     */
    @Test
    public void testGetRankingsOfNullPlayersOfLastRound(){
        try {
            scorer.getRankings(null, new HashSet<>(), publicObjectiveCards);
            fail();
        }catch (NullPointerException e){}
    }

    /**
     * Tests the throwing of an EmptyListException if playersOfLastRound parameter is empty
     * @see Scorer#getRankings(Set, Set, Set)
     */
    @Test
    public void testGetRankingsOfEmptyPlayersOfLastRound(){
        try {
            scorer.getRankings(new HashSet<>(), new HashSet<>(), publicObjectiveCards);
            fail();
        }catch (EmptyListException e){}
    }

    /**
     * Tests the throwing of a NullPointerException if publicObjectiveCards parameter is null
     * @see Scorer#getRankings(Set, Set, Set)
     */
    @Test
    public void testGetRankingsOfNullPublicObjectiveCards(){
        Set<Player> playersOfLastRound = new HashSet<>();
        playersOfLastRound.add(p1);
        try {
            scorer.getRankings(playersOfLastRound, new HashSet<>(), null);
            fail();
        }catch (NullPointerException e){}
    }

    /**
     * Tests the throwing of an EmptyListException if publicObjectiveCards parameter is empty
     * @see Scorer#getRankings(Set, Set, Set)
     */
    @Test
    public void testGetRankingsOfEmptyPublicObjectiveCards(){
        Set<Player> playersOfLastRound = new HashSet<>();
        playersOfLastRound.add(p1);
        try {
            scorer.getRankings(playersOfLastRound, new HashSet<>(), new HashSet<>());
            fail();
        }catch (EmptyListException e){}
    }


    /**
     * Tests the winner of a game with four players, all with different scores
     * Rankings are manually created
     * Rankings should be ordered by descending score
     * @see Scorer#getWinner(Map)
     */
    @Test
    public void testGetWinner(){
        Map<Player, Integer> rankings = new LinkedHashMap<>();
        rankings.put(p1,p1Score);
        rankings.put(p2,p2Score);
        rankings.put(p3,p3Score);
        rankings.put(p4,p4Score);

        Player winner = scorer.getWinner(rankings);

        assertEquals(p1, winner);
    }

    /**
     * Tests the winner of a game with four players, all with different scores
     * Rankings are computed by the scorer
     * Rankings should be ordered by descending score
     * @see ScorerTest#initializeDefaultGame()
     * @see Scorer#getRankings(Set, Set, Set)
     * @see Scorer#getWinner(Map)
     */
    @Test
    public void testGetWinnerFromRankings(){
        Set<Player> playersOfLastRound = initializeDefaultGame();

        Map<Player, Integer> rankings = scorer.getRankings(playersOfLastRound, new HashSet<>(), publicObjectiveCards);
        Player winner = scorer.getWinner(rankings);
        Player expectedWinner = p3;

        assertEquals(expectedWinner, winner);
    }

    /**
     * Teststhe impossibility of getting the winner of a game if the parameter rankings is null
     * @see Scorer#getRankings(Set, Set, Set)
     */
    @Test
    public void testGetWinnerOfNullRankings(){
        try {
            scorer.getWinner(null);
            fail();
        }catch (NullPointerException e){}
    }

    /**
     * Tests the throwing of an EmptyListException if rankings parameter is empty
     * @see Scorer#getWinner(Map)
     */
    @Test
    public void testGetWinnerOfEmptyRankings(){
        Map<Player, Integer> rankings = new LinkedHashMap<>();

        try {
            scorer.getWinner(rankings);
            fail();
        }catch (EmptyListException e){}
    }


    /**
     * Tests the calculation of the winner score
     * Rankings are computed by the scorer
     * Rankings should be ordered by descending score
     * @see ScorerTest#initializeDefaultGame()
     * @see Scorer#getRankings(Set, Set, Set)
     * @see Scorer#getWinner(Map)
     */
    @Test
    public void testGetWinnerScore(){
        Set<Player> playersOfLastRound = initializeDefaultGame();

        Map<Player, Integer> rankings = scorer.getRankings(playersOfLastRound, new HashSet<>(), publicObjectiveCards);
        Player winner = scorer.getWinner(rankings);
        int winnerScore = rankings.get(winner);

        assertEquals(p3Score, winnerScore);
    }


    /**
     * Tests the rankings of a game with four players
     * All players have the same score
     * All players have different PrivateObjectiveCard score
     * Rankings should be ordered by descending PrivateObjectiveCard score
     * @see ScorerTest#assignSameScoreWindowPatterns()
     * @see Scorer#getRankings(Set, Set, Set)
     */
    @Test
    public void testSameScoreRankings(){
        assignSameScoreWindowPatterns();

        Set<Player> playersOfLastRound = addDefaultPlayersToPlayersOfLastRound();

        p2.decreaseTokens(1);

        Map<Player, Integer> rankings = scorer.getRankings(playersOfLastRound, new HashSet<>(), publicObjectiveCards);
        List<Player> playersByScore = new ArrayList<>(rankings.keySet());

        int score = rankings.get(p1);

        for (int playerScore: rankings.values()) {
            assertTrue(score == playerScore);
        }

        List<Player> expectedPlayersByScore = new ArrayList<>();
        expectedPlayersByScore.add(p1);
        expectedPlayersByScore.add(p2);
        expectedPlayersByScore.add(p4);
        expectedPlayersByScore.add(p3);

        assertEquals(expectedPlayersByScore, playersByScore);
    }


    /**
     * Tests the rankings of a game with four players
     * All players have the same score
     * All players have the same PrivateObjectiveCard score
     * All players have a different number of tokens
     * Rankings should be ordered by descending number of tokens
     * @see ScorerTest#assignSamePrivateScoreWindowPatterns() ()
     * @see Scorer#getRankings(Set, Set, Set)
     */
    @Test
    public void testSamePrivateObjectiveCardScoreRankings(){
        assignSamePrivateScoreWindowPatterns();

        Set<Player> playersOfLastRound = addDefaultPlayersToPlayersOfLastRound();

        //decreasing the tokens in order to make all players have the same score
        p1.decreaseTokens(1);
        p2.decreaseTokens(3);
        p3.decreaseTokens(2);
        p4.decreaseTokens(0);

        Map<Player, Integer> rankings = scorer.getRankings(playersOfLastRound, new HashSet<>(), publicObjectiveCards);
        List<Player> playersByScore = new ArrayList<>(rankings.keySet());

        int score = rankings.get(p1);

        for (int playerScore: rankings.values()) {
            assertTrue(score == playerScore);
        }

        List<Player> expectedPlayersByScore = new ArrayList<>();
        expectedPlayersByScore.add(p4);
        expectedPlayersByScore.add(p1);
        expectedPlayersByScore.add(p3);
        expectedPlayersByScore.add(p2);

        assertEquals(expectedPlayersByScore, playersByScore);
    }

    /**
     * Tests the rankings of a game with four players
     * All players have the same score
     * All players have the same PrivateObjectiveCard score
     * All players have the same number of tokens
     * The rankings should be the same as the ordered list of players of last round (reverse turn order)
     * @see ScorerTest#assignSameScoreAndSamePrivateScoreWindowPatterns()
     * @see Scorer#getRankings(Set, Set, Set)
     */
    @Test
    public void testSameFavorTokensRankings(){
        assignSameScoreAndSamePrivateScoreWindowPatterns();

        Set<Player> playersOfLastRound = new HashSet<>();
        playersOfLastRound.add(p2);
        playersOfLastRound.add(p4);
        playersOfLastRound.add(p3);
        playersOfLastRound.add(p1);

        Map<Player, Integer> rankings = scorer.getRankings(playersOfLastRound, new HashSet<>(), publicObjectiveCards);
        Set<Player> playersByScore = new LinkedHashSet<>(rankings.keySet());

        int score = rankings.get(p1);

        for (int playerScore: rankings.values()) {
            assertTrue(score == playerScore);
        }

        assertEquals(playersOfLastRound, playersByScore);
    }

    @Test
    public void testInactivePlayers(){
        Set<Player> playersOfLastRound = initializeDefaultGame();
        Set<String> inactivePlayersIDs = new HashSet<>();
        inactivePlayersIDs.add(p2.getID());
        inactivePlayersIDs.add(p1.getID());

        Map<Player, Integer> expectedRankings = new LinkedHashMap<>();
        expectedRankings.put(p3,p3Score);
        expectedRankings.put(p4,p4Score);
        expectedRankings.put(p1,p1Score);
        expectedRankings.put(p2,p2Score);

        Map<Player, Integer> rankings = scorer.getRankings(playersOfLastRound, inactivePlayersIDs, publicObjectiveCards);

        assertEquals(expectedRankings, rankings);
    }


    /**
     * Assigns different score window patterns to players
     */
    private void assignDifferentScoreWindowPatterns() {
        p1.setWindowPattern(wp1);
        p2.setWindowPattern(wp2);
        p3.setWindowPattern(wp3);
        p4.setWindowPattern(wp4);
    }

    /**
     * Assigns same score, different PrivateObjectiveCard score window patterns to players
     */
    private void assignSameScoreWindowPatterns() {
        p1.setWindowPattern(wpSameScore1);
        p2.setWindowPattern(wpSameScore2);
        p3.setWindowPattern(wpSameScore3);
        p4.setWindowPattern(wpSameScore4);
    }

    /**
     * Assigns different score, same PrivateObjectiveCard score window patterns to players
     */
    private void assignSamePrivateScoreWindowPatterns() {
        p1.setWindowPattern(wpSamePrivateScore1);
        p2.setWindowPattern(wpSamePrivateScore2);
        p3.setWindowPattern(wpSamePrivateScore3);
        p4.setWindowPattern(wpSamePrivateScore4);
    }

    /**
     * Assigns same score, same PrivateObjectiveCard score window patterns to players
     */
    private void assignSameScoreAndSamePrivateScoreWindowPatterns() {
        p1.setWindowPattern(wpSame1);
        p2.setWindowPattern(wpSame2);
        p3.setWindowPattern(wpSame3);
        p4.setWindowPattern(wpSame4);
    }

    /**
     * Adds p1 to p4 players to the playersOfLastRound list
     */
    private Set<Player> addDefaultPlayersToPlayersOfLastRound() {

        Set<Player> playersOfLastRound = new HashSet<>();

        playersOfLastRound.add(p1);
        playersOfLastRound.add(p2);
        playersOfLastRound.add(p3);
        playersOfLastRound.add(p4);

        return playersOfLastRound;
    }

    /**
     * Initializes a default game used in multiple tests
     * @see ScorerTest#assignDifferentScoreWindowPatterns()
     * @see ScorerTest#addDefaultPlayersToPlayersOfLastRound()
     */
    private Set<Player> initializeDefaultGame() {
        assignDifferentScoreWindowPatterns();

        p1Score = 32 + p1.getFavorTokens();
        p2Score = 19 + p2.getFavorTokens();
        p3Score = 33 + p3.getFavorTokens();
        p4Score = 17 + p4.getFavorTokens();

        return addDefaultPlayersToPlayersOfLastRound();
    }


}