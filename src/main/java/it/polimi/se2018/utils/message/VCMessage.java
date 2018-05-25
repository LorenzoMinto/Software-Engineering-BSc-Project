package it.polimi.se2018.utils.message;

import it.polimi.se2018.model.Player;

import java.util.Map;

public class VCMessage extends Message {

    public VCMessage(Enum type, Map<String, Object> params, Player player) {
        super(type, params, player);
    }

    public VCMessage(Enum type, Map<String, Object> params) {
        super(type, params);
    }

    public VCMessage(Enum type) {
        super(type);
    }

    /**
     * Enum for all types of VCMessage instances
     */
    public enum types {
        //TODO: complete
    }
}
