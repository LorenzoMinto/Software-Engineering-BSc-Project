package it.polimi.se2018.model;

import it.polimi.se2018.controller.ControllerState;

import java.util.HashMap;

public class ToolCard {
    private String title;
    private String description;
    private int tokensUsed;
    private int neededTokens;
    private String imageURL;
    HashMap<String,String> controllerStateRules;
    private PlacementRule placementRule;

    public ToolCard(String title, String description, String imageURL, HashMap<String, String> controllerStateRules, PlacementRule placementRule) {
        this.title = title;
        this.description = description;
        //TODO: neededTokens should be taken from the configuration file
        this.neededTokens = 1;
        this.tokensUsed = 0;
        this.imageURL = imageURL;
        this.controllerStateRules = controllerStateRules;
        this.placementRule = placementRule;
    }

    private ToolCard(String title, String description, String imageURL, HashMap<String, String> stateRules, PlacementRule placementRule, int neededTokens, int tokensUsed){
        this(title, description, imageURL, stateRules, placementRule);
        this.neededTokens = neededTokens;
        this.tokensUsed = tokensUsed;
    }

    public ToolCard copy(){
        //TODO: ensure that this.controllerStateRules does not need to be copied
        return new ToolCard(this.title, this.description, this.imageURL, this.controllerStateRules, this.placementRule, this.neededTokens, this.tokensUsed);
    }

    public void use() {
        this.tokensUsed += this.neededTokens;

        //Update neededTokens
        if (this.tokensUsed==1) { this.neededTokens *= 2; }
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

        String nextStateID;

        //TODO: use controllerStateRules HasMap
        nextStateID = null;

        return nextStateID;
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
}
