package it.polimi.se2018.controller;

/**
 * This is an implicit state that ends automatically the effect of the current used toolcard.
 * It changes the state to the next one depending on hasDraftedAndPlaced() value.
 * @author Lorenzo Minto
 */
public class EndToolCardEffectControllerState extends ImplicitControllerState {

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public EndToolCardEffectControllerState(Controller controller) {
        super(controller);
    }

    /**
     * Resets the active tool card and the placement rules to default.
     */
    @Override
    public void executeImplicitBehaviour() {
        this.controller.resetActiveToolCard();
        if (controller.game.getCurrentRound().getCurrentTurn().hasDraftedAndPlaced()) {
            controller.setControllerState(controller.stateManager.getEndControllerState());
        } else {
            controller.setControllerState(controller.stateManager.getDraftControllerState());
        }
    }
}
