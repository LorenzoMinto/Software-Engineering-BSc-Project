package it.polimi.se2018.controller;


/**
 * This is an abstract class for all Implicit Controller States, that are states
 * that execute automatically something (calling executeImplicitBehaviour) and
 * then they push controller to a new state.
 *
 * @author Federico Haag
 */
abstract class ImplicitControllerState extends ControllerState {

    /**
     * String to be printed if a method that should not be called in this state is actually called
     */
    private static final String IMPLICIT_WARNING= "This is an implicit state.";

    /**
     * Constructor of the Implicit Controller State
     *
     * @param controller the controller to which the state has to be assigned
     */
    ImplicitControllerState(Controller controller) {
        super(controller);
        this.defaultMessage = IMPLICIT_WARNING;
    }

    /**
     * Executes some implicit behaviour relative to the state. It does nothing when state is not Implicit.
     */
    @Override
    public abstract void executeImplicitBehaviour();
}
