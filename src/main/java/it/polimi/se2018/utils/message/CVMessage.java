package it.polimi.se2018.utils.message;

import it.polimi.se2018.model.Player;

import java.util.Map;

public class CVMessage extends Message {

    public CVMessage(Enum type, Map<String, Object> params, Player player) {
        super(type, params, player);
    }

    public CVMessage(Enum type, Map<String, Object> params) {
        super(type, params);
    }

    public CVMessage(Enum type) {
        super(type);
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
