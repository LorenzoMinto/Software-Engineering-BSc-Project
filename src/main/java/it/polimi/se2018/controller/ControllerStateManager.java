package it.polimi.se2018.controller;

public class ControllerStateManager {

    private Controller controller = null;

    public ControllerStateManager(Controller controller) {
        this.controller = controller;
    }

    public ControllerState getNextState(ControllerState prevState){

        ControllerState nextState;

        String nextControllerStateID = controller.getActiveToolCard().nextStateID(prevState);

        return createStateByID(nextControllerStateID);
    }

    private ControllerState createStateByID(String controllerStateID){

        //TODO: create class using ID

        //NOTE: https://stackoverflow.com/questions/6094575/creating-an-instance-using-the-class-name-and-calling-constructor

        ControllerState controllerState;

        return controllerState;
    }
}
