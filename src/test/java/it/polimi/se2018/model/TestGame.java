package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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

    @BeforeClass
    public static void initializeVariables(){
        user = new User(1,"username");
        privateObjectiveCard = new PrivateObjectiveCard(null,null,null,DiceColors.RED);
        player = new Player(user, "player", privateObjectiveCard);
        publicObjectiveCards = new ArrayList<>();
        publicObjectiveCards.add(new DiagonalsPublicObjectiveCard(
                null,null, null, Dice::getColor));
        toolCard = ToolCard.createTestInstance();
        toolCards = new ArrayList<>();
        toolCards.add(toolCard);
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
    public void testAddPlayer(){
        game.addPlayer(player);

        List<Player> expectedPlayersOfGame = new ArrayList<>();
        expectedPlayersOfGame.add(player);

        List<Player> players = game.getPlayers();

        assertEquals(expectedPlayersOfGame, players);
    }

    @Test
    public void testAddSamePlayerTwice(){
        game.addPlayer(player);
        game.addPlayer(player);

        List<Player> expectedPlayersOfGame = new ArrayList<>();
        expectedPlayersOfGame.add(player);

        List<Player> players = game.getPlayers();

        assertEquals(expectedPlayersOfGame, players);
    }

    @Test
    public void testCanAcceptPlayer(){
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
    public void testGetToolCard(){
        game.setCards(toolCards, publicObjectiveCards);
        ToolCard gameToolCard = game.getToolCard(toolCard);
        assertEquals(toolCard, gameToolCard);
    }

}