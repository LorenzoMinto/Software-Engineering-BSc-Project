package it.polimi.se2018.model;

/**
 * {@link PlacementRule} decorator to enforce the following rule: A dice must be of the argument specified in the
 * constructor.
 *
 * @author Lorenzo Minto
 */
public class ColorRestrictionPlacementRuleDecorator extends PlacementRuleDecorator {
    DiceColor color;

    /**
     * Class constructor specifying {@link PlacementRule} to be decorated.
     *
     * @param decoratedPlacementRule rule to be decorated.
     * @param color the restriction's color
     */
    public ColorRestrictionPlacementRuleDecorator(PlacementRule decoratedPlacementRule, DiceColor color){
        super(decoratedPlacementRule);
        this.color = color;
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
        return dice.getColor() == this.color &&
                decoratedPlacementRule.isMoveAllowed(windowPattern, dice, row, col);
    }

}
