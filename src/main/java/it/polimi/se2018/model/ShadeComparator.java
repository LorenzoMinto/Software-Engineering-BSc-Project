package it.polimi.se2018.model;

import java.util.Comparator;

//Class used to compare two dice and decide if they have the same value
public class ShadeComparator implements Comparator<Dice> {

    //Returns 0 if the dice have the same value, otherwise 1
    @Override
    public int compare(Dice dice1, Dice dice2) {
        if(dice1.getValue() == dice2.getValue()){ return 0;}
        else{ return 1;}
    }
}
