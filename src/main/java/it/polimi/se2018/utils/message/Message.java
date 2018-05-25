package it.polimi.se2018.utils.message;

import it.polimi.se2018.model.Player;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Message class used for comunication between Model, Controller, View, Server, Client
 */
public abstract class Message implements Serializable{

    /**
     * Type of message (answering the question: "what is this message aim?")
     */
    private Enum type;

    /**
     * Parameters passed in the message
     */
    private HashMap<String,Object> params;

    /**
     * The player to send the message (null if broadcasting)
     */
    private final Player player;

    /**
     * Full constructor for Message with possibility to send it in broadcast or specifically to a player
     * @param type the type of the message
     * @param params the parameters of the message
     * @param player the player to send the message (null if broadcasting)
     */
    public Message(Enum type, Map<String, Object> params, Player player) {
        this.type = type;
        this.params = (HashMap<String,Object>)params;
        this.player = player;
    }

    /**
     * Full constructor of broadcasting Message
     * @param type the type of the message
     * @param params the parameters of the message
     */
    public Message(Enum type, Map<String, Object> params) {
        this(type,params,null);
    }

    /**
     * Constructor of broadcasting Message without parameters, just type
     * @param type the type of the message
     */
    public Message(Enum type){
        this(type,null,null);
    }

    /**
     * Returns the type of the message.
     *
     * @return the type of the message
     */
    public Enum getType() {
        return type;
    }

    /**
     * Returns the message's param of the given key
     * @param key the key of the requested param
     * @return the message's param of the given key
     */
    public Object getParam(String key){
        return this.params.get(key);
    }

    /**
     * Returns all the message's params
     * @return all the message's params
     */
    public Map<String,Object> getAllParams(){

        return Collections.unmodifiableMap(this.params);
    }

    /**
     * Returns the recipient player if the message is specific (not in broadcast).
     *
     * @return the recipient player if the message is specific (not in broadcast)
     */
     Player getPlayer(){
        return player;
    }
}
