package it.polimi.se2018.utils.message;

import it.polimi.se2018.model.Player;

import java.util.Map;

public class NetworkMessage extends Message {

    public NetworkMessage(Enum type, Map<String, Object> params, Player player) {
        super(type, params, player);
    }

    public NetworkMessage(Enum type, Map<String, Object> params) {
        super(type, params);
    }

    public NetworkMessage(Enum type) {
        super(type);
    }

    /**
     * Enum for all types of NetworkMessage instances
     */
    public enum types {
        CONNECTED,
        REFUSED
    }
}
