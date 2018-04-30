package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;


//Public Objective Card that counts the number of sets of dice one of each color of a specific list in a window pattern

public class SetPublicObjectiveCard extends PublicObjectiveCard {

    private List<Object> items;

    private Function<Dice,Object> getPropertyFunction;

    private int multiplier;

    //The list of colors that form a set is passed in the constructor
    public SetPublicObjectiveCard(List<Object> items, Function<Dice,Object> getPropertyFunction, int multiplier) {
        this.items = items;
        this.getPropertyFunction = getPropertyFunction;
        this.multiplier = multiplier;
    }

    //When calling this method, pass a copy of the windowPattern after checking that it isn't null
    @Override
    public int calculateScore(WindowPattern windowPattern) {
        int numberOfCompletedSets = 0;
        Cell[][] pattern = windowPattern.getPattern();
        List<HashSet<Object>> listOfSets = new ArrayList<>();
        HashSet<Object> set = new HashSet<>();
        listOfSets.add(set);
        HashSet<Object> currentSet;

        for(int i=0; i<windowPattern.getNumberOfRows(); i++){
            for(int j=0; j < windowPattern.getNumberOfColumns(); j++){
                if(pattern[i][j].hasDice()) {
                    Object currentProperty = this.getPropertyFunction.apply( pattern[i][j].getDice() );

                    for(int k=0; k<listOfSets.size(); k++){
                        currentSet = listOfSets.get(k);

                        if(!currentSet.contains(currentProperty)){          //if the current set of colors does not contain the current color
                            currentSet.add(currentProperty);                //then add it to the first set

                            if(currentSet.equals(items)){               //if the current set is full then remove it and create a new set
                                listOfSets.remove(currentSet);
                                set = new HashSet<>();
                                listOfSets.add(set);
                                numberOfCompletedSets++;
                            }

                            break;

                        }else if(k+1 == listOfSets.size()) {             //if another set is needed because all previous ones
                            set = new HashSet<>();                 //already have the current color
                            set.add(currentProperty);                   //create a new set and add the current color in it
                            listOfSets.add(set);
                        }
                    }
                }
            }
        }

        return this.multiplier*numberOfCompletedSets;
    }
}