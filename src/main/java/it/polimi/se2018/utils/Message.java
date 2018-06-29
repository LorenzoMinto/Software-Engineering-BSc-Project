package it.polimi.se2018.utils;

import java.io.Serializable;
import java.util.*;

/**
 * Message class used for comunication between Model, Controller, View, Server, Client
 *
 * @author Federico Haag
 */
public final class Message implements Serializable{

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 4259191690541234881L;

    /**
     * Type of message (answering the question: "what is this message aim?")
     */
    private final Enum type;

    /**
     * Parameters passed in the message
     */
    private HashMap<String,Object> params;

    /**
     * The player to send the message (null if broadcasting) or the player sending the message,
     * depending on type of message (ControllerBound or ViewBound)
     */
    private String playerID;

    /**
     * If the message is the reply to a move, it contains the new permissions set for the player recipient of this message
     */
    private EnumSet<Move> permissions;

    /**
     * Constructor for Message with type, params, playerID and permissions.
     * @see Message#playerID
     * @see it.polimi.se2018.view.View#permissions
     * @param type the type of the message
     * @param params the parameters of the message
     * @param playerID the player to send the message in case of controllerbound messages, the sending player
     *                 in case of viewbound message,
     * @param permissions list of moves that the specified player (or all players if no playerID is specified)
     *                    can perform during game play.
     */
    public Message(Enum type, Map<String, Object> params, String playerID, Set<Move> permissions) {
        this.type = type;
        this.params = (params==null) ? new HashMap<>() : (HashMap<String,Object>) params;
        this.playerID = playerID;
        this.permissions = (EnumSet<Move>) permissions;
    }

    /**
     * Constructor for Message with type, params and playerID.
     * @see Message#playerID
     * @param type the type of the message
     * @param params the parameters of the message
     * @param playerID the player to send the message in case of controllerbound messages, the sending player
     *                 in case of viewbound message,
     */
    public Message(Enum type, Map<String, Object> params, String playerID) {
        this(type,params,playerID,null);
    }

    /**
     * Constructor of a Message with type and params. If controllerbound, it is broadcast.
     * @param type the type of the message
     * @param params the parameters of the message
     */
    public Message(Enum type, Map<String, Object> params) {
        this(type,params,null,null);
    }

    /**
     * Constructor of a Message with just type.  If controllerbound, it is broadcast.
     * @param type the type of the message
     */
    public Message(Enum type){
        this(type,null,null,null);
    }

    /**
     * Constructor of a Message with type and message param.  If controllerbound, it is broadcast.
     * @param type the type of the message
     * @param message the message to be sent
     */
    public Message(Enum type, String message){
        this(type,fastMap("message",message),null,null);
    }

    /**
     * Returns the type of the message.
     * @return the type of the message
     */
    public Enum getType(){
        return this.type;
    }

    /**
     * Returns the permissions sent within the message.
     * @return the permissions sent within the message
     */
    public Set<Move> getPermissions() {
        if (this.permissions == null) {
            return EnumSet.noneOf(Move.class);
        }
        return EnumSet.copyOf(this.permissions);
    }

    /**
     * Returns the message's param of the given key.
     * @param key the key of the requested param
     * @return the message's param of the given key
     * @throws NoSuchParamInMessageException if the requested param is not in the params map
     */
    public Object getParam(String key) throws NoSuchParamInMessageException {
        if(!this.params.containsKey(key)){
            throw new NoSuchParamInMessageException();
        }
        return this.params.get(key);
    }

    /**
     * Returns all params of the message
     * @return all params of the message
     */
    public Map<String, Object> getParams() {
        return params;
    }

    /**
     * Returns the recipient player if the message is specific (not in broadcast).
     *
     * @return the recipient player if the message is specific (not in broadcast)
     */
    public String getPlayerID(){
        return playerID;
    }

    /**
     * Sets the playerID of the message. Ensure that playerID is immutable.
     * @param playerID the player ID of the sender (controller bound) or receiver (view bound) player
     */
    public void setPlayerID(String playerID){
        if(this.playerID!=null){ return; }

        this.playerID = playerID;
    }

    /**
     * Set permissions to the given set of allowed moves.
     * @param permissions set of allowed moves
     */
    public void setPermissions(Set<Move> permissions){
        if(this.permissions!=null){ return; }

        if(permissions==null){
            this.permissions = EnumSet.noneOf(Move.class);
        } else {
            this.permissions = (EnumSet<Move>)permissions;
        }
    }

    /**
     * Sets the parameters of the message. Ensure that params is immutable.
     * @param params hashmap containing parameters of the message
     */
    void setParams(Map<String, Object> params) {
        if(!this.params.isEmpty()){ return; }

        this.params = new HashMap<>(params);
    }

    /**
     * Method used to build fast a map containing just an element (very common).
     * @param key the key to be added
     * @param value the value to be added
     * @return a map containing just the key-value specified
     */
    public static Map<String, Object> fastMap(String key, Object value){
        HashMap<String, Object> params = new HashMap<>();
        params.put(key, value);
        return params;
    }

    /**
     * Returns if this message is a "move" message or not
     * @return if this message is a "move" message or not
     */
    public boolean isMove(){
        return this.type==ControllerBoundMessageType.MOVE;
    }

    /**
     * Returns if this message is a "move" message of the given move or not
     * @param move the move that is compared to the built in message param "move"
     * @return if this message is a "move" message or the given move or not
     */
    public boolean isMove(Move move){
        if(!isMove()){
            return false;
        }

        Object o;
        try {
            o = getParam("move");
        } catch (NoSuchParamInMessageException e) {
            return false;
        }
        Move mMove = (Move) o;
        return move.equals(mMove);
    }

    /**
     * Returns the string representation of the message.
     * @return the string representation of the message
     */
    @Override
    public String toString() {
        return type.toString().concat(" ").concat((params==null)?"_":params.toString());
    }
}