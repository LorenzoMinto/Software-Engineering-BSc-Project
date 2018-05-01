package it.polimi.se2018.model;


/*
Class PrivateObjectiveCard

Attributes:
    color: each private objective card has a different color

Methods:
    calculateScore()
    scoreDice()
*/
public class PrivateObjectiveCard extends ObjectiveCard {

    private DiceColors color;

    public PrivateObjectiveCard(String title, String description, String imageURL, DiceColors color) {
        super(title, description, imageURL);
        this.color = color;
    }

    //Returns a new PrivateObjectiveCard instance with same properties of this one
    public PrivateObjectiveCard copy(){
        return new PrivateObjectiveCard(super.getTitle(), super.getDescription(), super.getImageURL(), this.color);
    }

    /*
    Calculates the score of the private objective card, which depends only on its color
    by scoring each dice on the window pattern
    */
    @Override
    public int calculateScore(WindowPattern windowPattern) {

        int score = 0;
        Cell[][] pattern = windowPattern.getPattern();

        for (int i = 0; i < windowPattern.getNumberOfRows(); i++) {
            for (int j = 0; j < windowPattern.getNumberOfColumns(); j++) {
                if (pattern[i][j].hasDice()) {
                    score += scoreDice(pattern[i][j].getDice());
                }
            }
        }
        return score;
    }

    /*
    Returns the value of a dice if its color is the same as the one of the private objective card, otherwise returns 0
    */
    private int scoreDice(Dice dice) {
        if (dice.getColor() == this.color) {
            return dice.getValue();
        }else{
            return 0;
        }
    }
}
