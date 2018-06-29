package it.polimi.se2018.controller;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for {@link Persistency} class
 *
 * @author Jacopo Pio Gargano
 */
public class PersistencyTest {

    private static Persistency persistency;

    @BeforeClass
    public static void getSingleton(){
        persistency = new Persistency("globalrankings.xml");
    }

/*    *//**
     * Tests the singleton getInstance method does not return null, even if called multiple times
     * @see Persistency#getInstance()
     *//*
    @Test
    public void testSingletonInstanceIsNotNull(){
        assertNotNull(Persistency.getInstance());
        assertNotNull(Persistency.getInstance());
        assertNotNull(Persistency.getInstance());
    }

    *//**
     * Tests that the two instances of the singleton are the same instance
     * @see Persistency#getInstance()
     *//*
    @Test
    public void testSingletonInstance(){
        Persistency Persistency1 = Persistency.getInstance();
        Persistency Persistency2 = Persistency.getInstance();
        assertEquals(Persistency1, Persistency2);
    }*/

    /**
     * Tests the loading and retrieval of Global Rankings
     * @see Persistency#loadRankings()
     * @see Persistency#getGlobalRankings()
     */
    @Test
    public void testLoadAndGetGlobalRankings(){
        persistency.loadRankings();
        assertNotNull(persistency.getGlobalRankings());
    }

    /**
     * Tests the loading and retrieval of Global Rankings for a specific player
     * @see Persistency#getRankingForPlayerID(String)
     */
    @Test
    public void testGetRecordsForPlayer(){
        persistency.loadRankings();
        String playerID = persistency.getGlobalRankings().get(0).getPlayerID();
        if(playerID != null) {
            assertNotNull(persistency.getRankingForPlayerID(playerID));
        }
    }

    /**
     * Tests the update of a player's rankings
     * @see Persistency#updateRankingForPlayerID(String, int, boolean, int)
     */
    @Test
    public void testUpdateRankingsForPlayer(){
        RankingRecord rankingForPlayer;
        persistency.loadRankings();
        String playerID = persistency.getGlobalRankings().get(0).getPlayerID();
        if(playerID != null) {
            rankingForPlayer = persistency.getRankingForPlayerID(playerID);
            assertNotNull(rankingForPlayer);
        }else{
            return;
        }

        int gamesWon = rankingForPlayer.getGamesWon();
        int cumulativePoints = rankingForPlayer.getCumulativePoints();
        int timePlayed = rankingForPlayer.getTimePlayed();

        persistency.updateRankingForPlayerID(playerID,30,true,20);

        rankingForPlayer = persistency.getRankingForPlayerID(playerID);

        assertEquals(gamesWon+1, rankingForPlayer.getGamesWon());
        assertEquals(cumulativePoints+30, rankingForPlayer.getCumulativePoints());
        assertEquals(timePlayed+20, rankingForPlayer.getTimePlayed());
    }



}