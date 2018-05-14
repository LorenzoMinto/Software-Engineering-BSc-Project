package it.polimi.se2018.controller;

/**
 * @author Lorenzo Minto
 */
public class EndControllerState extends ImplicitControllerState {

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public EndControllerState(Controller controller) {
        super(controller);
    }

    /**
     * Ends current turn and advances game.
     */
    @Override
    public void executeImplicitBehaviour() {
        controller.advanceGame();
        //advance game takes care of setting the toolcard to null and resetting the placement rule
    }
}
