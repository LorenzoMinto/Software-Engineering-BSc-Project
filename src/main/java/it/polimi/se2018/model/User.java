package it.polimi.se2018.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Class representing a user (one per person, created one during first time sign up).
 * Each user has one or more players connected (player is just the relationship
 * between a user and the game the user is playing).
 *
 * @author Federico Haag
 */
//TODO: verificare se ha ancora senso la classe user o se va rimossa
//NOTE: comments are not completed because is not sure this class will continue existing
public class User {
    public User(int userID, String username) {
        this.userID = userID;
        this.username = username;
        this.gamesWon = 0;
        this.gamesPlayed = 0;
        this.unfinishedGameID = new HashSet<>();
    }

    private int userID;

    private String username;

    private int gamesWon;

    private int gamesPlayed;

    private Set<Integer> unfinishedGameID;

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public Set<Integer> getUnfinishedGameID() {
        return new HashSet<>(unfinishedGameID);
    }

    public int increaseGamesWon() {
        return ++gamesWon;
    }

    public int increaseGamesPlayed() {
        return ++gamesPlayed;
    }

    public void addUnfinishedGameID(int id){
        unfinishedGameID.add(id);
    }
}
