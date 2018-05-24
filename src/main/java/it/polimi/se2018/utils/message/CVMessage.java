package it.polimi.se2018.utils.message;

import java.util.HashMap;
import java.util.Map;

public class CVMessage extends Message {

    public CVMessage(Enum type) {
        super(type);
    }

    public CVMessage(Enum type, Map<String, Object> params) {
        super(type, params);
    }

    public enum types {
        MIDDLE_OF_EFFECT,
        NO_DICE_DRAFTED,
        ONLY_DRAFT_AND_PLACE,
        PLACE_DICE,
        TOOLCARD_ONLY
    }
}
