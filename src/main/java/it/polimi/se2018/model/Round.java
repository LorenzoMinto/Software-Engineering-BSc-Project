package it.polimi.se2018.model;

import java.util.List;

public class Round {

    private int number;

    private Turn currentTurn;

    private DraftPool draftPool;

    public Round(int number, DraftPool draftPool) {
        if(draftPool==null){ throw new IllegalArgumentException("Asked to create a round giving null draftPool"); }

        this.number = number;
        this.currentTurn = null;
        this.draftPool = draftPool;
    }


    //Getters

    public int getNumber() {
        return number;
    }

    public Turn getCurrentTurn() {
        return currentTurn;
    }

    public DraftPool getDraftPool() {
        return draftPool;
    }

    //Setters

    public void setCurrentTurn(Turn turn){
        this.currentTurn = turn;
    }

}
