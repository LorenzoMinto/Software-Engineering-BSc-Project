package it.polimi.se2018.utils.message;

import it.polimi.se2018.model.Player;

import java.util.Map;

public class VCMessage extends ControllerBoundMessage {

    public VCMessage(types type, Map<String, Object> params, Player player) {
        super(type, params, player);
    }

    public VCMessage(types type, Map<String, Object> params) {
        super(type, params);
    }

    public VCMessage(types type) {
        super(type);
    }

    /**
     * Enum for all types of VCMessage instances
     */
    public enum types {
        CHOOSE_DICE_VALUE,
        CHOOSE_DICE_FROM_TRACK,
        DRAFT_DICE_FROM_DRAFTPOOL,
        INCREMENT_DICE,
        DECREMENT_DICE,
        MOVE_DICE,
        PLACE_DICE,
        USE_TOOLCARD,
        CHOOSE_WINDOW_PATTERN
    }
}