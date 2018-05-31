package it.polimi.se2018.utils.message;

import it.polimi.se2018.utils.Move;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author Federico Haag
 */
public class MVMessage extends ViewBoundMessage {

    private static final long serialVersionUID = -519162435752587549L;

    public MVMessage(types type, Map<String, Object> params, String playerID, Set<Move> permissions) {
        super(type, params, playerID, permissions);
    }

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
        NEW_TURN,              //NOTE: nella view aumentare il currentTurnNumber
        USED_TOOLCARD,
        RANKINGS,
        WINDOWPATTERN,
        DRAFTPOOL,
        YOUR_TURN
    }
}
