package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

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
    public void draftDiceFromDraftPool(Dice dice, View view) {
        Round currentRound = controller.game.getCurrentRound();

        if (currentRound.getDraftPool().draftDice(dice)) {
            currentRound.getCurrentTurn().setDraftedDice(dice);
        }
        controller.setControllerState(controller.stateManager.getPlaceState());
    }

    @Override
    public void useToolCard(Player player, ToolCard toolcard, View view) {
        if (controller.canUseSpecificToolCard(toolcard)) {
            controller.setActiveToolCard(toolcard);
            controller.stateManager.getNextState(this);
        } else {
            view.showMessage("Can't use this toolcard.");
        }
    }
}
