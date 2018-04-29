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

        for (int i = 0; i < windowPattern.getNumberOfRows(); i++) {
            for (int j = 0; j < windowPattern.getNumberOfColumns(); j++) {
                if (pattern[i][j].hasDice()) {
                    Dice currentDice = pattern[i][j].getDice();   //get the dice that is on the current cell of the for loop
                    if (currentDice.getColor() == this.color) {
                        score = score + currentDice.getValue();   //increase the score by the value that is on the current dice
                    }
                }
            }
        }

        return score;
    }
}
