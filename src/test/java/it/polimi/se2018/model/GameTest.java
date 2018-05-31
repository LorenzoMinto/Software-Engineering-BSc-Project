package it.polimi.se2018.model;

import it.polimi.se2018.controller.NoMoreRoundsAvailableException;
import it.polimi.se2018.controller.NoMoreTurnsAvailableException;
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
 * Test for {@link Game} class
 *
 * @author Jacopo Pio Gargano
 */

public class GameTest {

    private Game game;

    private static WindowPattern windowPattern;
    private static final int numberOfRounds = 10;
    private static final int maxNumberOfPlayers = 1;
    private static Player player;
    private static ToolCard toolCard;
    private static ToolCard toolCard1;
    private static ToolCard toolCard2;
    private static ToolCard toolCard3;

    private static List<PublicObjectiveCard> publicObjectiveCards;
    private static List<ToolCard> toolCards;
    private static List<Dice> dices;
    private static Map<Player, Integer> rankings;


    /**
     * Initializes variables for the tests
     */
    @BeforeClass
    public static void setUp(){
        PrivateObjectiveCard privateObjectiveCard = new PrivateObjectiveCard(null, null, null, DiceColor.RED);
        player = new Player( "player", privateObjectiveCard);

        publicObjectiveCards = new ArrayList<>();
        publicObjectiveCards.add(new DiagonalsPublicObjectiveCard(
                null,null, null, Dice::getColor));

        toolCard = ToolCard.createTestInstance();
        WindowPatternManager windowPatternManager = new WindowPatternManager();
        List<WindowPattern> windowPatterns = new ArrayList<>(windowPatternManager.getPairsOfPatterns(1));
        windowPattern = windowPatterns.get(0);
    }

    /**
     * Initializes the ToolCards needed in the tests
     */
    @BeforeClass
    public static void initializeToolCards(){
        Properties properties = new Properties();

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

        toolCard3 = ToolCard.createTestInstance();

    }

    /**
     * Initializes the lists needed in the tests before each test
     */
    @Before
    public void initializeLists(){
        toolCards = new ArrayList<>();
        toolCards.add(toolCard);

        dices = new ArrayList<>();
        dices.add(new Dice(DiceColor.RED));

        rankings = new HashMap<>();
        rankings.put(player, 0);
    }

    /**
     * Initializes the game before each test to reset properties
     */
    @Before
    public void initializeGame(){
        game = new Game(numberOfRounds, maxNumberOfPlayers);
    }

    /**
     * Runs all rounds of a game
     */
    private void runAllRounds() {
        game.startGame(dices);

        for(int i=1; i <= numberOfRounds; i++){
            try {
                game.nextRound(dices);
                for(int j=1; j <= 2; j++){
                    try{
                        game.nextTurn();
                    }catch (NoMoreTurnsAvailableException e ){}
                }
            } catch (IllegalArgumentException | BadBehaviourRuntimeException e){
                e.printStackTrace();
                fail();
            } catch (NoMoreRoundsAvailableException e){}
        }
    }

    /**
     * Tests the constructor of {@link Game} class with legal parameters
     */
    @Test
    public void testConstructor(){
        game = new Game(numberOfRounds, maxNumberOfPlayers);
        assertNotNull(game);
    }

    /**
     * Tests the impossibility of creating a {@link Game} with a negative number of rounds
     */
    @Test
    public void testConstructorWithNegativeNumberOfRounds(){
        try{
            game = new Game(-1, maxNumberOfPlayers);
            fail();
        }catch (ValueOutOfBoundsException e){}
    }

    /**
     * Tests the impossibility of creating a {@link Game} with a negative number of players
     */
    @Test
    public void testConstructorWithNegativeMaxNumberOfPlayers(){
        try{
            game = new Game(numberOfRounds, -1);
            fail();
        }catch (ValueOutOfBoundsException e){}
    }

