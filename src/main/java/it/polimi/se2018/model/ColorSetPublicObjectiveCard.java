package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.List;


//Public Objective Card that counts the number of sets of dice one of each color of a specific list in a window pattern

public class ColorSetPublicObjectiveCard extends PublicObjectiveCard {

    private List<DiceColors> colors;

    //The list of colors that form a set is passed in the constructor
    public ColorSetPublicObjectiveCard(List<DiceColors> colors) {
        this.colors = colors;
    }

    //When calling this method, pass a copy of the windowPattern after checking that it isn't null
    @Override
    public int calculateScore(WindowPattern windowPattern) {
        int multiplier = 4;
        int numberOfCompletedSets = 0;
        Cell[][] pattern = windowPattern.getPattern();
        List<List<DiceColors>> listOfSets = new ArrayList<>();
        List<DiceColors> set = new ArrayList<>();
        listOfSets.add(set);
        List<DiceColors> currentSet;

        for(int i=0; i<windowPattern.getNumberOfRows(); i++){
            for(int j=0; j < windowPattern.getNumberOfColumns(); j++){
                if(pattern[i][j].hasDice()) {
                    DiceColors currentColor = pattern[i][j].getDice().getColor();

                    for(int k=0; k<listOfSets.size(); k++){
                         currentSet = listOfSets.get(k);

                        if(!currentSet.contains(currentColor)){          //if the current set of colors does not contain the current color
                            currentSet.add(currentColor);                //then add it to the first set

                            if(currentSet.equals(colors)){               //if the current set is full then remove it and create a new set
                                listOfSets.remove(currentSet);
                                set = new ArrayList<>();
                                listOfSets.add(set);
                                numberOfCompletedSets++;
                            }

                            break;

                        }else if(k+1 == listOfSets.size()) {             //if another set is needed because all previous ones
                                set = new ArrayList<>();                 //already have the current color
                                set.add(currentColor);                   //create a new set and add the current color in it
                                listOfSets.add(set);
                        }
                    }
                }
            }
        }

        return multiplier*numberOfCompletedSets;
    }
}
