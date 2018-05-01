package it.polimi.se2018.model;

public abstract class ObjectiveCard {

    private String title;
    private String description;
    private String imageURL;

    public ObjectiveCard(String title, String description, String imageURL) {
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
    }

    public abstract int calculateScore(WindowPattern windowPattern);

}
