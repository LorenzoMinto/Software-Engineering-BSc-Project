package it.polimi.se2018.controller;

public class ControllerStateManager {

    private Controller controller = null;

    StartControllerState startState;
    PlaceControllerState placeState;
    ToolCardControllerState toolCardState;
    DraftControllerState draftControllerState;

    public ControllerStateManager(Controller controller) {
        this.controller = controller;

        this.startState = new StartControllerState(this.controller);
        this.placeState = new PlaceControllerState(this.controller);
        this.toolCardState = new ToolCardControllerState(this.controller);
    }

    public ControllerState getNextState(ControllerState prevState){
        ControllerState nextState;

        String nextControllerStateID = controller.getActiveToolCard().nextStateID(prevState);
        return createStateByID(nextControllerStateID);
    }

    private ControllerState createStateByID(String controllerStateID){

        //TODO: create class using ID, keep instance in a dictionary (don't create more than one instance per state)

        //NOTE: https://stackoverflow.com/questions/6094575/creating-an-instance-using-the-class-name-and-calling-constructor

        ControllerState controllerState;

        return controllerState;
    }

    //getters

    //Getters for controller states
    public StartControllerState getStartState() {
        return startState;
    }
    public PlaceControllerState getPlaceState() {
        return placeState;
    }
    public ToolCardControllerState getToolCardState() { return toolCardState; }
    public DraftControllerState getDraftControllerState() { return draftControllerState; }
}
