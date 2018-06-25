package it.polimi.se2018.model;

/**
 * Abstract class defining placement rule decorator structure.
 *
 * A placement rule decorator's responsibility should be to enforce on the decoratedPlacementRule an additional
 * constraint. New rules can be composed by decorating an empty PlacementRule with the wanted decorators (and
 * corresponding constraints).
 *
 * @author Lorenzo Minto
 */
public abstract class PlacementRuleDecorator implements PlacementRule {

    /**
     * The {@link PlacementRule} to be decorated. All additional constraints will be enforced together with this
     * rule's existing constraints.
     */
    protected PlacementRule decoratedPlacementRule;

    /**
     * Class constructor specifying {@link PlacementRule} to be decorated.
     *
     * @param decoratedPlacementRule rule to be decorated.
     */
    public PlacementRuleDecorator(PlacementRule decoratedPlacementRule){
        this.decoratedPlacementRule = decoratedPlacementRule;
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
    public boolean isMoveAllowed(WindowPattern windowPattern, Dice dice, int row, int col){
        return decoratedPlacementRule.isMoveAllowed(windowPattern, dice, row, col);
    }

}
