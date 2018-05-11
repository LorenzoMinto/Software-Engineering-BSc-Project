package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

public class RerollDraftPoolControllerState extends ImplicitControllerState {

    public RerollDraftPoolControllerState(Controller controller) {
        super(controller);
    }

    @Override
    public void executeImplicitBehaviour() {
        controller.game.getCurrentRound().getDraftPool().reroll();
        controller.setControllerState(controller.stateManager.getNextState(this));
    }
}
