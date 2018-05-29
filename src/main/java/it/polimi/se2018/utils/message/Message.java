package it.polimi.se2018.utils.message;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Message class used for comunication between Model, Controller, View, Server, Client
 *
 * @author Federico Haag
 */
public abstract class Message implements Serializable{

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 4259191690541234881L;

    /**
     * Type of message (answering the question: "what is this message aim?")
     */
    private Enum type;

    /**
     * Parameters passed in the message
     */
    private HashMap<String,Object> params;

    /**
     * The player to send the message (null if broadcasting) or the player sending the message
     * Depending on type of message (ControllerBound or ViewBound)
     */
    private final String playerID;

    /**
     * Full constructor for Message with possibility to send it in broadcast or specifically to a player
     * @param type the type of the message
     * @param params the parameters of the message
     * @param playerID the player to send the message (null if broadcasting)
     */
    public Message(Enum type, Map<String, Object> params, String playerID) {
        this.type = type;
        this.params = (HashMap<String,Object>)params;
        this.playerID = playerID;
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
    public Enum getType(){
        return this.type;
    }

    /**
     * Returns the message's param of the given key
     * @param key the key of the requested param
     * @return the message's param of the given key
     */
    public Object getParam(String key){
        return this.params.get(key);
    }

    void setParams(HashMap<String, Object> params) {
        this.params = params;
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
     String getPlayerID(){
        return playerID;
    }

    static HashMap<String, Object> createHashMapWithMessage(String message){
        HashMap<String, Object> params = new HashMap<>();
        params.put("message", message);
        return params;
    }
}
