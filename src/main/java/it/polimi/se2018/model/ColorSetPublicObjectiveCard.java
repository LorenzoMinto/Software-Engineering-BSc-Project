package it.polimi.se2018.model;

import java.util.*;


//Public Objective Card that counts the number of sets of dice one of each color of a specific list in a window pattern

public class ColorSetPublicObjectiveCard extends PublicObjectiveCard {

    private HashSet<DiceColors> colors;

    //The list of colors that form a set is passed in the constructor
    public ColorSetPublicObjectiveCard(HashSet<DiceColors> colors) {
        this.colors = colors;
    }

    //When calling this method, pass a copy of the windowPattern after checking that it isn't null
    @Override
    public int calculateScore(WindowPattern windowPattern) {
        int multiplier = 4;
        int numberOfCompletedSets = 0;
        Cell[][] pattern = windowPattern.getPattern();
        List<HashSet<DiceColors>> listOfSets = new ArrayList<>();
        HashSet<DiceColors> set = new HashSet<>();
        listOfSets.add(set);
        HashSet<DiceColors> currentSet;

        for(int i=0; i<windowPattern.getNumberOfRows(); i++){
            for(int j=0; j < windowPattern.getNumberOfColumns(); j++){
                if(pattern[i][j].hasDice()) {
                    DiceColors currentColor = pattern[i][j].getDice().getColor();

                    for(int k=0; k<listOfSets.size(); k++){
                         currentSet = listOfSets.get(k);

                        if(colors.contains(currentColor) && !currentSet.contains(currentColor)){          //if the current set of colors does not contain the current color
                            currentSet.add(currentColor);                //then add it to the first set

                            if(currentSet.equals(colors)){               //if the current set is full then remove it and create a new set
                                listOfSets.remove(currentSet);
                                set = new HashSet<>();
                                listOfSets.add(set);
                                numberOfCompletedSets++;
                            }

                            break;

                        }else if(colors.contains(currentColor) && k+1 == listOfSets.size()) {             //if another set is needed because all previous ones
                                set = new HashSet<>();                 //already have the current color
                                set.add(currentColor);                   //create a new set and add the current color in it
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
