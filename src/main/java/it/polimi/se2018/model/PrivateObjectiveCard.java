package it.polimi.se2018.model;

/**
 * Represents Private Objective Cards.
 *
 * Each private objective card has a different color.
 *
 * @author Jacopo Pio Gargano
 */
public class PrivateObjectiveCard extends ObjectiveCard {


    /**
     * The color of the Private Objective Card
     */
    private DiceColor color;

    /**
     * Constructor for a new Private Objective Card
     * @param title the title of the card
     * @param description the description of the card
     * @param imageURL the image url of the card
     * @param color the color of the card
     */
    public PrivateObjectiveCard(String title, String description, String imageURL, DiceColor color) {
        super(title, description, imageURL);
        this.color = color;
    }

    /**
     * Returns a new PrivateObjectiveCard instance with same properties of this one.
     *
     * @return a new PrivateObjectiveCard instance with same properties of this one
     */
    public PrivateObjectiveCard copy(){
        return new PrivateObjectiveCard(super.getTitle(), super.getDescription(), super.getImageURL(), this.color);
    }

    /**
     * Returns the color of the card.
     *
     * @return the color of the card
     */
    public DiceColor getColor() {
        return color;
    }

    /**
     * Calculates the score of a given window pattern according this objective card
     * (scoring depends only on card's color). The score is achieved by scoring each cell
     * on the window pattern.
     *
     * @param windowPattern the windowPattern to evaluate
     * @return the score of a given window pattern according the private objective card
     */
    @Override
    public int calculateScore(WindowPattern windowPattern) {
        int score = 0;
        Cell[][] pattern = windowPattern.getPattern();

        for (int i = 0; i < windowPattern.getNumberOfRows(); i++) {
            for (int j = 0; j < windowPattern.getNumberOfColumns(); j++) {
                score += scoreDice(pattern[i][j]);
            }
        }
        return score;
    }

    /**
     * Returns the value of the dice placed in the given cell if its color
     * is the same as the one of the private objective card, otherwise returns 0
     *
     * @param cell the cell to check and eventually score
     * @return the value of the dice placed in the given cell if its color
     * is the same as the one of the private objective card, otherwise 0
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
