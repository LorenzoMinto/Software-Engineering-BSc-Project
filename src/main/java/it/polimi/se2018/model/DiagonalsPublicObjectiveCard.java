package it.polimi.se2018.model;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Public Objective Card that counts the number of diagonally adjacent dice
 * in a window pattern with the same property (color or value)
 *
 * @author Jacopo Pio Gargano
 */
public class DiagonalsPublicObjectiveCard extends PublicObjectiveCard {

    /**
     *
     */
    private static final String NULL_WINDOW_PATTERN_SCORE = "Cannot calculate score of a null windowPattern.";

    /**
     * Constructor for Diagonals Public Objective Card
     *
     * @param title the title of the card
     * @param description the description of the card
     * @param imageURL the image url of the card
     * @param propertyFunction function of Dice used to get a certain property of it
     */
    public DiagonalsPublicObjectiveCard(String title, String description, String imageURL,
                                        Function<Dice,Object> propertyFunction) {
        super(title, description, imageURL, propertyFunction);
    }

    /**
     * Returns a new DiagonalsPublicObjectiveCard instance with same properties of this one
     *
     * @return new DiagonalsPublicObjectiveCard instance with same properties of this one
     */
    @Override
    public PublicObjectiveCard copy() {
        return new DiagonalsPublicObjectiveCard(super.getTitle(), super.getDescription(), super.getImageURL(),
                super.getPropertyFunction());
    }

    /**
     * Calculates the score of a given window pattern based on the DiagonalPublicObjectiveCard's score criteria
     *
     * @param windowPattern the window pattern to be evaluated
     * @return the score of a given window pattern based on the DiagonalPublicObjectiveCard's score criteria
     */
    @Override
    public int calculateScore(WindowPattern windowPattern) {
        if(windowPattern==null){ throw new IllegalArgumentException(NULL_WINDOW_PATTERN_SCORE); }

        int score = 0;
        int numberOfRows = windowPattern.getNumberOfRows();
        int numberOfColumns = windowPattern.getNumberOfColumns();

        //Contains all ints from 0 to numberOfRows*numberOfColumns -1
        List<Integer> listOfNotConsideredDice = IntStream.rangeClosed(0, numberOfRows*numberOfColumns-1)
                .boxed().collect(Collectors.toList());

        //Score diagonals from left to right
        score += getScoreLeftToRight(windowPattern, listOfNotConsideredDice);
        //Score diagonals from right to left
        score += getScoreRightToLeft(windowPattern, listOfNotConsideredDice);

        return score;
    }

    /**
     *  Calculates the score of the diagonals from left to right: diagonals have origin
     *  in a cell and proceed to the right.
     *
     *  The 'move' parameter in the scoreDiagonal() method needs to be set to 1.
     *
     *  The algorithm covers the whole matrix starting from the bottom left corner going
     *  vertically upward and finally horizontally to the right.
     *
     * @param windowPattern the windowPattern to be evaluated
     * @param listOfNotConsideredDice list of dice of the linear indexes of the dice that are not or not yet considered
     *                               as part of the score
     * @return the score of all the left to right diagonals
     */
    private int getScoreLeftToRight(WindowPattern windowPattern, List<Integer> listOfNotConsideredDice) {
        int score = 0;

        //Move vertically upward
        for(int row = windowPattern.getNumberOfRows()-1; row > 0; row--){
                score+= scoreDiagonal(windowPattern, listOfNotConsideredDice, row, 0, true);
        }

        //Move horizontally to the right
        for(int col = 0; col < windowPattern.getNumberOfColumns(); col++){
            score+= scoreDiagonal(windowPattern, listOfNotConsideredDice, 0, col, true);
        }

        return score;
    }

    /**
     * Calculates the score of the diagonals from right to left: diagonals have origin
     * in a cell and proceed to the left.
     *
     * The 'move' parameter in the scoreDiagonal() method needs to be set to -1.
     *
     * The algorithm covers the whole matrix starting from the bottom right corner going
     * vertically upward and finally horizontally to the left.
     *
     * @param windowPattern the windowPattern to be evaluated
     * @param listOfNotConsideredDice list of dice of the linear indexes of the dice that are not or not yet considered
     *                               as part of the score
     * @return the score of all the right to left diagonals
     */
    private int getScoreRightToLeft(WindowPattern windowPattern, List<Integer> listOfNotConsideredDice) {
        int score = 0;
        int numberOfRows = windowPattern.getNumberOfRows();
        int numberOfColumns = windowPattern.getNumberOfColumns();

        //Move vertically upward
        for(int row = numberOfRows-1; row > 0; row--){
            score+= scoreDiagonal(windowPattern, listOfNotConsideredDice, row, numberOfColumns-1,
                    false);
        }

        //Move horizontally to the left
        for(int col=numberOfColumns-1;  col>=0; col--){
            score+= scoreDiagonal(windowPattern, listOfNotConsideredDice, 0, col,
                    false);
        }

        return score;
    }

