package it.polimi.se2018.utils.message;

import it.polimi.se2018.utils.MovePermission;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author Federico Haag
 */
public abstract class ControllerBoundMessage extends Message {

    public ControllerBoundMessage(Enum type, Map<String, Object> params, String playerID, Set<MovePermission> permissions) {
        super(type, params, playerID, permissions);
    }

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
