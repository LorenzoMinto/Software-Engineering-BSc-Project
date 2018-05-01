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

    public Cell(int allowedValue, DiceColors allowedColor){
        this.allowedColor = allowedColor;
        this.allowedValue = allowedValue;
        this.dice = null;
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
        if(dice==null) throw new IllegalArgumentException("Asked to set a null dice on a cell [I am the cell]");
        this.dice = dice;
    }

    public boolean hasDice() {
        return dice != null;
    }

    public Dice removeDice(){
        Dice d = this.dice;
        this.dice = null;
        return d;
    }

    public Cell copy(){
        Cell c = new Cell(this.allowedValue,this.allowedColor);
        if(this.hasDice()) c.setDice(this.dice.copy());
        return c;
    }

    @Override
    public String toString() {
        if(this.dice == null) {
            return "(" + allowedValue + ":" + allowedColor + ")";
        }else{
            return "(" + dice + ")";
        }
    }
}
