package it.polimi.se2018.utils;

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
    JOIN_GAME ("Join game"),
    BACK_GAME ("Back to game"),
    LEAVE ("Leave waiting room");

    private final String textualRepresentation;

    Move(String t){
        this.textualRepresentation = t;
    }

    public String getTextualREP(){
        return this.textualRepresentation;
    }
}
