package it.polimi.se2018.utils.message;

import java.util.Map;

public class VCMessage extends Message {

    public VCMessage(Enum type) {
        super(type);
    }

    public VCMessage(Enum type, Map<String, Object> params) {
        super(type, params);
    }

    public enum types {
        TIPO51,
        TIPO52
    }
}
