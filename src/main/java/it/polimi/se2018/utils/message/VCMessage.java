package it.polimi.se2018.utils.message;

import java.util.Map;

public class VCMessage extends Message {

    public VCMessage(types type) {
        super(type);
    }

    public VCMessage(types type, Map<String, Object> params) {
        super(type, params);
    }

    /**
     * Enum for all types of VCMessage instances
     */
    public enum types {
        //TODO: complete
    }
}