    /**
     * Method to count the number of adjacent dice with the same property,
     * specified in the getPropertyFunction, in a diagonal.
     *
     * @param windowPattern the windowPattern to be evaluated
     * @param listOfNotConsideredDice list of dice of the linear indexes of the dice that are not or not yet considered
     *                               as part of the score
     * @param initialRow row index of the first cell of the diagonal to be checked
     * @param initialCol row index of the first cell of the diagonal to be checked
     * @param isLeftToRightDiagonal boolean value meaning if the score must be calculated for a left to right or
     *                             for a left to right diagonal
     * @return the score of a diagonal
     */
    private int scoreDiagonal(WindowPattern windowPattern, List<Integer> listOfNotConsideredDice,
                              int initialRow, int initialCol, boolean isLeftToRightDiagonal){
        int score = 0;
        int move; // 1 if moving to the right, -1 if moving to the left
        int numberOfRows = windowPattern.getNumberOfRows();
        int numberOfColumns = windowPattern.getNumberOfColumns();

        if(isLeftToRightDiagonal){
            move = 1;
        }else{
            move = -1;
        }

        for (int currentRow = initialRow, currentCol = initialCol; currentRow + 1 < numberOfRows &&
                currentCol + move < numberOfColumns && currentCol + move >= 0; currentRow++, currentCol+=move) {

            score += getCellPairScore(windowPattern, listOfNotConsideredDice, move, currentRow, currentCol);
        }

        return score;
    }

    /**
     * Gets the score of a pair of cells identified by the row and column of the first cell
     *
     * @param windowPattern the windowPattern to be evaluated
     * @param listOfNotConsideredDice list of dice of the linear indexes of the dice that are not or not yet considered
     *                               as part of the score
     * @param move integer indicating the kind of diagonal the cell pair belongs to:
     *             1 if left to right diagonal, -1 if right to left diagonal
     * @param row row number of the first cell
     * @param column column number of the first cell
     * @return the score of a pair of cells identified by the row and column of the first cell
     */
    private int getCellPairScore(WindowPattern windowPattern, List<Integer> listOfNotConsideredDice, int move,
                                 int row, int column){
        int score = 0;
        Cell[][] pattern = windowPattern.getPattern();
        int numberOfRows = windowPattern.getNumberOfRows();

        Dice currentDice;
        Dice adjacentDice;
        Object currentDiceProperty;
        Object adjacentDiceProperty;
        int currentDiceLinearIndex;
        int adjacentDiceLinearIndex;

        //If one of the cells does not have a dice, then return 0 as the score of the pair
        if(!pattern[row][column].hasDice() || !pattern[row+1][column+move].hasDice()) return 0;

        //Get the two dice, their property and their linear index
        currentDice = pattern[row][column].getDice();
        currentDiceProperty = super.getPropertyFunction().apply(currentDice);
        currentDiceLinearIndex = getLinearIndex(row, column, numberOfRows);

        adjacentDice = pattern[row + 1][column + move].getDice();
        adjacentDiceProperty = super.getPropertyFunction().apply(adjacentDice);
        adjacentDiceLinearIndex = getLinearIndex(row+1,column+move, numberOfRows);

        //Compare the dice. Return 0 as the score of the pair if the comparison is not positive
        if(currentDiceProperty != adjacentDiceProperty ) return 0;

        if(listOfNotConsideredDice.contains(currentDiceLinearIndex)){
            listOfNotConsideredDice.remove(Integer.valueOf(currentDiceLinearIndex));
            score++;
        }
        if(listOfNotConsideredDice.contains(adjacentDiceLinearIndex)){
            listOfNotConsideredDice.remove(Integer.valueOf(adjacentDiceLinearIndex));
            score++;
        }

        return score;
    }

    /**
     * Returns the linear index of a dice contained in a window pattern
     *
     * @param row row index of the cell containing the dice
     * @param col column index of the cell containing the dice
     * @param numberOfRows number of rows of the window pattern to be evaluated
     * @return the linear index of a dice contained in a window pattern
     */
    private int getLinearIndex(int row, int col, int numberOfRows) {
        return (row * (numberOfRows+1)) + col;
    }

}