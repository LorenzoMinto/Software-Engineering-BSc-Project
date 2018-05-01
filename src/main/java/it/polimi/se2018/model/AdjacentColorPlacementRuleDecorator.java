package it.polimi.se2018.model;

public class AdjacentColorPlacementRuleDecorator extends PlacementRuleDecorator {

    public AdjacentColorPlacementRuleDecorator(PlacementRule decoratedPlacementRule){
        super(decoratedPlacementRule);
    }

    @Override
    public boolean checkIfMoveIsAllowed(WindowPattern windowPattern, Dice dice, int row, int col) {
        return checkAdjacentColorConstraints(windowPattern, dice, row, col) && decoratedPlacementRule.checkIfMoveIsAllowed(windowPattern, dice, row, col);
    }

    //Checks whether specified move complies with color adjacency constraints
    private boolean checkAdjacentColorConstraints(WindowPattern windowPattern, Dice dice, int row, int col){
        Cell[][] pattern = windowPattern.getPattern();
        int numberOfRows = windowPattern.getNumberOfRows();
        int numberOfColumns = windowPattern.getNumberOfColumns();

        return !((row != 0 && pattern[row-1][col].hasDice() && dice.getColor() != pattern[row-1][col].getDice().getColor()) ||
                (col != numberOfColumns-1 && pattern[row][col+1].hasDice() && dice.getColor() != pattern[row][col+1].getDice().getColor()) ||
                (row != numberOfRows-1 && pattern[row+1][col].hasDice() && dice.getColor() != pattern[row+1][col].getDice().getColor()) ||
                (col != 0 && pattern[row][col-1].hasDice() && dice.getColor() != pattern[row][col-1].getDice().getColor()));
    }


}
