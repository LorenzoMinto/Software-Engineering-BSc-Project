package it.polimi.se2018.utils.message;

import it.polimi.se2018.model.Player;

import java.util.HashMap;
import java.util.Map;

public class CVMessage extends ViewBoundMessage {

    public CVMessage(types type, Map<String, Object> params, Player player) {
        super(type, params, player);
    }

    public CVMessage(types type, Map<String, Object> params) {
        this(type, params, null);
    }

    public CVMessage(types type) {
        this(type, null, null);
    }

    public CVMessage(types type, String message) {
        this(type, null, null);
        HashMap<String, Object> params = new HashMap<>();
        params.put("message", message);
        this.setParams(params);
    }

    /**
     * Enum for all types of CVMessage instances
     */
    public enum types {
        ERROR_MESSAGE,
        ACKNOWLEDGMENT_MESSAGE
    }
}
