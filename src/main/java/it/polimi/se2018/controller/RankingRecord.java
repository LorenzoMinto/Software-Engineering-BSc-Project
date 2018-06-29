package it.polimi.se2018.controller;

import java.io.Serializable;

/**
 * Class that represents the ranking record for a single played, including the cumulative points made,
 * the number of lost and won games and the total time played. It also includes the game points when it
 * is used as a Local Ranking record
 *
 * @author Lorenzo Minto
 */
public class RankingRecord implements Serializable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 5554972831173888578L;

    /**
     * The player's usename
     */
    private String playerID;
    /**
     * The player's game points
     */
    private int points;
    /**
     * The player's cumulative game points
     */
    private int cumulativePoints;
    /**
     * The player's number of won games
     */
    private int gamesWon;
    /**
     * The player's number of lost games
     */
    private int gamesLost;
    /**
     * The player's total time played
     */
    private int timePlayed;

    /**
     * Class  default constructor.
     *
     * @param playerID the player's username
     * @param cumulativePoints the total number of points made cumulatively
     * @param gamesWon the number of games won
     * @param gamesLost the number of games lost
     * @param timePlayed the total played time
     */
    RankingRecord(String playerID, int cumulativePoints, int gamesWon, int gamesLost, int timePlayed) {
        this.cumulativePoints = cumulativePoints;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        this.timePlayed = timePlayed;
        this.playerID = playerID;
    }

    /**
     * Class auxiliary constructor. This is used to create a local ranking record as it also add
     * the last game points.
     *
     * @param points the last game points
     * @param playerID the player's username
     * @param cumulativePoints the total number of points made cumulatively
     * @param gamesWon the number of games won
     * @param gamesLost the number of games lost
     * @param timePlayed the total played time
     */
    private RankingRecord(int points, String playerID, int cumulativePoints, int gamesWon, int gamesLost, int timePlayed) {
        this(playerID, cumulativePoints, gamesWon, gamesLost, timePlayed);
        this.points = points;
    }

    /**
     * Returns the player's username
     *
     * @return the player's username
     */
    public String getPlayerID() { return playerID; }

    /**
     * Returns the player's last game points
     *
     * @return the player's last game points
     */
    public int getPoints() { return points; }

    /**
     * Sets the player's last game points
     * @param points the player's last game points
     */
    public void setPoints(int points) { this.points = points; }

    /**
     * Returns the player's cumulative game points
     *
     * @return the player's cumulative game points
     */
    public int getCumulativePoints() { return cumulativePoints; }

    /**
     * Returns the player's number of games won
     *
     * @return the player's number of games won
     */
    public int getGamesWon() { return gamesWon; }

    /**
     * Returns the player's number of games lost
     *
     * @return the player's number of games lost
     */
    public int getGamesLost() { return gamesLost; }

    /**
     * Returns the player's total time played
     *
     * @return the player's total time played
     */
    public int getTimePlayed() { return timePlayed; }

    /**
     * Returns a copy of the ranking record
     *
     * @return a copy of the ranking record
     */
    public RankingRecord copy(){
        return new RankingRecord(this.points, this.playerID, this.cumulativePoints, this.gamesWon, this.gamesLost, this.timePlayed);
    }

    @Override
    public String toString() {
        return playerID + " -      PTS:" + points + "   GW: " + gamesWon + "   GL: " + gamesLost +
                "   CPTS: " + cumulativePoints + "   TT: " + timePlayed + " m";
    }
}
