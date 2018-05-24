package it.polimi.se2018.utils.message;

import java.util.Map;

public class MVMessage extends Message {

    public MVMessage(Enum type) {
        super(type);
    }

    public MVMessage(Enum type, Map<String, Object> params) {
        super(type, params);
    }

    public enum types {
        TIPO31,
        TIPO32
    }
}
