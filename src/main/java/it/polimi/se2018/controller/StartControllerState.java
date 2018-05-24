package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.message.CVMessage;
import it.polimi.se2018.utils.message.Message;

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
        if (controller==null) { throw new IllegalArgumentException("Can't create a State Controller without a Controller");}
        this.controller = controller;
        this.defaultMessage = NO_DICE_DRAFTED;
    }

    @Override
    public Message draftDiceFromDraftPool(Dice dice) {
        Round currentRound = controller.game.getCurrentRound();

        if (currentRound.getDraftPool().draftDice(dice)) {
            currentRound.getCurrentTurn().setDraftedDice(dice);
            controller.setControllerState(controller.stateManager.getPlaceState());
            return new CVMessage("Dice drafted.");
        } else {
            return new CVMessage("Dice not in the draft pool.");
        }
    }

    @Override
    public Message useToolCard(ToolCard toolcard) {
        if (controller.canUseSpecificToolCard(toolcard)) {
            controller.setActiveToolCard(toolcard);
            controller.stateManager.getNextState(this);
        } else {
            return new CVMessage("Can't use this toolcard.");
        }
        return new CVMessage("Toolcard activated.");
    }
}
