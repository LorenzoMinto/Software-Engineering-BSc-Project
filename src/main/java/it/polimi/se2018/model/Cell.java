package it.polimi.se2018.model;

public class Cell{

    //The value that a Dice must have to be putted in this Cell. '0' means every value is allowed.
    private int allowedValue;

    //The color that a Dice must have to be putted in this Cell. 'NOCOLOR' means every color is allowed.
    private DiceColors allowedColor;

    //The Dice placed on this Cell
    private Dice dice;

    public Cell() {
        this.allowedValue = 0;
        this.allowedColor = DiceColors.NOCOLOR;
        this.dice = null;
    }

    public Cell(int allowedValue, DiceColors allowedColor, Dice dice){
        this.allowedColor = allowedColor;
        this.allowedValue = allowedValue;
        this.dice = dice;
    }

    //Returns the allowed value for the dice
    public int getAllowedValue() {
        return allowedValue;
    }

    //Returns the allowed color for the dice
    public DiceColors getAllowedColor() {
        return allowedColor;
    }

    //Returns the dice in the cell
    public Dice getDice() {
        return this.dice.copy();
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    public boolean hasDice() {
        return dice == null;
    }

    public void removeDice(){
        this.dice = null;
    }

    public Cell copy(){
        return new Cell(this.allowedValue,this.allowedColor,this.dice.copy());
    }
}
