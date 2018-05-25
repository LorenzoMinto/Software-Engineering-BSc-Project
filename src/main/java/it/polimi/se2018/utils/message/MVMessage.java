package it.polimi.se2018.utils.message;

import it.polimi.se2018.model.Player;

import java.util.Map;

public class MVMessage extends ViewBoundMessage {

    public MVMessage(Enum type, Map<String, Object> params, Player player) {
        super(type, params, player);
    }

    public MVMessage(Enum type, Map<String, Object> params) {
        super(type, params);
    }

    public MVMessage(Enum type) {
        super(type);
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
