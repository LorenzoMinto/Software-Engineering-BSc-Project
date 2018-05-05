package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.List;

public class Round {

    private int number;

    public Turn currentTurn;

    public DraftPool draftPool;

    public Round(int number, DraftPool draftPool) {
        this.number = number;
        this.draftPool = draftPool;
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

    public Turn nextTurn(Player nextTurnPlayer){

        int nextTurnNumber = this.currentTurn.getNumber() + 1;

        if(nextTurnNumber > Game.NUMBER_OF_TURNS_PER_ROUND){ throw new IllegalStateException("Asked to create a turn but exceding turns number limit"); }

        //Creates the turn
        Turn t = new Turn(nextTurnNumber,nextTurnPlayer);

        //Sets the new turn as the current one
        this.currentTurn = t;

        return this.currentTurn;
    }

}
