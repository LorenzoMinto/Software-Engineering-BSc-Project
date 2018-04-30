package it.polimi.se2018.model;



import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class RowsColumnsPublicObjectiveCard extends PublicObjectiveCard {

    private Comparator<Dice> comparator;
    private int multiplier;
    private boolean checkRows;

    public RowsColumnsPublicObjectiveCard(Comparator<Dice> comparator, int multiplier, boolean checkRows) {
        this.comparator = comparator;
        this.multiplier = multiplier;
        this.checkRows = checkRows;
    }

    @Override
    public int calculateScore(WindowPattern windowPattern) {
        int score = 0;
        int numberOfRows = windowPattern.getNumberOfRows();
        int numberOfColumns = windowPattern.getNumberOfColumns();
        Cell[][] pattern = windowPattern.getPattern();
        int comparisonResult;

        if(!checkRows){
            pattern = transpose(pattern, numberOfRows, numberOfColumns);
            numberOfRows = windowPattern.getNumberOfColumns();
            numberOfColumns = windowPattern.getNumberOfRows();
        }


        for(int i=0; i < numberOfRows; i++){
            Set<Integer> setOfDifferentDice = new HashSet<>();
            for(int j=0; j < numberOfColumns; j++) {

                if(pattern[i][j].hasDice()){

                    Dice dice1 = pattern[i][j].getDice();

                    if(j==numberOfColumns-1){
                        comparisonResult = comparator.compare(dice1,dice1);
                        setOfDifferentDice.add(comparisonResult);
                        if(setOfDifferentDice.size() == numberOfColumns){
                            score += 1;
                        }
                    }

                    else if(pattern[i][j+1].hasDice()) {

                        Dice dice2 = pattern[i][j + 1].getDice();
                        comparisonResult = comparator.compare(dice1,dice2);
                        if (comparator.compare(dice1, dice2) == 0) {
                            break;
                        } else {
                            setOfDifferentDice.add(comparisonResult);
                        }
                    }
                }
            }
        }

        return score*multiplier;
    }

    private Cell[][] transpose(Cell[][] pattern, int rows, int columns) {
        Cell[][] transposedMatrix = new Cell[columns][rows];
        for(int i=0; i < rows; i++){
            for(int j=0; j < columns; j++){
                transposedMatrix[j][i] = pattern[i][j];
            }
        }
        return transposedMatrix;
    }
}
