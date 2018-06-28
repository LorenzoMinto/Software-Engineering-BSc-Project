package it.polimi.se2018.model;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * Represents a type of Public Objective Card that counts the number of rows or columns in a window pattern
 * with all different values for a specific property (color or value).
 * If the line or the column is empty or not complete, it is not considered as valid for the scoring
 *
 * The Function, which gets the property of the dice (color or value), is passed in the constructor
 *
 * @author Jacopo Pio Gargano
 */
public class RowsColumnsPublicObjectiveCard extends PublicObjectiveCard {

    /**
     * Part of the toString representation of the SetPublicObjectiveCard. Contains content shown before multiplier
     */
    private static final String PRE_MULTIPLIER = "Multiplier: ";
    /**
     * Part of the toString representation of the SetPublicObjectiveCard. Contains content shown after multiplier
     */
    private static final String POST_MULTIPLIER = "";
    /**
     * The score multiplier that is specific for each different combination of row/column - property.
     */
    private int multiplier;

    /**
     *   Specifies if the card checks the property by row or by column
     */
    private boolean checkByRow;


    /**
     * @param title the title of the specific objective card
     * @param description the description of the specific objective card
     * @param imageURL the imageURL of the specific objective card
     * @param propertyFunction function of Dice used to get a certain property of it
     * @param multiplier the card multiplier used in the scoring process
     * @param checkByRow boolean to check the property by row or by column
     */
    public RowsColumnsPublicObjectiveCard(String title, String description, String imageURL,
                                          Function<Dice, Object> propertyFunction,
                                          int multiplier, boolean checkByRow) {
        super(title, description, imageURL, propertyFunction);
        this.multiplier = multiplier;
        this.checkByRow = checkByRow;
    }

    /**
     * Returns a new RowsColumnsPublicObjectiveCard instance with same properties of this one
     *
     * @return new RowsColumnsPublicObjectiveCard instance with same properties of this one
     */
    @Override
    public PublicObjectiveCard copy() {
        return new RowsColumnsPublicObjectiveCard(super.getTitle(), super.getDescription(), super.getImageURL(),
                super.getPropertyFunction(), this.multiplier, this.checkByRow);
    }

    /**Calculates the score of a given window pattern based on the RowsColumnsPublicObjectiveCard's score criteria
     *
     * Transposes the pattern if the check is by columns, in order to avoid code duplication
     * The number of different properties of a row must be equal to the number of columns of the pattern if every
     * property is different, since there is one property for each dice and one dice for each cell (row, column)
     *
     * @param windowPattern the windowPattern to calculate the score of
     * @return the score of a given window pattern based on the RowsColumnsPublicObjectiveCard's score criteria
     */
    @Override
    public int calculateScore(WindowPattern windowPattern) {

        int numberOfValidRows = 0;
        int numberOfDifferentProperties;
        int numberOfRows = windowPattern.getNumberOfRows();
        int numberOfColumns = windowPattern.getNumberOfColumns();
        Cell[][] pattern = windowPattern.getPattern();

        //If checking by columns, transpose the pattern and swap the number of rows and columns
        if(!checkByRow){
            pattern = transpose(pattern, numberOfRows, numberOfColumns);
            numberOfRows = windowPattern.getNumberOfColumns();
            numberOfColumns = windowPattern.getNumberOfRows();
        }

        /*
        For each row:
            - check if it is incomplete
            - get the number of different properties
            - compare it to the number of columns of the pattern
        */
        for(int row = 0; row < numberOfRows; row++){

            //If the current row is incomplete, then skip to the next row
            if(rowIsIncomplete(pattern[row])) continue;

            numberOfDifferentProperties = getRowDifferentPropertiesNumber(pattern[row]);

            if(numberOfDifferentProperties == numberOfColumns){
                numberOfValidRows += 1;
            }
        }

        return numberOfValidRows*multiplier;
    }

    /**
     * Gets the number of different property values of a specific row
     *
     * @param row the row to calculate the number of different properties of
     * @return the number of different property values of a specific row
     */
    private int getRowDifferentPropertiesNumber(Cell[] row) {

        Set<Object> differentProperties = new HashSet<>();
        Dice dice1;
        Dice dice2;
        Object property1;
        Object property2;

        /*
        Compare each two adjacent dice
            If their properties are different add the properties to the set of different properties
            Otherwise return 0 since there are at least two dice with the same property on the row
        */
        for(int col = 0; col +1 < row.length; col++) {

            dice1 = row[col].getDice();
            dice2 = row[col+1].getDice();

            property1 = super.getPropertyFunction().apply(dice1);
            property2 = super.getPropertyFunction().apply(dice2);

            if (property1.equals(property2)) {
                return 0;
            }else {
                differentProperties.add(property1);
                differentProperties.add(property2);
            }
        }

        return differentProperties.size();
    }


    /**
     * Returns false if every cell that forms a specific row has a dice
     *
     * @param row the row to be evaluated
     * @return false if every cell that forms a specific row has a dice, otherwise true
     */
    private boolean rowIsIncomplete(Cell[] row) {

        for(Cell cell : row){
            if(!cell.hasDice()){return true;}
        }
        return false;
    }

    /**
     * Transposes a given pattern
     *
     * @param pattern the pattern to be transposed
     * @param rows the number of rows of the pattern
     * @param columns the number of columns of the pattern
     * @return the transposed pattern
     */
    private Cell[][] transpose(Cell[][] pattern, int rows, int columns) {

        Cell[][] transposedPattern = new Cell[columns][rows];
        for(int i=0; i < rows; i++){
            for(int j=0; j < columns; j++){
                transposedPattern[j][i] = pattern[i][j];
            }
        }
        return transposedPattern;
    }
    
    /**
     * Returns the String representation of the card.
     *
     * @return the String representation of the card.
     */
    @Override
    public String toString(){
        String s = super.toString();
        s = s.concat(PRE_MULTIPLIER + multiplier + POST_MULTIPLIER);
        s = s.concat(System.lineSeparator());
        return s;
    }
}
