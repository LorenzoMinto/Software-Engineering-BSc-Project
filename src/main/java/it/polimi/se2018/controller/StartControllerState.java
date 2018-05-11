package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

public class StartControllerState extends ControllerState {

    public StartControllerState(Controller controller) {
        this.controller = controller;
        this.defaultMessage = FIRST_DRAFT_DICE;
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
