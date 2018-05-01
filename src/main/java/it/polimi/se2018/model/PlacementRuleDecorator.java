package it.polimi.se2018.model;

public abstract class PlacementRuleDecorator implements PlacementRule {
    protected PlacementRule decoratedPlacementRule;

    public PlacementRuleDecorator(PlacementRule decoratedPlacementRule){
        this.decoratedPlacementRule = decoratedPlacementRule;
    }

    public boolean checkIfMoveIsAllowed(WindowPattern windowPattern, Dice dice, int row, int col){
        return decoratedPlacementRule.checkIfMoveIsAllowed(windowPattern, dice, row, col);
    }
}
