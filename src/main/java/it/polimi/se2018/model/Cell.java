package it.polimi.se2018.model;

/**
 * Represent a cell of a {@link WindowPattern}. May contains a {@link Dice}.
 *
 * @author Federico Haag
 * @see Dice
 * @see WindowPattern
 */
public class Cell{


    /**
     * The value that a Dice must have to be putted in this Cell. '0' means every value is allowed.
     */
    private int allowedValue;


    /**
     * The color that a Dice must have to be putted in this Cell. 'NOCOLOR' means every color is allowed.
     */
    private DiceColors allowedColor;


    /**
     * The Dice placed on this Cell
     */
    private Dice dice;

    /**
     * Constructor for Cell class without specifying value and color constraints.
     * No constraints are inserted.
     */
    public Cell() {
        this(0, DiceColors.NOCOLOR);
    }

    /**
     * Constructor for Cell specifying value and color constraints.
     *
     * @param allowedValue Dice value constraint
     * @param allowedColor Dice color constraint
     */
    public Cell(int allowedValue, DiceColors allowedColor){
        this.allowedColor = allowedColor;
        this.allowedValue = allowedValue;
        this.dice = null;
    }


    /**
     * Returns the allowed value for the dice.
     *
     * @return the allowed value for the dice
     */
    public int getAllowedValue() {
        return allowedValue;
    }


    /**
     * Returns the allowed color for the dice.
     *
     * @return the allowed color for the dice
     */
    public DiceColors getAllowedColor() {
        return allowedColor;
    }


    /**
     * Returns the dice in the cell
     *
     * @return the dice in the cell
     */
    public Dice getDice() {
        return this.dice.copy();
    }

    /**
     * Sets the given {@link Dice} to the cell.
     *
     * @param dice the dice that must be putted on the cell
     */
    public void setDice(Dice dice) {
        if(dice==null){ throw new IllegalArgumentException("Asked to set a null dice on a cell [I am the cell]"); }
        this.dice = dice;
    }

    /**
     * Returns if the cell has a Dice placed on it.
     *
     * @return if the cell has a Dice placed on it
     */
    public boolean hasDice() {
        return dice != null;
    }

    /**
     * Removes from the Cell the Dice and returns it.     *
     *
     * @return the Dice that was placed on the Cell. If no Dice was placed, null is returned.
     */
    public Dice removeDice(){
        Dice d = this.dice;
        this.dice = null;
        return d;
    }

    /**
     * Makes a copy of the cell.
     *
     * @return a copy of the cell
     */
    public Cell copy(){
        Cell c = new Cell(this.allowedValue,this.allowedColor);
        if(this.hasDice()) { c.setDice(this.dice.copy()); }
        return c;
    }

    /**
     * Returns the String representation of the Cell.
     *
     * @return the String representation of the Cell
     */
    @Override
    public String toString() {
        if(this.dice == null) {
            return "(" + allowedValue + ":" + allowedColor.toOneLetter() + ")";
        }else{
            return "(" + dice + ")";
        }
    }
}
