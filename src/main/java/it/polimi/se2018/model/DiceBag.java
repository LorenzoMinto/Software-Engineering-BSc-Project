package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiceBag {

    private List<Integer> availableDices;

    public DiceBag(int numberOfDicesPerColor) {
        availableDices = new ArrayList<>(DiceColors.values().length);
        for(int i=0; i<availableDices.size(); i++){
            availableDices.set(i,numberOfDicesPerColor);
        }
    }

    //Takes "quantity" of dices out of DiceBag returning as a DraftPool
    public DraftPool getDices(int quantity) {
        DraftPool draftPool = new DraftPool();
        for(int i=0; i<quantity; i++){

            //Looks for a color dice that can be created, and creates it
            int availableDicesForRandomColor;
            DiceColors randomColor;
            do {
                randomColor = DiceColors.getRandomColor();
                availableDicesForRandomColor = availableDices.get(Arrays.asList(DiceColors.values()).indexOf(randomColor));
            } while (availableDicesForRandomColor==0);

            draftPool.addDice( new Dice(randomColor) );
        }
        return draftPool;
    }

    //Returns true if no more dices can be created. False in the other case.
    private boolean isEmpty(){
        for (Integer i : availableDices){
            if(i!=0){
                return false;
            }
        }
        return true;
    }
}
