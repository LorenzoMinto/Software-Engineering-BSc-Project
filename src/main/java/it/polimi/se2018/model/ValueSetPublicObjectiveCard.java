package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.List;


//Public Objective Card that counts the number of sets of dice one of each value of a specific list in a window pattern

public class ValueSetPublicObjectiveCard extends PublicObjectiveCard {
    private List<Integer> values;

    //The list of values that form a set is passed in the constructor
    public ValueSetPublicObjectiveCard(List<Integer> values) {
        this.values = values;
    }

    //When calling this method, pass a copy of the windowPattern after checking that it isn't null
    @Override
    public int calculateScore(WindowPattern windowPattern) {
        int multiplier = 5;
        int numberOfSets = 0;
        Cell[][] pattern = windowPattern.getPattern();
        List<List<Integer>> listOfSets = new ArrayList<>();
        List<Integer> set = new ArrayList<>();
        listOfSets.add(set);
        List<Integer> currentSet;

        for(int i=0; i<windowPattern.getNumberOfRows(); i++){
            for(int j=0; j < windowPattern.getNumberOfColumns(); j++){
                if(pattern[i][j].hasDice()) {
                    Integer currentValue = pattern[i][j].getDice().getValue();

                    for(int k=0; k<listOfSets.size(); k++){
                        currentSet = listOfSets.get(k);

                        if(!currentSet.contains(currentValue)){          //if the current set of values does not contain the current value
                            currentSet.add(currentValue);                //then add it to the first set

                            if(currentSet.equals(values)){               //if the current set is full then remove it and create a new set
                                listOfSets.remove(currentSet);
                                set = new ArrayList<>();
                                listOfSets.add(set);
                                numberOfSets++;
                            }

                            break;

                        }else if(k+1 == listOfSets.size()) {             //if another set is needed because all previous ones
                            set = new ArrayList<>();                     //already have the current value
                            set.add(currentValue);                       //create a new set and add the current value in it
                            listOfSets.add(set);
                        }
                    }
                }
            }
        }

        return multiplier*numberOfSets;
    }
}
