package it.polimi.se2018.model;

import it.polimi.se2018.controller.NoMoreRoundsAvailableException;
import it.polimi.se2018.utils.Observable;

import javax.tools.Tool;
import java.util.*;

public class Game extends Observable {

    private final int NUMBER_OF_ROUNDS;
    private final int MAX_NUMBER_OF_PLAYERS;
    private final int NUMBER_OF_TURNS_PER_ROUND;

    private Round currentRound;

    private Track track;

    private List<Player> players;

    private ArrayList<ToolCard> drawnToolCards;

    private Set<PublicObjectiveCard> drawnPublicObjectiveCards;

    private GameStatus status;

    private List<Player> rankings;

    private HashMap<Player,Integer> scores;

    public Game(int numberOfRounds, int maxNumberOfPlayers) {
        this.currentRound = null;
        this.track = new Track();
        this.players = new ArrayList<>();
        this.drawnToolCards = new ArrayList<>();
        this.drawnPublicObjectiveCards = new HashSet<>();
        this.status = GameStatus.WAITING_FOR_PLAYERS;
        this.rankings = null;
        this.scores = null;

        this.NUMBER_OF_ROUNDS = numberOfRounds;
        this.MAX_NUMBER_OF_PLAYERS = maxNumberOfPlayers;
        this.NUMBER_OF_TURNS_PER_ROUND = maxNumberOfPlayers * 2;
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

    public GameStatus getStatus() {
        return status;
    }

    public void setCurrentRound(Round currentRound) {
        this.currentRound = currentRound;
    }

    public void setRankings(List<Player> rankings) {
        if(this.status==GameStatus.ENDED) {
            this.rankings = rankings;
        } else {
            throw new RuntimeException("Can't se rankings if game is not ended");
        }
    }

    public void setScores(HashMap<Player, Integer> scores) {
        if(this.status==GameStatus.ENDED) {
            this.scores = scores;
        } else {
            throw new RuntimeException("Can't se rankings if game is not ended");
        }
    }

    public boolean addPlayer(Player player){
        if( !players.contains(player) && players.size() < MAX_NUMBER_OF_PLAYERS ){
            players.add(player);
            return true;
        }
        return false;
    }

    public boolean canAcceptNewPlayer(){
        return players.size() < MAX_NUMBER_OF_PLAYERS;
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

    public void nextRound(List<Dice> dices) throws NoMoreRoundsAvailableException{

        int nextRoundNumber;
        if( this.currentRound == null ){
            nextRoundNumber = 0;
        } else {
            nextRoundNumber = this.currentRound.getNumber() + 1;
        }

        if(nextRoundNumber > NUMBER_OF_ROUNDS - 1){
            throw new NoMoreRoundsAvailableException();
        }

        this.currentRound = new Round(nextRoundNumber,NUMBER_OF_TURNS_PER_ROUND,this.players,new DraftPool(dices));
    }
}
