package it.polimi.se2018.utils.message;

import it.polimi.se2018.utils.MovePermission;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author Federico Haag
 */
public class VCMessage extends ControllerBoundMessage {

    private static final long serialVersionUID = 3510649137929416077L;

    public VCMessage(types type, Map<String, Object> params, String playerID, Set<MovePermission> permissions) {
        super(type, params, playerID, permissions);
    }

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
    public enum types {
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