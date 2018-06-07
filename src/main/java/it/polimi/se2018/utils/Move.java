package it.polimi.se2018.utils;

/**
 * Enum for each kind of move that can be performed during game play
 */
public enum Move {
    END_TURN ("End turn"),
    DRAFT_DICE_FROM_DRAFTPOOL ("Draft dice from draft pool"),
    PLACE_DICE_ON_WINDOWPATTERN ("Place dice on windowpattern"),
    USE_TOOLCARD ("Use toolcard"),
    INCREMENT_DRAFTED_DICE ("Increment drafted dice"),
    DECREMENT_DRAFTED_DICE ("Decrement drafted dice"),
    CHANGE_DRAFTED_DICE_VALUE ("Change drafted dice value"),
    CHOOSE_DICE_FROM_TRACK ("Choose dice from track"),
    MOVE_DICE ("Move dice"),
    END_EFFECT ("End ToolCard Effect"),
    JOIN_GAME ("Join game"),
    BACK_GAME ("Back to game"),
    LEAVE ("Leave waiting room");

    /**
     * A human readable text representation of the move
     */
    private final String textualRepresentation;

    /**
     * Constructor for this enum
     * @param t the human readable text representation of the move
     */
    Move(String t){
        this.textualRepresentation = t;
    }

    /**
     * Returns a human readable text representation of the move.
     * Used in CLI for printing moves and in GUI for building buttons.
     *
     * @return a human readable text representation of the move.
     */
    public String getTextualREP(){
        return this.textualRepresentation;
    }
}
