package it.polimi.se2018.controller;

import it.polimi.se2018.model.Dice;

public class DraftDiceFromTrackMove extends PlayerMove {
    private Dice dice;

    public DraftDiceFromTrackMove(Dice dice) {
        this.dice = dice;
    }

    public Dice getDice() {
        return dice;
    }
}
