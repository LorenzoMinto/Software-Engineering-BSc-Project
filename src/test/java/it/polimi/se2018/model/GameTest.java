package it.polimi.se2018.model;

import it.polimi.se2018.controller.NoMoreRoundsAvailableException;
import it.polimi.se2018.controller.WindowPatternManager;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.EmptyListException;
import it.polimi.se2018.utils.ValueOutOfBoundsException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Jacopo Pio Gargano
 */

public class GameTest {

    private static WindowPattern windowPattern;
    private Game game;
    private static final int numberOfRounds = 10;
    private static final int maxNumberOfPlayers = 4;
    private static PrivateObjectiveCard privateObjectiveCard;
    private static Player player;
    private static ToolCard toolCard;
    private static ToolCard toolCard1;
    private static ToolCard toolCard2;
    private static ToolCard toolCard3;

    private static List<PublicObjectiveCard> publicObjectiveCards;
    private static List<ToolCard> toolCards;
    private static List<Dice> dices;
    private static Map<Player, Integer> rankings;
    private static Properties properties;


    @BeforeClass
    public static void initializeVariables(){
        privateObjectiveCard = new PrivateObjectiveCard(null,null,null,DiceColors.RED);
        player = new Player( "player", privateObjectiveCard);

        publicObjectiveCards = new ArrayList<>();
        publicObjectiveCards.add(new DiagonalsPublicObjectiveCard(
                null,null, null, Dice::getColor));

        toolCard = ToolCard.createTestInstance();
        WindowPatternManager windowPatternManager = new WindowPatternManager();
        List<WindowPattern> windowPatterns = new ArrayList<>(windowPatternManager.getPairsOfPatterns(1));
        windowPattern = windowPatterns.get(0);
    }

    @BeforeClass
    public static void initializeToolCards(){
        properties = new Properties();

        properties.put("id","ID1");
        properties.put("title","title1");
        properties.put("description","description1");
        properties.put("imageURL","imageURL1");
        properties.put("neededTokens", "1");
        properties.put("tokensUsageMultiplier", "2");

        toolCard1 = new ToolCard(properties, null, null );

        properties.put("id","ID2");
        properties.put("title","title2");
        properties.put("description","description2");
        properties.put("imageURL","imageURL2");
        properties.put("neededTokens", "1");
        properties.put("tokensUsageMultiplier", "2");

        toolCard2 = new ToolCard(properties, null, null );

        properties.put("id","ID3");
        properties.put("title","title3");
        properties.put("description","description3");
        properties.put("imageURL","imageURL3");
        properties.put("neededTokens", "1");
        properties.put("tokensUsageMultiplier", "2");

        toolCard2 = new ToolCard(properties, null, null );

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

    private void runAllRounds() {
        game.startGame(dices);

        for(int i=1; i <= numberOfRounds; i++){
            try {
                game.nextRound(dices);
            } catch (IllegalArgumentException | BadBehaviourRuntimeException e){
                e.printStackTrace();
                fail();
            } catch (NoMoreRoundsAvailableException e){}
        }
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
        }catch (ValueOutOfBoundsException e){}
    }

    @Test
    public void testConstructorWithNegativeMaxNumberOfPlayers(){
        try{
            game = new Game(numberOfRounds, -1);
            fail();
        }catch (ValueOutOfBoundsException e){}
    }

