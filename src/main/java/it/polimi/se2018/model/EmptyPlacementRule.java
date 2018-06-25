package it.polimi.se2018.model;

/**
 * Void (empty) placement rule to be decorated.
 *
 * @author Lorenzo Minto
 */
public class EmptyPlacementRule implements PlacementRule {

    /**
     * Checks if the move specified by a given {@link Dice} and a position (row and col) is allowed on
     * the given {@link WindowPattern}. Always returns true if not decorated.
     *
     * @param windowPattern the window pattern on which the move is played.
     * @param dice the dice to be placed.
     * @param row  the row index of the cell where the dice is to be placed.
     * @param col  the column index of the cell where the dice is to be placed.
     * @return whether or not the move is allowed.
     */
    @Override
    public boolean isMoveAllowed(WindowPattern windowPattern, Dice dice, int row, int col) {
        return !windowPattern.getPattern()[row][col].hasDice();
    }
}
