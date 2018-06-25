package it.polimi.se2018.model;

/**
 * {@link PlacementRule} decorator to enforce the following rule: A dice can only be placed on an edge or corner space.
 *
 * @author Lorenzo Minto
 */
public class BorderPlacementRuleDecorator extends PlacementRuleDecorator {

    /**
     * Class constructor specifying {@link PlacementRule} to be decorated.
     *
     * @param decoratedPlacementRule rule to be decorated.
     */
    public BorderPlacementRuleDecorator(PlacementRule decoratedPlacementRule){
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
        return checkBorderConstraints(windowPattern, row, col) &&
                decoratedPlacementRule.isMoveAllowed(windowPattern, dice, row, col);
    }


    /**
     * Checks if the move specified by a given {@link Dice} and a position (row and col) is either on the border
     * or corner of the given {@link WindowPattern}.
     *
     * @param windowPattern the window pattern on which the move is played.
     * @param row the row index of the cell where the dice is to be placed.
     * @param col the column index of the cell where the dice is to be placed.
     * @return whether or not the move is on the border or corner of the window pattern.
     *
     * @see BorderPlacementRuleDecorator for formal rule.
     */
    private boolean checkBorderConstraints(WindowPattern windowPattern, int row, int col){
        return row==0 ||
                row==windowPattern.getNumberOfRows()-1 ||
                col==0 ||
                col==windowPattern.getNumberOfColumns()-1;
    }
}
