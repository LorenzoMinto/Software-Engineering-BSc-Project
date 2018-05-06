package it.polimi.se2018.controller;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.PrivateObjectiveCard;
import it.polimi.se2018.model.PublicObjectiveCard;
import it.polimi.se2018.model.WindowPattern;

import java.util.*;

//Singleton
public class Scorer {
    private static Scorer instance = null;

    private Scorer(){}

    public static Scorer getInstance(){
        if (instance == null){
            instance = new Scorer();
        }
        return instance;
    }


    List<Player> getRankings(List<Player> players, Set<PublicObjectiveCard> publicObjectiveCards){
        if(players.isEmpty()){ throw new IllegalArgumentException("ERROR: Can't determine winner" +
                " if the list of players is empty");}
        List<Player> rankings;
        Map<Player,Integer> scores;

        //calculate score for each player
        scores = getScores(players, publicObjectiveCards);

        rankings = orderPlayersFromLastRound(players);
        rankings = orderPlayersByFavorTokens(rankings);
        rankings = orderPlayersByScore(rankings, scores);

        return rankings;
    }

    private Map<Player, Integer> getScores(List<Player> players, Set<PublicObjectiveCard> publicObjectiveCards) {
        Map <Player, Integer> scores = new HashMap<>();

        for (Player player: players) {
            int playerScore = calculatePlayerScore(player,publicObjectiveCards);
            scores.put(player,playerScore);
        }

        return scores;
    }

    private List<Player> orderPlayersFromLastRound(List<Player> players) {
        if(players.isEmpty()){ throw new IllegalArgumentException("ERROR: Can't order players from last round" +
                " if the list of players is empty");}
        List<Player> orderedPlayers = new ArrayList<>();

        for(int i = players.size()-1; i > players.size()/2; i--) {
            orderedPlayers.add(players.get(i));
        }
        return orderedPlayers;
    }


    private List<Player> orderPlayersByFavorTokens(List<Player> players) {
        if(players.isEmpty()){ throw new IllegalArgumentException("ERROR: Can't order players by favor tokens" +
                " if the list of players is empty");}

        List<Player> playersByFavorTokens = new ArrayList<>();

        while(!players.isEmpty()) {
            Player playerWithMaxFavorTokens = getPlayerWithMaxFavorTokens(players);
            playersByFavorTokens.add(playerWithMaxFavorTokens);
            players.remove(playerWithMaxFavorTokens);
        }

        return playersByFavorTokens;
    }


    private List<Player> orderPlayersByScore(List<Player> players, Map<Player, Integer> scores) {
        if(players.isEmpty()){ throw new IllegalArgumentException("ERROR: Can't order players by score" +
                " if the list of players is empty");}

        List<Player> playersByScore = new ArrayList<>();

        while(!players.isEmpty()) {
            Player playerWithMaxScore = getPlayerWithMaxScore(players, scores);
            playersByScore.add(playerWithMaxScore);
            players.remove(playerWithMaxScore);
        }
        return playersByScore;
    }

    private Player getPlayerWithMaxScore(List<Player> players, Map<Player, Integer> scores){
        Player playerWithMaxScore = players.get(0);
        int maxScore = scores.get(playerWithMaxScore);

        for (Player player: players) {
            int score = scores.get(player);

            if(score > maxScore){
                playerWithMaxScore = player;
                maxScore = score;
            }
        }
        players.remove(playerWithMaxScore);
        return playerWithMaxScore;
    }

    private Player getPlayerWithMaxFavorTokens(List<Player> players) {
        Player playerWithMaxFavorTokens = players.get(0);

        for (Player player: players) {
            if(player.getFavorTokens() > playerWithMaxFavorTokens.getFavorTokens()){
                playerWithMaxFavorTokens = player;
            }
        }
        players.remove(playerWithMaxFavorTokens);
        return playerWithMaxFavorTokens;
    }

    private int calculatePlayerScore(Player player, Set<PublicObjectiveCard> publicObjectiveCards){
        int score = 0;
        WindowPattern wp = player.getWindowPattern();

        score += getPublicObjectiveCardsScore(wp, publicObjectiveCards);
        score += getPrivateObjectiveCardScore(wp, player.getPrivateObjectiveCard());
        score += player.getFavorTokens();
        score -= getNumberOfOpenSpaces(wp);

        return score;
    }

    private int getPrivateObjectiveCardScore(WindowPattern windowPattern, PrivateObjectiveCard card){
        return card.calculateScore(windowPattern);
    }

    private int getPublicObjectiveCardsScore(WindowPattern windowPattern, Set<PublicObjectiveCard> cards){
        int score = 0;

        for (PublicObjectiveCard card: cards) {
            score += card.calculateScore(windowPattern);
        }

        return score;
    }


    private int getNumberOfOpenSpaces(WindowPattern wp){

        int numberOfOpenSpaces = 0;

        for(int i=0; i < wp.getNumberOfRows(); i++){
            for(int j=0; j < wp.getNumberOfColumns(); j++){
                if(!wp.getPattern()[i][j].hasDice()){
                    numberOfOpenSpaces++;
                }
            }
        }

        return numberOfOpenSpaces;
    }


}
