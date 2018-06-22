package it.polimi.se2018.model;

import it.polimi.se2018.controller.NoMoreTurnsAvailableException;
import it.polimi.se2018.utils.EmptyListException;
import it.polimi.se2018.utils.ValueOutOfBoundsException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static it.polimi.se2018.model.DiceColor.*;
import static org.junit.Assert.*;

import java.util.*;

/**
 * Test for {@link Round} class
 *
 * @author Jacopo Pio Gargano
 */

public class RoundTest {

    private Round round;

    private static DraftPool draftPool;

    private static List<Player> players = new ArrayList<>();
    private static Player p1;
    private static Player p2;
    private static Player p3;
    private static Player p4;

    /**
     * Initializes variables for the tests
     */
    @BeforeClass
    public static void setUp(){
        PrivateObjectiveCard privateObjectiveCard = new PrivateObjectiveCard("","","", RED);

        p1 = new Player( "p1", privateObjectiveCard);
        p2 = new Player( "p2", privateObjectiveCard);
        p3 = new Player( "p3", privateObjectiveCard);
        p4 = new Player( "p4", privateObjectiveCard);

        draftPool = new DraftPool(new ArrayList<>());
    }


    /**
     * Initializes players list before each test
     */
    @Before
    public void initializePlayersList(){
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
    }

    /**
     * Clears the players list after each test
     */
    @After
    public void clearList(){
        players.clear();
    }

    /**
     * Tests the constructor with allowed parameters
     * @see Round#Round(int, int, List, DraftPool)
     */
    @Test
    public void testConstructor(){
        round = new Round(0,players.size(), players, draftPool);
        assertNotNull(round);
    }

    /**
     * Tests the impossibility of creating a round with a negative number
     * @see Round#Round(int, int, List, DraftPool)
     */
    @Test
    public void testConstructorWithNegativeRoundNumber(){
        try{
            round = new Round(-1, players.size()*2, players, draftPool);
            fail();
        }catch (ValueOutOfBoundsException e){}
    }

    /**
     * Tests the impossibility of creating a round with a negative number of turns
     * @see Round#Round(int, int, List, DraftPool)
     */
    @Test
    public void testConstructorWithNegativeNumberOfTurns(){
        try{
            round = new Round(0, -2, players, draftPool);
            fail();
        }catch (ValueOutOfBoundsException e){}
    }

    /**
     * Tests the impossibility of creating a round with a null list of players
     * @see Round#Round(int, int, List, DraftPool)
     */
    @Test
    public void testConstructorWithNullListOfPlayers(){
        try{
            round = new Round(0, 8, null, draftPool);
            fail();
        }catch (NullPointerException e){}
    }

    /**
     * Tests the impossibility of creating a round with a list of no players
     * @see Round#Round(int, int, List, DraftPool)
     */
    @Test
    public void testConstructorWithEmptyListOfPlayers(){
        try{
            round = new Round(0, 8, new ArrayList<>(), draftPool);
            fail();
        }catch (EmptyListException e){}
    }

