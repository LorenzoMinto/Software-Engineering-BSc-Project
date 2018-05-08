package it.polimi.se2018.model;

import it.polimi.se2018.utils.Observable;

import javax.tools.Tool;
import java.util.*;

public class Game extends Observable {

    //TODO: load these values from config file
    public static final int NUMBER_OF_ROUNDS = 10;
    public static final int MAX_NUMBER_OF_PLAYERS = 4;
    public static final int NUMBER_OF_TURNS_PER_ROUND = MAX_NUMBER_OF_PLAYERS * 2;

    private Round currentRound;

    private Track track;

    private List<Player> players;

    private ArrayList<ToolCard> drawnToolCards;

    private Set<PublicObjectiveCard> drawnPublicObjectiveCards;

    public Game() {
        this.currentRound = null;
        this.track = new Track();
        this.players = new ArrayList<>();
        this.drawnToolCards = new ArrayList<>();
        this.drawnPublicObjectiveCards = new HashSet<>();
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public Track getTrack() {
        return track;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public ArrayList<ToolCard> getDrawnToolCards() {
        return drawnToolCards;
    }

    public Set<PublicObjectiveCard> getDrawnPublicObjectiveCards() {
        return drawnPublicObjectiveCards;
    }

    public void setCurrentRound(Round currentRound) {
        this.currentRound = currentRound;
    }

    public boolean addPlayer(Player player){
        if( !players.contains(player) && players.size() < MAX_NUMBER_OF_PLAYERS ){
            players.add(player);
            return true;
        }
        return false;
    }

    public boolean isCurrentPlayer(Player player) {
        return getCurrentRound().getCurrentTurn().isCurrentPlayer(player);
    }

    public void useToolCard(ToolCard toolCard){

        if( !this.drawnToolCards.contains(toolCard) ) {
            throw new IllegalArgumentException("Asked to use a toolcard but it is not in the drawn set");
        }

        this.drawnToolCards.get( this.drawnToolCards.indexOf(toolCard) ).use();
        this.getCurrentRound().getCurrentTurn().setUsedToolCard(toolCard);

    }
}
