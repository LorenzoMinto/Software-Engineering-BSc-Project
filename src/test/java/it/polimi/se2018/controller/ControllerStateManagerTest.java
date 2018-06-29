package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;

import it.polimi.se2018.utils.ControllerBoundMessageType;
import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.Move;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Test for {@link ControllerStateManager} class
 *
 * @author Lorenzo Minto
 * @author Jacopo Pio Gargano
 */
public class ControllerStateManagerTest {

    private Controller controller;
    private ControllerStateManager stateManager;


    /**
     * Advances the Game in order to test the ControllerStateManager
     */
    @Before
    public void setUp(){
        Game game = new Game(4,4);
        Properties gameProperties = new Properties();
        gameProperties.setProperty("numberOfRounds","10");
        gameProperties.setProperty("numberOfDicesPerColor","18");
        gameProperties.setProperty("numberOfToolCards","12");
        gameProperties.setProperty("numberOfPublicObjectiveCards","2");
        gameProperties.setProperty("maxNumberOfPlayers","4");
        gameProperties.setProperty("minNumberOfPlayers","2");
        gameProperties.setProperty("timeoutLaunchingGame","1000");
        gameProperties.setProperty("timeoutChoosingPatterns","1000");
        gameProperties.setProperty("amountOfCouplesOfPatternsPerPlayer","4");
        gameProperties.setProperty("timeoutPlayerMove","1000");
        gameProperties.setProperty("persistencyPath","globalrankings.xml");

        controller = new Controller(game, gameProperties);

        Set<String> nicknames = new HashSet<>(Arrays.asList("Johnnyfer", "Rubens"));

        stateManager = controller.stateManager;

        Properties toolCardProperties = new Properties();
        toolCardProperties.put("id", "GrozingPliers");
        toolCardProperties.put("title", "title");
        toolCardProperties.put("description", "desc");
        toolCardProperties.put("neededTokens", "1");
        toolCardProperties.put("tokensUsageMultiplier", "2");
        toolCardProperties.put("imageURL", "imageURL");
        HashMap<String, String> controllerStateRules = new HashMap<>();

        ToolCard toolCard = new ToolCard(toolCardProperties, controllerStateRules, new EmptyPlacementRule(), new HashSet<>());

        controller.launchGame(nicknames);

        WindowPatternManager WPManager = new WindowPatternManager();
        WindowPattern wp = WPManager.getPairsOfPatterns(1).iterator().next();


        for (Player p : controller.game.getPlayers()) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("windowPattern", wp);
            params.put("move", Move.CHOOSE_WINDOW_PATTERN);
            controller.handleMoveMessage(new Message(ControllerBoundMessageType.MOVE, params, p.getID()));
        }

        controller.setActiveToolCard(toolCard);
    }

    /**
     * Tests the retrieval of the next state of the {@link Controller}
     * @see ControllerStateManager#getNextState(ControllerState)
     */
    @Test
    public void testGetNextState() {
        StartControllerState startState = new StartControllerState(controller);
        ControllerState nextState = stateManager.getNextState(startState);
        assertEquals("DraftControllerState", nextState.getClass().getSimpleName());
        nextState = stateManager.getNextState(nextState);
        assertEquals("ChangeDiceValueUnitaryControllerState", nextState.getClass().getSimpleName());
        nextState = stateManager.getNextState(nextState);
        assertEquals("PlaceControllerState", nextState.getClass().getSimpleName());
        nextState = stateManager.getNextState(nextState);
        assertEquals("EndControllerState", nextState.getClass().getSimpleName());
    }

    /**
     * Tests the retrieval of the next state of the {@link Controller} which is already in the state table
     */
    @Test
    public void testGetNextStateWhichIsAlreadyInStateTable() {
        StartControllerState startState = new StartControllerState(controller);
        ControllerState nextState = stateManager.getNextState(startState);
        ControllerState nextState2 = stateManager.getNextState(startState);

        assertEquals(nextState, nextState2);
    }

    /**
     * Tests the retrieval of the {@link ToolCardControllerState}
     * @see ControllerStateManager#getToolCardState()
     */
    @Test
    public void testGetToolCardState() {
        assertNotNull(controller.stateManager.getToolCardState());
    }

    /**
     * Tests the retrieval of the {@link DraftControllerState}
     * @see ControllerStateManager#getDraftControllerState()
     */
    @Test
    public void testGetDraftControllerState() {
        assertNotNull(controller.stateManager.getDraftControllerState());
    }

    /**
     * Tests the retrieval of the {@link EndToolCardEffectControllerState}
     * @see ControllerStateManager#getEndControllerState()
     */
    @Test
    public void testGetEndToolCardEffectControllerState() {
        assertNotNull(controller.stateManager.getEndToolCardEffectControllerState());
    }

    /**
     * Tests the retrieval of the {@link StartControllerState}
     * @see ControllerStateManager#getStartState()
     */
    @Test
    public void testGetStartControllerState() {
        assertNotNull(controller.stateManager.getStartState());
    }

    /**
     * Tests the retrieval of the {@link PlaceControllerState}
     * @see ControllerStateManager#getPlaceState()
     */
    @Test
    public void testGetPlaceControllerState() {
        assertNotNull(controller.stateManager.getPlaceState());
    }

    /**
     * Tests the retrieval of the {@link EndControllerState}
     * @see ControllerStateManager#getEndControllerState() ()
     */
    @Test
    public void testGetEndControllerState() {
        assertNotNull(controller.stateManager.getEndControllerState());
    }
}