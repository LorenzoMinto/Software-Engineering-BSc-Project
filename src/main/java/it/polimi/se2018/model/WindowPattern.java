package it.polimi.se2018.model;

import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.ValueOutOfBoundsException;
import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.ViewBoundMessageType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing Window Pattern.
 *
 * @author Federico Haag
 */
public class WindowPattern extends Observable implements Serializable{

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 2377367135661621968L;

    /**
     * String passed as message of ValueOutOfBoundsException when it is asked to move a dice from or to an illegal position
     */
    private static final String DICE_IN_ILLEGAL_POSITION_ERROR = "Can't move the dice from or to an illegal position.";

    /**
     * String passed as message of IllegalArgumentException when it is asked to create a windowPattern giving
     * a null pattern
     */
    private static final String NULL_PATTERN = "Can't create a window pattern with null pattern.";

    /**
     * String passed as message of IllegalArgumentException when it is asked to put a null dice on cell
     */
    private static final String NULL_DICE_ON_CELL = "Asked to put a null dice on cell.";

    /**
     * Part of the toString representation of the windowPattern. Contains content shown before difficulty
     */
    private static final String PRE_DIFFICULTY = "Difficulty: ";

    /**
     * Part of the toString representation of the windowPattern. Contains content shown before difficulty
     */
    private static final String POST_DIFFICULTY = "";

    /**
     * Window Pattern ID
     */
    private String id;

    /**
     * Name of the pattern
     */
    private String title;

    /**
     * URL of the specific WindowPattern
     */
    private String imageURL;

    /**
     * Difficulty of the pattern
     */
    private int difficulty;

    /**
     * Cells that form the pattern
     */
    private Cell[][] pattern;

    /**
     * Boolean value stating if the pattern is empty or not (true=empty)
     */
    private boolean isEmpty;

    /**
     * The player that owns the window pattern
     */
    private Player owner;

    /**
     * Constructor of a new Window Pattern
     *
     * @param id the id of the pattern
     * @param title the title of the pattern
     * @param imageURL the imageURL of the pattern
     * @param difficulty the difficulty of the pattern
     * @param pattern the pattern structured as array of cells with already set constraints
     */
    public WindowPattern(String id, String title, String imageURL ,int difficulty, Cell[][] pattern) {
        if(pattern==null) throw new IllegalArgumentException(NULL_PATTERN);

        this.id = id;
        this.title = title;
        this.imageURL = imageURL;
        this.difficulty = difficulty;
        this.pattern = pattern;
        this.isEmpty = true;
    }

    /**
     * Sets the window pattern's owner
     *
     * @param player the window pattern's owner
     */
    public void setOwner(Player player){
        if(player.getWindowPattern().equals(this)){
            this.owner = player;
        }
    }

    /**
     * Returns the pattern difficulty.
     *
     * @return  the pattern difficulty
     */
    public int getDifficulty(){
        return this.difficulty;
    }

    /**
     * Returns the number of rows of the pattern.
     *
     * @return the number of rows of the pattern
     */
    public int getNumberOfRows(){
        return pattern.length;
    }

    /**
     * Returns the number of columns of the pattern.
     *
     * @return the number of columns of the pattern
     */
    public int getNumberOfColumns(){
        return pattern[0].length;
    }

    /**
     * Returns the id of the pattern.
     *
     * @return id of the pattern
     */
    public String getID() {
        return id;
    }

    /**
     * Returns the title of the pattern.
     *
     * @return the title of the pattern
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the URL of the pattern
     *
     * @return the URL of the pattern
     */
    public String getImageURL() {
        return imageURL;
    }


    /**
     * Returns the cells that form the pattern.
     *
     * @return the cells that form the pattern
     */
    public Cell[][] getPattern() {
        int nRows = getNumberOfRows();
        int nCols = getNumberOfColumns();

        Cell[][] p = new Cell[nRows][nCols];
        for (int i=0; i<nRows; i++){
            for (int j=0; j<nCols; j++){
                p[i][j] = this.pattern[i][j].copy();
            }
        }

        return p;
    }

    /**
     * Returns the Dice that is placed on the cell corresponding to the given row and column numbers.
     *
     * @param row row number of the patter corresponding to the cell where to get the dice.
     * @param col column number of the patter corresponding to the cell where to get the dice.
     * @return the Dice that is placed on the cell corresponding to the given row and column numbers.
     */
    public Dice getDiceOnCell(int row, int col) {
        if (isIllegalPosition(row, col)){
            throw new ValueOutOfBoundsException(DICE_IN_ILLEGAL_POSITION_ERROR);}

        Dice dice = null;

        if (pattern[row][col].hasDice()) {
            dice = pattern[row][col].getDice();
        }

        return dice;
    }