    /**
     * Tests the impossibility of creating a round with a null {@link DraftPool}
     * @see Round#Round(int, int, List, DraftPool)
     */
    @Test
    public void testConstructorWithNullDraftPool(){
        try{
            round = new Round(0, 8, players, null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests the retrieval of the current {@link Turn}
     * @see Round#getCurrentTurn()
     */
    @Test
    public void testGetCurrentTurn(){
        round = new Round(0,players.size()*2, players, draftPool);
        try {
            round.nextTurn();
        } catch (NoMoreTurnsAvailableException e) {
            e.printStackTrace();
            fail();
        }
        Turn turn = round.getCurrentTurn();
        assertNotNull(turn);
    }

    /**
     * Tests the retrieval of the players of the last round in reverse turn order
     * @see Round#getPlayersByReverseTurnOrder()
     */
    @Test
    public void testGetPlayersOfLastRoundByReverseTurnOrder(){
        Set<Player> expectedPlayers = new LinkedHashSet<>();
        expectedPlayers.add(p2);
        expectedPlayers.add(p3);
        expectedPlayers.add(p4);
        expectedPlayers.add(p1);

        round = new Round(9,players.size()*2, players, draftPool);
        Set<Player> playersByReverseTurnOrder = round.getPlayersByReverseTurnOrder();

        assertEquals(expectedPlayers, playersByReverseTurnOrder);
    }

    /**
     * Tests the retrieval of the players of the last round in reverse turn order when there is only one player playing
     * @see Round#getPlayersByReverseTurnOrder()
     */
    @Test
    public void testGetPlayersOfLastRoundByReverseTurnOrderWhenOnePlayer(){
        Set<Player> expectedPlayers = new LinkedHashSet<>();
        expectedPlayers.add(p2);

        players = new ArrayList<>();
        players.add(p2);

        round = new Round(9,players.size()*2, players, draftPool);
        Set<Player> playersByReverseTurnOrder = round.getPlayersByReverseTurnOrder();

        assertEquals(expectedPlayers, playersByReverseTurnOrder);
    }

    /**
     * Tests the retrieval of the player for the first turn of the game using the getPlayersByReverseTurnOrder method
     * @see Round#getPlayersByReverseTurnOrder()
     */
    @Test
    public void testGetPlayerForFirstTurnOfGame(){
        Player expectedPlayer = p1;

        round = new Round(0,players.size()*2, players, draftPool);
        Set<Player> playersByReverseTurnOrder = round.getPlayersByReverseTurnOrder();
        Player firstPlayer = new ArrayList<>(playersByReverseTurnOrder).get(0);

        assertEquals(expectedPlayer, firstPlayer);
    }

    /**
     * Tests the retrieval of the player for the last turn of the game using the getPlayersByReverseTurnOrder method
     * @see Round#getPlayersByReverseTurnOrder()
     */
    @Test
    public void testGetPlayerForLastTurnOfGame(){
        Player expectedPlayer = p2;

        round = new Round(9,players.size()*2, players, draftPool);
        Set<Player> playersByReverseTurnOrder = round.getPlayersByReverseTurnOrder();
        Player lastPlayer = new ArrayList<>(playersByReverseTurnOrder).get(0);

        assertEquals(expectedPlayer, lastPlayer);
    }

    /**
     * Tests that the first turn of a round has 0 as index
     * @see Round#nextTurn()
     * @see Round#getCurrentTurn()
     * @see Turn#getNumber()
     */
    @Test
    public void testFirstTurnOfRoundHasZeroIndex(){
        round = new Round(0,players.size()*2, players, draftPool);
        try {
            round.nextTurn();
        } catch (NoMoreTurnsAvailableException e) {
            e.printStackTrace();
            fail();
        }
        int currentTurnNumber = round.getCurrentTurn().getNumber();

        assertEquals(0,currentTurnNumber);
    }

    /**
     * Tests proceeding to the next turn of a round from the first turn of the round
     * @see Round#nextTurn()
     */
    @Test
    public void testNextTurnFromFirstTurnOfRound(){
        round = new Round(0,players.size()*2, players, draftPool);

        try {
            round.nextTurn();
            round.nextTurn();
        } catch (NoMoreTurnsAvailableException e) {
            e.printStackTrace();
            fail();
        }

        int currentTurnNumber = round.getCurrentTurn().getNumber();

        assertEquals(1,currentTurnNumber);
    }

    /**
     * Tests the impossibility of proceeding to the next turn when no more turns are available
     * @see Round#nextTurn()
     */
    @Test
    public void testNextTurnWhenNoMoreTurnsAvailable(){
        int numberOfTurnsPerRound = players.size()*2;

        round = new Round(0,numberOfTurnsPerRound, players, draftPool);

        try {
            for(int i = 0; i < numberOfTurnsPerRound; i++) {
                round.nextTurn();
            }
        } catch (NoMoreTurnsAvailableException e) {
            e.printStackTrace();
            fail();
        }

        try{
            round.nextTurn();
            fail();
        }catch (NoMoreTurnsAvailableException e){}
    }

    /**
     * Tests the retrieval of the round number
     * @see Round#getNumber()
     */
    @Test
    public void testGetNumber(){
        round = new Round(7,players.size()*2, players, draftPool);

        assertEquals(7,round.getNumber());
    }

    /**
     * Tests the retrieval of the {@link DraftPool}
     * @see Round#getDraftPool()
     */
    @Test
    public void testGetDraftPool(){
        draftPool = new DraftPool(new ArrayList<>());
        round = new Round(0,players.size()*2, players, draftPool);

        assertEquals(draftPool, round.getDraftPool());
    }

    /**
     * Tests the removal of the next turn of a player
     * @see Round#removeNextTurnOfPlayer(Player)
     */
    @Test
    public void testRemoveNextTurnOfPlayer(){

        int numberOfTurns = players.size() * 2;
        round = new Round(0, numberOfTurns, players, draftPool);
        try {
            round.nextTurn();
        } catch (NoMoreTurnsAvailableException e) {
            e.printStackTrace();
            fail();
        }
        Player currentPlayer = round.getCurrentTurn().getPlayer();

        assertTrue(round.removeNextTurnOfPlayer(currentPlayer));

        int currentTurnNumber = round.getCurrentTurn().getNumber();

        for(int i = currentTurnNumber; i < numberOfTurns - 1; i++) {

            try {
                round.nextTurn();
                if (round.getCurrentTurn().getPlayer() == currentPlayer) {
                    fail();
                }
            } catch (NoMoreTurnsAvailableException e) {}
        }
    }

    /**
     * Tests the impossibility of removing the next turn of the current player twice
     * @see Round#removeNextTurnOfPlayer(Player)
     */
    @Test
    public void testRemoveNextTurnOfCurrentPlayerTwice(){

        int numberOfTurns = players.size() * 2;
        round = new Round(0, numberOfTurns, players, draftPool);
        try {
            round.nextTurn();
        } catch (NoMoreTurnsAvailableException e) {
            e.printStackTrace();
            fail();
        }
        Player currentPlayer = round.getCurrentTurn().getPlayer();

        assertTrue(round.removeNextTurnOfPlayer(currentPlayer));
        assertFalse(round.removeNextTurnOfPlayer(currentPlayer));

    }

    /**
     * Tests the impossibility of removing the next turn of a player three times
     * @see Round#removeNextTurnOfPlayer(Player)
     */
    @Test
    public void testRemoveNextTurnOfPlayerThreeTimes(){

        int numberOfTurns = players.size() * 2;
        round = new Round(0, numberOfTurns, players, draftPool);

        assertTrue(round.removeNextTurnOfPlayer(p2));
        assertTrue(round.removeNextTurnOfPlayer(p2));
        assertFalse(round.removeNextTurnOfPlayer(p2));

    }

    /**
     * Tests the impossibility of removing the next turn of a null player
     * @see Round#removeNextTurnOfPlayer(Player)
     */
    @Test
    public void testRemoveNextTurnOfNullPlayer(){
        round = new Round(0, players.size()*2, players, draftPool);
        try{
            round.removeNextTurnOfPlayer(null);
            fail();
        }catch (IllegalArgumentException e ){}
    }


}