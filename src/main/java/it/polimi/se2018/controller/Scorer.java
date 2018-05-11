package it.polimi.se2018.controller;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.PrivateObjectiveCard;
import it.polimi.se2018.model.PublicObjectiveCard;
import it.polimi.se2018.model.WindowPattern;

import java.util.*;

/**
 * Calculates and retrieves the scores and rankings of a given list
 * of players and public objective cards.
 *
 * This class is a SINGLETON.
 *
 * @author Jacopo Pio Gargano
 * @see Player
 * @see PublicObjectiveCard
 */
public class Scorer {

    /**
     * Instance of the class in order to achieve the Singleton Pattern.
     */
    private static Scorer instance = null;

    /**
     * Private Constructor in order to prevent from multiple instantiation of the class.
     */
    private Scorer(){}

    /**
     * Gets the instance of the class (according to Singleton Pattern).
     *
     * @return the instance of the class
     */
    public static Scorer getInstance(){
        if (instance == null){
            instance = new Scorer();
        }
        return instance;
    }

    /**
     * Calculates and returns the rankings and scores of a given list of players
     * based on a given list of public objective cards.
     *
     * @param playersOfLastRound ordered list of players of last round
     * @param playersOfGame list of players of the game
     * @param publicObjectiveCards public objective cards of the game, players will be scored according to these
     * @return rankings as an ordered list of players (first is winner) and scores as an HashMap<Player,Score(Integer)>
     * @see Scorer#getScores(List, List)
     */
    Object[] compute(List<Player> playersOfLastRound, List<Player> playersOfGame,
                     List<PublicObjectiveCard> publicObjectiveCards){
        if(playersOfLastRound.isEmpty()){ throw new IllegalArgumentException("ERROR: Can't determine winner" +
                " if the list of players is empty");}
        List<Player> rankings;
        Map<Player,Integer> scores;

        //calculate score for each player
        scores = getScores(playersOfGame, publicObjectiveCards);

        rankings = orderPlayersByFavorTokens(playersOfLastRound);
        rankings = orderPlayersByPrivateObjectiveCardScore(rankings);
        rankings = orderPlayersByScore(rankings, scores);

        Object[] result = new Object[2];
        result[0] = rankings;
        result[1] = scores;

        return result;
    }


    /**
     * Calculates and returns the scores of a given list of players and public objective cards.
     *
     * @param players the list of players to be evaluated
     * @param publicObjectiveCards the cards according to which the players must be evaluated
     * @return scores of a given list of players and public objective cards
     */
    private Map<Player, Integer> getScores(List<Player> players, List<PublicObjectiveCard> publicObjectiveCards) {
        Map <Player, Integer> scores = new HashMap<>();

        for (Player player: players) {
            int playerScore = calculatePlayerScore(player, publicObjectiveCards);
            scores.put(player,playerScore);
        }

        return scores;
    }


    /**
     * Orders a given list of players by favor tokens.
     *
     * @param players the list of players to be ordered
     * @return the list of players ordered by favor tokens
     */
    private List<Player> orderPlayersByFavorTokens(List<Player> players) {
        if(players.isEmpty()){ throw new IllegalArgumentException("ERROR: Can't order players by favor tokens" +
                " if the list of players is empty");}

        List<Player> playersByFavorTokens = new ArrayList<>();
        List<Player> playersCopy = new ArrayList<>(players);

        while(!players.isEmpty()) {
            Player playerWithMaxFavorTokens = getPlayerWithMaxFavorTokens(playersCopy);
            playersByFavorTokens.add(playerWithMaxFavorTokens);
            players.remove(playerWithMaxFavorTokens);
        }

        return playersByFavorTokens;
    }


