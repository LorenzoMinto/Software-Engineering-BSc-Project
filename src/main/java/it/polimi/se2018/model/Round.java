package it.polimi.se2018.model;

import java.util.List;

public class Round {

    private int number;

    public Turn currentTurn;

    public DraftPool draftPool;

    //List of players of the game passed from game
    private List<Player> players;

    public Round(int number, DraftPool draftPool, List<Player> players) {
        if(draftPool==null){ throw new IllegalArgumentException("Asked to create a round giving null draftPool"); }

        this.number = number;
        this.currentTurn = null;
        this.draftPool = draftPool;
        this.players = players;
    }


    //Getters

    public int getNumber() {
        return number;
    }

    //Setters

    public void setCurrentTurn(Turn turn){
        this.currentTurn = turn;
    }


    //Turns management

    public boolean hasNextTurn(){
        return ( this.currentTurn.getNumber() < Game.NUMBER_OF_TURNS_PER_ROUND );
    }

    public Turn nextTurn(){

        int nextTurnNumber;

        if( this.currentTurn == null ){
            nextTurnNumber = 0;
        } else {
            nextTurnNumber = this.currentTurn.getNumber() + 1;

            if(nextTurnNumber >= Game.NUMBER_OF_TURNS_PER_ROUND){
                throw new IllegalStateException("Asked to create a turn but exceeding turns number limit");
            }
        }

        Player nextTurnPlayer = this.whoShouldBePlayingDuringTurn(this.getNumber(),nextTurnNumber);

        //Creates the new turn an sets it as the current one
        this.currentTurn = new Turn(nextTurnNumber,nextTurnPlayer);

        return this.currentTurn;
    }


    public List<Player> getPlayers() {
        return players;
    }

    private Player whoShouldBePlayingDuringTurn(int roundNumber, int turnNumber){

        int numberOfPlayers = this.players.size();

        if( turnNumber >= numberOfPlayers ){ turnNumber = Game.NUMBER_OF_TURNS_PER_ROUND - turnNumber - 1; }

        int playerShouldPlayingIndex = (turnNumber + (roundNumber % numberOfPlayers)) % numberOfPlayers;

        return this.players.get(playerShouldPlayingIndex);
    }

}
