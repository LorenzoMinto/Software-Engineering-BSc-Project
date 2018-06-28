package it.polimi.se2018.model;

/**
 * {@link PlacementRule} decorator to enforce the following rule: a dice must be adjacent to a previously
 * placed die, orthogonally or diagonally.
 *
 * @author Lorenzo Minto
 */
public class AdjacentDicePlacementRuleDecorator extends PlacementRuleDecorator {

    /**
     * Class constructor specifying {@link PlacementRule} to be decorated.
     *
     * @param decoratedPlacementRule rule to be decorated.
     */
    public AdjacentDicePlacementRuleDecorator(PlacementRule decoratedPlacementRule){
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
        boolean adjacency = checkAdjacentDiceConstraints(windowPattern, row, col);
        return  adjacency &&
                decoratedPlacementRule.isMoveAllowed(windowPattern, dice, row, col);
    }


    /**
     * Checks if the move specified by a given {@link Dice} and a position (row and col) complies with the dice
     * adjacency constraint on the given {@link WindowPattern}.
     *
     * @param windowPattern the window pattern on which the move is played.
     * @param row the row index of the cell where the dice is to be placed.
     * @param col the column index of the cell where the dice is to be placed.
     * @return whether or not the move complies with the dice adjacency constraint
     *
     * @see AdjacentColorPlacementRuleDecorator for formal rule.
     */
    private boolean checkAdjacentDiceConstraints(WindowPattern windowPattern, int row, int col){
        if (windowPattern.isEmpty()) {
            return true;
        }
        Cell[][] pattern = windowPattern.getPattern();
        int numberOfRows = windowPattern.getNumberOfRows();
        int numberOfColumns = windowPattern.getNumberOfColumns();

        return checkAdjacentCells(pattern, numberOfRows, numberOfColumns, row, col) ||
                checkDiagonallyAdjacentCells(pattern, numberOfRows, numberOfColumns, row, col);
    }

    /**
     * Returns true if there's a dice in the cardinally adjacent cells
     *
     * @param pattern the cell pattern
     * @param numberOfRows the number of rows in the pattern
     * @param numberOfColumns the number of cols in the pattern
     * @param row the row index of the cell
     * @param col the col index of the cell
     * @return true if there's a dice in the cardinally adjacent cells, false otherwise
     */
    private boolean checkAdjacentCells(Cell[][] pattern, int numberOfRows, int numberOfColumns, int row, int col) {
        return (row != 0 && pattern[row-1][col].hasDice()) ||
                (col != numberOfColumns-1 && pattern[row][col+1].hasDice()) ||
                (row != numberOfRows-1 && pattern[row+1][col].hasDice()) ||
                (col != 0 && pattern[row][col-1].hasDice());

    }

    /**
     * Returns true if there's a dice in the diagonally adjacent cells
     *
     * @param pattern the cell pattern
     * @param numberOfRows the number of rows in the pattern
     * @param numberOfColumns the number of cols in the pattern
     * @param row the row index of the cell
     * @param col the col index of the cell
     * @return true if there's a dice in the diagonally adjacent cells, false otherwise
     */
    private boolean checkDiagonallyAdjacentCells(Cell[][] pattern, int numberOfRows, int numberOfColumns, int row, int col) {
        return (row!=0 && col!=0 && pattern[row-1][col-1].hasDice()) ||
                (row!=0 && col!=numberOfColumns-1 && pattern[row-1][col+1].hasDice()) ||
                (row!=numberOfRows-1 && col!=0 && pattern[row+1][col-1].hasDice()) ||
                (row!=numberOfRows-1 && col!=numberOfColumns-1 && pattern[row+1][col+1].hasDice());
    }
}
