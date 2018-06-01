package it.polimi.se2018.utils.message;

import java.util.Map;

/**
 *
 * @author Federico Haag
 */
public class WaitingRoomMessage extends Message {

    private static final long serialVersionUID = -2346949723822160140L;

    public WaitingRoomMessage(Enum type, Map<String, Object> params, String playerID) {
        super(type, params, playerID);
    }

    public WaitingRoomMessage(types type, Map<String, Object> params) {
        super(type, params);
    }

    public WaitingRoomMessage(types type) {
        super(type);
    }

    public WaitingRoomMessage(types type, String message) {
        super(type, fastMap("message",message));
    }

    @Override
    public types getType() {
        return (types) super.getType();
    }

    /**
     * Enum for all types of WaitingRoomMessage instances
     */
    public enum types {
        BAD_FORMATTED,
        DENIED_PLAYING,
        DENIED_NICKNAME,
        DENIED_LIMIT,
        JOIN,
        ADDED,
        LEAVE,
        REMOVED,

    }
}
