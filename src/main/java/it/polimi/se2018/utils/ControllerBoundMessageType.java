package it.polimi.se2018.utils;

/**
 * Enum of all types of controller bound messages
 */
public enum ControllerBoundMessageType {
    CHOOSE_DICE_VALUE,
    CHOOSE_DICE_FROM_TRACK,
    DRAFT_DICE_FROM_DRAFTPOOL,
    RETURN_DICE_TO_DRAFTPOOL,
    INCREMENT_DICE,
    DECREMENT_DICE,
    MOVE_DICE,
    PLACE_DICE,
    USE_TOOLCARD,
    CHOSEN_WINDOW_PATTERN,
    BACK_GAMING,
    END_TURN,
    END_TOOLCARD_EFFECT,
    JOIN_WR,
    LEAVE_WR
}
