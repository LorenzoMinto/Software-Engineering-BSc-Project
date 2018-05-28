package it.polimi.se2018.utils.message;

import java.util.Map;

/**
 *
 * @author Federico Haag
 */
public class VCMessage extends ControllerBoundMessage {

    public VCMessage(types type, Map<String, Object> params, String playerID) {
        super(type, params, playerID);
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
    public enum types implements MessageType {
        CHOOSE_DICE_VALUE,
        CHOOSE_DICE_FROM_TRACK,
        DRAFT_DICE_FROM_DRAFTPOOL,
        INCREMENT_DICE,
        DECREMENT_DICE,
        MOVE_DICE,
        PLACE_DICE,
        USE_TOOLCARD,
        CHOOSE_WINDOW_PATTERN,
        BACK_GAMING
    }
}