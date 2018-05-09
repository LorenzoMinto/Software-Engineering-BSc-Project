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

            try {
                nextState = createStateByID(nextControllerStateID);
            } catch (ClassNotFoundException e){
                throw new RuntimeException("ClassNotFoundException thrown, caught but cannot be handled.");
            }
            stateTable.put(nextControllerStateID, nextState);
        }

        return nextState;
    }

    private ControllerState createStateByID(String controllerStateID) throws ClassNotFoundException{
        try {

            //Checks that the class it is created is a subclass of ControllerState
            if( ! Class.forName(controllerStateID).isAssignableFrom(ControllerState.class) ){ throw new Exception(); }

            return (ControllerState) Class.forName(controllerStateID).getConstructor(Controller.class).newInstance(controller);

        } catch( Exception e ){
            throw new ClassNotFoundException();
        }
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
