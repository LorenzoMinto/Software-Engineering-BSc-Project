package it.polimi.se2018.controller;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.view.View;

public abstract class ControllerState {

    protected static final String MIDDLEOFEFFECT= "Can't do. You are in the middle of a Toolcard effect";
    protected static final String NODICEDRAFTED = "Can't do. No dice has been drafted.";
    protected static final String DRAFTFIRST= "Can't do. You have to draft first";
    protected static final String ONLYDRAFTANDPLACE= "Can't do. You can only draft and place";
    protected static final String PLACEDICE= "Can't do. You have to place the drafted dice";
    protected static final String IMPLICITWARNING= "This is an implicit state.";
    protected static final String FIRSTDRAFTDICE = "Can't do. You have to choose a dice from the draft pool first.";
    protected static final String TOOLCARDONLY= "Can't do. You have already drafted and placed. You can only use a Toolcard";

    public void draftDiceFromDraftPool(Dice dice, View view){}

    public void placeDice(int row, int col, View view){}

    public void useToolCard(Player player, ToolCard toolcard, View view){}

    public void chooseDiceFromTrack(Dice dice, int slotNumber, View view){}

    public void moveDice(int rowFrom, int colFrom, int rowTo, int colTo, View view){}

    public void incrementDice(View view){}

    public void decrementDice(View view){}

    public void chooseDiceValue(int value, View view){}

    public void executeImplicitBehaviour(){}

}
