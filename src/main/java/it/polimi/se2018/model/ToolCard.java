package it.polimi.se2018.model;

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
        this.updateNeededTokens();
    }

    //this function incapsulates the logic of the update for the neededTokens, which may vary
    protected void updateNeededTokens() {
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
}
