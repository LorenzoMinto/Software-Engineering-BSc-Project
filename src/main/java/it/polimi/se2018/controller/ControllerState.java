package it.polimi.se2018.controller;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.view.View;

public abstract class ControllerState {

    static final String MIDDLE_OF_EFFECT = "Can't do. You are in the middle of a Toolcard effect";
    static final String NO_DICE_DRAFTED = "Can't do. No dice has been drafted.";
    static final String DRAFT_FIRST = "Can't do. You have to draft first";
    static final String ONLY_DRAFT_AND_PLACE = "Can't do. You can only draft and place";
    static final String PLACE_DICE = "Can't do. You have to place the drafted dice";
    static final String FIRST_DRAFT_DICE = "Can't do. You have to choose a dice from the draft pool first.";
    static final String TOOLCARD_ONLY = "Can't do. You have already drafted and placed. You can only use a Toolcard";

    String defaultMessage = "Default Message. Actually I don't know what to say.";

    protected Controller controller;

    public void draftDiceFromDraftPool(Dice dice, View view){ view.showMessage(defaultMessage); }

    public void placeDice(int row, int col, View view){ view.showMessage(defaultMessage); }

    public void useToolCard(Player player, ToolCard toolcard, View view){ view.showMessage(defaultMessage); }

    public void chooseDiceFromTrack(Dice dice, int slotNumber, View view){ view.showMessage(defaultMessage); }

    public void moveDice(int rowFrom, int colFrom, int rowTo, int colTo, View view){ view.showMessage(defaultMessage); }

    public void incrementDice(View view){ view.showMessage(defaultMessage); }

    public void decrementDice(View view){ view.showMessage(defaultMessage); }

    public void chooseDiceValue(int value, View view){ view.showMessage(defaultMessage); }

    public void executeImplicitBehaviour(){
        //do nothing by default
    }

}
