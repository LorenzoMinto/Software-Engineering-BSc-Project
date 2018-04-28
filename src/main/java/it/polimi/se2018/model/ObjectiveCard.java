package it.polimi.se2018.model;

public abstract class ObjectiveCard {

    private String title;
    private String description;
    private int score;
    private String imageURL;

    public abstract int calculateScore(WindowPattern windowPattern);

}
