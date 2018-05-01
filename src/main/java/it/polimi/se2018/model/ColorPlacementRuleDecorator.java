package it.polimi.se2018.model;

public class ColorPlacementRuleDecorator extends PlacementRuleDecorator {

    public ColorPlacementRuleDecorator(PlacementRule decoratedPlacementRule){
        super(decoratedPlacementRule);
    }

    @Override
    public boolean checkIfMoveIsAllowed(WindowPattern windowPattern, Dice dice, int row, int col) {
        return checkColorConstraints(windowPattern, dice, row, col) && decoratedPlacementRule.checkIfMoveIsAllowed(windowPattern, dice, row, col);
    }

    //Checks whether specified move complies with color constraints
    private boolean checkColorConstraints(WindowPattern windowPattern, Dice dice, int row, int col){
        return windowPattern.getPattern()[row][col].getAllowedColor() != dice.getColor();
    }


}
