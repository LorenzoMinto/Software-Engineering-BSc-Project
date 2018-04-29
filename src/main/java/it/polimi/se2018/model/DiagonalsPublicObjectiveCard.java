package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DiagonalsPublicObjectiveCard extends PublicObjectiveCard {

    private Comparator<Dice> comparator;

    public DiagonalsPublicObjectiveCard(Comparator<Dice> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int calculateScore(WindowPattern windowPattern) {
        int score = 0;

        //List of not counted dice in algorithm
        List<Integer> listOfNotCountedDice = fillListWithNumbers(windowPattern.getNumberOfRows()*windowPattern.getNumberOfColumns());

        //Checking diagonals from left to right
        score += getScoreLeftRight(windowPattern, listOfNotCountedDice);
        //Checking diagonals from right to left
        score += getScoreRightLeft(windowPattern, listOfNotCountedDice);

        return score;
    }


    //When testing, check score only for left to right diagonals
    private int getScoreLeftRight(WindowPattern windowPattern, List<Integer> listOfNotCountedDice) {
        int score = 0;
        //The algorithm covers the whole matrix starting from the bottom left corner, going upwards vertically and finally horizontally right
        for(int row = windowPattern.getNumberOfRows()-1; row >= 0; row--){
            if(row == 0){
                for(int col = 0;  col < windowPattern.getNumberOfColumns(); col++){
                    score+= scoreDiagonal(windowPattern, listOfNotCountedDice, row, col, +1);
                }
            }else{
                score+= scoreDiagonal(windowPattern, listOfNotCountedDice, row, 0, +1);
            }
        }
        return score;
    }

    //When testing, check score only for right to left diagonals
    private int getScoreRightLeft(WindowPattern windowPattern, List<Integer> listOfNotCountedDice) {
        int score = 0;
        //The algorithm covers the whole matrix starting from the bottom right corner, going upwards vertically and finally horizontally left
        for(int row = windowPattern.getNumberOfRows()-1; row >= 0; row--){
            if(row == 0){
                for(int col=windowPattern.getNumberOfColumns()-1;  col>=0; col--){
                    score+= scoreDiagonal(windowPattern, listOfNotCountedDice, row, col, -1);
                }
            }else{
                score+= scoreDiagonal(windowPattern, listOfNotCountedDice, row, windowPattern.getNumberOfColumns(), -1);
            }
        }
        return score;
    }


    /*
    Method to count the adjacent dice with the same property according to the comparator in a diagonal
    Parameters:
        row, col: indexes of the first cell of the diagonal to be checked
        move: +1 if moving from left to right, -1 if moving from right to left
    */
    private int scoreDiagonal(WindowPattern windowPattern, List<Integer> listOfNotCountedDice, int row, int col, int move){
        int score = 0;
        int linearIndex;

        //The condition inside the while statement checks if the algorithm is still inside the matrix
        while(row+1 < windowPattern.getNumberOfRows() && col+move < windowPattern.getNumberOfColumns()){

            //If there are any, get the two diagonally adjacent dice
            if(windowPattern.getPattern()[row][col].hasDice() && windowPattern.getPattern()[row+1][col+move].hasDice()){
                Dice currentDice = windowPattern.getPattern()[row][col].getDice();
                Dice adjacentDice = windowPattern.getPattern()[row+1][col+move].getDice();

                    //If the comparison is positive, then update the score and update listOfNotCountedDice in order not to repeat dice
                    if(comparator.compare(currentDice, adjacentDice) == 0 ){

                        linearIndex = getLinearIndex(row,col,windowPattern.getNumberOfRows());
                        if(listOfNotCountedDice.contains(linearIndex)){
                            score++;
                            listOfNotCountedDice.remove(linearIndex);
                        }

                        linearIndex = getLinearIndex(row+1,col+move, windowPattern.getNumberOfRows());
                        if(listOfNotCountedDice.contains(linearIndex)){
                            score++;
                            listOfNotCountedDice.remove(linearIndex);
                        }
                    }

            }
            //Continue scoring the diagonal
            row++;
            col++;
        }

        return score;
    }

    //Returns a list full of all integers from 0 to parameter 'numbers' -1
    private List<Integer> fillListWithNumbers(int numbers){
        List<Integer> list = new ArrayList<>();
        for(int i=0; i < numbers; i++){
            list.add(i);
        }
        return list;
    }

    private int getLinearIndex(int row, int col, int numberOfRows) {
        return (row * numberOfRows) + col;
    }
}