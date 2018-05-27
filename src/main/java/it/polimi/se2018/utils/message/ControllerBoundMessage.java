package it.polimi.se2018.utils.message;

import java.util.Map;

public class ControllerBoundMessage extends Message {

    public ControllerBoundMessage(MessageType type, Map<String, Object> params, String playerID) {
        super(type, params, playerID);
    }

    public ControllerBoundMessage(MessageType type, Map<String, Object> params) {
        super(type, params);
    }

    public ControllerBoundMessage(MessageType type) {
        super(type);
    }

    public String getSendingPlayerID() {
        return getPlayerID();
    }
}
