package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;

/**
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class RerollDraftedControllerState extends ImplicitControllerState {

    public RerollDraftedControllerState(Controller controller) {
        super(controller);
    }

    @Override
    public void executeImplicitBehaviour() {
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        Dice draftedDice = currentTurn.getDraftedDice();
        draftedDice.roll();
        currentTurn.setDraftedDice(draftedDice);
        controller.setControllerState(controller.stateManager.getNextState(this));
    }
}
