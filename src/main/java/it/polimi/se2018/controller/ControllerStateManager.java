package it.polimi.se2018.controller;

import java.util.HashMap;

public class ControllerStateManager {

    private Controller controller = null;

    private HashMap<String, ControllerState> stateTable;

    StartControllerState startState;
    PlaceControllerState placeState;
    ToolCardControllerState toolCardState;
    DraftControllerState draftControllerState;

    public ControllerStateManager(Controller controller) {
        this.controller = controller;
        this.stateTable = new HashMap<String, ControllerState>();

        this.startState = new StartControllerState(this.controller);
        this.placeState = new PlaceControllerState(this.controller);
        this.toolCardState = new ToolCardControllerState(this.controller);
    }

    public ControllerState getNextState(ControllerState prevState){
        ControllerState nextState;

        String nextControllerStateID = controller.getActiveToolCard().nextStateID(prevState);

        if (stateTable.containsKey(nextControllerStateID)) {
            nextState = stateTable.get(nextControllerStateID);
        } else {
            //NOTE: should an exception be thrown if specified state does not exist?
            nextState = createStateByID(nextControllerStateID);
            stateTable.put(nextControllerStateID, nextState);
        }

        return nextState;
    }

    private ControllerState createStateByID(String controllerStateID){

        //TODO: https://stackoverflow.com/questions/6094575/creating-an-instance-using-the-class-name-and-calling-constructor

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
