package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

public interface ControllerInterface {

    void handleDraftDiceFromDraftPoolMove(Dice dice, View view);

    void handleUseToolCardPlayerMove(ToolCard toolcard, View view);

    void handlePlaceDiceMove(int row, int col, View view);

    void handleIncrementOrDecrementMove(boolean isIncrement, View view);

    void handleMoveDiceMove(int rowFrom, int colFrom, int rowTo, int colTo, View view);

    void handleDraftDiceFromTrackMove(Dice dice, int slotNumber, View view);

    void handleChooseDiceValueMove(int value, View view);

    void handleEndMove(View view);
}
