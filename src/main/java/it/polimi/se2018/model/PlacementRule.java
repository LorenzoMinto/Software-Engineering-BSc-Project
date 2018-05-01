package it.polimi.se2018.model;

public interface PlacementRule {
    boolean checkIfMoveIsAllowed(WindowPattern windowPattern, Dice dice, int row, int col);
}