    @Test
    public void testGetCurrentRound(){
            game.setCards(toolCards, publicObjectiveCards);
            game.addPlayer(player);
            game.setStatusAsWaitingForPatternsChoice();
            game.assignWindowPatternToPlayer(windowPattern, player.getID());
            game.startGame(dices);
            assertNotNull(game.getCurrentRound());
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
            e.printStackTrace();
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
    public void testSetRankingsWhenIllegalStatus(){
        rankings.put(player,9);
        try {
            game.setRankings(rankings);
            fail();
        }catch (BadBehaviourRuntimeException e) {}
    }

    @Test
    public void testSetRankingsToNull(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);
        game.setStatusAsWaitingForPatternsChoice();
        game.assignWindowPatternToPlayer(windowPattern, player.getID());

        runAllRounds();

        try{
            game.setRankings(null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    @Test
    public void testSetRankings(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);
        game.setStatusAsWaitingForPatternsChoice();
        game.assignWindowPatternToPlayer(windowPattern, player.getID());
        runAllRounds();

        game.setRankings(rankings);
        assertEquals(rankings, game.getRankings());
    }


    @Test
    public void testUseToolCard(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);
        game.setStatusAsWaitingForPatternsChoice();
        game.assignWindowPatternToPlayer(windowPattern, player.getID());
        try {
            game.startGame(dices);
        } catch (BadBehaviourRuntimeException e) {
            e.printStackTrace();
            fail();
        }

        game.useToolCard(toolCard);
        assertTrue(game.getCurrentRound().getCurrentTurn().hasUsedToolCard());
    }

    @Test
    public void testUseToolCardNotInDrawnSet(){
        Properties properties1 = new Properties();

        properties1.put("id","ID1");
        properties1.put("title","title1");
        properties1.put("description","description1");
        properties1.put("imageURL","imageURL1");
        properties1.put("neededTokens", "1");
        properties1.put("tokensUsageMultiplier", "2");

        ToolCard toolCard1 = new ToolCard(properties1, null, null );

        Properties properties2 = new Properties();
        properties2.put("id","ID2");
        properties2.put("title","title2");
        properties2.put("description","description2");
        properties2.put("imageURL","imageURL2");
        properties2.put("neededTokens", "1");
        properties2.put("tokensUsageMultiplier", "2");

        ToolCard toolCard2 = new ToolCard(properties2, null, null );

        toolCards.clear();
        toolCards.add(toolCard1);

        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);
        game.setStatusAsWaitingForPatternsChoice();
        game.assignWindowPatternToPlayer(windowPattern, player.getID());
        try {
            game.startGame(dices);
        } catch (BadBehaviourRuntimeException e) {
            e.printStackTrace();
            fail();
        }

        try {
            game.useToolCard(toolCard2);
            fail();
        }catch (BadBehaviourRuntimeException e){}
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
        }catch (NullPointerException e){}
    }

    @Test
    public void testGetToolCardNotInDrawnToolCards(){

        toolCards.clear();
        toolCards.add(toolCard1);
        toolCards.add(toolCard2);

        game.setCards(toolCards, publicObjectiveCards);

        try {
            game.getToolCard(toolCard3);
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }

    @Test
    public void testStartGame(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);
        game.setStatusAsWaitingForPatternsChoice();
        game.assignWindowPatternToPlayer(windowPattern, player.getID());

        try {
            game.startGame(dices);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        assertNotNull(game.getCurrentRound());
    }

    @Test
    public void testStartGameWithNullDices(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);

        try {
            game.startGame(null);
            fail();
        } catch (BadBehaviourRuntimeException e) {
            e.printStackTrace();
            fail();
        } catch (IllegalArgumentException e){}
    }

    @Test
    public void testStartGameWithEmptyDices(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);

        try {
            game.startGame(new ArrayList<>());
            fail();
        } catch (BadBehaviourRuntimeException e) {
            e.printStackTrace();
            fail();
        } catch (EmptyListException e){}
    }

    @Test
    public void testStartGameIllegalStatus(){
        try {
            game.startGame(dices);
            fail();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail();
        } catch (BadBehaviourRuntimeException e){}
    }

    @Test
    public void testNextRound(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);
        game.setStatusAsWaitingForPatternsChoice();
        game.assignWindowPatternToPlayer(windowPattern, player.getID());
        game.startGame(dices);

        try {
            game.nextRound(dices);
        } catch (NoMoreRoundsAvailableException e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(1, game.getCurrentRound().getNumber());
    }

    @Test
    public void testNextRoundWithNullDices(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);
        game.setStatusAsWaitingForPatternsChoice();
        game.assignWindowPatternToPlayer(windowPattern, player.getID());
        game.startGame(dices);

        try {
            game.nextRound(null);
            fail();
        } catch (NoMoreRoundsAvailableException | EmptyListException e) {
            e.printStackTrace();
            fail();
        } catch (IllegalArgumentException e){}
    }

    @Test
    public void testNextRoundWithEmptyDices(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);
        game.setStatusAsWaitingForPatternsChoice();
        game.assignWindowPatternToPlayer(windowPattern, player.getID());
        game.startGame(dices);

        try {
            game.nextRound(new ArrayList<>());
            fail();
        } catch (NoMoreRoundsAvailableException e) {
            e.printStackTrace();
            fail();
        } catch (EmptyListException e){}
    }

    @Test
    public void testNextRoundIllegalStatus(){
        try {
            game.nextRound(dices);
            fail();
        } catch (IllegalArgumentException | NoMoreRoundsAvailableException e) {
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }
}