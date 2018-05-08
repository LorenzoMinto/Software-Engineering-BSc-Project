package it.polimi.se2018.model;

import it.polimi.se2018.utils.Observable;

import javax.tools.Tool;
import java.util.*;

public class Game extends Observable {

    //TODO: load these values from config file
    public static final int NUMBER_OF_ROUNDS = 10;
    public static final int MAX_NUMBER_OF_PLAYERS = 4;
    public static final int NUMBER_OF_TURNS_PER_ROUND = MAX_NUMBER_OF_PLAYERS * 2;


    public Round currentRound;
    public Track track;
    public List<Player> players;
    public ArrayList<ToolCard> drawnToolCards;
    public Set<PublicObjectiveCard> drawnPublicObjectiveCards;

    public Game() {
        this.currentRound = null;
        this.track = new Track();
        this.players = new ArrayList<>();
        this.drawnToolCards = new ArrayList<>();
        this.drawnPublicObjectiveCards = new HashSet<>();
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

        this.currentRound = new Round(nextRoundNumber, new DraftPool( dices ), this.players );
        return this.currentRound;
    }

    public boolean isCurrentPlayer(Player player) {
        return this.currentRound.currentTurn.isCurrentPlayer(player);
    }

    public void useToolCard(ToolCard toolCard){

        if( !this.drawnToolCards.contains(toolCard) ) {
            throw new IllegalArgumentException("Asked to use a toolcard but it is not in the drawn set");
        }

        this.drawnToolCards.get( this.drawnToolCards.indexOf(toolCard) ).use();
        this.currentRound.currentTurn.setUsedToolCard(toolCard);

    }
}
