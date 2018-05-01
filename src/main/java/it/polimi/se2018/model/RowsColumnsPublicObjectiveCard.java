package it.polimi.se2018.model;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;


/*
Public Objective Card that counts the number of lines or columns in a window pattern
with all different values for a specific property (color or value)
If the line or the column is empty or not complete, it is not considered as valid for the scoring
The comparator, which compares the property of the dice in the line or column, is passed in the constructor
The boolean 'checkByRow' specifies if the card checks the property by row or by column

Attributes:
    comparator: compares two dice according to a specific rule
    multiplier: the score multiplier that is specific for each combination of the couple (property, row ||column)
    checkByRow

Methods:
    calculateScore()
    getRowDifferentPropertiesNumber()
    rowIsIncomplete()
    transpose()
*/

public class RowsColumnsPublicObjectiveCard extends PublicObjectiveCard {

    private Comparator<Dice> comparator;
    private int multiplier;
    private boolean checkByRow;

    public RowsColumnsPublicObjectiveCard(String title, String description, String imageURL, Comparator<Dice> comparator, int multiplier, boolean checkByRow) {
        super(title, description, imageURL);
        this.comparator = comparator;
        this.multiplier = multiplier;
        this.checkByRow = checkByRow;
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
            if(rowIsIncomplete(row, pattern, numberOfColumns)) continue;

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

        Set<Integer> differentProperties = new HashSet<>();
        int propertyValue;
        Dice dice1;
        Dice dice2;

        /*
        Compare each two adjacent dice
            If their properties are different add the property of the first one to the set of different properties
            Otherwise return 0 since there are at least two same dice on the row
        */
        for(int col = 0; col+1 < numberOfColumns; col++) {

            dice1 = row[col].getDice();
            dice2 = row[col+1].getDice();

            propertyValue = comparator.compare(dice1,dice2);

            if (propertyValue == 0) {
                return 0;
            }else {
                differentProperties.add(propertyValue);
            }
        }

        /*
        Set dice1 to the last dice and compare it to the second to last dice
        The comparison will be positive as it was already done in the for loop
        The purpose of the comparison is to get the property of the last dice and add it to
        the set of different properties
        */
        dice1 = row[numberOfColumns-1].getDice();
        dice2 = row[numberOfColumns-2].getDice();
        differentProperties.add(comparator.compare(dice1,dice2));

        return differentProperties.size();
    }

    /*
    Returns true if every cell that forms a specified row has a dice
    */
    private boolean rowIsIncomplete(int row, Cell[][] pattern, int numberOfColumns) {
        for(int col=0; col < numberOfColumns; col++){
            if(!pattern[row][col].hasDice()) return true;
        }
        return false;
    }

    /*
    Transposes a given pattern
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
}