    /**
     * Returns the Dice that is placed on the cell corresponding to the given row and column numbers after having removed
     * it.
     *
     * @param row row number of the patter corresponding to the cell where to remove the dice.
     * @param col column number of the patter corresponding to the cell where to remove the dice.
     * @return the removed Dice that was placed on the cell corresponding to the given row and column numbers.
     */
    public Dice removeDiceFromCell(int row, int col) {
        if (isIllegalPosition(row, col)){
            throw new ValueOutOfBoundsException(DICE_IN_ILLEGAL_POSITION_ERROR);}

        Dice dice = null;

        if (pattern[row][col].hasDice()) {
            dice = pattern[row][col].getDice();
            pattern[row][col].removeDice();
        }

        return dice;
    }

    /**
     * Put a dice in the cell corresponding to the given row and column numbers.
     *
     * @param dice Dice to put in the given cell
     * @param row row number of the pattern corresponding the cell where to put the dice
     * @param col column number of the pattern corresponding the cell where to put the dice
     * @return a boolean value representing if the action succeeded
     */
    public boolean putDiceOnCell(Dice dice, int row, int col) {

        if(dice==null) throw new IllegalArgumentException(NULL_DICE_ON_CELL);

        //Checks if location row,col is correct
        if(isIllegalPosition(row, col)){
            throw new ValueOutOfBoundsException(DICE_IN_ILLEGAL_POSITION_ERROR);
        }

        if (isThereADice(row, col)) {
            return false;
        }

        this.pattern[row][col].setDice(dice);
        isEmpty = false;

        notifyGame();

        return true;
    }

    /**
     * Returns trues if there is a dice at the row and col queried
     *
     * @param row the row in the window pattern
     * @param col the col in the window pattern
     * @return boolean
     */
    public boolean isThereADice(int row,int col) {
        //Checks if no dice is present on the specified cell
        return this.pattern[row][col].hasDice();
    }

    /**
     * Move the dice placed on the specified row,column to the specified new row,column position
     *
     * @param fromRow row number representing the cell where to pick the dice
     * @param fromCol column number representing the cell where to pick the dice
     * @param toRow row number representing the cell where to place the picked dice
     * @param toCol column number representing the cell where to place the picked dice
     * @return a boolean value representing if the action succeeded
     */
    public boolean moveDiceFromCellToCell(int fromRow, int fromCol, int toRow, int toCol) {
        //check if positions are legal
        if (isIllegalPosition(fromRow, fromCol) || isIllegalPosition(toRow, toCol)) {
            throw new ValueOutOfBoundsException(DICE_IN_ILLEGAL_POSITION_ERROR);}

        if (pattern[fromRow][fromCol].hasDice() && !pattern[toRow][toCol].hasDice()) {
            Dice removedDice = pattern[fromRow][fromCol].removeDice();
            pattern[toRow][toCol].setDice(removedDice);

            notifyGame();

            return true;
        } else {
            return false;
        }
    }


    /**
     * Makes a copy of the Window Pattern and returns it.
     *
     * @return a copy of the Window Pattern
     */
    public WindowPattern copy(){

        return new WindowPattern(this.id,this.title,this.imageURL,this.difficulty,this.getPattern());
    }

    /**
     * Checks if the given row and column numbers represent a valid
     * cell of the pattern or not.
     *
     * @param row row number to check
     * @param col column number to check
     * @return true if the position is illegal (out of bounds)
     */
    private boolean isIllegalPosition(int row, int col) {
        return (row<0 || row>=getNumberOfRows() || col<0 || col>=getNumberOfColumns());
    }

    /**
     * Rturns if the Window Pattern is empty or not (true=empty)
     *
     * @return boolean value representing if the Window Pattern is empty or not (true=empty)
     */
    public boolean isEmpty() {
        return isEmpty;
    }

    /**
     * Method to notify observers (Game) with the updated window pattern and its owner
     *
     * @author Jacopo Pio Gargano
     */
    private void notifyGame() {
        Map<String, Object> messageAttributes = new HashMap<>();

        messageAttributes.put("windowPattern", this.copy());
        messageAttributes.put("currentPlayer", owner.getID());

        notify(new Message(ViewBoundMessageType.SOMETHING_CHANGED_IN_WINDOWPATTERN, messageAttributes));
    }

    /**
     * Returns a string representation of the Window Pattern.
     *
     * @return a string representation of the Window Pattern
     */
    @Override
    public String toString() {
        String s = "{"+this.title +"}";
        s = s.concat(System.lineSeparator());
        s = s.concat(PRE_DIFFICULTY + difficulty + POST_DIFFICULTY);
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
