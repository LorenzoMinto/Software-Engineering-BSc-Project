package it.polimi.se2018.model;

/**
 * {@link PlacementRule} decorator to enforce the following rule: A dice must match color restriction of the space.
 *
 * @author Lorenzo Minto
 */
public class ColorPlacementRuleDecorator extends PlacementRuleDecorator {

    /**
     * Class constructor specifying {@link PlacementRule} to be decorated.
     *
     * @param decoratedPlacementRule rule to be decorated.
     */
    public ColorPlacementRuleDecorator(PlacementRule decoratedPlacementRule){
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
        return checkColorConstraints(windowPattern, dice, row, col) &&
                decoratedPlacementRule.isMoveAllowed(windowPattern, dice, row, col);
    }


    /**
     * Checks if the move specified by a given {@link Dice} and a position (row and col) matches the color
     * restrictions of the specified cell on the given {@link WindowPattern}.
     *
     * @param windowPattern the window pattern on which the move is played.
     * @param row the row index of the cell where the dice is to be placed.
     * @param col the column index of the cell where the dice is to be placed.
     * @return whether or not the dice to be placed matches the color restriction of the specified cell.
     *
     * @see ColorPlacementRuleDecorator for formal rule.
     */
    private boolean checkColorConstraints(WindowPattern windowPattern, Dice dice, int row, int col){
        return windowPattern.getPattern()[row][col].getAllowedColor() == DiceColor.NOCOLOR
                || windowPattern.getPattern()[row][col].getAllowedColor() == dice.getColor();
    }
}
