package it.polimi.se2018.controller;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for {@link RankingRecord} class
 *
 * @author Jacopo Pio Gargano
 */
public class RankingRecordTest {
    private static final String playerID = "playerID";
    private static final int cumulativePoints = 30;
    private static final int gamesWon = 10;
    private static final int gamesLost = 5;
    private static final int timePlayed = 1200;

    private RankingRecord rankingRecord;

    @Before
    public void initializeRankingRecord(){
        rankingRecord = new RankingRecord(playerID, cumulativePoints, gamesWon, gamesLost, timePlayed);
    }

    /**
     * Tests the copy() method of {@link RankingRecord}
     * @see RankingRecord#copy()
     */
    @Test
    public void testCopyRankingRecord(){
        assertNotNull(rankingRecord.copy());
    }

    /**
     * Tests the toString() method of {@link RankingRecord}
     * @see RankingRecord#toString()
     */
    @Test
    public void testRankingRecordToString(){
        String expectedString = playerID + " -      PTS:" + "0" + "   GW: " + gamesWon + "   GL: " + gamesLost +
                "   CPTS: " + cumulativePoints + "   TT: " + timePlayed + " m";
        String rankingRecordToString = rankingRecord.toString();
        assertEquals(expectedString, rankingRecordToString);
    }

}