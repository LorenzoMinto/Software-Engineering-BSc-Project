package it.polimi.se2018.controller;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.view.View;

/**
 * Abstract class that represents a generic state of the {@link Controller}.
 *
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public abstract class ControllerState {

    /**
     * String to be printed if a non pertaining method is called in the middle of a ToolCard effect.
     */
    static final String MIDDLE_OF_EFFECT = "Can't do. You are in the middle of a Toolcard effect";

    /**
     * String to be printed if a method that requires drafting is called before any dice was drafted.
     */
    static final String NO_DICE_DRAFTED = "Can't do. No dice has been drafted.";

    /**
     * String to be printed if only action possible is to draft and place.
     */
    static final String ONLY_DRAFT_AND_PLACE = "Can't do. You can only draft and place";

    /**
     * String to be printed if there's a drafted dice that needs to be placed.
     */
    static final String PLACE_DICE = "Can't do. You have to place the drafted dice";

    /**
     * String to be printed if the only action possible is a ToolCard activation.
     */
    static final String TOOLCARD_ONLY = "Can't do. You have already drafted and placed. You can only use a Toolcard";

    /**
     * Default message
     */
    String defaultMessage = "Default Message. Actually I don't know what to say.";


    /**
     * Reference to the {@link Controller} to which this state belongs.
     */
    protected Controller controller;


    /**
     * Drafts a specified dice from the current draft pool and sets it as the drafted dice on the current turn.
     *
     * @param dice the dice to be drafted.
     * @param view the view requesting the action.
     */
    public void draftDiceFromDraftPool(Dice dice, View view){ view.showMessage(defaultMessage); }

    /**
     * Places the drafted dice of the current turn on the specified cell (row and column).
     *
     * @param row the row index of the cell where the dice is to be placed.
     * @param col the column index of the cell where the dice is to be placed.
     * @param view the view requesting the action.
     */
    public void placeDice(int row, int col, View view){ view.showMessage(defaultMessage); }

    /**
     * Activates, if allowed, the effect of the passed tool card by initiating the relative state succession. The
     * passed toolCard is set as activeToolCard in the Controller.
     *
     * @param player the player requesting the action.
     * @param toolcard the toolcard to be activated.
     * @param view the view requesting the action.
     */
    public void useToolCard(Player player, ToolCard toolcard, View view){ view.showMessage(defaultMessage); }

    /**
     * Removes the specified dice from the specified track slot and sets it as trackChosenDice on the current turn.
     *
     * @param dice the chosen dice.
     * @param slotNumber the number of the TrackSlot where the dice is.
     * @param view the view requesting the action.
     */
    public void chooseDiceFromTrack(Dice dice, int slotNumber, View view){ view.showMessage(defaultMessage); }

    /**
     * Moves, if legal, the dice found at the cell designated by (rowFrom, colFrom) to the cell designated by (rowTo,
     * colTo).
     *
     * @param rowFrom the row index of the cell FROM which the dice is to be moved.
     * @param colFrom the column index of the cell FROM which the dice is to be moved.
     * @param rowTo the row index of the cell TO which the dice is to be moved.
     * @param colTo the column index of the cell TO which the dice is to be moved.
     * @param view the view requesting the action.
     */
    public void moveDice(int rowFrom, int colFrom, int rowTo, int colTo, View view){ view.showMessage(defaultMessage); }


    /**
     * Increments the value of the drafted dice found in the current turn.
     *
     * @param view the view requesting the action.
     */
    public void incrementDice(View view){ view.showMessage(defaultMessage); }


    /**
     * Decrements the value of the drafted dice found in the current turn.
     *
     * @param view the view requesting the action.
     */
    public void decrementDice(View view){ view.showMessage(defaultMessage); }

    /**
     * Sets the value of the drafted dice found in the current turn to the specified value.
     *
     * @param value the chosen value for the drafted dice.
     * @param view the view requesting the action.
     */
    public void chooseDiceValue(int value, View view){ view.showMessage(defaultMessage); }

    /**
     * Executes some implicit behaviour relative to the state. It does nothing when state is not Implicit.
     */
    public void executeImplicitBehaviour(){
        //do nothing by default
    }

}
