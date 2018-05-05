package it.polimi.se2018.model;

import it.polimi.se2018.controller.ControllerState;

public class ToolCard {
    private String title;
    private String description;
    private int tokensUsed;
    private int neededTokens;
    private String imageURL;

    public ToolCard(String title, String description, String imageURL) {
        this.title = title;
        this.description = description;
        //TODO: neededTokens should be taken from the configuration file
        this.neededTokens = 1;
        this.tokensUsed = 0;
        this.imageURL = imageURL;
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

    public ControllerState nextState(ControllerState state){

        ControllerState nextState;

        //TODO: implement here calculation of nextState
        nextState = null;

        return nextState;
    }
}
