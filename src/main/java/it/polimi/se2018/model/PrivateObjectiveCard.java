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

    private PrivateObjectiveCard(){}

    public static PrivateObjectiveCard createTestInstance(){
        return new PrivateObjectiveCard();
    }

    //Returns a new PrivateObjectiveCard instance with same properties of this one
    public PrivateObjectiveCard copy(){
        return new PrivateObjectiveCard(super.getTitle(), super.getDescription(), super.getImageURL(), this.color);
    }

    public DiceColors getColor() {
        return color;
    }

    /*
    Calculates the score of the private objective card, which depends only on its color
    by scoring each cell on the window pattern
    */
    @Override
    public int calculateScore(WindowPattern windowPattern) {
        if(windowPattern==null){ throw new IllegalArgumentException("ERROR: Cannot calculate score of" +
                " a null window pattern."); }

        int score = 0;
        Cell[][] pattern = windowPattern.getPattern();
        if(pattern==null){ throw new IllegalArgumentException("ERROR: Pattern is null."); }

        for (int i = 0; i < windowPattern.getNumberOfRows(); i++) {
            for (int j = 0; j < windowPattern.getNumberOfColumns(); j++) {
                score += scoreDice(pattern[i][j]);
            }
        }
        return score;
    }
    /*
    Returns the value of a dice if its color is the same as the one of the private objective card, otherwise returns 0
    */
    private int scoreDice(Cell cell) {
        if (cell.hasDice()) {
            Dice dice = cell.getDice();
            if (dice.getColor() == this.color) {
                return dice.getValue();
            }
        }
        return 0;
    }

}
