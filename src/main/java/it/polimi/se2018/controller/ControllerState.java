package it.polimi.se2018.controller;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCard;

public interface ControllerState {

    public void draftDiceFromDraftPool(Dice dice);

    public void placeDice(int row, int col);

    public boolean useToolCard(ToolCard toolcard);

    public void draftDiceFromTrack(Dice dice);

    public void moveDice(int rowFrom, int colFrom, int rowTo, int colTo);

    public void incrementDice();

    public void decrementDice();

    public void chooseDiceValue(int value);

    public void executeImplicitBehaviour();

}
