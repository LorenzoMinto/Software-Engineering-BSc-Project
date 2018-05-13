package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

/**
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class PlaceControllerState extends ControllerState {

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public PlaceControllerState(Controller controller) {
        this.controller = controller;
        this.defaultMessage = PLACE_DICE;
    }

    @Override
    public void placeDice(int row, int col, View view) {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();
        WindowPattern pattern = currentTurn.getPlayer().getWindowPattern();
        //NOTE: checking for currentTurn.hasDrafted might be redundant in the context of the State pattern
        if (currentTurn.hasDrafted()) {
            if (controller.placementRule.checkIfMoveIsAllowed(pattern, currentTurn.getDraftedDice(), row, col)
                    && pattern.putDiceOnCell(currentTurn.getDraftedDice(), row, col)) {
                view.showMessage("Dice placed!");

                if (controller.getActiveToolCard() != null) {
                    controller.setControllerState(controller.stateManager.getNextState(this));
                } else {
                    controller.setControllerState(controller.stateManager.getToolCardState());
                }
            } else {
                view.showMessage("Move is illegal. There's another dice in that position.");
            }
        } else {
            view.showMessage("No drafted dice.");
        }

    }
}
