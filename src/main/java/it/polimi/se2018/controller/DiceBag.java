package it.polimi.se2018.controller;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.DiceColors;

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

    //Takes "quantity" of dices out of DiceBag
    public List<Dice> getDices(int quantity) {
        List<Dice> drawnDices = new ArrayList<>();
        for(int i=0; i<quantity; i++){

            //Looks for a color dice that can be created, and creates it
            int availableDicesForRandomColor;
            DiceColors randomColor;
            do {
                randomColor = DiceColors.getRandomColor();
                availableDicesForRandomColor = availableDices.get(Arrays.asList(DiceColors.values()).indexOf(randomColor));
            } while (availableDicesForRandomColor==0);

            drawnDices.add(new Dice(randomColor));
        }
        return drawnDices;
    }

    public void addDice(Dice dice) {
        int index = Arrays.asList(DiceColors.values()).indexOf(dice.getColor());
        int availableDicesForColor = availableDices.get(index);
        availableDices.set(index, availableDicesForColor+1);
    }

    //Returns true if no more dices can be created. False in the other case.
    private boolean isEmpty(){
        return availableDices.stream().mapToInt(Integer::intValue).sum() == 0;
    }
}
