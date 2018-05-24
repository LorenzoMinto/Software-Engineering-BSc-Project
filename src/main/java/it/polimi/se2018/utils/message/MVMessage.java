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
        SETUP,                  //TODO: nella view impostare il currentTurn e currentRound a 1
        NEXT_ROUND,             //TODO: nella view aumentare il currentRound e reset currentTurn
        NEXT_TURN, USED_TOOLCARD, RANKINGS, WINDOWPATTERN,              //TODO: nella view aumentare il currentTurn
    }
}
