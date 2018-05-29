package it.polimi.se2018.utils.message;

import it.polimi.se2018.utils.MovePermission;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author Federico Haag
 */
public class CVMessage extends ViewBoundMessage {

    public CVMessage(Enum type, Map<String, Object> params, String playerID, Set<MovePermission> permissions) {
        super(type, params, playerID, permissions);
    }

    public CVMessage(Enum type, Map<String, Object> params, String playerID) {
        super(type, params, playerID);
    }

    public CVMessage(Enum type, Map<String, Object> params) {
        super(type, params);
    }

    public CVMessage(Enum type) {
        super(type);
    }

    public CVMessage(types type, String message) {
        super(type, createHashMapWithMessage(message), null);
    }

    /**
     * Enum for all types of CVMessage instances
     */
    public enum types {
        ERROR_MESSAGE,
        ACKNOWLEDGMENT_MESSAGE,
        INACTIVE_PLAYER,
        BACK_TO_GAME, //NOTE: if view receive this message, must assume that permissions are "waiting for your turn"
        INACTIVE, //NOTE: if view receive this message, must assume that permissions are "come back to game!"
        GIVE_WINDOW_PATTERNS
    }
}
