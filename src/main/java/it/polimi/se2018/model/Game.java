package it.polimi.se2018.model;

import it.polimi.se2018.controller.NoMoreRoundsAvailableException;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.Observable;

import java.util.*;

/**
 * Main class of the Model.
 *
 * An instance of this class represent a specific real game going on on the server.
 *
 * This class implements the OBSERVER PATTERN in order to notify all the Views connected.
 */
public class Game extends Observable {

    /**
     * The number of rounds the game is composed of
     */
    private final int numberOfRounds;

    /**
     * The maximum number of players that the game can have
     */
    private final int maxNumberOfPlayers;

    /**
     * The number of turns that has each round. Round with
     * different number of turns are not allowed.
     */
    private final int numberOfTurnsPerRound;

    /**
     * Reference to the instance of the current {@link Round}
     */
    private Round currentRound;

    /**
     * Reference to the instance of the game's {@link Track}
     */
    private Track track;

    /**
     * List of players playing the game
     */
    private List<Player> players;

    /**
     * List of ToolCards that were assigned to this game at the beginning of it
     */
    private List<ToolCard> drawnToolCards;

    /**
     * List of Public Objective Cards that were assigned to this game at the beginning of it
     */
    private List<PublicObjectiveCard> drawnPublicObjectiveCards;

    /**
     * Represents the status of the game
     */
    private GameStatus status;

    /**
     * Represents the final players' rankings (during the middle of the game is a null object)
     */
    private List<Player> rankings;

    /**
     * Represents the final players' scores (during the middle of the game is a null object)
     */
    private HashMap<Player,Integer> scores;

    /**
     * Constructor for a new Game instance. Basic configuration is passed as argument.
     * Cards will be assigned later.
     *
     * @param numberOfRounds the number of rounds the game is composed of
     * @param maxNumberOfPlayers the maximum number of players that the game can have
     */
    public Game(int numberOfRounds, int maxNumberOfPlayers) {
        this.currentRound = null;
        this.track = new Track();
        this.players = new ArrayList<>();
        this.status = GameStatus.WAITING_FOR_CARDS;
        this.rankings = null;
        this.scores = null;

        this.numberOfRounds = numberOfRounds;
        this.maxNumberOfPlayers = maxNumberOfPlayers;
        this.numberOfTurnsPerRound = maxNumberOfPlayers * 2;
    }

    /**
     * Returns the current round.
     *
     * @return the current round
     */
    public Round getCurrentRound() {
        return currentRound;
    }

    /**
     * Returns the current track.
     *
     * @return the current track
     */
    public Track getTrack() {
        return track;
    }

    /**
     * Returns the list of players.
     *
     * @return the list of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Returns the list of ToolCards that were assigned to this game at the beginning of it.
     *
     * @return the list of ToolCards that were assigned to this game at the beginning of it
     */
    public List<ToolCard> getDrawnToolCards() {
        return drawnToolCards;
    }

    /**
     * Returns the list of Public Objective Cards that were assigned to this game at the beginning of it.
     *
     * @return the list of Public Objective Cards that were assigned to this game at the beginning of it
     */
    public List<PublicObjectiveCard> getDrawnPublicObjectiveCards() {
        return drawnPublicObjectiveCards;
    }

    /**
     * Returns the game's status.
     *
     * @return the game's status
     */
    public GameStatus getStatus() {
        return status;
    }

    /**
     * Assigns to the game the selected toolcards and publicobjectivecards (given randomly by the controller)
     *
     * @param drawnToolCards list of ToolCards that are assigned
     * @param drawnPublicObjectiveCards list of PublicObjectiveCards that are assigne
     */
    public void setCards(List<ToolCard> drawnToolCards, List<PublicObjectiveCard> drawnPublicObjectiveCards){
        if( this.status != GameStatus.WAITING_FOR_CARDS ){
            throw new BadBehaviourRuntimeException("Can't assign cards more than once to the game. Controller should not ask for it. Bad unhandleable behaviour.");
        }

        this.drawnToolCards = drawnToolCards;
        this.drawnPublicObjectiveCards = drawnPublicObjectiveCards;

        this.status = GameStatus.WAITING_FOR_PLAYERS;
    }

    /**
     * Sets the final rankings.
     *
     * @param rankings list of ordered players: first is winner
     */
    public void setRankings(List<Player> rankings) {
        if(this.status==GameStatus.ENDED) {
            this.rankings = rankings;
        } else {
            throw new BadBehaviourRuntimeException("Can't set rankings if game is not ended. Controller should not ask for it. Bad unhandleable behaviour.");
        }
    }

    /**
     * Sets the final scores.
     *
     * @param scores map each player to its finale score
     */
    public void setScores(Map<Player, Integer> scores) {
        if(this.status==GameStatus.ENDED) {
            this.scores = (HashMap<Player, Integer>) scores;
        } else {
            throw new BadBehaviourRuntimeException("Can't set scores if game is not ended. Controller should not ask for it. Bad unhandleable behaviour.");
        }
    }

    /**
     * If possibile, adds the given player to the game.
     *
     * @param player player to add to the game
     * @return if the action succeeded
     */
    public boolean addPlayer(Player player){
        if( !players.contains(player) && players.size() < maxNumberOfPlayers){
            players.add(player);
            return true;
        }
        return false;
    }

    /**
     * Returns if the game can accept a new player according to its max number of players.
     *
     * @return if the game can accept a new player according to its max number of players
     */
    public boolean canAcceptNewPlayer(){
        return players.size() < maxNumberOfPlayers;
    }

    /**
     * Returns if the given player is the current playing one.
     *
     * @param player the player to be checked
     * @return if the given player is the current playing one
     */
    public boolean isCurrentPlayer(Player player) {
        return getCurrentRound().getCurrentTurn().isCurrentPlayer(player);
    }

    /**
     * Set the given ToolCard as used in the current Turn
     * @param toolCard
     */
    public void useToolCard(ToolCard toolCard){

        if( !this.drawnToolCards.contains(toolCard) ) {
            throw new IllegalArgumentException("Asked to use a toolcard but it is not in the drawn set");
        }

        this.drawnToolCards.get( this.drawnToolCards.indexOf(toolCard) ).use();
        this.getCurrentRound().getCurrentTurn().setUsedToolCard(toolCard);

    }

    /**
     * Proceed the game going to the next round (if available).
     *
     * @param dices the dices that are drafted from the dicebag for the new round
     * @throws NoMoreRoundsAvailableException if the method is called but all the rounds
     * that could have been played in this game were actually already played
     */
    public void nextRound(List<Dice> dices) throws NoMoreRoundsAvailableException{

        int nextRoundNumber;
        if( this.currentRound == null ){
            nextRoundNumber = 0;
        } else {
            nextRoundNumber = this.currentRound.getNumber() + 1;
        }

        if(nextRoundNumber > numberOfRounds - 1){
            throw new NoMoreRoundsAvailableException();
        }

        this.currentRound = new Round(nextRoundNumber, numberOfTurnsPerRound,this.players,new DraftPool(dices));
    }
}
