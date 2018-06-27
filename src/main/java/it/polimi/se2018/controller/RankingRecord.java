package it.polimi.se2018.controller;

import java.io.Serializable;

/**
 *
 * @author Lorenzo Minto
 */
//TODO: fare documentazione completa per questa classe @lorenzo
public class RankingRecord implements Serializable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 5554972831173888578L;

    private String playerID;
    private int points;
    private int cumulativePoints;
    private int gamesWon;
    private int gamesLost;
    private int timePlayed;

    RankingRecord(String playerID, int cumulativePoints, int gamesWon, int gamesLost, int timePlayed) {
        this.cumulativePoints = cumulativePoints;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        this.timePlayed = timePlayed;
        this.playerID = playerID;
    }

    private RankingRecord(int points, String playerID, int cumulativePoints, int gamesWon, int gamesLost, int timePlayed) {
        this(playerID, cumulativePoints, gamesWon, gamesLost, timePlayed);
        this.points = points;
    }

    public String getPlayerID() { return playerID; }

    public int getPoints() { return points; }

    public void setPoints(int points) { this.points = points; }

    public int getCumulativePoints() { return cumulativePoints; }

    public int getGamesWon() { return gamesWon; }

    public int getGamesLost() { return gamesLost; }

    public int getTimePlayed() { return timePlayed; }

    public RankingRecord copy(){

        return new RankingRecord(this.points, this.playerID, this.cumulativePoints, this.gamesWon, this.gamesLost, this.timePlayed);
    }

    @Override
    public String toString() {
        //TODO: if necessary this toString, then creates constants @lorenzo
        return playerID + " -      PTS:" + points + "   GW: " + gamesWon + "   GL: " + gamesLost +
                "   CPTS: " + cumulativePoints + "   TT: " + timePlayed + " m";
    }
}
