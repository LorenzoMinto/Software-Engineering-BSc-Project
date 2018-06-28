package it.polimi.se2018.model;

import it.polimi.se2018.controller.ControllerState;
import it.polimi.se2018.controller.DraftControllerState;

import java.io.Serializable;
import java.util.*;


/**
 * Class representing the Game's tool cards. Each card's effect is encoded in a placement rule and
 * an HashMap that describes the effect's own state transitions.
 *
 * @author Lorenzo Minto
 */
public class ToolCard implements Serializable{

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 3828562971059857480L;

    /**
     * Part of the toString representation of the toolCard. Contains content shown before title
     */
    private static final String PRE_TITLE = "[";

    /**
     * Part of the toString representation of the toolCard. Contains content shown after title
     */
    private static final String POST_TITLE = "]";

    /**
     * Part of the toString representation of the toolCard. Contains a divider that separates each block of information
     */
    private static final String DIVIDER = " ";

    /**
     * Part of the toString representation of the toolCard. Contains content shown before needed tokens
     */
    private static final String PRE_NEEDED_TOKENS = "Needed: ";

    /**
     * Part of the toString representation of the toolCard. Contains content shown before used tokens
     */
    private static final String PRE_USED_TOKENS = "Used: ";

    /**
     * Part of the toString representation of the toolCard. Contains content shown after needed tokens
     */
    private static final String POST_NEEDED_TOKENS = "|";

    /**
     * Part of the toString representation of the toolCard. Contains content shown after used tokens
     */
    private static final String POST_USED_TOKENS = "]";

    /**
     * Part of the toString representation of the toolCard. Contains content shown before description
     */
    private static final String PRE_DESCRIPTION = "";

    /**
     * Part of the toString representation of the toolCard. Contains content shown after description
     */
    private static final String POST_DESCRIPTION = "";

    /**
     * The parameter literal for the needed tokens
     */
    private static final String NEEDED_TOKENS = "neededTokens";
    /**
     * The id of the toolCard
     */
    private final String toolCardID;

    /**
     * The tool card's title.
     */
    private String title;

    /**
     * The tool card's description.
     */
    private String description;

    /**
     * The number of tokens spent on the tool card.
     */
    private int tokensUsed;

    /**
     * The number of tokens needed to activate the tool card.
     */
    private int neededTokens;

    /**
     * The number of tokens needed to activate the tool card for the first time.
     */
    private int baseNeededTokens;

    /**
     * The factor that multiplies the neededTokens after the first activation.
     */
    private int tokensUsageMultiplier;

    /**
     * The url of the tool card's image.
     */
    private String imageURL;

    /**
     * The number of possible dice moves
     */
    private Set<Integer> possibleMovesCountSet;

    /**
     * The tool card's state transition table. Represents the tool card's effect (when active).
     */
    private transient HashMap<String,String> controllerStateRules;

    /**
     * The tool card's own placement rule. Enforced on the game only when active.
     */
    private transient PlacementRule placementRule;

    /**
     * Class constructor.
     *
     * @param p contains id, title, description, imageURL, neededtokens, tokensUsageMultiplier
     * @param controllerStateRules the state table that governs state transitions when the tool card is active.
     * @param placementRule the placement rules that need to be enforced when the tool card is active.
     * @param possibleMovesCountSet the number of possible dice moves
     */
    public ToolCard(Properties p, Map<String, String> controllerStateRules, PlacementRule placementRule, Set<Integer> possibleMovesCountSet) {
        //the id of toolCard
        this.toolCardID = p.getProperty("id");
        //the title of the tool card.
        this.title = p.getProperty("title");
        //the description of the tool card.
        this.description = p.getProperty("description");
        //the tokens needed to activate the tool card.
        this.neededTokens = Integer.parseInt( p.getProperty(NEEDED_TOKENS) );
        this.baseNeededTokens = Integer.parseInt( p.getProperty(NEEDED_TOKENS) );
        this.tokensUsed = 0;
        //the factor that multiplies needed tokens after the first activation.
        this.tokensUsageMultiplier = Integer.parseInt( p.getProperty("tokensUsageMultiplier") );
        //the URL of the tool card image.
        this.imageURL = p.getProperty("imageURL");
        this.possibleMovesCountSet = possibleMovesCountSet;

        this.controllerStateRules = (HashMap<String,String>)controllerStateRules;
        this.placementRule = placementRule;
    }

