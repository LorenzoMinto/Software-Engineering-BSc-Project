package it.polimi.se2018.model;

import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.ValueOutOfBoundsException;

/**
 * Class representing a round's Turn.
 *
 * @author Federico Haag
 */
public class Turn {

    /**
     * Constructor for a new Turn.
     *
     * @param number the sequential turn number
     * @param player se player playing the new turn
     */
    public Turn(int number, Player player) {
        if(number < 0 ){ throw new ValueOutOfBoundsException("Can't create a turn with negative turnNumber");}
        if(player==null){ throw new IllegalArgumentException("Can't create a turn giving null player"); }

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
     * Boolean value stating if during the turn there was a draft&place move (true=happened)
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
     * Reference to the toolcard used in the turn (if no toolcards used, this is null)
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
        if(slotOfTrackChosenDice == -1) {throw new BadBehaviourRuntimeException("No slot set");}
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
     * The callers has the responsability to check if the value is legal or not
     *
     * @param value value representing the sequential number of {@link TrackSlot}
     */
    public void setSlotOfTrackChosenDice(int value) {
        if(value < 0) {throw new ValueOutOfBoundsException("ERROR: Can't set the slot of chosen dice to a negative value.");}
        this.slotOfTrackChosenDice = value;
    }

    /**
     * Sets the given Dice as the draftedDice.
     *
     * @param dice Dice to be set as the draftedDice
     */
    public void setDraftedDice(Dice dice){
        if(dice == null){throw new IllegalArgumentException("Can't set the drafted dice with a null dice.");}
        this.draftedDice = dice;
    }

    /**
     * Sets the given Dice as the trackChosenDice.
     *
     * @param dice Dice to be set as the trackChosenDice
     */
    public void setTrackChosenDice(Dice dice) {
        if(dice == null){throw new IllegalArgumentException("Can't set the track chosen dice with a null dice.");}
        this.trackChosenDice = dice;
    }

    /**
     * Sets the draftedAndPlaced to true
     */
    public void setDraftedAndPlaced(){
        if(this.draftedDice==null){ throw new IllegalStateException("Asked to setDraftedAndPlaced" +
                " but draftedDice is null"); }
        this.draftedAndPlaced = true;
    }

    /**
     * Sets the given ToolCard as usedToolCard.
     *
     * @param toolCard the ToolCard to be set as usedToolCard.
     */
    public void setUsedToolCard(ToolCard toolCard){
        if(toolCard == null){throw new IllegalArgumentException("Can't set the used toolcard with a null toolcard.");}
        this.usedToolCard = toolCard;
    }

    /**
     * Check if the given player is the one playing this turn.
     *
     * @param player the player to be checked
     * @return if the given player is the one playing this turn
     */
    public boolean isCurrentPlayer(Player player){
        if(player == null){throw new IllegalArgumentException("ERROR: Can't check if null player is current player");}
        return ( this.player.equals(player) );
    }
}
