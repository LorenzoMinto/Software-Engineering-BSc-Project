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


public class ControllerTest {

    private static Controller controller;
    private static ToolCard toolCard;

    @BeforeClass
    public static void init(){

        Game game = new Game(4,4);
        Properties gameProperties = new Properties();
        gameProperties.setProperty("numberOfRounds","10");
        gameProperties.setProperty("numberOfDicesPerColor","18");
        gameProperties.setProperty("numberOfToolCards","3");
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
            controller.handleMove(new Message(ControllerBoundMessageType.CHOSEN_WINDOW_PATTERN, params, p.getID()));
        }

        Properties toolCardProperties = new Properties();
        toolCardProperties.put("id", "LensCutter");
        toolCardProperties.put("title", "title");
        toolCardProperties.put("description", "desc");
        toolCardProperties.put("neededTokens", "1");
        toolCardProperties.put("tokensUsageMultiplier", "2");
        toolCardProperties.put("imageURL", "imageURL");

        toolCard = new ToolCard(toolCardProperties, new HashMap<>(), null);
    }

    @Test
    public void testSetControllerState() {
        controller.setControllerState(controller.stateManager.getToolCardState());
        Message m = controller.controllerState.draftDiceFromDraftPool(new Dice(DiceColor.RED));
        assertEquals(ERROR_MESSAGE, m.getType());
        m = controller.controllerState.useToolCard(controller.game.getDrawnToolCards().get(0));
        assertEquals(ACKNOWLEDGMENT_MESSAGE, m.getType());
    }

    @Test
    public void canUseSpecificToolCard() {
    }

    @Test
    public void setActiveToolCard() {
    }

    @Test
    public void resetActiveToolCard() {
    }

    @Test
    public void getActiveToolCard() {
    }

    @Test
    public void advanceGame() {
    }

    @Test
    public void endGame() {
    }

    @Test
    public void getRankingsAndScores() {
    }

    @Test
    public void getDefaultPlacementRule() {
    }

    @Test
    public void getConfigProperty() {
        assertEquals(4,controller.getConfigProperty("maxNumberOfPlayers"));
        assertEquals(10,controller.getConfigProperty("numberOfRounds"));
    }
}
