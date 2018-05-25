package it.polimi.se2018.utils.message;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Message class used for comunication between Model, Controller, View, Server, Client
 */
public abstract class Message implements Serializable{

    private static final String RECIPIENT_PLAYER_KEY = "RECIPIENT_PLAYER_KEY";

    /**
     * Type of message (answering the question: "what is this message aim?")
     */
    private Enum type;

    /**
     * Parameters passed in the message
     */
    private HashMap<String,Object> params;

    /**
     * Constructor of Message without parameters, just type
     * @param type the type of the message
     */
    public Message(Enum type){
        this(type,null);
    }

    /**
     * Full constructor of Message
     * @param type the type of the message
     * @param params the parameters of the message
     */
    public Message(Enum type, Map<String, Object> params) {
        if(params.containsKey(RECIPIENT_PLAYER_KEY)){ throw new IllegalArgumentException("Please don't insert in params of message the RECIPIENT_PLAYER_KEY:"+RECIPIENT_PLAYER_KEY); }
        this.type = type;
        this.params = (HashMap<String,Object>)params;
    }

    public Message(Enum type, Map<String, Object> params, Player player) {
        this(type,params);
        this.params.put("player",player);
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

    public boolean isBroadcast(){
        return params.get(RECIPIENT_PLAYER_KEY) == null;
    }

    public Player getRecipientPlayer(){
        if(isBroadcast()){
            throw new BadBehaviourRuntimeException();
        }
        return (Player)params.get(RECIPIENT_PLAYER_KEY);
    }
}
