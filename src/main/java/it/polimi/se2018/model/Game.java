package it.polimi.se2018.model;

import java.util.List;

public class Game {

    //TODO: load these values from config file
    public static final int NUMBER_OF_ROUNDS = 10;
    public static final int NUMBER_OF_TURNS_PER_ROUND = 8;
    public static final int MAX_NUMBER_OF_PLAYERS = 4;


    public Round currentRound;
    public Track track;
    public List<Player> players;
    public List<ToolCard> drawnToolCards;
    public List<PublicObjectiveCard> drawnPublicObjectiveCards;

    public Game() {
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
        int nextRoundNumber = this.currentRound.getNumber() + 1;
        if(nextRoundNumber > NUMBER_OF_ROUNDS){ throw new IllegalStateException("Asked to create a round but exceeding round number limit"); }

        return new Round(nextRoundNumber,new DraftPool( dices ));
    }



    public Player endGame(){
        //TODO: implement method. Returns the winner.
        Player winnerPlayer = null;

        return winnerPlayer;
    }

}
