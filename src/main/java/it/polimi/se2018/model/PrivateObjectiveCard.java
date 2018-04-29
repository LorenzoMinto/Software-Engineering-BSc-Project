package it.polimi.se2018.model;

public class PrivateObjectiveCard extends ObjectiveCard {

    private DiceColors color;

    public PrivateObjectiveCard(DiceColors color) {
        this.color = color;
    }

    //Returns a new PrivateObjectiveCard instance with same properties of this one
    public PrivateObjectiveCard copy(){
        return new PrivateObjectiveCard(this.color);
    }

    //The score of each PrivateObjectiveCard depends only on its color
    @Override
    public int calculateScore(WindowPattern windowPattern) {

        int score = 0;
        Cell[][] pattern = windowPattern.getPattern();


        try {
            for (int i = 0; i < windowPattern.getNumberOfRows(); i++) {
                for (int j = 0; j < windowPattern.getNumberOfColumns(); j++) {
                    if (pattern[i][j].hasDice()) {
                        DiceColors currentDiceColor = pattern[i][j].getDice().getColor();   //get the color of the dice that is on the current cell of the for loop
                        if (currentDiceColor == this.color) {
                            score++;
                        }
                    }
                }
            }
        }
        catch (NullPointerException e){
            //TODO: notify the view that something went wrong in the WindowPattern that was passed as a parameter
        }

        return score;
    }
}
