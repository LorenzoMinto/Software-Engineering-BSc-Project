package it.polimi.se2018.utils.message;

import java.util.Map;

public class MVMessage extends Message {

    public MVMessage(types type) {
        super(type);
    }

    public MVMessage(types type, Map<String, Object> params) {
        super(type, params);
    }

    /**
     * Enum for all types of MCMessage instances
     */
    public enum types {
        SETUP,                  //TODO: nella view impostare il currentTurnNumber e currentRoundNumber a 1
        NEXT_ROUND,             //TODO: nella view aumentare il currentRoundNumber e reset currentTurnNumber
        NEXT_TURN,              //TODO: nella view aumentare il currentTurnNumber
        USED_TOOLCARD,
        RANKINGS,
        WINDOWPATTERN,
        DRAFTPOOL,
    }
}
