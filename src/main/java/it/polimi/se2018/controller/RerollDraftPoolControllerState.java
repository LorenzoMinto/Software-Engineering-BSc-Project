package it.polimi.se2018.controller;

/**
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class RerollDraftPoolControllerState extends ImplicitControllerState {

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public RerollDraftPoolControllerState(Controller controller) {
        super(controller);
    }

    /**
     * Re-rolls the whole draft pool of the current round.
     */
    @Override
    public void executeImplicitBehaviour() {
        controller.game.getCurrentRound().getDraftPool().reroll();
        controller.setControllerState(controller.stateManager.getNextState(this));
    }
}
