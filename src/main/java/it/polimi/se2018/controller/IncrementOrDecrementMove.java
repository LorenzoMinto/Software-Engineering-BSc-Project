package it.polimi.se2018.controller;

public class IncrementOrDecrementMove extends PlayerMove{
    private boolean isIncrement;

    public IncrementOrDecrementMove(boolean isIncrement) {
        this.isIncrement = isIncrement;
    }

    public boolean isIncrement(){
        return this.isIncrement;
    }
}
