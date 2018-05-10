package it.polimi.se2018.model;

import it.polimi.se2018.controller.ControllerState;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ToolCard {
    private String title;
    private String description;
    private int tokensUsed;
    private int neededTokens;
    private int baseNeededTokens;
    private int tokensUsageMultiplier;
    private String imageURL;
    private HashMap<String,String> controllerStateRules;
    private PlacementRule placementRule;

    public ToolCard(String title, String description, String imageURL, int neededTokens, int tokensUsageMultiplier, Map<String, String> controllerStateRules, PlacementRule placementRule) {
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

    public void use() {
        this.tokensUsed += this.neededTokens;

        //Increase neededTokens on the first usage (check of first usage is made thanks baseNeededTokens)
        if (this.tokensUsed==this.baseNeededTokens) { this.neededTokens *= this.tokensUsageMultiplier; }
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getTokensUsed() {
        return tokensUsed;
    }

    public int getNeededTokens() {
        return neededTokens;
    }

    public String getImageURL() {
        return imageURL;
    }

    public PlacementRule getPlacementRule() { return placementRule; }

    public String nextStateID(ControllerState currentState){

        return controllerStateRules.get( currentState.getClass().getSimpleName() );
    }

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
        return Objects.hash(title,description,tokensUsed,neededTokens,tokensUsageMultiplier,imageURL,controllerStateRules,placementRule);
    }
}
