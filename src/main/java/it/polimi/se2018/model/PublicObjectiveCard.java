package it.polimi.se2018.model;

/*
Abstract class that extends ObjectiveCard
The objective of this abstract class is to better understand that a Game has a certain amount of public objective cards
which are visible to every player, whereas the private ones are only visible to the player who owns them.

propertyFunction: gets the property of the dice specified in the constructor
                         needed to compare the properties of two dice
*/


import java.util.function.Function;

public abstract class PublicObjectiveCard extends ObjectiveCard{

    private Function<Dice,Object> propertyFunction;


    public PublicObjectiveCard(String title, String description, String imageURL, Function<Dice,Object> propertyFunction) {
        super(title, description, imageURL);
        this.propertyFunction = propertyFunction;
    }

    public Function<Dice, Object> getPropertyFunction() {
        return propertyFunction;
    }
}
