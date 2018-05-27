package it.polimi.se2018.controller;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.utils.message.CVMessage;

import static it.polimi.se2018.utils.message.CVMessage.types.ERROR_MESSAGE;

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
    static final String MIDDLE_OF_EFFECT = "Can't do that. You're currently in the middle of a toolcard effect.";

    /**
     * String to be printed if a method that requires drafting is called before any dice was drafted.
     */
    static final String NO_DICE_DRAFTED = "Can't do that. You haven't drafted a die yet.";

    /**
     * String to be printed if only action possible is to draft and place.
     */
    static final String ONLY_DRAFT_AND_PLACE =  "Can't do that. You can only draft and place. ToolCard already used.";

    /**
     * String to be printed if there's a drafted dice that needs to be placed.
     */
    static final String PLACE_DICE = "Can't do that. You have to place the drafted dice first.";

    /**
     * String to be printed if the only action possible is a ToolCard activation.
     */
    static final String TOOLCARD_ONLY = "Can't do that. You can only use a toolcard that doesn't require drafting.";

    /**
     * Default message
     */
    String defaultMessage = "Move not legal.";


    /**
     * Reference to the {@link Controller} to which this state belongs.
     */
    protected Controller controller;


    /**
     * Drafts a specified dice from the current draft pool and sets it as the drafted dice on the current turn.
     * @param dice the dice to be drafted.
     *
     */
    public CVMessage draftDiceFromDraftPool(Dice dice){ return new CVMessage(ERROR_MESSAGE, defaultMessage); }

    /**
     * Places the drafted dice of the current turn on the specified cell (row and column).
     * @param row the row index of the cell where the dice is to be placed.
     * @param col the column index of the cell where the dice is to be placed.
     */
    public CVMessage placeDice(int row, int col){ return new CVMessage(ERROR_MESSAGE, defaultMessage); }

    /**
     * Activates, if allowed, the effect of the passed tool card by initiating the relative state succession. The
     * passed toolCard is set as activeToolCard in the Controller.
     * @param toolcard the toolcard to be activated.
     */
    public CVMessage useToolCard(ToolCard toolcard){ return new CVMessage(ERROR_MESSAGE, defaultMessage); }

    /**
     * Removes the specified dice from the specified track slot and sets it as trackChosenDice on the current turn.
     * @param dice the chosen dice.
     * @param slotNumber the number of the TrackSlot where the dice is.
     */
    public CVMessage chooseDiceFromTrack(Dice dice, int slotNumber){ return new CVMessage(ERROR_MESSAGE, defaultMessage); }

    /**
     * Moves, if legal, the dice found at the cell designated by (rowFrom, colFrom) to the cell designated by (rowTo,
     * colTo).
     * @param rowFrom the row index of the cell FROM which the dice is to be moved.
     * @param colFrom the column index of the cell FROM which the dice is to be moved.
     * @param rowTo the row index of the cell TO which the dice is to be moved.
     * @param colTo the column index of the cell TO which the dice is to be moved.
     */
    public CVMessage moveDice(int rowFrom, int colFrom, int rowTo, int colTo){ return new CVMessage(ERROR_MESSAGE, defaultMessage); }


    /**
     * Increments the value of the drafted dice found in the current turn.
     *
     */
    public CVMessage incrementDice(){ return new CVMessage(ERROR_MESSAGE, defaultMessage); }


    /**
     * Decrements the value of the drafted dice found in the current turn.
     *
     */
    public CVMessage decrementDice(){ return new CVMessage(ERROR_MESSAGE, defaultMessage); }

    /**
     * Sets the value of the drafted dice found in the current turn to the specified value.
     * @param value the chosen value for the drafted dice.
     *
     */
    public CVMessage chooseDiceValue(int value){ return new CVMessage(ERROR_MESSAGE, defaultMessage); }

    /**
     * Executes some implicit behaviour relative to the state. It does nothing when state is not Implicit.
     */
    public void executeImplicitBehaviour(){
        //do nothing by default
    }

}
