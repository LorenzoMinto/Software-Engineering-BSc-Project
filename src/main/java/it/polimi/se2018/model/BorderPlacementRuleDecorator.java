package it.polimi.se2018.model;

public class BorderPlacementRuleDecorator extends PlacementRuleDecorator {

    public BorderPlacementRuleDecorator(PlacementRule decoratedPlacementRule){
        super(decoratedPlacementRule);
    }

    @Override
    public boolean checkIfMoveIsAllowed(WindowPattern windowPattern, Dice dice, int row, int col) {
        return checkBorderConstraints(windowPattern, row, col) &&
                decoratedPlacementRule.checkIfMoveIsAllowed(windowPattern, dice, row, col);
    }

    //Checks whether specified move complies with border constraints (first move)
    private boolean checkBorderConstraints(WindowPattern windowPattern, int row, int col){
        return row==0 ||
                row==windowPattern.getNumberOfRows()-1 ||
                col==0 ||
                col==windowPattern.getNumberOfColumns()-1;
    }


}
