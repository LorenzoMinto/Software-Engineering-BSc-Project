package it.polimi.se2018.controller;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.PrivateObjectiveCard;
import it.polimi.se2018.model.PublicObjectiveCard;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.utils.EmptyListException;

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
        if(rankings.isEmpty()){ throw new EmptyListException("ERROR: Can't determine winner if the list of players is empty.");}

        List<Player> playersOfRankings = new ArrayList<>( rankings.keySet() );

        return playersOfRankings.get(0);
    }


    /**
     * Calculates and returns the rankings of a given list of players
     * based on a given list of public objective cards.
     *
     * @param playersOfLastRound sorted list of players of last round
     * @param publicObjectiveCards public objective cards of the game, players will be scored according to these
     * @return rankings as a Map of Player and Integer(score)
     * @see Scorer#getScores(List, List)
     * @see Scorer#sortRankingsByFavorTokens(Map)
     * @see Scorer#sortRankingsByPrivateObjectiveCardScore(Map)
     * @see Scorer#sortRankingsByScore(Map)
     */
     public Map<Player, Integer> getRankings(List<Player> playersOfLastRound,
                                     List<PublicObjectiveCard> publicObjectiveCards){
        if(playersOfLastRound.isEmpty()){ throw new EmptyListException(
                "ERROR: Can't determine winner if the list of players is empty.");}
        if(publicObjectiveCards.isEmpty()){ throw new EmptyListException(
                "ERROR: Can't determine winner if the list of public objective cards is empty.");}

        Map<Player,Integer> rankings;

        //calculate score for each player
        rankings = getScores(playersOfLastRound, publicObjectiveCards);

        rankings = sortRankingsByFavorTokens(rankings);
        rankings = sortRankingsByPrivateObjectiveCardScore(rankings);
        rankings = sortRankingsByScore(rankings);

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

        players.forEach(player -> scores.put(player,calculatePlayerScore(player,publicObjectiveCards)));

        return scores;
    }


    /**
     * Orders a given list of players by favor tokens.
     *
     * @param rankings player rankings to be sorted
     * @return rankings sorted by favor tokens
     */
    private Map<Player, Integer> sortRankingsByFavorTokens(Map<Player, Integer> rankings) {

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
     * @param rankings rankings to be
     * @return rankings sorted by Private Objective Cards score
     * @see Scorer#sortRankingsBy(Map, Map)
     */
    private Map<Player, Integer> sortRankingsByPrivateObjectiveCardScore(Map<Player, Integer> rankings) {

        Set<Player> players = rankings.keySet();
        Map<Player, Integer> privateObjectiveCardsScores = getPrivateObjectiveCardScores(players);


        return sortRankingsBy(privateObjectiveCardsScores, rankings);
    }




    /**
     * Orders rankings by score
     *
     * @param rankings rankings to be sorted
     * @return rankings sorted by each player's score (descending order)
     * @see Scorer#sortRankingsBy(Map, Map)
     */
    private Map<Player, Integer> sortRankingsByScore(Map<Player, Integer> rankings) {

        //Rankings are here sorted by each player score that is in 'rankings' itself. This is why both the parameters
        //passed to 'sortRankingsBy' are 'rankings'
        return sortRankingsBy(rankings, rankings);
    }


    /**
     * Orders rankings ( Map <Players, Scores(Integer) ) by descending value of the criteria Map criteria
     *
     * @param criteria criteria rankings to follow
     * @param playerRankings rankings to be sorted
     * @return rankings sorted by criteria specified in method parameter
     */

    private Map<Player, Integer> sortRankingsBy(Map<Player, Integer> criteria, Map<Player, Integer> playerRankings) {
        Map<Player, Integer> rankingsByCriteria = new LinkedHashMap<>();
        Map<Player, Integer> criteriaCopy = new LinkedHashMap<>(criteria);

        while(!criteriaCopy.isEmpty()) {
            Player playerWithMaxScore = getPlayerWithMaxScore(criteriaCopy);
            int playerScore = playerRankings.get(playerWithMaxScore);
            rankingsByCriteria.put(playerWithMaxScore, playerScore);
            criteriaCopy.remove(playerWithMaxScore);
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
        
        return  players
                .stream()
                .max(Comparator.comparing(rankings::get))
                .orElse(players.get(0));
    }

    /**
     * Calculates and returns the Player in the given list with
     * the maximum number of favor tokens left.
     *
     * @param scores the list to be evaluated
     * @return the Player in the given list with the maximum number of favor tokens left
     */
    private Player getPlayerWithMaxFavorTokens(Map<Player, Integer> scores) {

        List<Player> players = new ArrayList<>(scores.keySet());
        Player playerWithMaxFavorTokens = players.get(0);

        for (Player player: players) {
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
