package it.polimi.se2018.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Federico Haag
 */
public class Player implements Serializable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -3361603123980854887L;
    /**
     * String passed as message of IllegalArgumentException when is asked to decrease tokens of a negative quantity
     */
    private static final String CANNOT_DECREASE_TOKENS_OF_A_NEGATIVE_QUANTITY = "Cannot decrease tokens of a negative quantity.";
    /**
     * String passed as message of IllegalArgumentException when is asked to set a null window pattern to player
     */
    private static final String SET_A_NULL_WINDOWPATTERN_TO_PLAYER = "Can't set a null window pattern to player.";
    /**
     * String passed as message of IllegalArgumentException when is asked to create a player giving a null card
     */
    private static final String CREATE_PLAYER_NULL_CARD = "Asked to create a player giving null card.";
    /**
     * Favor Tokens of the player.
     */
    private int favorTokens;

    /**
     * Nickname of the player.
     */
    private final String nickname;

    /**
     * WindowPattern chosen by the player.
     */
    private WindowPattern windowPattern;

    /**
     * Private Objective Card assigned to the player.
     */
    private PrivateObjectiveCard privateObjectiveCard;

    /**
     * Constructor: creates a new Player based on given nickname and private objective card.
     * @param nickname the nickname the user choose before entering the game
     * @param card the private objective card assigned randomly to the player
     */
    public Player(String nickname, PrivateObjectiveCard card) {
        if(card==null){ throw new IllegalArgumentException(CREATE_PLAYER_NULL_CARD); }

        this.nickname = nickname;
        this.windowPattern = null;
        this.favorTokens = 0;
        this.privateObjectiveCard = card;
    }

    /**
     * Returns the number of favor tokens of the player.
     * @return the number of favor tokens of the player
     */
    public int getFavorTokens() {
        return favorTokens;
    }

    /**
     * Returns the player's nickname.
     * @return the player's nickname
     */
    public String getID() {
        return nickname;
    }


    /**
     * Assign to the player the given windowPattern. Can be assigned only one time at all.
     * @param windowPattern the windowPattern to be assigned to this player
     */
    public void setWindowPattern(WindowPattern windowPattern) {
        if(windowPattern==null) throw new IllegalArgumentException(SET_A_NULL_WINDOWPATTERN_TO_PLAYER);

        this.windowPattern = windowPattern;

        this.windowPattern.setOwner(this);
        this.favorTokens = this.windowPattern.getDifficulty();
    }

    /**
     * Returns the private objective card of the player.
     * @return the private objective card of the player
     */
    public PrivateObjectiveCard getPrivateObjectiveCard() {
        return privateObjectiveCard;
    }

    /**
     * Decrease favorTokens of the given quantity. Return false if not enough tokens left.
     * @param quantity how much favorTokens must be decreased
     * @return true if the action succeeded, false if not (not enough tokens left)
     */
    public boolean decreaseTokens(int quantity) {
        if(quantity < 0){ throw new IllegalArgumentException(CANNOT_DECREASE_TOKENS_OF_A_NEGATIVE_QUANTITY);}
        if(favorTokens<quantity){
            return false;
        } else {
            favorTokens -= quantity;
            return true;
        }
    }

    /**
     * Returns the windowPattern of the player.
     * @return the windowPattern of the player
     */
    public WindowPattern getWindowPattern() {
        return windowPattern;
    }

    /**
     * Indicates whether some other Player is "equal to" this one.
     * @param o some other Player
     * @return if the other Player is equal to this
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Player)) {
            return false;
        }

        Player p = (Player) o;

        return this.getID().equals(p.getID());
    }

    /**
     * Returns a hash code value for the Player.
     * @return a hash code value for the Player
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(getID());
    }
}
