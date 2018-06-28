package it.polimi.se2018.model;

import it.polimi.se2018.utils.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a round's Turn.
 *
 * @author Federico Haag
 */
public class Turn extends Observable implements Serializable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -8829876577425331362L;

    /**
     * String passed as message of ValueOutOfBoundsException when it is asked to create a turn with a negative turnnumber
     */
    private static final String CREATE_TURN_WITH_NEGATIVE_TURN_NUMBER = "Can't create a turn with negative turnNumber.";

    /**
     * String passed as message of IllegalArgumentException when it is asked to create a turn giving as a parameter
     * a null player
     */
    private static final String NEW_TURN_WITH_NULL_PLAYER = "Can't create a turn giving null player.";

    /**
     * String passed as message of BadBehaviourRuntimeException when it is asked to get a track slot that does not exist
     */
    private static final String REQUESTED_TRACKSLOT_DOES_NOT_EXIST = "The requested trackslot does not exists.";

    /**
     * String passed as message of ValueOutOfBounds when it is asked to set the slot number of the slot
     * where it is drafted the current track drafted dice, but a negative value is passed.
     */
    private static final String NEGATIVE_VALUE_AS_SLOT_NUMBER = "Can't set the slot of chosen dice to a negative value.";

    /**
     * String passed as message of IllegalArgumentException or IllegalStateException when referenced dice is null
     */
    private static final String NULL_DICE = "Can't use or reference a null dice.";

    /**
     * String passed as message of IllegalArgumentException when it is asked to set a null toolCard as the current one
     */
    private static final String NULL_TOOL_CARD = "Can't use or reference a null toolCard.";

    /**
     * String passed as message of IllegalArgumentException when it is asked to check if a given
     * player is the current one, but it is passed as argument a null object.
     */
    private static final String GIVEN_A_NULL_PLAYER = "Can't check if null player is current player.";

    /**
     * Constructor for a new Turn.
     *
     * @param number the sequential turn number
     * @param player se player playing the new turn
     */
    public Turn(int number, Player player) {
        if(number < 0 ){ throw new ValueOutOfBoundsException(CREATE_TURN_WITH_NEGATIVE_TURN_NUMBER);}
        if(player==null){ throw new IllegalArgumentException(NEW_TURN_WITH_NULL_PLAYER); }

        this.number = number;
        this.player = player;
        this.draftedDice = null;
        this.draftedAndPlaced = false;
        this.usedToolCard = null;
        this.slotOfTrackChosenDice = -1;
    }

    /**
     * Sequential number of the turn.
     */
    private int number;

    /**
     * Player playing the turn.
     */
    private Player player;

    /**
     * Boolean value stating if during the turn there was a draftAndplace move (true=happened)
     */
    private boolean draftedAndPlaced;

    /**
     * Reference to the Dice that is being drafted in the turn (if no dice drafted, this is null)
     */
    private Dice draftedDice;

    /**
     * Reference to the Dice that is being drafted from the {@link Game#track} (if no dice drafted, this is null
     */
    private Dice trackChosenDice;

    /**
     * Reference to the sequential number representing the {@link TrackSlot} where the {@link Turn#trackChosenDice} was drafted.
     */
    private int slotOfTrackChosenDice;

    /**
     * Reference to the toolCard used in the turn (if no toolCards used, this is null)
     */
    private ToolCard usedToolCard;

    /**
     * Returns the sequential number of the Turn.
     *
     * @return the sequential number of the Turn
     */
    public int getNumber() {
        return number;
    }

    /**
     * Returns a boolean value representing if during the turn has been drafted a Dice.
     *
     * @return boolean value representing if during the turn has been drafted a Dice.
     */
    public boolean hasDrafted(){
        return ( draftedDice != null );
    }

    /**
     * Returns a boolean value representing if during the turn was made a "draft and place" move.
     *
     * @return boolean value representing if during the turn was made a "draft and place" move
     */
    public boolean hasDraftedAndPlaced(){
        return draftedAndPlaced;
    }

    /**
     * Returns a boolean value representing if during the turn has been used a ToolCard.
     *
     * @return boolean value representing if during the turn has been used a ToolCard
     */
    public boolean hasUsedToolCard(){
        return ( this.usedToolCard != null );
    }

    /**
     * Returns the Dice that was drafted during the turn. If no Dice drafted, returns null.
     *
     * @return the Dice that was drafted during the turn. If no Dice drafted, returns null.
     */
    public Dice getDraftedDice() { return draftedDice; }

    /**
     * Returns the Dice that was drafted from the {@link Game#track} during the turn.
     *
     * @return the Dice that was drafted from the {@link Game#track} during the turn
     */
    public Dice getTrackChosenDice() { return trackChosenDice; }

    /**
     * Returns the sequential number representing the {@link TrackSlot} where the {@link Turn#trackChosenDice} was drafted.
     *
     * @return the sequential number representing the {@link TrackSlot} where the {@link Turn#trackChosenDice} was drafted
     */
    public int getSlotOfTrackChosenDice() {
        if(slotOfTrackChosenDice == -1) {throw new BadBehaviourRuntimeException(REQUESTED_TRACKSLOT_DOES_NOT_EXIST);}
            return slotOfTrackChosenDice;
    }

    /**
     * Returns the player playing in this turn.
     *
     * @return the player playing in this turn
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the slotOfTrackChosenDice to the given value.
     * The callers has the responsibility to check if the value is legal or not
     *
     * @param value value representing the sequential number of {@link TrackSlot}
     */
    public void setSlotOfTrackChosenDice(int value) {
        if(value < 0) {throw new ValueOutOfBoundsException(NEGATIVE_VALUE_AS_SLOT_NUMBER);}
        this.slotOfTrackChosenDice = value;

        Map<String, Object> messageAttributes = new HashMap<>();

        messageAttributes.put("slotOfTrackChosenDice", this.slotOfTrackChosenDice);

        notify(new Message(ViewBoundMessageType.SLOT_OF_TRACK_CHOSEN_DICE, messageAttributes));
    }

    /**
     * Sets the given Dice as the draftedDice.
     *
     * @param dice Dice to be set as the draftedDice
     */
    public void setDraftedDice(Dice dice){
        if(dice == null){throw new IllegalArgumentException(NULL_DICE);}
        this.draftedDice = dice.copy();
        setDraftedAndPlaced();

        notify(new Message(ViewBoundMessageType.DRAFTED_DICE, Message.fastMap("draftedDice",dice)));
    }

    /**
     * Sets the given Dice as the trackChosenDice.
     *
     * @param dice Dice to be set as the trackChosenDice
     */
    public void setTrackChosenDice(Dice dice) {
        if(dice == null){throw new IllegalArgumentException(NULL_DICE);}
        this.trackChosenDice = dice.copy();

        notify(new Message(ViewBoundMessageType.TRACK_CHOSEN_DICE, Message.fastMap("trackChosenDice", dice)));
    }

    /**
     * Sets the draftedAndPlaced to true
     */
    public void setDraftedAndPlaced(){
        if(this.draftedDice==null){ throw new IllegalStateException(NULL_DICE); }
        this.draftedAndPlaced = true;
    }

    /**
     * Sets the given ToolCard as usedToolCard.
     *
     * @param toolCard the ToolCard to be set as usedToolCard.
     */
    public void setUsedToolCard(ToolCard toolCard){
        if(toolCard == null){throw new IllegalArgumentException(NULL_TOOL_CARD);}
        this.usedToolCard = toolCard;
    }

    /**
     * Reset trackChosenDice and slotOfTrackChosenDice variables to default values
     */
    public void resetTrackChosenDice() {
        this.trackChosenDice = null;
        this.slotOfTrackChosenDice = -1;
    }

    /**
     * Reset draftedDice variable to default value
     */
    public void resetDraftedDice() {
        this.draftedDice = null;
        notify(new Message(ViewBoundMessageType.DRAFTED_DICE, Message.fastMap("noDrafted",0)));
    }

    /**
     * Check if the given player is the one playing this turn.
     *
     * @param playerID the player to be checked
     * @return if the given player is the one playing this turn
     */
    public boolean isCurrentPlayer(String playerID){
        if(playerID == null){throw new IllegalArgumentException(GIVEN_A_NULL_PLAYER);}
        return ( this.player.getID().equals(playerID) );
    }
}
