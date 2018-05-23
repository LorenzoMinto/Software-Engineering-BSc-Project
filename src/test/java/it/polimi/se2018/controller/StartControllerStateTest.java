package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.networking.message.Message;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Properties;

import static org.junit.Assert.*;

public class StartControllerStateTest {
    private Controller controller;
    private ToolCard toolCard;


    @Before
    public void setUp() throws Exception {
        Game game = new Game(4,4);
        Properties properties = new Properties();
        properties.setProperty("numberOfDicesPerColor","18");
        properties.setProperty("numberOfToolCards","3");
        properties.setProperty("numberOfPublicObjectiveCards","2");
        properties.setProperty("maxNumberOfPlayers","4");
        controller = new Controller(game, properties);

        User user1 = new User(1, "johnniffer");
        User user2 = new User(2, "rubens");
        controller.acceptPlayer(user1, "a");
        controller.acceptPlayer(user2, "b");

        WindowPatternManager wpmanager = new WindowPatternManager();
        WindowPattern wp = wpmanager.getPatterns(1).get(0);

        for (Player p : controller.game.getPlayers()) {
            p.setWindowPattern(wp);
        }

        properties = new Properties();

        properties.put("id","ID1");
        properties.put("title","title1");
        properties.put("description","description1");
        properties.put("imageURL","imageURL1");
        properties.put("neededTokens", "1");
        properties.put("tokensUsageMultiplier", "2");

        toolCard = new ToolCard(properties, new HashMap<>(), new EmptyPlacementRule());

        controller.startGame();
    }

    @Test
    public void testConstructor(){
        ControllerState state = new StartControllerState(controller);
        assertNotNull(state);
    }

    @Test
    public void testConstructorWithNullController(){
        try{
            new StartControllerState(null);
            fail();
        }catch (IllegalArgumentException e){}
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
        assertTrue(controller.game.getCurrentRound().getCurrentTurn().hasUsedToolCard());
    }

    @Test
    public void testUseToolCardWhenNotDrawn() {
        try {
            controller.controllerState.useToolCard(toolCard);
            fail();
        } catch (BadBehaviourRuntimeException e) { }

        assertNull(controller.getActiveToolCard());
        assertFalse(controller.game.getCurrentRound().getCurrentTurn().hasUsedToolCard());
    }

}