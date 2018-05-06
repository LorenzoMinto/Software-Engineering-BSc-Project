package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.List;

public class Game {

    //TODO: load these values from config file
    public static final int NUMBER_OF_ROUNDS = 10;
    public static final int MAX_NUMBER_OF_PLAYERS = 4;
    public static final int NUMBER_OF_TURNS_PER_ROUND = MAX_NUMBER_OF_PLAYERS * 2;


    public Round currentRound;
    public Track track;
    public List<Player> players;
    public List<ToolCard> drawnToolCards;
    public List<PublicObjectiveCard> drawnPublicObjectiveCards;

    public Game() {
        this.currentRound = null;
        this.track = new Track();
        this.players = new ArrayList<>();
        this.drawnToolCards = new ArrayList<>();
        this.drawnPublicObjectiveCards = new ArrayList<>();
    }

    public boolean addPlayer(Player player){
        if( !players.contains(player) && players.size() < MAX_NUMBER_OF_PLAYERS ){
            players.add(player);
            return true;
        }
        return false;
    }

    public boolean hasNextRound(){
        return ( this.currentRound.getNumber() < NUMBER_OF_ROUNDS );
    }

    public Round nextRound(List<Dice> dices){

        //TODO: end previous round, empty draftPool and fill track

        int nextRoundNumber;

        //Calculate the number of the next round
        if( this.currentRound == null ){
            nextRoundNumber = 0;
        } else {
            nextRoundNumber = this.currentRound.getNumber() + 1;

            if(nextRoundNumber >= NUMBER_OF_ROUNDS){
                throw new IllegalStateException("Asked to create a round but exceeding round number limit");
            }
        }

        //Creates the list of players in the correct order of the next round
        List<Player> roundPlayers = new ArrayList<>();
        for(int turnNumber=0; turnNumber<Game.NUMBER_OF_TURNS_PER_ROUND; turnNumber++){
            roundPlayers.add( this.whoShouldBePlayingDuringTurn(nextRoundNumber,turnNumber) );
        }

        this.currentRound = new Round(nextRoundNumber, new DraftPool( dices ), roundPlayers );
        return this.currentRound;
    }

    public Player endGame(){
        //TODO: implement method. Returns the winner.
        Player winnerPlayer = null;

        return winnerPlayer;
    }


    private Player whoShouldBePlayingDuringTurn(int roundNumber, int turnNumber){

        int numberOfPlayers = this.players.size();

        if( turnNumber >= numberOfPlayers ){ turnNumber = NUMBER_OF_TURNS_PER_ROUND - turnNumber - 1; }

        int playerShouldPlayingIndex = (turnNumber + (roundNumber % numberOfPlayers)) % numberOfPlayers;

        return this.players.get(playerShouldPlayingIndex);
    }

    public boolean isCurrentPlayer(Player player) {
        return this.currentRound.currentTurn.isCurrentPlayer(player);
    }
}
