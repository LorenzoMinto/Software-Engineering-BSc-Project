package it.polimi.se2018.utils.message;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;

import java.util.Map;

public abstract class ViewBoundMessage extends Message {

    public ViewBoundMessage(Enum type, Map<String, Object> params, Player player) {
        super(type, params, player);
    }

    public ViewBoundMessage(Enum type, Map<String, Object> params) {
        super(type, params);
    }

    public ViewBoundMessage(Enum type) {
        super(type);
    }

    /**
     * Returns if the message is sent in broadcast.
     *
     * @return if the message is sent in broadcast
     */
    public boolean isBroadcast(){
        return this.getPlayer() == null;
    }

    /**
     * Returns the recipient player if the message is specific (not in broadcast).
     *
     * @return the recipient player if the message is specific (not in broadcast)
     */
    public Player getRecievingPlayer() {
        if(this.isBroadcast()){
            throw new BadBehaviourRuntimeException("Can't ask for a Recipient Player in a Broadcast message");
        }
        return getPlayer();
    }
}
