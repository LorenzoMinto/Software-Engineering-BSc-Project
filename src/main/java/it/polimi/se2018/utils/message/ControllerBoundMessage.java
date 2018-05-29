package it.polimi.se2018.utils.message;

import java.util.Map;

/**
 *
 * @author Federico Haag
 */
public class ControllerBoundMessage extends Message {

    public ControllerBoundMessage(Enum type, Map<String, Object> params, String playerID) {
        super(type, params, playerID);
    }

    public ControllerBoundMessage(Enum type, Map<String, Object> params) {
        super(type, params);
    }

    public ControllerBoundMessage(Enum type) {
        super(type);
    }

    public String getSendingPlayerID() {
        return getPlayerID();
    }
}
