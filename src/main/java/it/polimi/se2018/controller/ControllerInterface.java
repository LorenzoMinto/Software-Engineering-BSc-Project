package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

/**
 * Interface for {@link Controller} class that can be used to prevent usage
 * of methods that are not handle***().
 *
 * Main usage is in {@link View}. The controller is passed casted with this
 * interface so the following methods are the only ones that the View can call
 * from the Controller.
 *
 * @author Lorenzo Minto
 */
public interface ControllerInterface {

    /**
     * Handles the move "Choose Window Pattern". Receives the {@link View} where just
     * happened the move and the reference to the WindowPattern choosen.
     *
     * @param windowPattern the choosen windowPattern
     * @param view the View where happened the move
     */
    void handleChooseWindowPattern(WindowPattern windowPattern, View view);

    /**
     * Handles the move "Draft Dice from Draft Pool". Receives the {@link View} where just
     * happened the move and the drafted {@link Dice}.
     *
     * @param dice the dice drafted from the DraftPool
     * @param view the View where happened the move
     */
    void handleDraftDiceFromDraftPoolMove(Dice dice, View view);

    /**
     * Handles the move "Use ToolCard". Receives the {@link View} where just
     * happened the move and the {@link ToolCard} used.
     * @param toolcard the used toolcard
     * @param view the View where happened the move
     */
    void handleUseToolCardPlayerMove(ToolCard toolcard, View view);

    /**
     * Handles the move "Place Dice". Receives the {@link View} where just
     * happened the move and the coordinates of windowpattern where to put
     * the {@link Dice}. The Dice is stored in {@link Turn#draftedDice}.
     *
     * @param row number of row of the windowpattern where to put the dice
     * @param col number of column of the windowpattern where to put the dice
     * @param view the View where happened the move
     */
    void handlePlaceDiceMove(int row, int col, View view);

    /**
     * Handles the move "Increment Dice" and "Decrement Dice". Receives the
     * {@link View} where just happened the move and a boolean value stating
     * if the action is "increment" (true) or "decrement" (false).
     *
     * @param isIncrement boolean value stating if the action is "increment" (true) or "decrement" (false).
     * @param view the View where happened the move
     */
    void handleIncrementOrDecrementMove(boolean isIncrement, View view);

    /**
     * Handles the move "Move Dice". Receives the {@link View} where just
     * happened the move and coordinates of the origin {@link Cell} and the destination
     * {@link Cell} (expressed as row and coloumn).
     *
     * @param rowFrom row of the windowpattern from which comes the Dice to be moved
     * @param colFrom column of the windowpattern from which comes the Dice to be moved
     * @param rowTo row of the windowpattern to move to the Dice
     * @param colTo column of the windowpattern to move to the Dice
     * @param view the View where happened the move
     */
    void handleMoveDiceMove(int rowFrom, int colFrom, int rowTo, int colTo, View view);

    /**
     * Handles the move "Draft Dice From Track". Receives the {@link View} where just
     * happened the move, the drafted {@link Dice}, the slot number of the track.
     *
     * @param dice the drafted {@link Dice}
     * @param slotNumber the slot number of the track
     * @param view the View where happened the move
     */
    void handleDraftDiceFromTrackMove(Dice dice, int slotNumber, View view);

    /**
     * Handles the move "Choose Dice Value".  Receives the {@link View} where just
     * happened the move and the value to which the drafted dice has to change to.
     *
     * @param value the value to which the drafted dice has to change to.
     * @param view the View where happened the move
     */
    void handleChooseDiceValueMove(int value, View view);

    /**
     * Handles the "End Move". Receives the {@link View} where just happened the move.
     *
     * @param view the View where happened the move
     */
    void handleEndMove(View view);
}
