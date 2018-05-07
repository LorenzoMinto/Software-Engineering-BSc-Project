package it.polimi.se2018.model;

public class WindowPattern {

    //Name of the pattern
    private String title;

    //Difficulty of the pattern
    private int difficulty;

    //Rows of the pattern
    private int numberOfRows;

    //Columns of the pattern
    private int numberOfColumns;

    //Cells that form the pattern
    private Cell[][] pattern;

    public WindowPattern(String title, int d, int r, int c, Cell[][] p) {
        if(p==null) throw new IllegalArgumentException();

        this.title = title;
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

    //Returns the title of the pattern
    public String getTitle() {
        return title;
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

    public Dice getDiceOnCell(int row, int col) {
        Dice dice = null;
        if (isLegalPosition(row, col)) {
            dice = pattern[row][col].getDice();
        }
        return dice;
    }

    //Put a dice in the cell corresponding to the given row and column
    public boolean putDiceOnCell(Dice dice, int row, int col) {

        if(dice==null) throw new IllegalArgumentException("Asked to put a null dice on cell [I am window pattern]");

        //Checks if location row,col is correct
        if(!isLegalPosition(row, col)){
            return false;
        }

        //Checks if no dice is present on the specified cell
        if(pattern[row][col].hasDice()){
            return false;
        }

        pattern[row][col].setDice(dice);

        return true;
    }

    public boolean moveDiceFromCellToCell(int fromRow, int fromCol, int toRow, int toCol) {
        //check if positions are legal
        if (!isLegalPosition(fromRow, fromCol) || !isLegalPosition(toRow, toCol)) {
            return false;
        }
        if (pattern[fromRow][fromCol].hasDice() && !pattern[toRow][toCol].hasDice()) {
            Dice removedDice = pattern[fromRow][fromCol].removeDice();
            pattern[toRow][toCol].setDice(removedDice);
            return true;
        } else {
            return false;
        }
    }

    public WindowPattern copy(){

        return new WindowPattern(this.title,this.difficulty,this.numberOfRows,this.numberOfColumns,this.getPattern());
    }

    private boolean isLegalPosition(int row, int col) {
        if ((row>0 && row<=6) && (col>0 && col<=6)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String s = "{"+this.title +"}";
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
