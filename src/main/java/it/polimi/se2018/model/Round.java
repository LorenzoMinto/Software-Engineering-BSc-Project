package it.polimi.se2018.model;

import it.polimi.se2018.controller.NoMoreTurnsAvailableException;

import java.util.*;

/**
 * Class representing a game's Round.
 *
 * @author Federico Haag
 */
public class Round {

    /**
     * The round progressive number.
     */
    private int number;

    /**
     * List of all turns of the round.
     */
    private List<Turn> turns;

    /**
     * Index of the current turn in the list {@link Round#turns}.
     */
    private int currentTurnIndex;

    /**
     * The Round's DraftPool
     */
    private DraftPool draftPool;

    /**
     * Constructor of a new Round.
     *
     * @param roundNumber progressive number of the round
     * @param numberOfTurns how many turns has the new round
     * @param players list of players playing in the new round
     * @param draftPool instance of the round's draftpool
     */
    public Round(int roundNumber, int numberOfTurns, List<Player> players, DraftPool draftPool) {
        if(draftPool==null){ throw new IllegalArgumentException("Asked to create a round giving null draftPool"); }
        if(players==null || players.isEmpty()){
            throw new IllegalArgumentException("Asked to create a round with no players"); }
        if(roundNumber<0){
            throw new IllegalArgumentException("Asked to create a round with negative roundNumber"); }
        if(numberOfTurns<0){
            throw new IllegalArgumentException("Asked to create a round with negative numberOfTurns"); }

        this.number = roundNumber;

        this.turns = new ArrayList<>();
        for(int i=0; i < numberOfTurns; i++){
            int turnNumber = i;
            Player player = getPlayerForTurn(players,turnNumber);
            turns.add( new Turn(turnNumber,player) );
        }

        this.currentTurnIndex = 0;
        this.draftPool = draftPool;
    }

    /**
     * Returns the progressive number of this round.
     *
     * @return the progressive number of this round.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Returns the current turn (based on {@link Round#currentTurnIndex}.
     *
     * @return the current turn (based on {@link Round#currentTurnIndex}
     */
    public Turn getCurrentTurn() {
        return turns.get(currentTurnIndex);
    }

    /**
     * Returns the round's draftpool.
     *
     * @return the round's draftpool
     */
    public DraftPool getDraftPool() {
        return draftPool;
    }

    /**
     * Method that gets the list of players who played the last round in order from last to first without repetitions
     *
     * @return the list of players who played the last round in order
     * @see it.polimi.se2018.controller.Scorer#getRankings(List, List)
     * @author Jacopo Pio Gargano
     */
    public List<Player> getPlayersByReverseTurnOrder(){

        Set<Player> players = new LinkedHashSet<>();
        Turn turn;

        for(int i= turns.size()-1; i>=0; i--){
            turn = turns.get(i);
            players.add(turn.getPlayer());
        }

        return new ArrayList<>(players);
    }

    /**
     * Proceed the game going to the next turn (if available).
     *
     * @throws NoMoreTurnsAvailableException if the method is called but all the turns
     * that could have been played in this round were actually already played
     */
    //TODO Test
    public void nextTurn() throws NoMoreTurnsAvailableException{

        int nextTurnIndex = this.currentTurnIndex + 1;
        if( nextTurnIndex > this.turns.size() - 1 ){
            throw new NoMoreTurnsAvailableException();
        }

        this.currentTurnIndex = nextTurnIndex;
    }

    /**
     * Returns the player that according to game rules should play
     * on the given round and turn (from the given list of players)
     *
     * @param players list of players playing the game
     * @param turnNumber sequential number of the turn (starting from 0)
     * @return the Player who should be playing according to the game rules in the specified round/turn
     * @author Jacopo Pio Gargano
     * @author Federico Haag
     */
    //TODO Test
    private Player getPlayerForTurn(List<Player> players, int turnNumber){
        if(turnNumber<0){
            throw new IllegalArgumentException("Asked to get a player for a turn with negative turnNumber"); }
        if(players==null || players.isEmpty()){
            throw new IllegalArgumentException("Asked to get a player for a turn from a list of no players"); }

        int numberOfPlayers = players.size();
        int roundNumber = this.number;
        int playerShouldPlayingIndex;

        if(turnNumber >= numberOfPlayers){
            turnNumber = 2*numberOfPlayers - turnNumber -1;
        }

        playerShouldPlayingIndex = ((roundNumber % numberOfPlayers) + turnNumber) % numberOfPlayers;

        Player playerShouldBePlaying = players.get(playerShouldPlayingIndex);

        return playerShouldBePlaying;
    }

}
