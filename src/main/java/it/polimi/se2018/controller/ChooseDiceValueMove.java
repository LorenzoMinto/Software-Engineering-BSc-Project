package it.polimi.se2018.controller;

public class ChooseDiceValueMove extends PlayerMove {
    private int value;

    public ChooseDiceValueMove(int value) {
        if(value > 0 && value <= 6) {
            this.value = value;
        }else throw new IllegalArgumentException("Cannot change dice value to "+ value);
    }

    public int getValue() {
        return value;
    }
}
