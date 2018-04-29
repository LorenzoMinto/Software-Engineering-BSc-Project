package it.polimi.se2018.model;

import java.util.*;


//Public Objective Card that counts the number of sets of dice one of each value of a specific list in a window pattern

public class ValueSetPublicObjectiveCard extends PublicObjectiveCard {

    private HashSet<Integer> values;

    //The list of values that form a set is passed in the constructor
    public ValueSetPublicObjectiveCard(HashSet<Integer> values) {
        this.values = values;
    }

    //When calling this method, pass a copy of the windowPattern after checking that it isn't null
    @Override
    public int calculateScore(WindowPattern windowPattern) {
        int multiplier = 5;
        int numberOfCompletedSets = 0;
        Cell[][] pattern = windowPattern.getPattern();
        List<HashSet<Integer>> listOfSets = new ArrayList<>();
        HashSet<Integer> set = new HashSet<>();
        listOfSets.add(set);
        HashSet<Integer> currentSet;

        for(int i=0; i<windowPattern.getNumberOfRows(); i++){
            for(int j=0; j < windowPattern.getNumberOfColumns(); j++){
                if(pattern[i][j].hasDice()) {
                    Integer currentValue = pattern[i][j].getDice().getValue();

                    for(int k=0; k<listOfSets.size(); k++){
                        currentSet = listOfSets.get(k);

                        if(values.contains(currentValue) && !currentSet.contains(currentValue)){          //if the current set of values does not contain the current value
                            currentSet.add(currentValue);                //then add it to the first set

                            if(currentSet.equals(values)){               //if the current set is full then remove it and create a new set
                                listOfSets.remove(currentSet);
                                set = new HashSet<>();
                                listOfSets.add(set);
                                numberOfCompletedSets++;
                            }

                            break;

                        }else if(values.contains(currentValue) && k+1 == listOfSets.size()) {             //if another set is needed because all previous ones
                            set = new HashSet<>();                     //already have the current value
                            set.add(currentValue);                       //create a new set and add the current value in it
                            listOfSets.add(set);
                            break;
                        }
                    }
                }
            }
        }

        return multiplier*numberOfCompletedSets;
    }
}
