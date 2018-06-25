package it.polimi.se2018.model;

/**
 * {@link PlacementRule} decorator to enforce the following rule: A dice may never be placed orthogonally adjacent to
 * a die of the same color.
 *
 * @author Lorenzo Minto
 */
public class AdjacentColorPlacementRuleDecorator extends PlacementRuleDecorator {

    /**
     * Class constructor specifying {@link PlacementRule} to be decorated.
     *
     * @param decoratedPlacementRule rule to be decorated.
     */
    public AdjacentColorPlacementRuleDecorator(PlacementRule decoratedPlacementRule){
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
        return checkAdjacentColorConstraints(windowPattern, dice, row, col) &&
                decoratedPlacementRule.isMoveAllowed(windowPattern, dice, row, col);
    }

    /**
     * Checks if the move specified by a given {@link Dice} and a position (row and col) complies with the color
     * adjacency constraint on the given {@link WindowPattern}.
     *
     * @param windowPattern the window pattern on which the move is played.
     * @param dice the dice to be placed.
     * @param row the row index of the cell where the dice is to be placed.
     * @param col the column index of the cell where the dice is to be placed.
     * @return whether or not the move complies with the color adjacency constraint.
     *
     * @see AdjacentColorPlacementRuleDecorator for formal rule.
     */
    private boolean checkAdjacentColorConstraints(WindowPattern windowPattern, Dice dice, int row, int col){
        Cell[][] pattern = windowPattern.getPattern();
        int numberOfRows = windowPattern.getNumberOfRows();
        int numberOfColumns = windowPattern.getNumberOfColumns();

        return !((row != 0 && pattern[row-1][col].hasDice() && dice.getColor() == pattern[row-1][col].getDice().getColor()) ||
                (col != numberOfColumns-1 && pattern[row][col+1].hasDice() && dice.getColor() == pattern[row][col+1].getDice().getColor()) ||
                (row != numberOfRows-1 && pattern[row+1][col].hasDice() && dice.getColor() == pattern[row+1][col].getDice().getColor()) ||
                (col != 0 && pattern[row][col-1].hasDice() && dice.getColor() == pattern[row][col-1].getDice().getColor()));
    }
}
