package it.polimi.se2018.model;

import it.polimi.se2018.controller.NoMoreRoundsAvailableException;
import it.polimi.se2018.controller.NoMoreTurnsAvailableException;
import it.polimi.se2018.controller.WindowPatternManager;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.EmptyListException;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.ValueOutOfBoundsException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static it.polimi.se2018.model.DiceColor.RED;
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
    private static ToolCard toolCard1;
    private static ToolCard toolCard2;
    private static ToolCard toolCard3;

    private static List<PublicObjectiveCard> publicObjectiveCards;
    private static List<ToolCard> toolCards;
    private static List<Dice> dices;
    private static Map<Player, Integer> rankings;
    private static EnumSet<Move> permissions;

    /**
     * Initializes variables for the tests
     */
    @BeforeClass
    public static void initializeVariables(){
        PrivateObjectiveCard privateObjectiveCard = new PrivateObjectiveCard("","","", DiceColor.RED);
        player = new Player( "player", privateObjectiveCard);

        publicObjectiveCards = new ArrayList<>();
        publicObjectiveCards.add(new DiagonalsPublicObjectiveCard(
                null,null, null, Dice::getColor));

        WindowPatternManager windowPatternManager = new WindowPatternManager();
        List<WindowPattern> windowPatterns = new ArrayList<>(windowPatternManager.getPairsOfPatterns(1));
        windowPattern = windowPatterns.get(0);
        permissions = EnumSet.of(Move.DRAFT_DICE_FROM_DRAFTPOOL, Move.USE_TOOLCARD, Move.END_TURN);
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

        toolCard1 = new ToolCard(properties, new HashMap<>(), null, new HashSet<>());

        properties.put("id","ID2");
        properties.put("title","title2");
        properties.put("description","description2");
        properties.put("imageURL","imageURL2");
        properties.put("neededTokens", "1");
        properties.put("tokensUsageMultiplier", "2");

        toolCard2 = new ToolCard(properties, new HashMap<>(), null, new HashSet<>());

        properties.put("id","ID3");
        properties.put("title","title3");
        properties.put("description","description3");
        properties.put("imageURL","imageURL3");
        properties.put("neededTokens", "1");
        properties.put("tokensUsageMultiplier", "2");

        toolCard3 = new ToolCard(properties, new HashMap<>(), null, new HashSet<>());
    }

    /**
     * Initializes the lists needed in the tests before each test
     */
    @Before
    public void initializeLists(){
        toolCards = new ArrayList<>();
        toolCards.add(toolCard1);

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
        game.startGame(dices, permissions);

        for(int i=1; i <= numberOfRounds; i++){
            try {
                game.nextRound(dices, permissions);
                for(int j=1; j <= maxNumberOfPlayers*2; j++){
                    try{
                        game.nextTurn(permissions);
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
     * @see Game#Game(int, int)
     */
    @Test
    public void testConstructor(){
        game = new Game(numberOfRounds, maxNumberOfPlayers);
        assertNotNull(game);
    }

    /**
     * Tests the impossibility of creating a {@link Game} with a negative number of rounds
     * @see Game#Game(int, int)
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
     * @see Game#Game(int, int)
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
     * @see Game#getCurrentRound()
     */
    @Test
    public void testGetCurrentRound(){
            game.setCards(toolCards, publicObjectiveCards);
            game.addPlayer(player);
            game.setStatusAsWaitingForPatternsChoice();
            game.assignWindowPatternToPlayer(windowPattern, player.getID());
            game.startGame(dices, permissions);
            assertNotNull(game.getCurrentRound());
    }

    /**
     * Tests the retrieval of the PublicObjectiveCards
     * @see Game#getDrawnPublicObjectiveCards()
     */
    @Test
    public void testGetDrawnPublicObjectiveCards(){
        game.setCards(toolCards, publicObjectiveCards);
        assertEquals(publicObjectiveCards, game.getDrawnPublicObjectiveCards());
    }

    /**
     * Tests that the {@link Track} in the game is not null
     * @see Game#getTrack()
     */
    @Test
    public void testTrackNotNull(){
        assertNotNull(game.getTrack());
    }

    /**
     * Tests setting the game ToolCards and PublicObjectiveCards
     * @see Game#setCards(List, List)
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
     * @see Game#setCards(List, List)
     */
    @Test
    public void testSetCardsTwice(){
        game.setCards(toolCards,publicObjectiveCards);

        try{
            game.setCards(toolCards,publicObjectiveCards);
            fail();
        }catch (IllegalStateException e){}
    }

    /**
     * Tests the current {@link Player} of the game
     * @see Game#isCurrentPlayer(String)
     */
    @Test
    public void testIsCurrentPlayer(){
        Player player = new Player("nickname", new PrivateObjectiveCard("","","",RED));
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);
        game.setStatusAsWaitingForPatternsChoice();
        game.assignWindowPatternToPlayer(windowPattern, player.getID());
        game.startGame(dices, permissions);
        assertTrue(game.isCurrentPlayer(player.getID()));
    }

    /**
     * Tests the impossibility of checking the current player if the game has not started
     * @see Game#isCurrentPlayer(String)
     */
    @Test
    public void testIsCurrentPlayerWhenIllegalStatus(){
        Player player = new Player("nickname", new PrivateObjectiveCard("","","",RED));
        try{
            game.isCurrentPlayer(player.getID());
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }

    /**
     * Tests setting the game status to waiting for patterns choice and assigning window patterns to players
     * @see Game#setStatusAsWaitingForPatternsChoice()
     * @see Game#assignWindowPatternToPlayer(WindowPattern, String)
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
     * @see Game#setStatusAsWaitingForPatternsChoice()
     */
    @Test
    public void testSetStatusAsWaitingForPatternsChoiceWhenIllegalStatus(){

        try{
            game.setStatusAsWaitingForPatternsChoice();
            fail();
        }catch (IllegalStateException e){}
    }

    /**
     * Tests the impossibility of adding a player when the game is not waiting for players
     * @see Game#addPlayer(Player)
     */
    @Test
    public void testAddPlayerWhenIllegalStatus(){
        try {
            game.addPlayer(player);
            fail();
        }catch (IllegalStateException e){}
    }

    /**
     * Tests adding a player when the game is waiting for players
     * @see Game#addPlayer(Player)
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
     * @see Game#addPlayer(Player)
     */
    @Test
    public void testAddSamePlayerTwice(){
        Game game1 = new Game (numberOfRounds, 2);
        game1.setCards(toolCards, publicObjectiveCards);

        game1.addPlayer(player);
        game1.addPlayer(player);

        List<Player> expectedPlayersOfGame = new ArrayList<>();
        expectedPlayersOfGame.add(player);

        assertEquals(expectedPlayersOfGame, game1.getPlayers());
    }

    /**
     * Tests the impossibility of adding more players than allowed
     * @see Game#addPlayer(Player)
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
     * @see Game#setRankings(Map)
     */
    @Test
    public void testSetRankingsWhenIllegalStatus(){
        rankings.put(player,9);
        try {
            game.setRankings(rankings);
            fail();
        }catch (IllegalStateException e) {}
    }

    /**
     * Tests the impossibility of setting the game rankings to null
     * @see Game#setRankings(Map)
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
     * Tests the usage of a {@link ToolCard} when the game has started
     * @see Game#useToolCard(ToolCard)
     */
    @Test
    public void testUseToolCard(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);
        game.setStatusAsWaitingForPatternsChoice();
        game.assignWindowPatternToPlayer(windowPattern, player.getID());
        try {
            game.startGame(dices, permissions);
        } catch (BadBehaviourRuntimeException e) {
            e.printStackTrace();
            fail();
        }

        game.useToolCard(toolCard1);
        assertTrue(game.getCurrentRound().getCurrentTurn().hasUsedToolCard());
    }

    /**
     * Tests the impossibility of using a {@link ToolCard} that is not part of the ToolCards of the game
     * @see Game#useToolCard(ToolCard)
     */
    @Test
    public void testUseToolCardNotInDrawnSet(){
        toolCards.clear();
        toolCards.add(toolCard2);

        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);
        game.setStatusAsWaitingForPatternsChoice();
        game.assignWindowPatternToPlayer(windowPattern, player.getID());
        try {
            game.startGame(dices, permissions);
        } catch (BadBehaviourRuntimeException e) {
            e.printStackTrace();
            fail();
        }

        try {
            game.useToolCard(toolCard1);
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }

    /**
     * Tests the impossibility of using a {@link ToolCard} when the game has not started
     * @see Game#useToolCard(ToolCard)
     */
    @Test
    public void testUseToolCardWhenIllegalStatus(){
        try {
            game.useToolCard(toolCard1);
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }

    /**
     * Tests the retrieval of a {@link ToolCard}
     * @see Game#getToolCard(ToolCard)
     */
    @Test
    public void testGetToolCard(){
        game.setCards(toolCards, publicObjectiveCards);
        ToolCard gameToolCard = game.getToolCard(toolCard1);
        assertEquals(toolCard1, gameToolCard);
    }

    /**
     * Tests the impossibility of retrieving a null {@link ToolCard}
     * @see Game#getToolCard(ToolCard)
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
     * @see Game#getToolCard(ToolCard)
     */
    @Test
    public void testGetToolCardNotInDrawnToolCards(){

        toolCards.clear();
        toolCards.add(toolCard2);
        toolCards.add(toolCard3);

        game.setCards(toolCards, publicObjectiveCards);

        try {
            game.getToolCard(toolCard1);
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }

    /**
     * Tests the normal start of a game
     * @see Game#startGame(List, Set)
     */
    @Test
    public void testStartGame(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);
        game.setStatusAsWaitingForPatternsChoice();
        game.assignWindowPatternToPlayer(windowPattern, player.getID());

        try {
            game.startGame(dices, permissions);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        assertNotNull(game.getCurrentRound());
    }

    /**
     * Tests the impossibility of starting a game with no {@link Round}
     * @see Game#startGame(List, Set)
     */
    @Test
    public void testStartGameWithNoRounds(){
        Game game1 = new Game(0, maxNumberOfPlayers);
        game1.setCards(toolCards, publicObjectiveCards);
        game1.addPlayer(player);
        game1.setStatusAsWaitingForPatternsChoice();
        game1.assignWindowPatternToPlayer(windowPattern, player.getID());

        try {
            game1.startGame(dices, permissions);
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }



    /**
     * Tests the impossibility of starting a game with null dices
     * @see Game#startGame(List, Set)
     */
    @Test
    public void testStartGameWithNullDices(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);

        try {
            game.startGame(null, permissions);
            fail();
        } catch (BadBehaviourRuntimeException e) {
            e.printStackTrace();
            fail();
        } catch (IllegalArgumentException e){}
    }

    /**
     * Tests the impossibility of starting a game with a list of no dices
     * @see Game#startGame(List, Set)
     */
    @Test
    public void testStartGameWithEmptyDices(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);

        try {
            game.startGame(new ArrayList<>(), permissions);
            fail();
        } catch (BadBehaviourRuntimeException e) {
            e.printStackTrace();
            fail();
        } catch (EmptyListException e){}
    }

    /**
     * Tests the impossibility of starting a game when not waiting for patterns choice
     * @see Game#startGame(List, Set)
     */
    @Test
    public void testStartGameWhenIllegalStatus(){
        try {
            game.startGame(dices, permissions);
            fail();
        } catch (IllegalStateException e) {}
    }

    /**
     * Tests the progress of a game by proceeding to the next {@link Round}
     * @see Game#nextRound(List, Set)
     */
    @Test
    public void testNextRound(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);
        game.setStatusAsWaitingForPatternsChoice();
        game.assignWindowPatternToPlayer(windowPattern, player.getID());
        game.startGame(dices, permissions);

        try {
            game.nextRound(dices, permissions);
        } catch (NoMoreRoundsAvailableException e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(1, game.getCurrentRound().getNumber());
    }

    /**
     * Tests the impossibility of proceeding to the next {@link Round} with null dices
     * @see Game#nextRound(List, Set)
     */
    @Test
    public void testNextRoundWithNullDices(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);
        game.setStatusAsWaitingForPatternsChoice();
        game.assignWindowPatternToPlayer(windowPattern, player.getID());
        game.startGame(dices, permissions);

        try {
            game.nextRound(null, permissions);
            fail();
        } catch (NoMoreRoundsAvailableException | EmptyListException e) {
            e.printStackTrace();
            fail();
        } catch (IllegalArgumentException e){}
    }


    /**
     * Tests the impossibility of proceeding to the next {@link Round} with a list of no dices
     * @see Game#nextRound(List, Set)
     */
    @Test
    public void testNextRoundWithEmptyDices(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);
        game.setStatusAsWaitingForPatternsChoice();
        game.assignWindowPatternToPlayer(windowPattern, player.getID());
        game.startGame(dices, permissions);

        try {
            game.nextRound(new ArrayList<>(), permissions);
            fail();
        } catch (NoMoreRoundsAvailableException e) {
            e.printStackTrace();
            fail();
        } catch (EmptyListException e){}
    }

    /**
     * Tests the impossibility of proceeding to the next {@link Round} if the game has not started
     * @see Game#nextRound(List, Set)
     */
    @Test
    public void testNextRoundWhenIllegalStatus(){
        try {
            game.nextRound(dices,permissions);
            fail();
        } catch (IllegalArgumentException | NoMoreRoundsAvailableException e) {
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }

    /**
     * Tests the progress of a game by proceeding to the next {@link Turn}
     * @see Game#nextTurn(Set)
     */
    @Test
    public void testNextTurn(){
        game.setCards(toolCards, publicObjectiveCards);
        game.addPlayer(player);
        game.setStatusAsWaitingForPatternsChoice();
        game.assignWindowPatternToPlayer(windowPattern, player.getID());
        game.startGame(dices, permissions);

        try {
            game.nextTurn(permissions);
        } catch (NoMoreTurnsAvailableException e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(1, game.getCurrentRound().getCurrentTurn().getNumber());
    }

    /**
     * Tests the impossibility of proceeding to the next {@link Turn} if the game has not started
     * @see Game#nextTurn(Set)
     */
    @Test
    public void testNextTurnWhenIllegalStatus(){
        try {
            game.nextTurn(permissions);
            fail();
        } catch (NoMoreTurnsAvailableException e) {
            fail();
        }catch (BadBehaviourRuntimeException e){}
    }
}