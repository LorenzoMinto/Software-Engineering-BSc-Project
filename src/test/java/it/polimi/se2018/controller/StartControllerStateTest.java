package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.networking.message.Message;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class StartControllerStateTest {
    private Controller controller;

    @Before
    public void setUp() throws Exception {
        Game game = new Game(4,4);
        Properties prop = new Properties();
        prop.setProperty("numberOfDicesPerColor","18");
        prop.setProperty("numberOfToolCards","3");
        prop.setProperty("numberOfPublicObjectiveCards","2");
        prop.setProperty("maxNumberOfPlayers","4");
        controller = new Controller(game, prop);

        User user1 = new User(1, "johnniffer");
        User user2 = new User(2, "rubens");
        controller.acceptPlayer(user1, "a");
        controller.acceptPlayer(user2, "b");

        WindowPatternManager wpmanager = new WindowPatternManager();
        WindowPattern wp = wpmanager.getPatterns(1).get(0);

        for (Player p : controller.game.getPlayers()) {
            p.setWindowPattern(wp);
        }

        controller.startGame();
    }

    @Test
    public void testDraftDiceFromDraftPool() {
        Dice dice = controller.game.getCurrentRound().getDraftPool().getDices().get(0);
        controller.controllerState.draftDiceFromDraftPool(dice);

        Round currentRound = controller.game.getCurrentRound();
        Turn currentTurn = currentRound.getCurrentTurn();
        assertEquals(dice, currentTurn.getDraftedDice());
        assertFalse(currentRound.getDraftPool().getDices().contains(dice));
    }

    @Test
    public void testDraftDiceFromDraftPoolWhenDiceNotInDraftPool() {
        Dice dice = new Dice(DiceColors.BLUE);
        while (controller.game.getCurrentRound().getDraftPool().getDices().contains(dice)) {
            dice = new Dice(DiceColors.getRandomColor());
        }
        Message m = controller.controllerState.draftDiceFromDraftPool(dice);

        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        assertNull(currentTurn.getDraftedDice());
    }

    @Test
    public void testUseToolCard() {
        ToolCard toolCard = controller.game.getDrawnToolCards().get(0);

        controller.controllerState.useToolCard(toolCard);

        assertEquals(toolCard, controller.getActiveToolCard());
        assertEquals(controller.placementRule, toolCard.getPlacementRule());
        assertTrue(controller.game.getCurrentRound().getCurrentTurn().hasUsedToolCard());
    }

    @Test
    public void testUseToolCardWhenNotDrawn() {
        Properties prop = new Properties();
        prop.put("id", "id");
        prop.put("title", "title");
        prop.put("description", "desc");
        prop.put("neededTokens", "1");
        prop.put("tokensUsageMultiplier", "2");
        prop.put("imageURL", "imageURL");
        ToolCard toolCard = new ToolCard(prop, new HashMap<>(), new EmptyPlacementRule());

        try {
            controller.controllerState.useToolCard(toolCard);
            fail();
        } catch (BadBehaviourRuntimeException e) { }

        assertNull(controller.getActiveToolCard());
        assertFalse(controller.game.getCurrentRound().getCurrentTurn().hasUsedToolCard());
    }

    @Test
    public void testUseToolCardWhenNotAllowed() {
        Properties prop = new Properties();
        prop.put("id", "id");
        prop.put("title", "title");
        prop.put("description", "desc");
        prop.put("neededTokens", "1");
        prop.put("tokensUsageMultiplier", "2");
        prop.put("imageURL", "imageURL");
        HashMap<String, String> controllerStateRules = new HashMap<>();

        controllerStateRules.put("StartControllerState","DraftControllerState");
        controllerStateRules.put("DraftControllerState","ChangeDiceValueControllerState");
        controllerStateRules.put("ChangeDiceValueControllerState","EndControllerState");

        ToolCard toolCard = new ToolCard(prop, controllerStateRules, new EmptyPlacementRule());

        controller.game.getCurrentRound().getCurrentTurn().setDraftedDice(new Dice(DiceColors.BLUE));
        controller.controllerState.useToolCard(toolCard);

        assertNull(controller.getActiveToolCard());
        assertFalse(controller.game.getCurrentRound().getCurrentTurn().hasUsedToolCard());
    }

    @Test
    public void testConstructorWithNullController() {
        try {
            ControllerState state = new StartControllerState(null);
            fail();
        } catch (IllegalArgumentException e) { }
    }
}