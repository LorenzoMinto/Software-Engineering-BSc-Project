package it.polimi.se2018.controller;

class ImplicitControllerState extends ControllerState {

    private static final String IMPLICIT_WARNING= "This is an implicit state.";

    ImplicitControllerState(Controller controller) {
        this.controller = controller;
        this.defaultMessage = IMPLICIT_WARNING;
    }
}
