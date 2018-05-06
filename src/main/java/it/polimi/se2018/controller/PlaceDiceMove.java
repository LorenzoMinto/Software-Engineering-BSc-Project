package it.polimi.se2018.controller;

public class PlaceDiceMove extends PlayerMove {
    private int row;
    private int column;

    public PlaceDiceMove(int row, int column) {
        if(row <= 4 && row >= 0 && column<=5 && column>=0) {
            this.row = row;
            this.column = column;
        }else throw new IllegalArgumentException("ERROR: Cannot place dice on row "+row+" column " + column);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
