package it.polimi.se2018.controller;

import it.polimi.se2018.utils.BadBehaviourRuntimeException;

import java.util.HashMap;

/**
 * Manages the creation of Controller's States
 *
 * @author Lorenzo Minto
 */
public class ControllerStateManager {

    /**
     * Controller to which is added the state
     */
    private Controller controller = null;

    /**
     * Table containing all the previously created states
     * in order to avoid creating more than once the same state.
     * If it is requested a state that was already created, it
     * is returned that state instead of creating a new one
     */
    private HashMap<String, ControllerState> stateTable;

    //TODO: check if this variables are needed
    StartControllerState startState;
    PlaceControllerState placeState;
    ToolCardControllerState toolCardState;
    DraftControllerState draftControllerState;
    EndToolCardEffectControllerState endToolCardEffectControllerState;

    /**
     * Constructor for the Controller State Manager. Each controller instance
     * has a specific controller state manager, so it is required as a param
     * the controller instance that will then receive the controller states
     * instances when asking for them
     *
     * @param controller the controller to which are added the states
     */
    public ControllerStateManager(Controller controller) {
        this.controller = controller;
        this.stateTable = new HashMap<>();

        //TODO: checks if are really needed this variables:
        this.startState = new StartControllerState(this.controller);
        this.placeState = new PlaceControllerState(this.controller);
        this.toolCardState = new ToolCardControllerState(this.controller);
        this.endToolCardEffectControllerState = new EndToolCardEffectControllerState(this.controller);
    }

    /**
     * Based on the active toolCard, decides what is the new state
     * that the connected controller should take
     *
     * @param prevState the previous controller state
     * @return the next controller state based on the active toolcard
     */
    public ControllerState getNextState(ControllerState prevState){
        ControllerState nextState;

        String nextControllerStateID = controller.getActiveToolCard().nextStateID(prevState);

        if (stateTable.containsKey(nextControllerStateID)) {
            nextState = stateTable.get(nextControllerStateID);
        } else {

            nextState = createStateByID(nextControllerStateID);

            stateTable.put(nextControllerStateID, nextState);
        }

        return nextState;
    }

    /**
     * Creates a {@link ControllerState} based on its ID String passed as an argument
     *
     * @param controllerStateID the ID String representing a specific {@link ControllerState}.
     *                          It is usually the name of the class.
     * @return the {@link ControllerState} represented by the given ID
     * @throws ClassNotFoundException if the given ID does not correspond to a subclass of {@link ControllerState}.
     * @author Federico Haag
     */
    private ControllerState createStateByID(String controllerStateID){
        try {

            //Checks that the class it is created is a subclass of ControllerState
            if( ! Class.forName(controllerStateID).isAssignableFrom(ControllerState.class) ){
                throw new BadBehaviourRuntimeException("Asked to create state: "+controllerStateID+" that seems to not being subclass of ControllerState");
            }

            return (ControllerState) Class.forName(controllerStateID).getConstructor(Controller.class).newInstance(controller);

        } catch( Exception e ){
            throw new BadBehaviourRuntimeException("Something during the creation of a controller state by id failed. This is the asked ID: "+controllerStateID);
        }
    }

    //Getters for controller states
    //TODO: check if are really needed this methods:
    public StartControllerState getStartState() {
        return startState;
    }
    public PlaceControllerState getPlaceState() {
        return placeState;
    }
    public ToolCardControllerState getToolCardState() { return toolCardState; }
    public DraftControllerState getDraftControllerState() { return draftControllerState; }
    public EndToolCardEffectControllerState getEndToolCardEffectControllerState() {
        return endToolCardEffectControllerState;
    }
}
