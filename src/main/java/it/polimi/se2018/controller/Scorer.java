package it.polimi.se2018.controller;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.PrivateObjectiveCard;
import it.polimi.se2018.model.PublicObjectiveCard;
import it.polimi.se2018.model.WindowPattern;

import java.util.*;

/**
 * Calculates and retrieves the rankings of a given list of players and public objective cards.
 *
 * This class is a SINGLETON.
 *
 * @author Jacopo Pio Gargano
 * @see Player
 * @see PublicObjectiveCard
 * @see PrivateObjectiveCard
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
     * Gets the first player of certain rankings
     *
     * @param rankings rankings to get the winner from
     * @return the first player of certain rankings
     */
    public Player getWinner(Map<Player, Integer> rankings){
        if(rankings == null || rankings.isEmpty()){ throw new IllegalArgumentException("ERROR: Can't determine winner" +
                " if the list of players is empty.");}
        Player winner;
        Set<Player> players = rankings.keySet();
        List<Player> playersOfRankings = new ArrayList<>(players);
        winner = playersOfRankings.get(0);
        return winner;
    }


    /**
     * Calculates and returns the rankings of a given list of players
     * based on a given list of public objective cards.
     *
     * @param playersOfLastRound ordered list of players of last round
     * @param publicObjectiveCards public objective cards of the game, players will be scored according to these
     * @return rankings as a Map<Player,Score(Integer)>
     * @see Scorer#getScores(List, List)
     * @see Scorer#orderRankingsByFavorTokens(Map)
     * @see Scorer#orderRankingsByPrivateObjectiveCardScore(Map)
     * @see Scorer#orderRankingsByScore(Map)
     */
    Map<Player, Integer> getRankings(List<Player> playersOfLastRound,
                                     List<PublicObjectiveCard> publicObjectiveCards){
        if(playersOfLastRound == null || playersOfLastRound.isEmpty()){ throw new IllegalArgumentException(
                "ERROR: Can't determine winner if the list of players is empty.");}
        if(publicObjectiveCards == null || publicObjectiveCards.isEmpty()){ throw new IllegalArgumentException(
                "ERROR: Can't determine winner if the list of public objective cards is empty.");}

        Map<Player,Integer> rankings;

        //calculate score for each player
        rankings = getScores(playersOfLastRound, publicObjectiveCards);

        rankings = orderRankingsByFavorTokens(rankings);
        rankings = orderRankingsByPrivateObjectiveCardScore(rankings);
        rankings = orderRankingsByScore(rankings);

        return rankings;
    }


    /**
     * Calculates and returns the scores of a given list of players and public objective cards.
     *
     * @param players the list of players to be evaluated
     * @param publicObjectiveCards the cards according to which the players must be evaluated
     * @return scores of a given list of players and public objective cards
     */
    private Map<Player, Integer> getScores(List<Player> players, List<PublicObjectiveCard> publicObjectiveCards) {
        Map <Player, Integer> scores = new LinkedHashMap<>();

        for (Player player: players) {
            int playerScore = calculatePlayerScore(player, publicObjectiveCards);
            scores.put(player,playerScore);
        }

        return scores;
    }


    /**
     * Orders a given list of players by favor tokens.
     *
     * @param rankings player rankings to be ordered
     * @return rankings ordered by favor tokens
     */
    private Map<Player, Integer> orderRankingsByFavorTokens(Map<Player, Integer> rankings) {
        if(rankings.isEmpty()){ throw new IllegalArgumentException("ERROR: Can't order players by favor tokens" +
                " if the list of players is empty.");}

        Map<Player, Integer> rankingsByFavorTokens = new LinkedHashMap<>();

        while(!rankings.isEmpty()) {
            Player playerWithMaxFavorTokens = getPlayerWithMaxFavorTokens(rankings);
            int playerScore = rankings.get(playerWithMaxFavorTokens);
            rankingsByFavorTokens.put(playerWithMaxFavorTokens, playerScore);
            rankings.remove(playerWithMaxFavorTokens);
        }

        return rankingsByFavorTokens;
    }

    /**
     * Orders players rankings based on the Private Objective Cards score
     *
     * @param rankings rankings to be ordered
     * @return rankings ordered by Private Objective Cards score
     * @see Scorer#orderRankingsByCriteria(Map, Map)
     */
    private Map<Player, Integer> orderRankingsByPrivateObjectiveCardScore(Map<Player, Integer> rankings) {
        if(rankings.isEmpty()){ throw new IllegalArgumentException("ERROR: Can't order players by private objective" +
                "card score if the list of players is empty.");}

        Set<Player> players = rankings.keySet();
        Map<Player, Integer> privateObjectiveCardsScores = getPrivateObjectiveCardScores(players);

        Map<Player, Integer> RankingsByPrivateObjectiveCard =
                orderRankingsByCriteria(rankings, privateObjectiveCardsScores);
        return RankingsByPrivateObjectiveCard;
    }




    /**
     * Orders rankings by score
     *
     * @param rankings rankings to be ordered
     * @return rankings ordered by score
     * @see Scorer#orderRankingsByCriteria(Map, Map)
     */
    private Map<Player, Integer> orderRankingsByScore(Map<Player, Integer> rankings) {
        if(rankings.isEmpty()){ throw new IllegalArgumentException("ERROR: Can't order players by score" +
                " if the list of players is empty.");}

        Map<Player, Integer> rankingsByValue = orderRankingsByCriteria(rankings, rankings);
        return rankingsByValue;
    }


    /**
     * Orders rankings ( Map <Players, Scores(Integer) ) by descending value of the criteria Map criteriaRankings
     *
     * @param playerRankings rankings to be ordered
     * @param criteriaRankings criteria rankings to follow
     * @return rankings ordered by criteria specified in method parameter
     */
    private Map<Player, Integer> orderRankingsByCriteria(Map<Player, Integer> playerRankings, Map<Player, Integer> criteriaRankings) {
        Map<Player, Integer> rankingsByCriteria = new LinkedHashMap<>();
        Map<Player, Integer> criteriaScoresCopy = new LinkedHashMap<>(criteriaRankings);

        while(!criteriaScoresCopy.isEmpty()) {
            Player playerWithMaxScoreByCriteria = getPlayerWithMaxScore(criteriaScoresCopy);
            int playerScore = playerRankings.get(playerWithMaxScoreByCriteria);
            rankingsByCriteria.put(playerWithMaxScoreByCriteria, playerScore);
            criteriaScoresCopy.remove(playerWithMaxScoreByCriteria);
        }
        return rankingsByCriteria;
    }


    /**
     * Gets the player with the max score from certain rankings
     *
     * @param rankings Map to get the player with the max score from
     * @return the player with the max score from certain rankings
     */
    private Player getPlayerWithMaxScore(Map<Player, Integer> rankings){

        List<Player> players = new ArrayList<>(rankings.keySet());

        if(players.isEmpty()){ throw new IllegalArgumentException("ERROR: Can't get players with max score" +
                " if there are no players.");}

        Player playerWithMaxScore = players.get(0);
        int maxScore = rankings.get(playerWithMaxScore);

        for (Player player: players) {
            int score = rankings.get(player);

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
     * @param scores the list to be evaluated
     * @return the Player in the given list with the maximum number of favor tokens left
     */
    private Player getPlayerWithMaxFavorTokens(Map<Player, Integer> scores) {
        if(scores.isEmpty()){ throw new IllegalArgumentException("ERROR: Can't get the player with max favor tokens" +
                " if the list of players is empty.");}

        List<Player> playersCopy = new ArrayList<>(scores.keySet());
        Player playerWithMaxFavorTokens = playersCopy.get(0);

        for (Player player: playersCopy) {
            if(player.getFavorTokens() > playerWithMaxFavorTokens.getFavorTokens()){
                playerWithMaxFavorTokens = player;
            }
        }
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
        WindowPattern windowPattern = player.getWindowPattern();
        PrivateObjectiveCard privateObjectiveCard = player.getPrivateObjectiveCard();

        score += getPublicObjectiveCardsScore(windowPattern, publicObjectiveCards);
        score += privateObjectiveCard.calculateScore(windowPattern);
        score += player.getFavorTokens();
        score -= getNumberOfEmptySpaces(windowPattern);

        return score;
    }

    /**
     * Gets the {@link PrivateObjectiveCard} score for each given player
     *
     * @param players Set of players to ge the {@link PrivateObjectiveCard} score of
     * @return {@link PrivateObjectiveCard} score for each player given in the players set
     */
    private Map<Player, Integer> getPrivateObjectiveCardScores(Set<Player> players){
        //used LinkedHashMap to preserve order of inserted pairs <key, value>
        Map <Player, Integer> privateObjectiveCardScores = new LinkedHashMap<>();

        for (Player player: players) {
            WindowPattern windowPattern = player.getWindowPattern();
            int playerScore = player.getPrivateObjectiveCard().calculateScore(windowPattern);
            privateObjectiveCardScores.put(player,playerScore);
        }

        return privateObjectiveCardScores;
    }


    /**
     * Calculates the score of a {@link WindowPattern} based on a given list of {@link PublicObjectiveCard}
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
