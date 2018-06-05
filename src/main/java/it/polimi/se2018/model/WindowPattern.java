package it.polimi.se2018.model;

import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.ValueOutOfBoundsException;
import it.polimi.se2018.utils.message.MVMessage;

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
     * In string representation of window pattern it is written the difficulty of it.
     * This constant contains the text written before the actual number.
     */
    private static final String DIFFICULTY_STRING = "Difficulty: ";
    public static final String DICE_IN_ILLEGAL_POSITION_ERROR = "Can't move the dice from or to an illegal position.";

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

    private Player owner;


    /**
     * Constructor of a new Window Pattern
     *
     * @param id the id of the pattern
     * @param title the title of the pattern
     * @param difficulty the difficulty of the pattern
     * @param pattern the pattern structured as array of cells with already set constraints
     */
    public WindowPattern(String id, String title, String imageURL ,int difficulty, Cell[][] pattern) {
        if(pattern==null) throw new IllegalArgumentException("Can't create a window pattern with null pattern.");

        this.id = id;
        this.title = title;
        this.imageURL = imageURL;
        this.difficulty = difficulty;
        this.pattern = pattern;
        this.isEmpty = true;
    }

    public void assignToPlayer(Player player){
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
        if (isPositionIllegal(row, col)){
            throw new ValueOutOfBoundsException("ERROR: Can't get dice from an illegal position.");}

        Dice dice = null;

        if (pattern[row][col].hasDice()) {
            dice = pattern[row][col].getDice();
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

        if(dice==null) throw new IllegalArgumentException("Asked to put a null dice on cell [I am window pattern]");

        //Checks if location row,col is correct
        if(isPositionIllegal(row, col)){
            throw new ValueOutOfBoundsException(DICE_IN_ILLEGAL_POSITION_ERROR);
        }

        //Checks if no dice is present on the specified cell
        if(this.pattern[row][col].hasDice()){
            return false;
        }

        this.pattern[row][col].setDice(dice);
        isEmpty = false;

        notifyGame();

        return true;
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
        if (isPositionIllegal(fromRow, fromCol) || isPositionIllegal(toRow, toCol)) {
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
    private boolean isPositionIllegal(int row, int col) {
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

        messageAttributes.put("windowPattern", this);
        messageAttributes.put("currentPlayer", owner.getID());

        notify(new MVMessage(MVMessage.types.WINDOWPATTERN, messageAttributes));
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
        s = s.concat(DIFFICULTY_STRING + difficulty);
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
