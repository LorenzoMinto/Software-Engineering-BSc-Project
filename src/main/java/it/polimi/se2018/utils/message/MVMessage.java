package it.polimi.se2018.utils.message;

import java.util.Map;

/**
 *
 * @author Federico Haag
 */
public class MVMessage extends ViewBoundMessage {

    public MVMessage(types type, Map<String, Object> params, String playerID) {
        super(type, params, playerID);
    }

    public MVMessage(types type, Map<String, Object> params) {
        super(type, params);
    }

    public MVMessage(types type) {
        super(type);
    }

    /**
     * Enum for all types of MCMessage instances
     */
    public enum types {
        SETUP,                  //NOTE: nella view impostare il currentTurnNumber e currentRoundNumber a 1
        NEXT_ROUND,             //NOTE: nella view aumentare il currentRoundNumber e reset currentTurnNumber
        NEXT_TURN,              //NOTE: nella view aumentare il currentTurnNumber
        USED_TOOLCARD,
        RANKINGS,
        WINDOWPATTERN,
        DRAFTPOOL,
    }
}
