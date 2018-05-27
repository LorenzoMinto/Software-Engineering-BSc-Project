package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
/*
public class ControllerStateManagerTest {

    private Controller controller;
    private ControllerStateManager stateManager;


    @Before
    public void setUp() throws Exception {
        Game game = new Game(4,4);
        Properties gprop = new Properties();
        gprop.setProperty("numberOfDicesPerColor","18");
        //so that all toolCards are drawn
        gprop.setProperty("numberOfToolCards","12");
        gprop.setProperty("maxNumberOfPlayers","4");
        gprop.setProperty("numberOfPublicObjectiveCards","2");

        controller = new Controller(game, gprop);

        User user1 = new User(1, "johnniffer");
        User user2 = new User(2, "rubens");
        controller.acceptPlayer(user1, "a");
        controller.acceptPlayer(user2, "b");

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

        controller.startGame();

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
}*/