    //TODO: javadoc
    private List<Player> orderPlayersByPrivateObjectiveCardScore(List<Player> players) {
        if(players.isEmpty()){ throw new IllegalArgumentException("ERROR: Can't order players by private objective" +
                "card score if the list of players is empty");}

        List<Player> playersByPrivateObjectiveCardScore = new ArrayList<>();
        List<Player> playersCopy = new ArrayList<>(players);
        Map<Player, Integer> privateObjectiveCardsScore;

        privateObjectiveCardsScore = getPrivateObjectiveCardScores(playersCopy);

        while(!players.isEmpty()) {
            Player playerWithMaxPrivateObjectiveCardScore = getPlayerWithMaxScore(playersCopy, privateObjectiveCardsScore);
            playersByPrivateObjectiveCardScore.add(playerWithMaxPrivateObjectiveCardScore);
            players.remove(playerWithMaxPrivateObjectiveCardScore);
        }
        return playersByPrivateObjectiveCardScore;
    }

    //TODO: javadoc
    private Map<Player, Integer> getPrivateObjectiveCardScores(List<Player> players){
        Map <Player, Integer> privateObjectiveCardScores = new HashMap<>();

        for (Player player: players) {
            WindowPattern windowPattern = player.getWindowPattern();
            int playerScore = player.getPrivateObjectiveCard().calculateScore(windowPattern);
            privateObjectiveCardScores.put(player,playerScore);
        }

        return privateObjectiveCardScores;
    }


    /**
     * Orders a given list of players by score.
     *
     * @param players the list of players to be ordered
     * @return the list of players ordered by score
     */
    private List<Player> orderPlayersByScore(List<Player> players, Map<Player, Integer> scores) {
        if(players.isEmpty()){ throw new IllegalArgumentException("ERROR: Can't order players by score" +
                " if the list of players is empty");}

        List<Player> playersByScore = new ArrayList<>();
        List<Player> playersCopy = new ArrayList<>(players);

        while(!players.isEmpty()) {
            Player playerWithMaxScore = getPlayerWithMaxScore(playersCopy, scores);
            playersByScore.add(playerWithMaxScore);
            players.remove(playerWithMaxScore);
        }
        return playersByScore;
    }

    //TODO: decidere se rivedere il funzionamento di questo metodo
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
        return playerWithMaxScore;
    }

    /**
     * Calculates and returns the Player in the given list with
     * the maximum number of favor tokens left.
     *
     * @param players the list to be evaluated
     * @return the Player in the given list with the maximum number of favor tokens left
     */
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

    /**
     * Calculates the score of a given Player, based to the given public objective cards.
     *
     * @param player the player to be evaluated
     * @param publicObjectiveCards the list of public objective cards to be used for evaluation
     * @return the score of a given Player, based to the given public objective cards.
     */
    private int calculatePlayerScore(Player player, List<PublicObjectiveCard> publicObjectiveCards){
        int score = 0;
        WindowPattern wp = player.getWindowPattern();

        score += getPublicObjectiveCardsScore(wp, publicObjectiveCards);
        score += getPrivateObjectiveCardScore(wp, player.getPrivateObjectiveCard());
        score += player.getFavorTokens();
        score -= getNumberOfEmptySpaces(wp);

        return score;
    }

    /**
     * Calculates the score of a {@link WindowPattern} based on a given {@link PrivateObjectiveCard}
     *
     * @param windowPattern the window pattern to be evaluated
     * @param card the private objective card to be used for evaluating the window pattern
     * @return the score of the given {@link WindowPattern} based on the given {@link PrivateObjectiveCard}
     */
    private int getPrivateObjectiveCardScore(WindowPattern windowPattern, PrivateObjectiveCard card){
        return card.calculateScore(windowPattern);
    }

    /**
     * Calculates the score of a {@link WindowPattern} based on a given list of public objective cards
     *
     * @param windowPattern the window pattern to be evaluated
     * @param cards the public objective cards list
     * @return the score of the given {@link WindowPattern} based on the given public objective cards
     */
    private int getPublicObjectiveCardsScore(WindowPattern windowPattern, List<PublicObjectiveCard> cards){
        int score = 0;

        for (PublicObjectiveCard card: cards) {
            score += card.calculateScore(windowPattern);
        }

        return score;
    }


    /**
     * Returns the number of empty spaces in given window pattern
     *
     * @param wp the window pattern to be evaluated
     * @return the number of empty spaces in given window pattern
     */
    private int getNumberOfEmptySpaces(WindowPattern wp){

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
