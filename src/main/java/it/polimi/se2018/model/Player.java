package it.polimi.se2018.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Federico Haag
 */
public class Player implements Serializable {

    /**
     * Favor Tokens of the player.
     */
    private int favorTokens;

    /**
     * Nickname of the player.
     */
    private String nickname;

    /**
     * WindowPattern chosen by the player.
     */
    private WindowPattern windowPattern;

    /**
     * A player is just the relationship between a user and the game
     * the user is playing. This is the user.
     */
    private User user;  //TODO: verificare se ha ancora senso la classe user o se va rimossa

    /**
     * Private Objective Card assigned to the player.
     */
    private PrivateObjectiveCard privateObjectiveCard;

    /**
     * Constructor: creates a new Player based on given user, nickname and private objective card.
     *
     * @param user the user playing in this game
     * @param nickname the nickname the user choose before entering the game
     * @param card the private objective card assigned randomly to the player
     */
    public Player(User user, String nickname, PrivateObjectiveCard card) {

        //Checks for bad params
        if(user==null){ throw new IllegalArgumentException("Asked to create a player giving null user"); }
        if(card==null){ throw new IllegalArgumentException("Asked to create a player giving null card"); }

        this.user = user;
        this.nickname = nickname;
        this.windowPattern = null;
        this.favorTokens = 0;
        this.privateObjectiveCard = card;
    }

    /**
     * Returns the number of favor tokens of the player.
     *
     * @return the number of favor tokens of the player
     */
    public int getFavorTokens() {
        return favorTokens;
    }

    /**
     * Returns the player's nickname.
     *
     * @return the player's nickname
     */
    public String getNickname() {
        return nickname;
    }


    /**
     * Assign to the player the given windowpattern. Can be assigned only one time at all.
     *
     * @param windowPattern the windowpattern to be assigned to this player
     */
    void setWindowPattern(WindowPattern windowPattern) {
        if(windowPattern==null) throw new IllegalArgumentException();

        this.windowPattern = windowPattern;
        this.favorTokens = windowPattern.getDifficulty();
    }

    /**
     * Returns the private objective card of the player.
     *
     * @return the private objective card of the player
     */
    public PrivateObjectiveCard getPrivateObjectiveCard() {
        return privateObjectiveCard;
    }


    /**
     * Decrease favorTokens of the given quantity. Return false if not enough tokens left.
     *
     * @param quantity how much favorTokens must be decreased
     * @return true if the action succeeded, false if not (not enough tokens left)
     */
    public boolean decreaseTokens(int quantity) {
        if(quantity < 0){ throw new IllegalArgumentException("ERROR: Cannot decrease tokens of a negative quantity.");}
        if(favorTokens<quantity) return false;
        favorTokens -= quantity;
        return true;
    }

    /**
     * Returns the windowpattern of the player.
     *
     * @return the windowpattern of the player
     */
    public WindowPattern getWindowPattern() {
        return windowPattern;
    }


    /**
     * Checks if the given toolcard can be used comparing favorTokens with {@link ToolCard#getNeededTokens()} .
     *
     * @param toolCard the toolcard to check if it's usable or not
     * @return true if the given toolcard can be used, false if not
     */
    public boolean canUseToolCard(ToolCard toolCard) {
        return toolCard.getNeededTokens() <= favorTokens;
    }

    /**
     * Returns the user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Indicates whether some other Player is "equal to" this one.
     *
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

        return this.nickname.equals(p.getNickname());
    }

    /**
     * Returns a hash code value for the Player.
     *
     * @return a hash code value for the Player
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(favorTokens,nickname,windowPattern,user,privateObjectiveCard);
    }
}