    /**
     * Updates the tokenUsed by adding the amount of tokens needed for the card's activation. Then, only if it is
     * the case, updates the amount of tokens needed for the card's activation.
     */
    public void use() {
        this.tokensUsed += this.neededTokens;

        //Increase neededTokens on the first usage (check of first usage is made thanks baseNeededTokens)
        if (this.tokensUsed==this.baseNeededTokens) { this.neededTokens *= this.tokensUsageMultiplier; }
    }

    private String getToolCardID() { return toolCardID; }

    /**
     * Returns the title of the tool card
     *
     * @return the title of the tool card
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the title of the tool card
     *
     * @return the title of the tool card
     */
    public Set<Integer> getPossibleMovesCountSet() {
        return possibleMovesCountSet;
    }

    /**
     * Returns the description of the tool card's effect
     *
     * @return the description of the tool card's effect
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the number of tokens spent on the tool card
     *
     * @return the number of tokens spent on the tool card
     */
    public int getUsedTokens() { return tokensUsed; }

    /**
     * Returns the number of tokens needed to activate the tool card
     *
     * @return the number of tokens needed to activate the tool card
     */
    public int getNeededTokens() {
        return neededTokens;
    }

    /**
     * Returns the url of the tool card's image
     *
     * @return the url of the tool card's image
     */
    public String getImageURL() {
        return imageURL;
    }


    /**
     * Returns the placement rule relative to the tool card's effect
     *
     * @return the placement rule relative to the tool card's effect
     */
    public PlacementRule getPlacementRule() { return placementRule; }

    /**
     * Returns the ID of the next state given the current state, after having queried the tool card's state
     * transition table.
     *
     * @param currentState the current state.
     * @return the ID of the state that follows the current according to the card's transition table
     */
    public String nextStateID(ControllerState currentState){

        return controllerStateRules.get( currentState.getClass().getSimpleName() );
    }

    /**
     * Returns true if the ToolCard's effect requires drafting, else false
     *
     * @return whether or not the ToolCard's effect requires drafting
     */
    public boolean needsDrafting(){
        return controllerStateRules.containsKey(DraftControllerState.class.getSimpleName());
    }

    /**
     * Returns whether or not some other ToolCard is "equal to" this one. Comparison is based on the ID
     *
     * @param o some other ToolCard
     * @return if the other ToolCard is equal to this
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ToolCard)) {
            return false;
        }

        ToolCard c = (ToolCard) o;

        return this.getToolCardID().equals(c.getToolCardID());
    }

    /**
     * Returns a hash code value for the ToolCard
     *
     * @return a hash code value for the ToolCard
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(toolCardID);
    }

    /**
     * Returns string representation of the ToolCard
     *
     * @return string representation of the ToolCard
     */
    @Override
    public String toString() {
        return PRE_TITLE + getTitle() + POST_TITLE + DIVIDER + PRE_NEEDED_TOKENS +getNeededTokens()+ POST_NEEDED_TOKENS + DIVIDER + PRE_USED_TOKENS + getUsedTokens()+ POST_USED_TOKENS + DIVIDER + PRE_DESCRIPTION + getDescription() + POST_DESCRIPTION;
    }

    /**
     * Set used tokens to the given quantity
     *
     * @param tokensUsed new amount of tokens used
     */
    void setTokensUsed(int tokensUsed) {
        this.tokensUsed = tokensUsed;
    }

    /**
     * Set base needed tokens
     * @param baseNeededTokens amount of base needed tokens
     */
    void setBaseNeededTokens(int baseNeededTokens) {
        this.baseNeededTokens = baseNeededTokens;
    }

    /**
     * Returns a new ToolCard instance with same properties of this ToolCard
     *
     * @return new ToolCard instance with same properties of this ToolCard
     */
    public ToolCard copy(){
        Properties p = new Properties();
        p.put("id",this.toolCardID);
        p.put("title",this.title);
        p.put("description",this.description);
        p.put(NEEDED_TOKENS,String.valueOf(this.neededTokens));
        p.put("tokensUsageMultiplier",String.valueOf(this.tokensUsageMultiplier));
        p.put("imageURL",this.imageURL);

        ToolCard copy = new ToolCard(p,new HashMap<>(this.controllerStateRules),this.placementRule,new HashSet<>(this.possibleMovesCountSet));

        copy.setBaseNeededTokens(this.baseNeededTokens);
        copy.setTokensUsed(this.tokensUsed);

        return copy;
    }
}
