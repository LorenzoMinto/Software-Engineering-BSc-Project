package it.polimi.se2018.model;

import it.polimi.se2018.controller.NoMoreRoundsAvailableException;
import it.polimi.se2018.utils.Observable;

import java.util.*;

public class Game extends Observable {

    private final int numberOfRounds;
    private final int maxNumberOfPlayers;
    private final int numberOfTurnsPerRound;

    private Round currentRound;

    private Track track;

    private List<Player> players;

    private ArrayList<ToolCard> drawnToolCards;

    private List<PublicObjectiveCard> drawnPublicObjectiveCards;

    private GameStatus status;

    private List<Player> rankings;

    private HashMap<Player,Integer> scores;

    public Game(int numberOfRounds, int maxNumberOfPlayers) {
        this.currentRound = null;
        this.track = new Track();
        this.players = new ArrayList<>();
        this.drawnToolCards = new ArrayList<>();
        this.drawnPublicObjectiveCards = new ArrayList<>();
        this.status = GameStatus.WAITING_FOR_PLAYERS;
        this.rankings = null;
        this.scores = null;

        this.numberOfRounds = numberOfRounds;
        this.maxNumberOfPlayers = maxNumberOfPlayers;
        this.numberOfTurnsPerRound = maxNumberOfPlayers * 2;
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

    public List<ToolCard> getDrawnToolCards() {
        return drawnToolCards;
    }

    public List<PublicObjectiveCard> getDrawnPublicObjectiveCards() {
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

    public void setScores(Map<Player, Integer> scores) {
        if(this.status==GameStatus.ENDED) {
            this.scores = (HashMap<Player, Integer>) scores;
        } else {
            throw new RuntimeException("Can't se rankings if game is not ended");
        }
    }

    public boolean addPlayer(Player player){
        if( !players.contains(player) && players.size() < maxNumberOfPlayers){
            players.add(player);
            return true;
        }
        return false;
    }

    public boolean canAcceptNewPlayer(){
        return players.size() < maxNumberOfPlayers;
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

        if(nextRoundNumber > numberOfRounds - 1){
            throw new NoMoreRoundsAvailableException();
        }

        this.currentRound = new Round(nextRoundNumber, numberOfTurnsPerRound,this.players,new DraftPool(dices));
    }
}
