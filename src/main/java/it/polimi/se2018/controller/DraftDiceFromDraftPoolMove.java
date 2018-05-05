package it.polimi.se2018.controller;

import it.polimi.se2018.model.Dice;

public class DraftDiceFromDraftPoolMove extends PlayerMove {

    Dice dice;

    public DraftDiceFromDraftPoolMove(Dice dice) {
        this.dice = dice;
    }

    public Dice getDice() {
        return dice;
    }
}
