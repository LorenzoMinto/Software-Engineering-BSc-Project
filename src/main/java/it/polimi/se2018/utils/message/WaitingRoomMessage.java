package it.polimi.se2018.utils.message;

import java.util.Map;

public class WaitingRoomMessage extends Message {

    public WaitingRoomMessage(MessageType type, Map<String, Object> params, String playerID) {
        super(type, params, playerID);
    }

    public WaitingRoomMessage(MessageType type, Map<String, Object> params) {
        super(type, params);
    }

    public WaitingRoomMessage(MessageType type) {
        super(type);
    }

    @Override
    public types getType() {
        return (types) super.getType();
    }

    /**
     * Enum for all types of WaitingRoomMessage instances
     */
    public enum types implements MessageType {
        BAD_FORMATTED,
        DENIED,
        JOIN,
        ADDED,
        LEAVE,
        REMOVED,

    }
}
