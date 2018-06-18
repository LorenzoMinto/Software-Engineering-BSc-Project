package it.polimi.se2018.controller;

/**
 *  This is the state during which, due to a toolcard, current player can reroll all dices in draftpool.
 *  The implicit behaviour is rolling of dices: in fact no input from user is needed.
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
        controller.game.getCurrentRound().getDraftPool().reRoll();
        controller.setControllerState(controller.stateManager.getNextState(this));
    }
}
