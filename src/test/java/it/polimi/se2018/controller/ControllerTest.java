package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.ControllerBoundMessageType;
import it.polimi.se2018.utils.Message;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static it.polimi.se2018.utils.ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.ViewBoundMessageType.ERROR_MESSAGE;
import static org.junit.Assert.*;


/**
 * Test for {@link Controller} class
 * This class cannot be fully tested. Only simple behaviors are tested. Testing this class would mean simulating the game.
 *
 * Many of the methods and behaviors of {@link Controller} are tested by other tests.
 * Since the {@link Controller} follows the State Design Pattern, part of its behavior is mainly tested in the State tests.
 * The remaining behavior is not worth testing, as it would involve creating Messages of all types and exchanging them,
 * which in practice is playing the game.
 *
 * @author Jacopo Pio Gargano
 */
public class ControllerTest {

    private static Controller controller;

    @BeforeClass
    public static void init(){

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

        controller = new Controller(game, gameProperties);

        Set<String> nicknames = new HashSet<>(Arrays.asList("Johnnyfer", "Rubens"));

        WindowPatternManager WPManager = new WindowPatternManager();
        WindowPattern wp = WPManager.getPairsOfPatterns(1).iterator().next();

        controller.launchGame(nicknames);

        for (Player p : controller.game.getPlayers()) {
            HashMap<String, Object> params = new HashMap<>();

            params.put("windowPattern", wp);
            controller.handleMoveMessage(new Message(ControllerBoundMessageType.CHOSEN_WINDOW_PATTERN, params, p.getID()));
        }
    }

    /**
     * Tests setting a specific controller state
     * @see Controller#setControllerState(ControllerState)
     */
    @Test
    public void testSetControllerState() {
        controller.setControllerState(controller.stateManager.getToolCardState());
        Message m = controller.controllerState.draftDiceFromDraftPool(new Dice(DiceColor.RED));
        assertEquals(ERROR_MESSAGE, m.getType());

        ToolCardManager manager = new ToolCardManager(new EmptyPlacementRule());
        ToolCard toolCard = null;
        while (toolCard == null || !toolCard.getTitle().equals("Eglomise Brush")) {
            toolCard = manager.getRandomToolCards(1).get(0);
        }
        m = controller.controllerState.useToolCard(toolCard);
        assertEquals(ACKNOWLEDGMENT_MESSAGE, m.getType());
    }

    /**
     * Tests the retrieval of the config properties
     * @see Controller#getConfigProperty(String)
     */
    @Test
    public void getConfigProperty() {
        assertEquals(4,controller.getConfigProperty("maxNumberOfPlayers"));
        assertEquals(10,controller.getConfigProperty("numberOfRounds"));
    }
}
