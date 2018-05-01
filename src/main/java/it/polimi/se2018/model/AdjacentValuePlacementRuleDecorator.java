package it.polimi.se2018.model;

public class AdjacentValuePlacementRuleDecorator extends PlacementRuleDecorator {

    public AdjacentValuePlacementRuleDecorator(PlacementRule decoratedPlacementRule){
        super(decoratedPlacementRule);
    }

    @Override
    public boolean checkIfMoveIsAllowed(WindowPattern windowPattern, Dice dice, int row, int col) {
        return checkAdjacentValueConstraints(windowPattern, dice, row, col) && decoratedPlacementRule.checkIfMoveIsAllowed(windowPattern, dice, row, col);
    }

    //Checks whether specified move complies with value adjacency constraints
    private boolean checkAdjacentValueConstraints(WindowPattern windowPattern, Dice dice, int row, int col){
        Cell[][] pattern = windowPattern.getPattern();
        int numberOfRows = windowPattern.getNumberOfRows();
        int numberOfColumns = windowPattern.getNumberOfColumns();

        return !((row != 0 && pattern[row-1][col].hasDice() && dice.getValue() != pattern[row-1][col].getDice().getValue()) ||
                (col != numberOfColumns-1 && pattern[row][col+1].hasDice() && dice.getValue() != pattern[row][col+1].getDice().getValue()) ||
                (row != numberOfRows-1 && pattern[row+1][col].hasDice() && dice.getValue() != pattern[row+1][col].getDice().getValue()) ||
                (col != 0 && pattern[row][col-1].hasDice() && dice.getValue() != pattern[row][col-1].getDice().getValue()));
    }


}
