package it.polimi.se2018.utils.message;

import java.util.Map;

public class NetworkMessage extends Message {
    public NetworkMessage(Enum type) {
        super(type);
    }

    public NetworkMessage(Enum type, Map<String, Object> params) {
        super(type, params);
    }

    public enum types {
        CONNECTED,
        REFUSED
    }
}
