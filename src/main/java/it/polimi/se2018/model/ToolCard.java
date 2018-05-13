package it.polimi.se2018.model;

import it.polimi.se2018.controller.ControllerState;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * Class representing the Game's tool cards. Each card's effect is encoded in a placement rule and
 * an HashMap that describes the effect's own state transitions.
 *
 * @author Lorenzo Minto
 */
public class ToolCard {

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
     * The tool card's state transition table. Represents the tool card's effect (when active).
     */
    private HashMap<String,String> controllerStateRules;

    /**
     * The tool card's own placement rule. Enforced on the game only when active.
     */
    private PlacementRule placementRule;

    /**
     * Class constructor.
     *
     * @param title the title of the tool card.
     * @param description the description of the tool card.
     * @param imageURL the URL of the tool card image.
     * @param neededTokens the tokens needed to activate the tool card.
     * @param tokensUsageMultiplier the factor that multiplies needed tokens after the first activation.
     * @param controllerStateRules the state table that governs state transitions when the tool card is active.
     * @param placementRule the placement rules that need to be enforced when the tool card is active.
     */
    public ToolCard(String title, String description, String imageURL, int neededTokens, int tokensUsageMultiplier,
                    Map<String, String> controllerStateRules, PlacementRule placementRule) {
        this.title = title;
        this.description = description;
        this.neededTokens = neededTokens;
        this.baseNeededTokens = neededTokens;
        this.tokensUsed = 0;
        this.imageURL = imageURL;
        this.controllerStateRules = (HashMap<String,String>)controllerStateRules;
        this.placementRule = placementRule;
        this.tokensUsageMultiplier = tokensUsageMultiplier;
    }

    /**
     * Empty private constructor.
     */
    private ToolCard(){}

    /**
     * Constructor for test purposes.
     *
     * @return an empty instance of ToolCard.
     */
    public static ToolCard createTestInstance(){
        return new ToolCard();
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

    /**
     * Returns the title of the tool card.
     *
     * @return the title of the tool card.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the description of the tool card's effect.
     *
     * @return the description of the tool card's effect.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the number of tokens spent on the tool card.
     *
     * @return the number of tokens spent on the tool card.
     */
    private int getTokensUsed() { return tokensUsed; }

    /**
     * Returns the number of tokens needed to activate the tool card.
     *
     * @return the number of tokens needed to activate the tool card.
     */
    public int getNeededTokens() {
        return neededTokens;
    }

    /**
     * Returns the url of the tool card's image.
     *
     * @return the url of the tool card's image.
     */
    public String getImageURL() {
        return imageURL;
    }


    /**
     * Returns the placement rule relative to the tool card's effect.
     *
     * @return the placement rule relative to the tool card's effect.
     */
    public PlacementRule getPlacementRule() { return placementRule; }

    /**
     * Returns the ID of the next state given the current state, after having queried the tool card's state
     * transition table.
     *
     * @param currentState the current state.
     * @return the ID of the state that follows the current according to the card's transition table.
     */
    public String nextStateID(ControllerState currentState){

        return controllerStateRules.get( currentState.getClass().getSimpleName() );
    }

    /**
     * Returns true if the ToolCard's effect requires drafting, else false.
     *
     * @return whether or not the ToolCard's effect requires drafting.
     */
    public boolean needsDrafting(){
        //TODO: implement here

        return false; //this is a placeholder waiting for implementation
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ToolCard)) {
            return false;
        }

        ToolCard c = (ToolCard) o;

        return this.title == c.getTitle()
                && this.description == c.getDescription()
                && this.imageURL == c.getImageURL();
    }

    @Override
    public int hashCode() {
        return Objects.hash(title,description,tokensUsed,neededTokens,tokensUsageMultiplier,imageURL,
                controllerStateRules,placementRule);
    }
}
