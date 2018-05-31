package it.polimi.se2018.utils.message;

import it.polimi.se2018.utils.Move;

import java.io.Serializable;
import java.util.*;

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
     * If the message is the reply to a move, it contains the new permissions set for the player recipient of this message
     */
    private final EnumSet<Move> permissions;

    public Message(Enum type, Map<String, Object> params, String playerID, Set<Move> permissions) {
        this.type = type;
        this.params = (HashMap<String,Object>)params;
        this.playerID = playerID;
        this.permissions = (EnumSet<Move>)permissions;
    }

    /**
     * Full constructor for Message with possibility to send it in broadcast or specifically to a player
     * @param type the type of the message
     * @param params the parameters of the message
     * @param playerID the player to send the message (null if broadcasting)
     */
    public Message(Enum type, Map<String, Object> params, String playerID) {
        this(type,params,playerID,null);
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
        this(type,null);
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
     * Returns the permissions sent within the message.
     *
     * @return the permissions sent within the message
     */
    public Set<Move> getPermissions() {
        return permissions.clone();
    }

    /**
     * Returns the message's param of the given key
     * @param key the key of the requested param
     * @return the message's param of the given key
     */
    public Object getParam(String key) throws NoSuchAParamInMessageException {
        if(!params.containsKey(key)){
            throw new NoSuchAParamInMessageException();
        }
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

    public static Map<String, Object> fastMap(String key, Object value){
        HashMap<String, Object> params = new HashMap<>();
        params.put(key, value);
        return params;
    }

    @Override
    public String toString() {
        return type.toString().concat(" ").concat((params==null)?"_":params.toString());
    }
}
