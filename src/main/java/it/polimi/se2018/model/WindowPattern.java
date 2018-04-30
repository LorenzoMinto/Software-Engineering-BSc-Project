package it.polimi.se2018.model;

public class WindowPattern {

    //Name of the pattern
    private String name;

    //Difficulty of the pattern
    private int difficulty;

    //Rows of the pattern
    private int numberOfRows;

    //Columns of the pattern
    private int numberOfColumns;

    //Cells that form the pattern
    private Cell[][] pattern;

    public WindowPattern(String name, int d, int r, int c, Cell[][] p) {
        this.name = name;
        this.difficulty = d;
        this.numberOfRows = r;
        this.numberOfColumns = c;
        this.pattern = p;
    }

    //Returns the pattern difficulty
    public int getDifficulty(){
        return this.difficulty;
    }

    //Returns the number of rows of the pattern
    public int getNumberOfRows(){
        return this.numberOfRows;
    }

    //Returns the number of columns of the pattern
    public int getNumberOfColumns(){
        return this.numberOfColumns;
    }

    //Returns the name of the pattern
    public String getName() {
        return name;
    }

    //Returns the cells that form the pattern
    public Cell[][] getPattern() {

        Cell[][] p = new Cell[this.numberOfRows][this.numberOfColumns];
        for (int i=0; i<this.numberOfRows; i++){
            for (int j=0; j<this.numberOfColumns; j++){
                p[i][j] = this.pattern[i][j].copy();
            }
        }

        return p;
    }

    //Put a dice in the cell corresponding to the given row and column
    public boolean putDiceOnCell(Dice dice, int row, int col) {

        //Checks if location row,col is correct
        if(row>numberOfRows || col > numberOfColumns){
            return false;
        }

        //Checks if no dice is present on the specified cell
        if(pattern[row][col].hasDice()){
            return false;
        }

        pattern[row][col].setDice(dice);
        return true;
    }

    public WindowPattern copy(){

        return new WindowPattern(this.name,this.difficulty,this.numberOfRows,this.numberOfColumns,this.getPattern());
    }

    @Override
    public String toString() {
        String s = "{"+this.name+"}";
        s = s.concat(System.lineSeparator());

        for(Cell[] cellsRow : this.pattern){
            for(Cell cell : cellsRow){
                s = s.concat( cell.toString() );
            }
            s = s.concat(System.lineSeparator());
        }

        return s;
    }
}
