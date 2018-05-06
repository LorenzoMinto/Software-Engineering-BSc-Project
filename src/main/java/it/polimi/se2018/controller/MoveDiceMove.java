package it.polimi.se2018.controller;

public class MoveDiceMove extends PlayerMove {
    private int rowFrom;
    private int colFrom;
    private int rowTo;
    private  int colTo;

    public MoveDiceMove(int rowFrom, int colFrom, int rowTo, int colTo) {
        if(rowFrom <= 4 && rowFrom >= 0 &&
                colFrom <= 5 && colFrom >= 0 &&
                rowTo <= 4 && rowTo >= 0 &&
                colTo <= 5 && colTo >= 0) {

            this.rowFrom = rowFrom;
            this.colFrom = colFrom;
            this.rowTo = rowTo;
            this.colTo = colTo;

        }else throw new IllegalArgumentException("ERROR: Cannot place dice from row "+rowFrom+" column " + colFrom +
                " to row " + rowTo + " column "+ colTo);
    }

    public int getRowFrom() {
        return rowFrom;
    }

    public int getColFrom() {
        return colFrom;
    }

    public int getRowTo() {
        return rowTo;
    }

    public int getColTo() {
        return colTo;
    }
}
