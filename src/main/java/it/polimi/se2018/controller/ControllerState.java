package it.polimi.se2018.controller;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.view.View;

public interface ControllerState {

    public void draftDiceFromDraftPool(Dice dice, View view);

    public void placeDice(int row, int col, View view);

    public void useToolCard(Player player, ToolCard toolcard, View view);

    public void chooseDiceFromTrack(Dice dice, int slotNumber, View view);

    public void moveDice(int rowFrom, int colFrom, int rowTo, int colTo, View view);

    public void incrementDice(View view);

    public void decrementDice(View view);

    public void chooseDiceValue(int value, View view);

    public void executeImplicitBehaviour();

}
