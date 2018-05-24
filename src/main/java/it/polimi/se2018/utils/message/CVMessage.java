package it.polimi.se2018.utils.message;

import java.util.Map;

public class CVMessage extends Message {

    public CVMessage(types type) {
        super(type);
    }

    public CVMessage(types type, Map<String, Object> params) {
        super(type, params);
    }

    /**
     * Enum for all types of CVMessage instances
     */
    public enum types {
        MIDDLE_OF_EFFECT,
        NO_DICE_DRAFTED,
        ONLY_DRAFT_AND_PLACE,
        PLACE_DICE,
        TOOLCARD_ONLY,
        //TODO: complete
    }
}
