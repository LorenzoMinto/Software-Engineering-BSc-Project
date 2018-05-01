package it.polimi.se2018.model;

public class DefaultPlacementRule implements PlacementRule {

    @Override
    public boolean checkIfMoveIsAllowed(WindowPattern windowPattern, Dice dice, int row, int col) {
        return true;
    }
}
