package it.polimi.se2018.utils.message;

import it.polimi.se2018.model.Player;

import java.util.Map;

public class ControllerBoundMessage extends Message {


    public ControllerBoundMessage(Enum type, Map<String, Object> params, Player player) {
        super(type, params, player);
    }

    public ControllerBoundMessage(Enum type, Map<String, Object> params) {
        super(type, params);
    }

    public ControllerBoundMessage(Enum type) {
        super(type);
    }

    public Player getSendingPlayer() {
        return getPlayer();
    }
}
