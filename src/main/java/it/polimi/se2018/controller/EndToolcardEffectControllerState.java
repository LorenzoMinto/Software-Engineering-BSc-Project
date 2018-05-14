package it.polimi.se2018.controller;

/**
 *
 * @author Lorenzo Minto
 */
public class EndToolcardEffectControllerState extends ImplicitControllerState {

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public EndToolcardEffectControllerState(Controller controller) {
        super(controller);
    }

    /**
     * Resets the active tool card and the placement rules to default.
     */
    @Override
    public void executeImplicitBehaviour() {
        ControllerState nextState = controller.stateManager.getNextState(this);

        this.controller.resetActiveToolCard();
        controller.setControllerState(nextState);
    }
}
