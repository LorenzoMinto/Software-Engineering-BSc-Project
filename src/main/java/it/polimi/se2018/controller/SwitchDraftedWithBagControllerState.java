package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;

/**
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class SwitchDraftedWithBagControllerState extends ImplicitControllerState {

    public SwitchDraftedWithBagControllerState(Controller controller) {
        super(controller);
    }

    @Override
    public void executeImplicitBehaviour() {
        DiceBag diceBag = controller.diceBag;
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        controller.diceBag.addDice(currentTurn.getDraftedDice());
        currentTurn.setDraftedDice(diceBag.getDices(1).get(0));
        controller.setControllerState(controller.stateManager.getNextState(this));
    }
}
