package it.polimi.se2018.model;

import it.polimi.se2018.controller.NoMoreRoundsAvailableException;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Jacopo Pio Gargano
 */

public class TestGame {

    private Game game;
    private static final int numberOfRounds = 10;
    private static final int maxNumberOfPlayers = 4;
    private static User user;
    private static PrivateObjectiveCard privateObjectiveCard;
    private static Player player;
    private static ToolCard toolCard;

    private static List<PublicObjectiveCard> publicObjectiveCards;
    private static List<ToolCard> toolCards;
    private static List<Dice> dices;
    private static Map<Player, Integer> rankings;

    @BeforeClass
    public static void initializeVariables(){
        user = new User(1,"username");
        privateObjectiveCard = new PrivateObjectiveCard(null,null,null,DiceColors.RED);
        player = new Player(user, "player", privateObjectiveCard);

        publicObjectiveCards = new ArrayList<>();
        publicObjectiveCards.add(new DiagonalsPublicObjectiveCard(
                null,null, null, Dice::getColor));

        toolCard = ToolCard.createTestInstance();
    }

    @Before
    public void initializeLists(){
        toolCards = new ArrayList<>();
        toolCards.add(toolCard);

        dices = new ArrayList<>();
        dices.add(new Dice(DiceColors.RED));

        rankings = new HashMap<>();
        rankings.put(player, 0);
    }

    @Before
    public void initializeGame(){
        game = new Game(numberOfRounds, maxNumberOfPlayers);
    }


    @Test
    public void testConstructor(){
        game = new Game(numberOfRounds, maxNumberOfPlayers);
        assertNotNull(game);
    }

    @Test
    public void testConstructorWithNegativeNumberOfRounds(){
        try{
            game = new Game(-1, maxNumberOfPlayers);
            fail();
        }catch (IllegalArgumentException e){}
    }

    @Test
    public void testConstructorWithNegativeMaxNumberOfPlayers(){
        try{
            game = new Game(numberOfRounds, -1);
            fail();
        }catch (IllegalArgumentException e){}
    }

    @Test
    public void testGetCurrentRound(){
        try {
            game.setCards(toolCards, publicObjectiveCards);
            game.addPlayer(player);
            game.nextRound(dices);
            assertNotNull(game.getCurrentRound());
        } catch (NoMoreRoundsAvailableException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTrackNotNull(){
        assertNotNull(game.getTrack());
    }

    @Test
    public void testSetCards(){
        try{
            game.setCards(toolCards,publicObjectiveCards);
        }catch (BadBehaviourRuntimeException e){
            fail();
        }
    }

    @Test
    public void testSetCardsTwice(){
        game.setCards(toolCards,publicObjectiveCards);

        try{
            game.setCards(toolCards,publicObjectiveCards);
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }

    @Test
    public void testAddPlayerWhenIllegalStatus(){
        try {
            game.addPlayer(player);
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }

    @Test
    public void testAddPlayer(){
        game.setCards(toolCards, publicObjectiveCards);

        assertTrue(game.canAcceptNewPlayer());

        game.addPlayer(player);

        List<Player> expectedPlayersOfGame = new ArrayList<>();
        expectedPlayersOfGame.add(player);

        List<Player> players = game.getPlayers();

        assertEquals(expectedPlayersOfGame, players);
    }

    @Test
    public void testAddSamePlayerTwice(){
        game.setCards(toolCards, publicObjectiveCards);

        game.addPlayer(player);
        game.addPlayer(player);

        List<Player> expectedPlayersOfGame = new ArrayList<>();
        expectedPlayersOfGame.add(player);

        List<Player> players = game.getPlayers();

        assertEquals(expectedPlayersOfGame, players);
    }

    @Test
    public void testCanAcceptPlayerAfterMaxPlayersJoined(){
        game.setCards(toolCards, publicObjectiveCards);

        player = new Player(user, "player1", privateObjectiveCard);
        game.addPlayer(player);
        player = new Player(user, "player2", privateObjectiveCard);
        game.addPlayer(player);
        player = new Player(user, "player3", privateObjectiveCard);
        game.addPlayer(player);
        player = new Player(user, "player4", privateObjectiveCard);
        game.addPlayer(player);

        assertFalse(game.canAcceptNewPlayer());
    }


    @Test
    public void testSetRankingsWhenIllegalStatus(){
        try {
            game.setRankings(rankings);
            fail();
        }catch (BadBehaviourRuntimeException e) {}
    }

    @Test
    public void testSetRankingsToNull(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);

        for(int i=0; i <= numberOfRounds; i++){
            try {
                game.nextRound(dices);
            } catch (NoMoreRoundsAvailableException e) {}
        }

        try{
            game.setRankings(null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    @Test
    public void testSetRankings(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);

        for(int i=0; i <= numberOfRounds; i++){
            try {
                game.nextRound(dices);
            } catch (NoMoreRoundsAvailableException e) {}
        }

        game.setRankings(rankings);
        assertEquals(rankings, game.getRankings());
    }

    @Test
    public void testUseToolCard(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);
        try {
            game.nextRound(dices);
        } catch (NoMoreRoundsAvailableException e) {}

        game.useToolCard(toolCard);
        assertTrue(game.getCurrentRound().getCurrentTurn().hasUsedToolCard());
    }

    @Test
    public void testUseToolCardNotInDrawnSet(){
        ToolCard toolCard1 = new ToolCard("ID1","title1",null, null, 3,
                3, null, null );
        ToolCard toolCard2 = new ToolCard("ID2","title2",null, null, 3,
                3, null, null );

        toolCards.clear();
        toolCards.add(toolCard1);

        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);
        try {
            game.nextRound(dices);
        } catch (NoMoreRoundsAvailableException e) {}

        try {
            game.useToolCard(toolCard2);
            fail();
        }catch (IllegalArgumentException e){}
    }

    @Test
    public void testUseToolCardWhenIllegalStatus(){
        try {
            game.useToolCard(ToolCard.createTestInstance());
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }

    @Test
    public void testGetToolCard(){
        game.setCards(toolCards, publicObjectiveCards);
        ToolCard gameToolCard = game.getToolCard(toolCard);
        assertEquals(toolCard, gameToolCard);
    }

    @Test
    public void testGetNullToolCard(){
        try {
            game.getToolCard(null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    @Test
    public void testGetToolCardNotContainedInDrawnToolCards(){
        ToolCard toolCard1 = new ToolCard("ID1","title1",null, null, 3,
                3, null, null );
        ToolCard toolCard2 = new ToolCard("ID2","title2",null, null, 3,
                3, null, null );
        ToolCard toolCard3 = new ToolCard("ID3","title3",null, null, 3,
                3, null, null );

        toolCards.clear();
        toolCards.add(toolCard1);
        toolCards.add(toolCard2);

        game.setCards(toolCards, publicObjectiveCards);

        try {
            game.getToolCard(toolCard3);
            fail();
        }catch (IllegalArgumentException e){}
    }

    @Test
    public void testNextRound(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);

        try {
            game.nextRound(dices);
        } catch (NoMoreRoundsAvailableException e) {
            fail();
        }
        assertNotNull(game.getCurrentRound());
    }

    @Test
    public void testNextRoundWithNullDices(){
        try {
            game.nextRound(null);
            fail();
        } catch (NoMoreRoundsAvailableException e) {
            fail();
        } catch (IllegalArgumentException e){}
    }

    @Test
    public void testNextRoundWhenIllegalStatus(){
        try {
            game.nextRound(dices);
            fail();
        } catch (NoMoreRoundsAvailableException e) {
            fail();
        } catch (BadBehaviourRuntimeException e){}
    }
}