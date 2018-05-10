package it.polimi.se2018.model;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;


/*
Public Objective Card that counts the number of lines or columns in a window pattern
with all different values for a specific property (color or value)
If the line or the column is empty or not complete, it is not considered as valid for the scoring
The Function, which gets the property of the dice, is passed in the constructor
The boolean 'checkByRow' specifies if the card checks the property by row or by column

Attributes:
    multiplier: the score multiplier that is specific for each combination of the couple (property, row ||column)
    checkByRow

Methods:
    calculateScore()
    getRowDifferentPropertiesNumber()
    rowIsIncomplete()
    transpose()
*/

public class RowsColumnsPublicObjectiveCard extends PublicObjectiveCard {

    private int multiplier;

    private boolean checkByRow;

    public RowsColumnsPublicObjectiveCard(String title, String description, String imageURL,
                                          Function<Dice, Object> propertyFunction,
                                          int multiplier, boolean checkByRow) {
        super(title, description, imageURL, propertyFunction);
        this.multiplier = multiplier;
        this.checkByRow = checkByRow;
    }

    public RowsColumnsPublicObjectiveCard(){}

    //Returns a new RowsColumnsPublicObjectiveCard instance with same properties of this one
    @Override
    public PublicObjectiveCard copy() {
        return new RowsColumnsPublicObjectiveCard(super.getTitle(), super.getDescription(), super.getImageURL(),
                super.getPropertyFunction(), this.multiplier, this.checkByRow);
    }

    /*
    Calculates a player's score relative to the specific RowsColumnsPublicObjectiveCard, given their window pattern
        Transposes the pattern if the check is by columns, in order to avoid code duplication
        The number of different properties of a row must be equal to the number of columns of the pattern if every
        property is different, since there is one property for each dice and one dice for each cell (row, column)

    Variables:
        numberOfValidRows: contains the number of rows that are complete and have no dice with the same property
    */
    @Override
    public int calculateScore(WindowPattern windowPattern) {
        if(windowPattern==null){ throw new IllegalArgumentException("ERROR: Cannot calculate score of" +
                " a null window pattern."); }

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
            if(rowIsIncomplete(pattern[row], numberOfColumns)) continue;

            numberOfDifferentProperties = getRowDifferentPropertiesNumber(pattern[row], numberOfColumns);

            if(numberOfDifferentProperties == numberOfColumns){
                numberOfValidRows += 1;
            }
        }

        return numberOfValidRows*multiplier;
    }

    /*
    Gets the number of different property values of a specific row
    Variables:
        propertyValue: the integer of the property that is compared
                       if it is 0, then the two dice compared have the same property
    */
    private int getRowDifferentPropertiesNumber(Cell[] row, int numberOfColumns) {
        if(row==null){ throw new IllegalArgumentException("ERROR: Cannot check a null row"); }

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
        for(int col = 0; col +1 < numberOfColumns; col++) {

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

    /*
    Returns false if every cell that forms a specified row has a dice
    */
    private boolean rowIsIncomplete(Cell[] row, int numberOfColumns) {
        if(row==null){ throw new IllegalArgumentException("ERROR: Cannot get dice of a null row."); }

        for(int col=0; col < numberOfColumns; col++){
            if(!row[col].hasDice()) return true;
        }
        return false;
    }

    /*
    Transposes a given pattern
    */
    private Cell[][] transpose(Cell[][] pattern, int rows, int columns) {
        if(pattern==null){ throw new IllegalArgumentException("ERROR: Cannot transpose a null pattern"); }

        Cell[][] transposedPattern = new Cell[columns][rows];
        for(int i=0; i < rows; i++){
            for(int j=0; j < columns; j++){
                transposedPattern[j][i] = pattern[i][j];
            }
        }
        return transposedPattern;
    }

    @Override
    public String toString(){
        String s = super.toString();
        s = s.concat("Multiplier: " + multiplier);
        s = s.concat(System.lineSeparator());
        return s;
    }
}
