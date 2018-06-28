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
     * String used as message of BadBehaviourRuntimeException thrown when the state that is asked to be created
     * is not subclass of ControllerState
     */
    private static final String STATE_NOT_SUBCLASS_OF_CONTROLLER_STATE = "Asked to create a state that does not seems a subclass of ControllerState: state is ";

    /**
     * String used as message of BadBehaviourRuntimeException thrown when something during creation of state goes wrong
     */
    private static final String ERROR_DURING_CREATION_OF_CONTROLLER_STATE_BY_ID = "Something during the creation of a controller state by id failed. This is the asked ID: ";
    /**
     * Controller to which is added the state
     */
    private Controller controller;

    /**
     * Table containing all the previously created states
     * in order to avoid creating more than once the same state.
     * If it is requested a state that was already created, it
     * is returned that state instead of creating a new one
     */
    private HashMap<String, ControllerState> stateTable;


    /**
     * The default start controller state used in the state transitions
     */
    private StartControllerState startState;
    /**
     * The default place controller state used in the state transitions
     */
    private PlaceControllerState placeState;
    /**
     * The default toolcard controller state used in the state transitions
     */
    private ToolCardControllerState toolCardState;
    /**
     * The default draft controller state used in the state transitions
     */
    private DraftControllerState draftControllerState;
    /**
     * The default end controller state used in the state transitions
     */
    private EndControllerState endControllerState;
    /**
     * The default end toolcard effect controller state used in the state transitions
     */
    private EndToolCardEffectControllerState endToolCardEffectControllerState;

    /**
     * Constructor for the Controller State Manager. Each controller instance
     * has a specific controller state manager, so it is required as a param
     * the controller instance that will then receiveFromServer the controller states
     * instances when asking for them
     *
     * @param controller the controller to which are added the states
     */
    public ControllerStateManager(Controller controller) {
        this.controller = controller;
        this.stateTable = new HashMap<>();

        this.draftControllerState = new DraftControllerState(this.controller);
        this.startState = new StartControllerState(this.controller);
        this.placeState = new PlaceControllerState(this.controller);
        this.toolCardState = new ToolCardControllerState(this.controller);
        this.endToolCardEffectControllerState = new EndToolCardEffectControllerState(this.controller);
        this.endControllerState = new EndControllerState(this.controller);
    }

    /**
     * Based on the active toolCard, decides what is the new state
     * that the connected controller should take
     *
     * @param prevState the previous controller state
     * @return the next controller state based on the active toolCard
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
     * @author Federico Haag
     */
    private ControllerState createStateByID(String controllerStateID){
        try {

            //Checks that the class it is created is a subclass of ControllerState
            Class<?> cs = Class.forName(ControllerState.class.getPackage().getName() + "." + controllerStateID);
            if( ! ControllerState.class.isAssignableFrom(cs) ){
                throw new BadBehaviourRuntimeException(STATE_NOT_SUBCLASS_OF_CONTROLLER_STATE + controllerStateID);
            }

            return (ControllerState) cs.getConstructor(Controller.class).newInstance(controller);

        } catch( Exception e ){
            throw new BadBehaviourRuntimeException(ERROR_DURING_CREATION_OF_CONTROLLER_STATE_BY_ID +controllerStateID);
        }
    }

    /**
     * Gets the {@link StartControllerState}
     * @return the {@link StartControllerState}
     */
    public StartControllerState getStartState() {
        return startState;
    }

    /**
     * Gets the {@link PlaceControllerState}
     * @return the {@link PlaceControllerState}
     */
    public PlaceControllerState getPlaceState() {
        return placeState;
    }

    /**
     * Gets the {@link ToolCardControllerState}
     * @return the {@link ToolCardControllerState}
     */
    public ToolCardControllerState getToolCardState() { return toolCardState; }

    /**
     * Gets the {@link DraftControllerState}
     * @return the {@link DraftControllerState}
     */
    public DraftControllerState getDraftControllerState() { return draftControllerState; }

    /**
     * Gets the {@link EndToolCardEffectControllerState}
     * @return the {@link EndToolCardEffectControllerState}
     */
    public EndToolCardEffectControllerState getEndToolCardEffectControllerState() { return endToolCardEffectControllerState; }

    /**
     * Gets the {@link EndControllerState}
     * @return the {@link EndControllerState}
     */
    public EndControllerState getEndControllerState() { return endControllerState; }
}