    /**
     * Tests the retrieval of the current round after starting the game
     */
    @Test
    public void testGetCurrentRound(){
            game.setCards(toolCards, publicObjectiveCards);
            game.addPlayer(player);
            game.setStatusAsWaitingForPatternsChoice();
            game.assignWindowPatternToPlayer(windowPattern, player.getID());
            game.startGame(dices);
            assertNotNull(game.getCurrentRound());
    }

    /**
     * Tests the retrieval of the PublicObjectiveCards
     */
    @Test
    public void testGetDrawnPublicObjectiveCards(){
        game.setCards(toolCards, publicObjectiveCards);
        assertEquals(publicObjectiveCards, game.getDrawnPublicObjectiveCards());
    }

    /**
     * Tests that the {@link Track} in the game is not null
     */
    @Test
    public void testTrackNotNull(){
        assertNotNull(game.getTrack());
    }

    /**
     * Tests setting the game ToolCards and PublicObjectiveCards
     */
    @Test
    public void testSetCards(){
        try{
            game.setCards(toolCards,publicObjectiveCards);
        }catch (BadBehaviourRuntimeException e){
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Tests the impossibility of setting the cards of a game twice
     */
    @Test
    public void testSetCardsTwice(){
        game.setCards(toolCards,publicObjectiveCards);

        try{
            game.setCards(toolCards,publicObjectiveCards);
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }

    /**
     * Tests the current {@link Player} of the game
     */
    @Test
    public void testIsCurrentPlayer(){
        Player player = new Player("nickname", PrivateObjectiveCard.createTestInstance());
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);
        game.setStatusAsWaitingForPatternsChoice();
        game.assignWindowPatternToPlayer(windowPattern, player.getID());
        game.startGame(dices);
        assertTrue(game.isCurrentPlayer(player.getID()));
    }

    /**
     * Tests the impossibility of checking the current player if the game has not started
     */
    @Test
    public void testIsCurrentPlayerWhenIllegalStatus(){
        Player player = new Player("nickname", PrivateObjectiveCard.createTestInstance());
        try{
            game.isCurrentPlayer(player.getID());
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }

    /**
     * Tests setting the game status to waiting for patterns choice
     */
    @Test
    public void testSetStatusAsWaitingForPatternsChoice(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);

        try{
            game.setStatusAsWaitingForPatternsChoice();
        }catch (BadBehaviourRuntimeException e){
            e.printStackTrace();
            fail();
        }

        try{
            game.assignWindowPatternToPlayer(windowPattern, player.getID());
        }catch (BadBehaviourRuntimeException e){
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Tests setting the game status to waiting for patterns choice when game is not waiting for players
     */
    @Test
    public void testSetStatusAsWaitingForPatternsChoiceWhenIllegalStatus(){

        try{
            game.setStatusAsWaitingForPatternsChoice();
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }

    /**
     * Tests the impossibility of adding a player when the game is not waiting for players
     */
    @Test
    public void testAddPlayerWhenIllegalStatus(){
        try {
            game.addPlayer(player);
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }

    /**
     * Tests adding a player when the game is waiting for players
     */
    @Test
    public void testAddPlayer(){
        game.setCards(toolCards, publicObjectiveCards);

        game.addPlayer(player);

        List<Player> expectedPlayersOfGame = new ArrayList<>();
        expectedPlayersOfGame.add(player);

        List<Player> players = game.getPlayers();

        assertEquals(expectedPlayersOfGame, players);
    }

    /**
     * Tests the impossibility of adding the same player twice
     */
    @Test
    public void testAddSamePlayerTwice(){
        Game game1 = new Game (numberOfRounds, 2);
        game1.setCards(toolCards, publicObjectiveCards);

        game1.addPlayer(player);
        game1.addPlayer(player);

        List<Player> expectedPlayersOfGame = new ArrayList<>();
        expectedPlayersOfGame.add(player);

        List<Player> players = game1.getPlayers();

        assertEquals(expectedPlayersOfGame, players);
    }

    /**
     * Tests the impossibility of adding more players than allowed
     */
    @Test
    public void testAddMorePlayersThanAllowed(){
        Game game1 = new Game (numberOfRounds, 1);
        game1.setCards(toolCards, publicObjectiveCards);

        game1.addPlayer(player);
        try{
            game1.addPlayer(player);
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }


    /**
     * Tests the impossibility of setting the game rankings when the game has not ended
     */
    @Test
    public void testSetRankingsWhenIllegalStatus(){
        rankings.put(player,9);
        try {
            game.setRankings(rankings);
            fail();
        }catch (BadBehaviourRuntimeException e) {}
    }

    /**
     * Tests the impossibility of setting the game rankings to null
     */
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

    /**
     * Tests setting the game rankings when the game has ended
     */
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


    /**
     * Tests the usage of a {@link ToolCard} when the game has started
     */
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

    /**
     * Tests the impossibility of using a {@link ToolCard} that is not part of the ToolCards of the game
     */
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

    /**
     * Tests the impossibility of using a {@link ToolCard} when the game has not started
     */
    @Test
    public void testUseToolCardWhenIllegalStatus(){
        try {
            game.useToolCard(ToolCard.createTestInstance());
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }

    /**
     * Tests the retrieval of a {@link ToolCard}
     */
    @Test
    public void testGetToolCard(){
        game.setCards(toolCards, publicObjectiveCards);
        ToolCard gameToolCard = game.getToolCard(toolCard);
        assertEquals(toolCard, gameToolCard);
    }

    /**
     * Tests the impossibility of retrieving a null {@link ToolCard}
     */
    @Test
    public void testGetNullToolCard(){
        try {
            game.getToolCard(null);
            fail();
        }catch (NullPointerException e){}
    }

    /**
     * Tests the impossibility of getting a {@link ToolCard} that is not in the drawn set
     */
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

    /**
     * Tests the normal start of a game
     */
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

    /**
     * Tests the impossibility of starting a game with no {@link Round}
     */
    @Test
    public void testStartGameWithNoRounds(){
        Game game1 = new Game(0, maxNumberOfPlayers);
        game1.setCards(toolCards, publicObjectiveCards);
        game1.addPlayer(player);
        game1.setStatusAsWaitingForPatternsChoice();
        game1.assignWindowPatternToPlayer(windowPattern, player.getID());

        try {
            game1.startGame(dices);
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }



    /**
     * Tests the impossibility of starting a game with null dices
     */
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

    /**
     * Tests the impossibility of starting a game with a list of no dices
     */
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

    /**
     * Tests the impossibility of starting a game when not waiting for patterns choice
     */
    @Test
    public void testStartGameWhenIllegalStatus(){
        try {
            game.startGame(dices);
            fail();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail();
        } catch (BadBehaviourRuntimeException e){}
    }

    /**
     * Tests the progress of a game by proceeding to the next {@link Round}
     */
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

    /**
     * Tests the impossibility of proceeding to the next {@link Round} with null dices
     */
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


    /**
     * Tests the impossibility of proceeding to the next {@link Round} with a list of no dices
     */
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

    /**
     * Tests the impossibility of proceeding to the next {@link Round} if the game has not started
     */
    @Test
    public void testNextRoundWhenIllegalStatus(){
        try {
            game.nextRound(dices);
            fail();
        } catch (IllegalArgumentException | NoMoreRoundsAvailableException e) {
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }

    /**
     * Tests the progress of a game by proceeding to the next {@link Turn}
     */
    @Test
    public void testNextTurn(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);
        game.setStatusAsWaitingForPatternsChoice();
        game.assignWindowPatternToPlayer(windowPattern, player.getID());
        game.startGame(dices);

        try {
            game.nextTurn();
        } catch (NoMoreTurnsAvailableException e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(1, game.getCurrentRound().getCurrentTurn().getNumber());
    }

    /**
     * Tests the impossibility of proceeding to the next {@link Turn} if the game has not started
     */
    @Test
    public void testNextTurnWhenIllegalStatus(){
        try {
            game.nextTurn();
            fail();
        } catch (NoMoreTurnsAvailableException e) {
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }
}