package it.polimi.se2018.model;

import java.util.List;

public class ObjectiveCardFactory {

    public SetPublicObjectiveCard createColorSetPublicObjectiveCard(List<Object> colors){
        return new SetPublicObjectiveCard(colors,Dice::getColor,4);
    }

    public SetPublicObjectiveCard createValueSetPublicObjectiveCard(List<Object> values){
        return new SetPublicObjectiveCard(values,Dice::getValue,5);
    }
}
