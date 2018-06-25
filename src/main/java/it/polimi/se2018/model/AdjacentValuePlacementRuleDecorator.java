package it.polimi.se2018.model;

/**
 * {@link PlacementRule} decorator to enforce the following rule: A dice may never be placed orthogonally adjacent to
 * a die of the same value.
 *
 * @author Lorenzo Minto
 */
public class AdjacentValuePlacementRuleDecorator extends PlacementRuleDecorator {

    /**
     * Class constructor specifying {@link PlacementRule} to be decorated.
     *
     * @param decoratedPlacementRule rule to be decorated.
     */
    public AdjacentValuePlacementRuleDecorator(PlacementRule decoratedPlacementRule){
        super(decoratedPlacementRule);
    }

    /**
     * Checks if the move specified by a given {@link Dice} and a position (row and col) is allowed on
     * the given {@link WindowPattern}.
     *
     * @param windowPattern the window pattern on which the move is played.
     * @param dice the dice to be placed.
     * @param row  the row index of the cell where the dice is to be placed.
     * @param col  the column index of the cell where the dice is to be placed.
     * @return whether or not the move is allowed.
     */
    @Override
    public boolean isMoveAllowed(WindowPattern windowPattern, Dice dice, int row, int col) {
        return checkAdjacentValueConstraints(windowPattern, dice, row, col) &&
                decoratedPlacementRule.isMoveAllowed(windowPattern, dice, row, col);
    }

    /**
     * Checks if the move specified by a given {@link Dice} and a position (row and col) complies with the value
     * adjacency constraint on the given {@link WindowPattern}.
     *
     * @param windowPattern the window pattern on which the move is played.
     * @param dice the dice to be placed.
     * @param row the row index of the cell where the dice is to be placed.
     * @param col the column index of the cell where the dice is to be placed.
     * @return whether or not the move complies with the value adjacency constraint.
     *
     * @see AdjacentValuePlacementRuleDecorator for formal rule.
     */
    private boolean checkAdjacentValueConstraints(WindowPattern windowPattern, Dice dice, int row, int col){
        Cell[][] pattern = windowPattern.getPattern();
        int numberOfRows = windowPattern.getNumberOfRows();
        int numberOfColumns = windowPattern.getNumberOfColumns();

        return !((row != 0 && pattern[row-1][col].hasDice() && dice.getValue() == pattern[row-1][col].getDice().getValue()) ||
                (col != numberOfColumns-1 && pattern[row][col+1].hasDice() && dice.getValue() == pattern[row][col+1].getDice().getValue()) ||
                (row != numberOfRows-1 && pattern[row+1][col].hasDice() && dice.getValue() == pattern[row+1][col].getDice().getValue()) ||
                (col != 0 && pattern[row][col-1].hasDice() && dice.getValue() == pattern[row][col-1].getDice().getValue()));
    }
}
