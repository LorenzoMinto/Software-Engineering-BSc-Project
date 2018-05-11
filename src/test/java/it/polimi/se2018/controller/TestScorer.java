package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TestScorer {

    private static Scorer scorer;

    private static WindowPatternManager windowPatternManager;

    private static WindowPattern genericWP;

    private static WindowPattern wp1;
    private static WindowPattern wp2;
    private static WindowPattern wp3;
    private static WindowPattern wp4;

    private static WindowPattern wpWithSamePrivateScore;

    private static WindowPattern wpSameScore1;
    private static WindowPattern wpSameScore2;
    private static WindowPattern wpSameScore3;
    private static WindowPattern wpSameScore4;

    private static WindowPattern wpSamePrivateScore1;
    private static WindowPattern wpSamePrivateScore2;
    private static WindowPattern wpSamePrivateScore3;
    private static WindowPattern wpSamePrivateScore4;

    private static WindowPattern wpSame1;
    private static WindowPattern wpSame2;
    private static WindowPattern wpSame3;
    private static WindowPattern wpSame4;



    private static User user = new User(1,null);
    private static String nickname = null;

    private List<Player> playersOfGame;
    private List<Player> playersOfLastRound;
    private static Player p1;
    private static Player p2;
    private static Player p3;
    private static Player p4;

    private static final int numberOfPublicObjectiveCardsPerGame = 3;

    private int p1Score;
    private int p2Score;
    private int p3Score;
    private int p4Score;


    private ObjectiveCardManager manager;
    private ObjectiveCardFactory factory = ObjectiveCardFactory.getInstance();

    private static final PrivateObjectiveCard card1 = new PrivateObjectiveCard(
            null, null, null, DiceColors.RED);
    private static final PrivateObjectiveCard card2 = new PrivateObjectiveCard(
            null, null, null, DiceColors.GREEN);
    private static final PrivateObjectiveCard card3 = new PrivateObjectiveCard(
            null, null, null, DiceColors.PURPLE);
    private static final PrivateObjectiveCard card4 = new PrivateObjectiveCard(
            null, null, null, DiceColors.YELLOW);

    private List<PublicObjectiveCard> publicObjectiveCards;
    private PublicObjectiveCard rowsColorPublicObjectiveCard = new RowsColumnsPublicObjectiveCard(
            null, null, null, Dice::getColor, 6, true);
    private PublicObjectiveCard columnsValuePublicObjectiveCard = new RowsColumnsPublicObjectiveCard(
            null, null, null, Dice::getValue, 4, false);
    private DiagonalsPublicObjectiveCard diagonalsPublicObjectiveCard = new DiagonalsPublicObjectiveCard(
            null, null, null, Dice::getColor);

    private static int private1wp1Score;
    private static int private2wp2Score;
    private static int private3wp3Score;
    private static int private4wp4Score;

    private static int public1wp1Score;
    private static int public2wp1Score;
    private static int public3wp1Score;
    private static int public1wp2Score;
    private static int public2wp2Score;
    private static int public3wp2Score;
    private static int public1wp3Score;
    private static int public2wp3Score;
    private static int public3wp3Score;
    private static int public1wp4Score;
    private static int public2wp4Score;
    private static int public3wp4Score;


    private List<Player> rankings;
    private List<Player> testRankings;
    private Player winner;
    private Player testWinner;

    private Map<Player, Integer> scores;
    Map<Player, Integer> testScores;

    private Object[] results;
    private int winnerScore;


    @BeforeClass
    public static void getSingleton(){
        scorer = Scorer.getInstance();
    }

    @BeforeClass
    public static void buildWindowPatterns(){

        try {
            windowPatternManager = new WindowPatternManager();
        }catch (NoPatternsFoundInFileSystemException e){
            e.printStackTrace();
            fail();
        }

        try {
            genericWP = windowPatternManager.getPatterns(1).get(0);

            wp1 = genericWP.copy();

            wp1.putDiceOnCell(new Dice(DiceColors.BLUE, 1), 0, 0);
            wp1.putDiceOnCell(new Dice(DiceColors.YELLOW, 2), 0, 1);
            wp1.putDiceOnCell(new Dice(DiceColors.PURPLE, 4), 0, 2);
            wp1.putDiceOnCell(new Dice(DiceColors.BLUE, 5), 0, 3);
            wp1.putDiceOnCell(new Dice(DiceColors.GREEN, 4), 0, 4);

            wp1.putDiceOnCell(new Dice(DiceColors.YELLOW, 3), 1, 0);
            wp1.putDiceOnCell(new Dice(DiceColors.BLUE, 3), 1, 1);
            wp1.putDiceOnCell(new Dice(DiceColors.BLUE, 3), 1, 2);
            wp1.putDiceOnCell(new Dice(DiceColors.RED, 5), 1, 3);

            wp1.putDiceOnCell(new Dice(DiceColors.PURPLE, 5), 2, 0);
            wp1.putDiceOnCell(new Dice(DiceColors.YELLOW, 6), 2, 1);
            wp1.putDiceOnCell(new Dice(DiceColors.BLUE, 1), 2, 2);
            wp1.putDiceOnCell(new Dice(DiceColors.GREEN, 3), 2, 3);
            wp1.putDiceOnCell(new Dice(DiceColors.RED, 4), 2, 4);

            wp1.putDiceOnCell(new Dice(DiceColors.YELLOW, 4), 3, 0);
            wp1.putDiceOnCell(new Dice(DiceColors.YELLOW, 2), 3, 2);
            wp1.putDiceOnCell(new Dice(DiceColors.YELLOW, 5), 3, 3);


            wp2 = genericWP.copy();

            wp2.putDiceOnCell(new Dice(DiceColors.BLUE, 1), 0, 0);
            wp2.putDiceOnCell(new Dice(DiceColors.YELLOW, 2), 0, 1);
            wp2.putDiceOnCell(new Dice(DiceColors.PURPLE, 3), 0, 2);
            wp2.putDiceOnCell(new Dice(DiceColors.GREEN, 5), 0, 3);
            wp2.putDiceOnCell(new Dice(DiceColors.GREEN, 4), 0, 4);

            wp2.putDiceOnCell(new Dice(DiceColors.YELLOW, 3), 1, 0);
            wp2.putDiceOnCell(new Dice(DiceColors.RED, 3), 1, 1);
            wp2.putDiceOnCell(new Dice(DiceColors.BLUE, 3), 1, 2);
            wp2.putDiceOnCell(new Dice(DiceColors.RED, 5), 1, 3);

            wp2.putDiceOnCell(new Dice(DiceColors.PURPLE, 5), 2, 0);
            wp2.putDiceOnCell(new Dice(DiceColors.YELLOW, 6), 2, 1);
            wp2.putDiceOnCell(new Dice(DiceColors.BLUE, 3), 2, 2);
            wp2.putDiceOnCell(new Dice(DiceColors.YELLOW, 3), 2, 3);
            wp2.putDiceOnCell(new Dice(DiceColors.RED, 4), 2, 4);

            wp2.putDiceOnCell(new Dice(DiceColors.YELLOW, 4), 3, 0);
            wp2.putDiceOnCell(new Dice(DiceColors.GREEN, 3), 3, 2);
            wp2.putDiceOnCell(new Dice(DiceColors.YELLOW, 5), 3, 3);


            wp3 = genericWP.copy();

            wp3.putDiceOnCell(new Dice(DiceColors.BLUE, 1), 0, 0);
            wp3.putDiceOnCell(new Dice(DiceColors.YELLOW, 2), 0, 1);
            wp3.putDiceOnCell(new Dice(DiceColors.PURPLE, 3), 0, 2);
            wp3.putDiceOnCell(new Dice(DiceColors.PURPLE, 5), 0, 3);
            wp3.putDiceOnCell(new Dice(DiceColors.GREEN, 2), 0, 4);

            wp3.putDiceOnCell(new Dice(DiceColors.YELLOW, 3), 1, 0);
            wp3.putDiceOnCell(new Dice(DiceColors.RED, 3), 1, 1);
            wp3.putDiceOnCell(new Dice(DiceColors.BLUE, 3), 1, 2);
            wp3.putDiceOnCell(new Dice(DiceColors.GREEN, 5), 1, 3);
            wp3.putDiceOnCell(new Dice(DiceColors.GREEN, 1), 1, 4);

            wp3.putDiceOnCell(new Dice(DiceColors.PURPLE, 5), 2, 0);
            wp3.putDiceOnCell(new Dice(DiceColors.YELLOW, 6), 2, 1);
            wp3.putDiceOnCell(new Dice(DiceColors.BLUE, 3), 2, 2);
            wp3.putDiceOnCell(new Dice(DiceColors.GREEN, 3), 2, 3);
            wp3.putDiceOnCell(new Dice(DiceColors.RED, 4), 2, 4);

            wp3.putDiceOnCell(new Dice(DiceColors.YELLOW, 4), 3, 0);
            wp3.putDiceOnCell(new Dice(DiceColors.GREEN, 5), 3, 4);


            wp4 = genericWP.copy();

            wp4.putDiceOnCell(new Dice(DiceColors.BLUE, 1), 0, 0);
            wp4.putDiceOnCell(new Dice(DiceColors.YELLOW, 2), 0, 1);
            wp4.putDiceOnCell(new Dice(DiceColors.GREEN, 3), 0, 2);
            wp4.putDiceOnCell(new Dice(DiceColors.BLUE, 5), 0, 3);
            wp4.putDiceOnCell(new Dice(DiceColors.GREEN, 4), 0, 4);

            wp4.putDiceOnCell(new Dice(DiceColors.YELLOW, 3), 1, 0);
            wp4.putDiceOnCell(new Dice(DiceColors.BLUE, 3), 1, 1);
            wp4.putDiceOnCell(new Dice(DiceColors.GREEN, 3), 1, 2);
            wp4.putDiceOnCell(new Dice(DiceColors.RED, 2), 1, 4);

            wp4.putDiceOnCell(new Dice(DiceColors.PURPLE, 5), 2, 0);
            wp4.putDiceOnCell(new Dice(DiceColors.BLUE, 6), 2, 1);
            wp4.putDiceOnCell(new Dice(DiceColors.BLUE, 3), 2, 2);
            wp4.putDiceOnCell(new Dice(DiceColors.GREEN, 3), 2, 3);
            wp4.putDiceOnCell(new Dice(DiceColors.RED, 3), 2, 4);

            wp4.putDiceOnCell(new Dice(DiceColors.RED, 4), 3, 0);
            wp4.putDiceOnCell(new Dice(DiceColors.PURPLE, 5), 3, 3);
            wp4.putDiceOnCell(new Dice(DiceColors.RED, 1), 3, 4);


            wpWithSamePrivateScore = genericWP.copy();
            wpWithSamePrivateScore.putDiceOnCell(new Dice(DiceColors.BLUE, 1), 0, 0);
            wpWithSamePrivateScore.putDiceOnCell(new Dice(DiceColors.YELLOW, 2), 0, 1);
            wpWithSamePrivateScore.putDiceOnCell(new Dice(DiceColors.PURPLE, 4), 0, 2);
            wpWithSamePrivateScore.putDiceOnCell(new Dice(DiceColors.BLUE, 5), 0, 3);
            wpWithSamePrivateScore.putDiceOnCell(new Dice(DiceColors.GREEN, 6), 0, 4);

            wpWithSamePrivateScore.putDiceOnCell(new Dice(DiceColors.YELLOW, 3), 1, 0);
            wpWithSamePrivateScore.putDiceOnCell(new Dice(DiceColors.BLUE, 3), 1, 1);
            wpWithSamePrivateScore.putDiceOnCell(new Dice(DiceColors.BLUE, 3), 1, 2);
            wpWithSamePrivateScore.putDiceOnCell(new Dice(DiceColors.RED, 5), 1, 3);

            wpWithSamePrivateScore.putDiceOnCell(new Dice(DiceColors.PURPLE, 6), 2, 0);
            wpWithSamePrivateScore.putDiceOnCell(new Dice(DiceColors.YELLOW, 6), 2, 1);
            wpWithSamePrivateScore.putDiceOnCell(new Dice(DiceColors.BLUE, 1), 2, 2);
            wpWithSamePrivateScore.putDiceOnCell(new Dice(DiceColors.GREEN, 3), 2, 3);
            wpWithSamePrivateScore.putDiceOnCell(new Dice(DiceColors.RED, 4), 2, 4);

            wpWithSamePrivateScore.putDiceOnCell(new Dice(DiceColors.YELLOW, 6), 3, 0);
            wpWithSamePrivateScore.putDiceOnCell(new Dice(DiceColors.YELLOW, 4), 3, 2);
            wpWithSamePrivateScore.putDiceOnCell(new Dice(DiceColors.YELLOW, 5), 3, 3);


            wpSameScore1 = genericWP.copy();

            wpSameScore1.putDiceOnCell(new Dice(DiceColors.RED,5),0,0);
            wpSameScore1.putDiceOnCell(new Dice(DiceColors.BLUE,1),0,1);
            wpSameScore1.putDiceOnCell(new Dice(DiceColors.YELLOW,1),0,2);
            wpSameScore1.putDiceOnCell(new Dice(DiceColors.PURPLE,1),0,3);
            wpSameScore1.putDiceOnCell(new Dice(DiceColors.GREEN,1),0,4);

            wpSameScore1.putDiceOnCell(new Dice(DiceColors.RED,3),1,1);

            wpSameScore1.putDiceOnCell(new Dice(DiceColors.RED,2),2,2);


            wpSameScore2 = genericWP.copy();

            wpSameScore2.putDiceOnCell(new Dice(DiceColors.RED,5),0,0);
            wpSameScore2.putDiceOnCell(new Dice(DiceColors.BLUE,1),0,1);
            wpSameScore2.putDiceOnCell(new Dice(DiceColors.YELLOW,1),0,2);
            wpSameScore2.putDiceOnCell(new Dice(DiceColors.PURPLE,1),0,3);
            wpSameScore2.putDiceOnCell(new Dice(DiceColors.GREEN,3),0,4);

            wpSameScore2.putDiceOnCell(new Dice(DiceColors.RED,5),1,2);

            wpSameScore2.putDiceOnCell(new Dice(DiceColors.RED,3),2,3);

            wpSameScore2.putDiceOnCell(new Dice(DiceColors.GREEN,6),3,0);
            wpSameScore2.putDiceOnCell(new Dice(DiceColors.RED,4),3,2);


            wpSameScore3 = genericWP.copy();

            wpSameScore3.putDiceOnCell(new Dice(DiceColors.PURPLE,1),0,0);
            wpSameScore3.putDiceOnCell(new Dice(DiceColors.RED,5),0,1);
            wpSameScore3.putDiceOnCell(new Dice(DiceColors.PURPLE,1),0,2);

            wpSameScore3.putDiceOnCell(new Dice(DiceColors.YELLOW,2),1,0);
            wpSameScore3.putDiceOnCell(new Dice(DiceColors.RED,2),1,2);

            wpSameScore3.putDiceOnCell(new Dice(DiceColors.RED,3),2,0);
            wpSameScore3.putDiceOnCell(new Dice(DiceColors.YELLOW,3),2,2);

            wpSameScore3.putDiceOnCell(new Dice(DiceColors.GREEN,4),3,0);
            wpSameScore3.putDiceOnCell(new Dice(DiceColors.GREEN,4),3,2);
            wpSameScore3.putDiceOnCell(new Dice(DiceColors.PURPLE,4),3,4);


            wpSameScore4 = genericWP.copy();

            wpSameScore4.putDiceOnCell(new Dice(DiceColors.RED, 4),0,0);
            wpSameScore4.putDiceOnCell(new Dice(DiceColors.YELLOW, 3),0,4);

            wpSameScore4.putDiceOnCell(new Dice(DiceColors.RED, 5),1,1);

            wpSameScore4.putDiceOnCell(new Dice(DiceColors.RED, 3),2,2);

            wpSameScore4.putDiceOnCell(new Dice(DiceColors.RED,5),3,0);
            wpSameScore4.putDiceOnCell(new Dice(DiceColors.BLUE,1),3,1);
            wpSameScore4.putDiceOnCell(new Dice(DiceColors.YELLOW,5),3,2);
            wpSameScore4.putDiceOnCell(new Dice(DiceColors.PURPLE,1),3,3);
            wpSameScore4.putDiceOnCell(new Dice(DiceColors.GREEN,1),3,4);



            wpSamePrivateScore1 = genericWP.copy();

            wpSamePrivateScore1.putDiceOnCell(new Dice(DiceColors.RED,5),0,0);
            wpSamePrivateScore1.putDiceOnCell(new Dice(DiceColors.BLUE,1),0,1);
            wpSamePrivateScore1.putDiceOnCell(new Dice(DiceColors.YELLOW,1),0,2);
            wpSamePrivateScore1.putDiceOnCell(new Dice(DiceColors.PURPLE,1),0,3);
            wpSamePrivateScore1.putDiceOnCell(new Dice(DiceColors.GREEN,1),0,4);

            wpSamePrivateScore1.putDiceOnCell(new Dice(DiceColors.RED,3),1,1);
            wpSamePrivateScore1.putDiceOnCell(new Dice(DiceColors.YELLOW,1),1,3);

            wpSamePrivateScore1.putDiceOnCell(new Dice(DiceColors.RED,2),2,2);

            wpSamePrivateScore1.putDiceOnCell(new Dice(DiceColors.RED,1),3,3);


            wpSamePrivateScore2 = genericWP.copy();

            wpSamePrivateScore2.putDiceOnCell(new Dice(DiceColors.BLUE,1),0,0);
            wpSamePrivateScore2.putDiceOnCell(new Dice(DiceColors.RED,5),0,1);
            wpSamePrivateScore2.putDiceOnCell(new Dice(DiceColors.YELLOW,1),0,2);
            wpSamePrivateScore2.putDiceOnCell(new Dice(DiceColors.PURPLE,1),0,3);
            wpSamePrivateScore2.putDiceOnCell(new Dice(DiceColors.GREEN,5),0,4);

            wpSamePrivateScore2.putDiceOnCell(new Dice(DiceColors.RED,5),1,2);
            wpSamePrivateScore2.putDiceOnCell(new Dice(DiceColors.RED,1),1,4);

            wpSamePrivateScore2.putDiceOnCell(new Dice(DiceColors.RED,3),2,3);

            wpSamePrivateScore2.putDiceOnCell(new Dice(DiceColors.GREEN,6),3,0);
            wpSamePrivateScore2.putDiceOnCell(new Dice(DiceColors.RED,4),3,2);
            wpSamePrivateScore2.putDiceOnCell(new Dice(DiceColors.RED,3),3,4);


            wpSamePrivateScore3 = genericWP.copy();

            wpSamePrivateScore3.putDiceOnCell(new Dice(DiceColors.PURPLE,5),0,0);
            wpSamePrivateScore3.putDiceOnCell(new Dice(DiceColors.RED,5),0,1);
            wpSamePrivateScore3.putDiceOnCell(new Dice(DiceColors.PURPLE,6),0,2);

            wpSamePrivateScore3.putDiceOnCell(new Dice(DiceColors.RED,3),1,0);
            wpSamePrivateScore3.putDiceOnCell(new Dice(DiceColors.RED,2),1,2);

            wpSamePrivateScore3.putDiceOnCell(new Dice(DiceColors.YELLOW,2),2,0);
            wpSamePrivateScore3.putDiceOnCell(new Dice(DiceColors.YELLOW,3),2,2);
            wpSamePrivateScore3.putDiceOnCell(new Dice(DiceColors.RED,3),2,3);

            wpSamePrivateScore3.putDiceOnCell(new Dice(DiceColors.GREEN,4),3,0);
            wpSamePrivateScore3.putDiceOnCell(new Dice(DiceColors.GREEN,4),3,2);


            wpSamePrivateScore4 = genericWP.copy();

            wpSamePrivateScore4.putDiceOnCell(new Dice(DiceColors.YELLOW, 5),0,4);

            wpSamePrivateScore4.putDiceOnCell(new Dice(DiceColors.RED, 5),1,1);

            wpSamePrivateScore4.putDiceOnCell(new Dice(DiceColors.RED, 3),2,2);
            wpSamePrivateScore4.putDiceOnCell(new Dice(DiceColors.YELLOW, 1),2,3);

            wpSamePrivateScore4.putDiceOnCell(new Dice(DiceColors.BLUE,1),3,0);
            wpSamePrivateScore4.putDiceOnCell(new Dice(DiceColors.RED,5),3,1);
            wpSamePrivateScore4.putDiceOnCell(new Dice(DiceColors.YELLOW,5),3,2);
            wpSamePrivateScore4.putDiceOnCell(new Dice(DiceColors.PURPLE,1),3,3);
            wpSamePrivateScore4.putDiceOnCell(new Dice(DiceColors.GREEN,1),3,4);


            wpSame1 = genericWP.copy();

            wpSame1.putDiceOnCell(new Dice(DiceColors.RED, 5), 0,0);
            wpSame1.putDiceOnCell(new Dice(DiceColors.BLUE, 1), 0,1);
            wpSame1.putDiceOnCell(new Dice(DiceColors.YELLOW, 1), 0,2);
            wpSame1.putDiceOnCell(new Dice(DiceColors.PURPLE, 1), 0,3);
            wpSame1.putDiceOnCell(new Dice(DiceColors.GREEN, 1), 0,4);

            wpSame1.putDiceOnCell(new Dice(DiceColors.RED, 3), 1,1);
            wpSame1.putDiceOnCell(new Dice(DiceColors.YELLOW, 1), 1,3);

            wpSame1.putDiceOnCell(new Dice(DiceColors.RED, 2), 2,2);

            wpSame1.putDiceOnCell(new Dice(DiceColors.RED, 1), 3,3);


            wpSame2 = genericWP.copy();

            wpSame2.putDiceOnCell(new Dice(DiceColors.BLUE, 1), 0,0);
            wpSame2.putDiceOnCell(new Dice(DiceColors.RED, 5), 0,1);
            wpSame2.putDiceOnCell(new Dice(DiceColors.YELLOW, 1), 0,2);
            wpSame2.putDiceOnCell(new Dice(DiceColors.PURPLE, 1), 0,3);
            wpSame2.putDiceOnCell(new Dice(DiceColors.GREEN, 5), 0,4);

            wpSame2.putDiceOnCell(new Dice(DiceColors.RED, 5), 1,2);

            wpSame2.putDiceOnCell(new Dice(DiceColors.RED, 3), 2,3);

            wpSame2.putDiceOnCell(new Dice(DiceColors.GREEN, 6), 3,0);
            wpSame2.putDiceOnCell(new Dice(DiceColors.RED, 4), 3,2);
            wpSame2.putDiceOnCell(new Dice(DiceColors.RED, 1), 3,4);


            wpSame3 = genericWP.copy();

            wpSame3.putDiceOnCell(new Dice(DiceColors.YELLOW, 2), 0,0);
            wpSame3.putDiceOnCell(new Dice(DiceColors.PURPLE, 1), 0,1);
            wpSame3.putDiceOnCell(new Dice(DiceColors.RED, 2), 0,2);

            wpSame3.putDiceOnCell(new Dice(DiceColors.PURPLE, 1), 1,0);
            wpSame3.putDiceOnCell(new Dice(DiceColors.PURPLE, 5), 1,2);

            wpSame3.putDiceOnCell(new Dice(DiceColors.RED, 3), 2,0);
            wpSame3.putDiceOnCell(new Dice(DiceColors.YELLOW, 3), 2,2);

            wpSame3.putDiceOnCell(new Dice(DiceColors.GREEN, 4), 3,0);
            wpSame3.putDiceOnCell(new Dice(DiceColors.GREEN, 4), 3,2);
            wpSame3.putDiceOnCell(new Dice(DiceColors.PURPLE, 4), 3,4);


            wpSame4 = genericWP.copy();

            wpSame4.putDiceOnCell(new Dice(DiceColors.RED, 4), 0,0);
            wpSame4.putDiceOnCell(new Dice(DiceColors.RED, 1), 0,2);
            wpSame4.putDiceOnCell(new Dice(DiceColors.YELLOW, 5), 0,4);

            wpSame4.putDiceOnCell(new Dice(DiceColors.RED, 5), 1,1);

            wpSame4.putDiceOnCell(new Dice(DiceColors.RED, 3), 2,2);

            wpSame4.putDiceOnCell(new Dice(DiceColors.BLUE, 1), 3,0);
            wpSame4.putDiceOnCell(new Dice(DiceColors.RED, 5), 3,1);
            wpSame4.putDiceOnCell(new Dice(DiceColors.YELLOW, 6), 3,2);
            wpSame4.putDiceOnCell(new Dice(DiceColors.PURPLE, 1), 3,3);
            wpSame4.putDiceOnCell(new Dice(DiceColors.GREEN, 1), 3,4);

        }catch (BadFormattedPatternFileException e){
            e.printStackTrace();
            fail();
        }
    }

    @Before
    public void initializePlayersWithPrivateObjectiveCards(){
        p1 = new Player(user, nickname, card1);
        p2 = new Player(user, nickname, card2);
        p3 = new Player(user, nickname, card3);
        p4 = new Player(user, nickname, card4);
    }

    @BeforeClass
    public static void initializeScores(){
        private1wp1Score = 9;
        private2wp2Score = 12;
        private3wp3Score = 13;
        private4wp4Score = 5;

        public1wp1Score = 6;
        public2wp1Score = 8;
        public3wp1Score = 12;

        public1wp2Score = 0;
        public2wp2Score = 4;
        public3wp2Score = 6;

        public1wp3Score = 6;
        public2wp3Score = 8;
        public3wp3Score = 9;

        public1wp4Score = 0;
        public2wp4Score = 8;
        public3wp4Score = 9;
    }

    @Before
    public void initializeListsOfPlayers(){
        playersOfGame = new ArrayList<>();
        playersOfLastRound = new ArrayList<>();
        testRankings = new ArrayList<>();
    }

    @Before
    public void initializeManagerAndGetPublicObjectiveCards(){
        manager = new ObjectiveCardManager();
        publicObjectiveCards = manager.getPublicObjectiveCards(numberOfPublicObjectiveCardsPerGame);
    }


    @Test
    public void testSingletonInstanceIsNotNull(){
        assertNotNull(Scorer.getInstance());
    }

    @Test
    public void testSinglePlayerIsWinner(){
        p1.setWindowPattern(wp1);
        playersOfGame.add(p1);
        playersOfLastRound.add(p1);
        results = scorer.compute(playersOfLastRound, playersOfGame, publicObjectiveCards);
        rankings = (List<Player>) results[0];
        winner = rankings.get(0);
        assertEquals(p1, winner);
    }

    @Test
    public void testGetRankings(){
        initializeDefaultGame();

        results = scorer.compute(playersOfLastRound, playersOfGame, publicObjectiveCards);
        rankings = (List<Player>) results[0];

        assertEquals(testRankings, rankings);
    }

    @Test
    public void testGetScores(){
        assignDefaultWindowPatterns();
        initializeDefaultGame();

        results = scorer.compute(playersOfLastRound, playersOfGame, publicObjectiveCards);
        scores = (Map<Player, Integer>) results[1];

        assertEquals(testScores, scores);
    }



    @Test
    public void testGetWinner(){
        initializeDefaultGame();

        results = scorer.compute(playersOfLastRound, playersOfGame, publicObjectiveCards);
        rankings = (List<Player>) results[0];
        winner = rankings.get(0);
        testWinner = p3;

        assertEquals(testRankings, rankings);
    }

    @Test
    public void testGetWinnerScore(){
        initializeDefaultGame();

        results = scorer.compute(playersOfLastRound, playersOfGame, publicObjectiveCards);
        rankings = (List<Player>) results[0];
        winner = rankings.get(0);
        scores = (Map<Player, Integer>) results[1];
        winnerScore = scores.get(winner);

        assertEquals(p3Score, winnerScore);
    }


    @Test
    public void testGetRankingsWithNoPlayersInGame(){
        try {
            scorer.compute(playersOfLastRound, playersOfGame, publicObjectiveCards);
            fail();
        }catch (IllegalArgumentException e){}
    }

    @Test
    public void testSameScoreRankings(){
        assignSameScoreWindowPatterns();

        addDefaultPlayersToLists();

        setCustomPublicObjectiveCards();

        p2.decreaseTokens(1);

        results = scorer.compute(playersOfLastRound, playersOfGame, publicObjectiveCards);
        rankings = (List<Player>) results[0];
        scores = (Map<Player, Integer>)results[1];

        int score = scores.get(p1);

        for (int playerScore: scores.values()) {
            assertTrue(score == playerScore);
        }

        testRankings.add(p1);
        testRankings.add(p4);
        testRankings.add(p2);
        testRankings.add(p3);

        assertEquals(testRankings, rankings);
    }


    //Also same score
    @Test
    public void testSamePrivateScoreRankings(){
        assignSamePrivateScoreWindowPatterns();

        addDefaultPlayersToLists();

        setCustomPublicObjectiveCards();

        p1.decreaseTokens(1);
        p2.decreaseTokens(3);
        p3.decreaseTokens(2);
        p4.decreaseTokens(0);

        results = scorer.compute(playersOfLastRound, playersOfGame, publicObjectiveCards);
        rankings = (List<Player>) results[0];
        scores = (Map<Player, Integer>)results[1];

        int score = scores.get(p1);

        for (int playerScore: scores.values()) {
            assertTrue(score == playerScore);
        }

        testRankings.add(p4);
        testRankings.add(p1);
        testRankings.add(p3);
        testRankings.add(p2);

        assertEquals(testRankings, rankings);
    }

    //Also same score and same private objective card score
    @Test
    public void testSameFavorTokensRankings(){
        assignSameScoreAndSamePrivateScoreWindowPatterns();

        playersOfGame.add(p1);
        playersOfGame.add(p2);
        playersOfGame.add(p3);
        playersOfGame.add(p4);

        playersOfLastRound.add(p2);
        playersOfLastRound.add(p4);
        playersOfLastRound.add(p3);
        playersOfLastRound.add(p1);

        testRankings = new ArrayList<>(playersOfLastRound);

        setCustomPublicObjectiveCards();

        results = scorer.compute(playersOfLastRound, playersOfGame, publicObjectiveCards);
        rankings = (List<Player>) results[0];
        scores = (Map<Player, Integer>)results[1];

        int score = scores.get(p1);

        for (int playerScore: scores.values()) {
            assertTrue(score == playerScore);
        }

        assertEquals(testRankings, rankings);
    }



    private void assignDefaultWindowPatterns() {
        p1.setWindowPattern(wp1);
        p2.setWindowPattern(wp2);
        p3.setWindowPattern(wp3);
        p4.setWindowPattern(wp4);
    }

    private void assignSameScoreWindowPatterns() {
        p1.setWindowPattern(wpSameScore1);
        p2.setWindowPattern(wpSameScore2);
        p3.setWindowPattern(wpSameScore3);
        p4.setWindowPattern(wpSameScore4);
    }

    private void assignSamePrivateScoreWindowPatterns() {
        p1.setWindowPattern(wpSamePrivateScore1);
        p2.setWindowPattern(wpSamePrivateScore2);
        p3.setWindowPattern(wpSamePrivateScore3);
        p4.setWindowPattern(wpSamePrivateScore4);
    }

    private void assignSameScoreAndSamePrivateScoreWindowPatterns() {
        p1.setWindowPattern(wpSame1);
        p2.setWindowPattern(wpSame2);
        p3.setWindowPattern(wpSame3);
        p4.setWindowPattern(wpSame4);
    }

    private void addDefaultPlayersToLists() {
        playersOfGame.add(p1);
        playersOfGame.add(p2);
        playersOfGame.add(p3);
        playersOfGame.add(p4);

        playersOfLastRound.add(p1);
        playersOfLastRound.add(p2);
        playersOfLastRound.add(p3);
        playersOfLastRound.add(p4);

    }

    private void setSameWindowPattern(WindowPattern wp) {
        p1.setWindowPattern(wp);
        p2.setWindowPattern(wp);
        p3.setWindowPattern(wp);
        p4.setWindowPattern(wp);
    }

    private void setCustomPublicObjectiveCards() {
        publicObjectiveCards.clear();
        publicObjectiveCards.add(columnsValuePublicObjectiveCard);
        publicObjectiveCards.add(rowsColorPublicObjectiveCard);
        publicObjectiveCards.add(diagonalsPublicObjectiveCard);
    }



    private void initializeDefaultGame() {
        assignDefaultWindowPatterns();

        addDefaultPlayersToLists();

        setCustomPublicObjectiveCards();

        testRankings.add(p3);
        testRankings.add(p1);
        testRankings.add(p2);
        testRankings.add(p4);

        p1Score = 32 + p1.getFavorTokens();
        p2Score = 19 + p2.getFavorTokens();
        p3Score = 33 + p3.getFavorTokens();
        p4Score = 17 + p4.getFavorTokens();

        testScores = new HashMap<>();
        testScores.put(p3,p3Score);
        testScores.put(p1,p1Score);
        testScores.put(p2,p2Score);
        testScores.put(p4,p4Score);

    }
}