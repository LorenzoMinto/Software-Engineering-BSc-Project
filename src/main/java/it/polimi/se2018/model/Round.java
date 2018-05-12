package it.polimi.se2018.model;

import it.polimi.se2018.controller.NoMoreTurnsAvailableException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        this.number = roundNumber;

        this.turns = new ArrayList<>();
        for(int i=0; i < numberOfTurns; i++){
            int turnNumber = i;
            Player player = getPlayerForTurn(players,turnNumber,numberOfTurns);
            turns.add( new Turn(turnNumber,player) );
        }

        this.currentTurnIndex = 0;
        this.draftPool = draftPool;
    }

    /**
     * Returns the prorgessive number of this round.
     *
     * @return the prorgessive number of this round.
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
     * @return
     * @author Jacopo Pio Gargano
     */
    public List<Player> getPlayersByTurnOrderReverse(){ //TODO: commentare
        Set<Player> players = new HashSet<>();
        for(int i= turns.size()-1; i>=0; i--){
            Turn turn = turns.get(i);
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
     * @return the Player obj relative to which player should play according to the game rules in the specified round/turn
     * @author Jacopo Pio Gargano
     * @author Federico Haag
     */
    private Player getPlayerForTurn(List<Player> players, int turnNumber, int numberOfTurnsPerRound){

        int numberOfPlayers = players.size();
        if( turnNumber >= numberOfPlayers ){ turnNumber = numberOfTurnsPerRound - turnNumber - 1; }
        int playerShouldPlayingIndex = (turnNumber + (this.number % numberOfPlayers)) % numberOfPlayers;

        return players.get(playerShouldPlayingIndex);
    }

}
