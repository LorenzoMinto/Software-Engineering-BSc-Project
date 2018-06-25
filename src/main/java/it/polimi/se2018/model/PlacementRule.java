package it.polimi.se2018.model;

/**
 * Interface for placement rule definition.
 *
 * Placement rules are to be composed via the decorator pattern by adding up single, independent constraints.
 *
 * @author Lorenzo Minto
 */
public interface PlacementRule {

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
    boolean isMoveAllowed(WindowPattern windowPattern, Dice dice, int row, int col);

}
