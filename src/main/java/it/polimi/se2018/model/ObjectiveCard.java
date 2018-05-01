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

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public abstract ObjectiveCard copy();

    public abstract int calculateScore(WindowPattern windowPattern);

    public String toString(){
        String s = title;
        s = s.concat(System.lineSeparator());
        s = s.concat(description);
        s = s.concat(System.lineSeparator());
        return s;
    }

}
