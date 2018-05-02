package it.polimi.se2018.model;

public class ValuePlacementRuleDecorator extends PlacementRuleDecorator {

    public ValuePlacementRuleDecorator(PlacementRule decoratedPlacementRule){
        super(decoratedPlacementRule);
    }

    @Override
    public boolean checkIfMoveIsAllowed(WindowPattern windowPattern, Dice dice, int row, int col) {
        return checkValueConstraints(windowPattern, dice, row, col) &&
                decoratedPlacementRule.checkIfMoveIsAllowed(windowPattern, dice, row, col);
    }

    //Checks whether specified move complies with value constraints
    private boolean checkValueConstraints(WindowPattern windowPattern, Dice dice, int row, int col){
        return windowPattern.getPattern()[row][col].getAllowedValue() != dice.getValue();
    }


}
