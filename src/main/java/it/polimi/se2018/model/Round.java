package it.polimi.se2018.model;

import it.polimi.se2018.controller.NoMoreTurnsAvailableException;

import java.util.ArrayList;
import java.util.List;

public class Round {

    private int number;

    private List<Turn> turns;

    private int currentTurnIndex;

    private DraftPool draftPool;

    public Round(int roundNumber, int numberOfTurns, List<Player> players, DraftPool draftPool) {
        if(draftPool==null){ throw new IllegalArgumentException("Asked to create a round giving null draftPool"); }

        this.number = roundNumber;

        this.turns = new ArrayList<>();
        for(int i=0; i<=numberOfTurns; i++){
            int turnNumber = i;
            Player player = getPlayerForTurn(players,turnNumber);
            turns.add( new Turn(turnNumber,player) );
        }

        this.currentTurnIndex = 0;
        this.draftPool = draftPool;
    }

    //Getters

    public int getNumber() {
        return number;
    }

    public Turn getCurrentTurn() {
        return turns.get(currentTurnIndex);
    }

    public DraftPool getDraftPool() {
        return draftPool;
    }

    public List<Player> getPlayersByTurnOrder(){
        List<Player> players = new ArrayList<>();
        for(Turn turn : turns){
            players.add(turn.getPlayer());
        }
        return players;
    }

    public void nextTurn() throws NoMoreTurnsAvailableException{

        int nextTurnIndex = this.currentTurnIndex + 1;
        if( nextTurnIndex > this.turns.size() - 1 ){
            throw new NoMoreTurnsAvailableException();
        }

        this.currentTurnIndex = nextTurnIndex;
    }

    //Utils

    /**
     * @author Jacopo Pio Gargano, Federico Haag
     * @param players list of players playing the game
     * @param turnNumber sequential number of the turn (starting from 0)
     * @return the Player obj relative to which player should play according to the game rules in the specified round/turn
     * @see Player
     */
    private Player getPlayerForTurn(List<Player> players, int turnNumber){

        int numberOfPlayers = players.size();
        if( turnNumber >= numberOfPlayers ){ turnNumber = Game.NUMBER_OF_TURNS_PER_ROUND - turnNumber - 1; }
        int playerShouldPlayingIndex = (turnNumber + (this.number % numberOfPlayers)) % numberOfPlayers;

        return players.get(playerShouldPlayingIndex);
    }

}
