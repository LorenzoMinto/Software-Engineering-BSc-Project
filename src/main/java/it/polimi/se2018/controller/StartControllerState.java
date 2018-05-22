package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.networking.message.ControllerMessage;
import it.polimi.se2018.networking.message.Message;

/**
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class StartControllerState extends ControllerState {

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public StartControllerState(Controller controller) {
        this.controller = controller;
        this.defaultMessage = NO_DICE_DRAFTED;
    }

    @Override
    public Message draftDiceFromDraftPool(Dice dice) {
        Round currentRound = controller.game.getCurrentRound();

        if (currentRound.getDraftPool().draftDice(dice)) {
            currentRound.getCurrentTurn().setDraftedDice(dice);
        }
        controller.setControllerState(controller.stateManager.getPlaceState());
        return new ControllerMessage("Dice drafted.");
    }

    @Override
    public Message useToolCard(Player player, ToolCard toolcard) {
        if (controller.canUseSpecificToolCard(toolcard)) {
            controller.setActiveToolCard(toolcard);
            controller.stateManager.getNextState(this);
        } else {
            return new ControllerMessage("Can't use this toolcard.");
        }
        return new ControllerMessage("Toolcard activated.");
    }
}
