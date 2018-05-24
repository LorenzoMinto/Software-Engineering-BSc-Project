package it.polimi.se2018.utils.message;

import java.io.Serializable;
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
        this.type = type;
        this.params = (HashMap<String,Object>)params;
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
}
