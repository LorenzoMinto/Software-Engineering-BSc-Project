package it.polimi.se2018.utils.message;

import java.util.HashMap;
import java.util.Map;

public class CVMessage extends ViewBoundMessage {

    public CVMessage(types type, Map<String, Object> params, String playerID) {
        super(type, params, playerID);
    }

    public CVMessage(types type, Map<String, Object> params) {
        super(type, params);
    }

    public CVMessage(types type) {
        super(type);
    }

    public CVMessage(types type, String message) {
        super(type, getHashMapWithMessage(message), null);
    }

    private static HashMap<String, Object> getHashMapWithMessage(String message){
        HashMap<String, Object> params = new HashMap<>();
        params.put("message", message);
        return params;
    }

    /**
     * Enum for all types of CVMessage instances
     */
    public enum types implements MessageType{
        ERROR_MESSAGE,
        ACKNOWLEDGMENT_MESSAGE,
        GIVE_WINDOW_PATTERNS
    }
}
