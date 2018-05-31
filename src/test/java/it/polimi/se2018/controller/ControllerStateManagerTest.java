package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.message.VCMessage;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ControllerStateManagerTest {

    private Controller controller;
    private ControllerStateManager stateManager;


    @Before
    public void setUp() throws Exception {
        Game game = new Game(4,4);
        Properties gprop = new Properties();
        gprop.setProperty("numberOfRounds","10");
        gprop.setProperty("numberOfDicesPerColor","18");
        gprop.setProperty("numberOfToolCards","12");
        gprop.setProperty("numberOfPublicObjectiveCards","2");
        gprop.setProperty("maxNumberOfPlayers","4");
        gprop.setProperty("minNumberOfPlayers","2");
        gprop.setProperty("timeoutLaunchingGame","1000");
        gprop.setProperty("timeoutChoosingPatterns","1000");
        gprop.setProperty("amountOfCouplesOfPatternsPerPlayer","4");
        gprop.setProperty("timeoutPlayerMove","1000");

        controller = new Controller(game, gprop);

        Set<String> nicknames = new HashSet<>(Arrays.asList("johnnifer", "rubens"));

        stateManager = controller.stateManager;

        //this will automatically associate with Pinza Sgrossatrice tool card and its state table
        Properties prop = new Properties();
        prop.put("id", "PinzaSgrossatrice");
        prop.put("title", "title");
        prop.put("description", "desc");
        prop.put("neededTokens", "1");
        prop.put("tokensUsageMultiplier", "2");
        prop.put("imageURL", "imageURL");
        HashMap<String, String> controllerStateRules = new HashMap<>();

        ToolCard toolCard = new ToolCard(prop, controllerStateRules, new EmptyPlacementRule());

        controller.launchGame(nicknames);

        WindowPatternManager wpmanager = new WindowPatternManager();
        WindowPattern wp = wpmanager.getPairsOfPatterns(1).iterator().next();


        for (Player p : controller.game.getPlayers()) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("windowPattern", wp);
            controller.handleMove(new VCMessage(VCMessage.types.CHOOSE_WINDOW_PATTERN, params, p.getID()));
        }

        controller.setActiveToolCard(toolCard);
    }

    @Test
    public void testGetNextState() {
        StartControllerState startState = new StartControllerState(controller);
        ControllerState nextState = stateManager.getNextState(startState);
        assertEquals("DraftControllerState", nextState.getClass().getSimpleName());
        nextState = stateManager.getNextState(nextState);
        assertEquals("ChangeDiceValueControllerState", nextState.getClass().getSimpleName());
        nextState = stateManager.getNextState(nextState);
        assertEquals("EndControllerState", nextState.getClass().getSimpleName());
    }

    @Test
    public void testGetNextStateWhichIsAlreadyInStateTable() {
        StartControllerState startState = new StartControllerState(controller);
        ControllerState nextState = stateManager.getNextState(startState);
        ControllerState nextState2 = stateManager.getNextState(startState);

        assertEquals(nextState, nextState2);
    }

    @Test
    public void testGetToolCardState() {
        assertNotNull(controller.stateManager.getToolCardState());
    }

    @Test
    public void testGetDraftControllerState() {
        assertNotNull(controller.stateManager.getDraftControllerState());
    }

    @Test
    public void testGetEndToolCardEffectControllerState() {
        assertNotNull(controller.stateManager.getEndToolCardEffectControllerState());
    }

    @Test
    public void testGetStartControllerState() {
        assertNotNull(controller.stateManager.getStartState());
    }

    @Test
    public void testGetPlaceControllerState() {
        assertNotNull(controller.stateManager.getPlaceState());
    }
}