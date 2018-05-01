package it.polimi.se2018.model;

/*
Abstract class that extends ObjectiveCard with no additional feature
The objective of this abstract class is to better understand that a Game has a certain amount of public objective cards
which are visible to every player, whereas the private ones are only visible to the player who owns them.
*/

public abstract class PublicObjectiveCard extends ObjectiveCard{
    public PublicObjectiveCard(String title, String description, String imageURL) {
        super(title, description, imageURL);
    }
    public abstract PublicObjectiveCard copy();
}
