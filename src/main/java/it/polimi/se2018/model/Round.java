package it.polimi.se2018.model;

import it.polimi.se2018.controller.NoMoreTurnsAvailableException;
import it.polimi.se2018.utils.EmptyListException;
import it.polimi.se2018.utils.ValueOutOfBoundsException;

import java.util.*;

/**
 * Class representing a game's Round.
 *
 * @author Federico Haag
 */
public class Round {

    /**
     * String passed as message of IllegalArgumentException when is asked to create a round giving a null draftpool
     */
    private static final String NULL_DRAFT_POOL = "Asked to create a round giving null draftPool.";

    /**
     * String passed as message of EmptyListException when is asked to create a round with no players
     */
    private static final String NO_PLAYERS = "Asked to create a round with no players.";

    /**
     * String passed as message of ValueOutOfBoundsException when is asked to create a round with negative roundNumber
     */
    private static final String NEGATIVE_ROUND_NUMBER = "Asked to create a round with negative roundNumber.";

    /**
     * String passed as message of ValueOutOfBoundsException when is asked to create a round with negative numberOfTurns
     */
    private static final String NEGATIVE_NUMBER_OF_TURNS = "Asked to create a round with negative numberOfTurns.";

    /**
     * String passed as message of IllegalArgumentException when is asked to remove the next turn of a null player
     */
    private static final String REMOVING_TURN_OF_NULL_PLAYER = "Can't remove the next turn of a null player.";

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
     * @param roundNumber progressive number of the round
     * @param numberOfTurns how many turns has the new round
     * @param players list of players playing in the new round
     * @param draftPool instance of the round's draftpool
     */
    public Round(int roundNumber, int numberOfTurns, List<Player> players, DraftPool draftPool) {
        if(draftPool==null){ throw new IllegalArgumentException(NULL_DRAFT_POOL); }
        if(players.isEmpty()){
            throw new EmptyListException(NO_PLAYERS); }

        if(roundNumber<0){
            throw new ValueOutOfBoundsException(NEGATIVE_ROUND_NUMBER); }
        if(numberOfTurns<0){
            throw new ValueOutOfBoundsException(NEGATIVE_NUMBER_OF_TURNS); }

        this.number = roundNumber;

        this.turns = new ArrayList<>();
        for(int i=0; i < numberOfTurns; i++){
            Player player = getPlayerForTurn(players,i, numberOfTurns);
            turns.add( new Turn(i,player) );
        }

        this.currentTurnIndex = -1;
        this.draftPool = draftPool;
    }

    /**
     * Returns the progressive number of this round.
     * @return the progressive number of this round
     */
    public int getNumber() {
        return number;
    }

    /**
     * Returns the current turn (based on {@link Round#currentTurnIndex}.
     * @return the current turn (based on {@link Round#currentTurnIndex}
     */
    public Turn getCurrentTurn() {
        return turns.get(currentTurnIndex);
    }

    /**
     * Returns the round's draftpool.
     * @return the round's draftpool
     */
    public DraftPool getDraftPool() {
        return draftPool;
    }

    /**
     * Method that gets the list of players who played the last round in order from last to first without repetitions.
     * @return the list of players who played the last round in order
     * @see it.polimi.se2018.controller.Scorer#getRankings(Set, Set, Set)
     * @author Jacopo Pio Gargano
     */
    public Set<Player> getPlayersByReverseTurnOrder(){

        Set<Player> players = new LinkedHashSet<>();
        Turn turn;

        for(int i= turns.size()-1; i>=0; i--){
            turn = turns.get(i);
            players.add(turn.getPlayer());
        }

        return players;
    }

    /**
     * Proceed the game going to the next turn (if available).
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
     * on the given round and turn (from the given list of players).
     * @param players list of players playing the game
     * @param turnNumber sequential number of the turn (starting from 0)
     * @param numberOfTurns number of turns of the new round
     * @return the Player who should be playing according to the game rules in the specified round/turn
     * @author Jacopo Pio Gargano
     * @author Federico Haag
     */
    private Player getPlayerForTurn(List<Player> players, int turnNumber, int numberOfTurns){

        int numberOfPlayers = players.size();
        int roundNumber = this.number;
        int playerShouldPlayingIndex;

        if(turnNumber >= numberOfPlayers){
            turnNumber = numberOfTurns - turnNumber -1;
        }

        playerShouldPlayingIndex = ((roundNumber % numberOfPlayers) + turnNumber) % numberOfPlayers;

        return players.get(playerShouldPlayingIndex);
    }

    /**
     * Removes the next turn of the specified player.
     * @param player the player whose turn needs to be removed
     * @return true if the operation was successful, otherwise false
     * @author Jacopo Pio Gargano
     */
    public boolean removeNextTurnOfPlayer(Player player) {
        if(player == null){throw new IllegalArgumentException(REMOVING_TURN_OF_NULL_PLAYER);}
        for (int i = currentTurnIndex + 1; i < turns.size(); i++) {
            Turn currentTurn = turns.get(i);
            Player currentPlayer = currentTurn.getPlayer();

            if (currentPlayer.equals(player)) {
                turns.remove(currentTurn);
                return true;
            }
        }
        return false;
    }
}
