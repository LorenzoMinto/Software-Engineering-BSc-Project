package it.polimi.se2018.controller;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.Message;

import java.util.EnumSet;
import java.util.Set;

import static it.polimi.se2018.utils.ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.ViewBoundMessageType.ERROR_MESSAGE;

/**
 * Abstract class that represents a generic state of the {@link Controller}.
 *
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public abstract class ControllerState {

    /**
     * String passed as message of IllegalArgumentException when it is asked to create the state but controller is null.
     */
    private static final String NO_CONTROLLER = "Can't create a Controller State without a Controller.";

    /**
     * String to be printed if a non pertaining method is called in the middle of a ToolCard effect.
     */
    static final String MIDDLE_OF_EFFECT = "Can't do that. You're currently in the middle of a toolCard effect.";

    /**
     * String to be printed if a method that requires drafting is called before any dice was drafted.
     */
    static final String NO_DICE_DRAFTED = "Can't do that. You haven't drafted a dice yet.";

    /**
     * String to be printed if only action possible is to draft and place.
     */
    static final String ONLY_DRAFT_AND_PLACE =  "Can't do that. You can only draft and place. A ToolCard was already used.";

    /**
     * String to be printed if there's a drafted dice that needs to be placed.
     */
    static final String PLACE_DICE = "Can't do that. You have to place the drafted dice first.";

    /**
     * String to be printed if the only action possible is a ToolCard activation.
     */
    static final String TOOLCARD_ONLY = "Can't do that. You can only use a toolCard that doesn't require drafting.";

    /**
     * String to be printed if the only action possible is to end the Turn.
     */
    static final String END_TURN_ONLY = "Can't do that. You are out of moves.";

    /**
     * Default message
     */
    String defaultMessage = "Move not legal.";

    /**
     * Reference to the {@link Controller} to which this state belongs.
     */
    protected Controller controller;

    public ControllerState(Controller controller) {
        if (controller==null) { throw new IllegalArgumentException(NO_CONTROLLER);}
        this.controller = controller;
    }

    /**
     * Drafts a specified dice from the current draft pool and sets it as the drafted dice on the current turn.
     * @param dice the dice to be drafted.
     * @return a message containing the result of the mov
     */
    public Message draftDiceFromDraftPool(Dice dice){ return new Message(ERROR_MESSAGE, defaultMessage); }

    /**
     * Places the drafted dice of the current turn on the specified cell (row and column).
     * @param row the row index of the cell where the dice is to be placed.
     * @param col the column index of the cell where the dice is to be placed.
     * @return a message containing the result of the mov
     */
    public Message placeDice(int row, int col){ return new Message(ERROR_MESSAGE, defaultMessage); }

    /**
     * Activates, if allowed, the effect of the passed tool card by initiating the relative state succession. The
     * passed toolCard is set as activeToolCard in the Controller.
     * @param toolCard the toolCard to be activated.
     * @return a message containing the result of the mov
     */
    public Message useToolCard(ToolCard toolCard){ return new Message(ERROR_MESSAGE, defaultMessage); }

    /**
     * Removes the specified dice from the specified track slot and sets it as trackChosenDice on the current turn.
     * @param dice the chosen dice.
     * @param slotNumber the number of the TrackSlot where the dice is.
     * @return a message containing the result of the mov
     */
    public Message chooseDiceFromTrack(Dice dice, int slotNumber){ return new Message(ERROR_MESSAGE, defaultMessage); }

    /**
     * Moves, if legal, the dice found at the cell designated by (rowFrom, colFrom) to the cell designated by (rowTo,
     * colTo).
     * @param rowFrom the row index of the cell FROM which the dice is to be moved.
     * @param colFrom the column index of the cell FROM which the dice is to be moved.
     * @param rowTo the row index of the cell TO which the dice is to be moved.
     * @param colTo the column index of the cell TO which the dice is to be moved.
     * @return a message containing the result of the mov
     */
    public Message moveDice(int rowFrom, int colFrom, int rowTo, int colTo){ return new Message(ERROR_MESSAGE, defaultMessage); }


    /**
     * Increments the value of the drafted dice found in the current turn.
     *
     * @return a message containing the result of the mov     *
     */
    public Message incrementDice(){ return new Message(ERROR_MESSAGE, defaultMessage); }


    /**
     * Decrements the value of the drafted dice found in the current turn.
     *
     * @return a message containing the result of the mov
     */
    public Message decrementDice(){ return new Message(ERROR_MESSAGE, defaultMessage); }

    /**
     * Sets the value of the drafted dice found in the current turn to the specified value.
     * @param value the chosen value for the drafted dice.
     * @return a message containing the result of the mov
     */
    public Message chooseDiceValue(int value){ return new Message(ERROR_MESSAGE, defaultMessage); }

    /**
     * Returns the currently drafted dice to the draftpool.
     * @return a message containing the result of the mov
     */
    public Message returnDiceToDraftPool() { return new Message(ERROR_MESSAGE, defaultMessage); }

    /**
     * Ends the current turn.
     * @return a message containing the result of the mov
     */
    public Message endCurrentTurn() {
        controller.advanceGame();
        return new Message(ACKNOWLEDGMENT_MESSAGE, "Turn ended.");
    }

    /**
     * Ends the current toolCard effect.
     * @return a message containing the result of the mov
     */
    public Message endToolCardEffect() { return new Message(ERROR_MESSAGE, defaultMessage); }

    /**
     * Gets the permission for the state
     * @return the permissions for the state
     */
    public Set<Move> getStatePermissions() {
        //do nothing by default
        return EnumSet.of(Move.END_TURN);
    }

    /**
     * Executes some implicit behaviour relative to the state. It does nothing when state is not Implicit.
     */
    public void executeImplicitBehaviour(){
        //do nothing
    }
}
