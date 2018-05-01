package it.polimi.se2018.model;

public class AdjacentDicePlacementRuleDecorator extends PlacementRuleDecorator {
    private boolean invert;

    public AdjacentDicePlacementRuleDecorator(PlacementRule decoratedPlacementRule, boolean invert){
        super(decoratedPlacementRule);
        this.invert = invert;
    }

    @Override
    public boolean checkIfMoveIsAllowed(WindowPattern windowPattern, Dice dice, int row, int col) {
        boolean adjacency = checkAdjacentDiceConstraints(windowPattern, row, col);
        return  invert ? !adjacency : adjacency && decoratedPlacementRule.checkIfMoveIsAllowed(windowPattern, dice, row, col);
    }

    //Checks whether specified move complies with dice adjacency constraints
    private boolean checkAdjacentDiceConstraints(WindowPattern windowPattern, int row, int col){
        Cell[][] pattern = windowPattern.getPattern();
        int numberOfRows = windowPattern.getNumberOfRows();
        int numberOfColumns = windowPattern.getNumberOfColumns();

        return (row != 0 && pattern[row-1][col].hasDice()) ||
                (row!=0 && col!=0 && pattern[row-1][col-1].hasDice()) ||
                (col != numberOfColumns-1 && pattern[row][col+1].hasDice()) ||
                (row!=0 && col!=numberOfColumns-1 && pattern[row-1][col+1].hasDice()) ||
                (row != numberOfRows-1 && pattern[row+1][col].hasDice()) ||
                (row!=numberOfRows-1 && col!=0 && pattern[row+1][col-1].hasDice()) ||
                (col != 0 && pattern[row][col-1].hasDice()) ||
                (row!=numberOfRows-1 && col!=numberOfColumns-1 && pattern[row+1][col+1].hasDice());

    }


}
