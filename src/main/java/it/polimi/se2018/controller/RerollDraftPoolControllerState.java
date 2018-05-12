package it.polimi.se2018.controller;

/**
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
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
