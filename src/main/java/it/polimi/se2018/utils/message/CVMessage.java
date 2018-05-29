package it.polimi.se2018.utils.message;

import java.util.Map;

/**
 *
 * @author Federico Haag
 */
public class CVMessage extends ViewBoundMessage {

    public CVMessage(types type, Map<String, Object> params, String playerID) {
        super(type, params, playerID);
    }

    public CVMessage(types type, Map<String, Object> params) {
        super(type, params);
    }

    public CVMessage(types type) {
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
