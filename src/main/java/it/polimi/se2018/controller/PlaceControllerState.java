package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.networking.message.CVMessage;
import it.polimi.se2018.networking.message.Message;

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
    public Message placeDice(int row, int col) {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();
        WindowPattern pattern = currentTurn.getPlayer().getWindowPattern();
        //NOTE: checking for currentTurn.hasDrafted might be redundant in the context of the State pattern
        if (currentTurn.hasDrafted()) {
            if (controller.placementRule.checkIfMoveIsAllowed(pattern, currentTurn.getDraftedDice(), row, col)
                    && pattern.putDiceOnCell(currentTurn.getDraftedDice(), row, col)) {

                if (controller.getActiveToolCard() != null) {
                    controller.setControllerState(controller.stateManager.getNextState(this));
                    return new CVMessage("Dice placed!");
                } else {
                    controller.setControllerState(controller.stateManager.getToolCardState());
                    return new CVMessage("Dice placed!");
                }
            } else {
                return new CVMessage("Move is illegal. There's another dice in that position.");
            }
        } else {
            return new CVMessage("No drafted dice.");
        }

    }
}
