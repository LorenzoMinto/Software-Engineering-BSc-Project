package it.polimi.se2018.model;

import java.util.Comparator;

//Class used to compare two dice and decide if they are the same color
public class ColorComparator implements Comparator<Dice> {

    //Returns 0 if the dice are the same color, otherwise 1
    @Override
    public int compare(Dice dice1, Dice dice2) {
        if(dice1.getColor() == dice2.getColor()){ return 0;}
        else{ return 1;}
    }
}
