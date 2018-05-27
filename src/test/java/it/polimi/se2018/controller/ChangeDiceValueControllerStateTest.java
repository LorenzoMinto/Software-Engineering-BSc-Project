package it.polimi.se2018.controller;
/*
import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.message.Message;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Properties;

import static it.polimi.se2018.utils.message.CVMessage.types.ERROR_MESSAGE;
import static org.junit.Assert.*;

public class ChangeDiceValueControllerStateTest {
    private Controller controller;
    private Properties prop;

    @Before
    public void setUp() throws Exception {
        Game game = new Game(4,4);
        Properties gprop = new Properties();
        gprop.setProperty("numberOfDicesPerColor","18");
        gprop.setProperty("numberOfToolCards","12");
        gprop.setProperty("numberOfPublicObjectiveCards","2");
        gprop.setProperty("maxNumberOfPlayers","4");
        controller = new Controller(game, gprop);

        User user1 = new User(1, "johnniffer");
        User user2 = new User(2, "rubens");
        controller.acceptPlayer(user1, "a");
        controller.acceptPlayer(user2, "b");

        WindowPatternManager wpmanager = new WindowPatternManager();
        WindowPattern wp = wpmanager.getPatterns(1).get(0);

        for (Player p : controller.game.getPlayers()) {
            game.assignWindowPatternToPlayer(wp, p);
        }

        prop = new Properties();
        prop.put("title", "title");
        prop.put("description", "desc");
        prop.put("neededTokens", "1");
        prop.put("tokensUsageMultiplier", "2");
        prop.put("imageURL", "imageURL");

        controller.startGame();

        prop.put("id", "PinzaSgrossatrice");
        ToolCard toolCard = new ToolCard(prop, new HashMap<>(), null);
        Message m = controller.controllerState.useToolCard(toolCard);
        Dice dice = controller.game.getCurrentRound().getDraftPool().getDices().get(0);

        m = controller.controllerState.draftDiceFromDraftPool(dice);
    }

    @Test
    public void testIncrementDice() {
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        int value = 3;
        Dice dice = new Dice(DiceColors.BLUE, value);
        currentTurn.setDraftedDice(dice);
        controller.controllerState.incrementDice();

        assertEquals(value+1, currentTurn.getDraftedDice().getValue());
    }

    @Test
    public void testIncrementDiceWhenDiceValueIsSix() {
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        int value = 6;
        Dice dice = new Dice(DiceColors.BLUE, value);
        currentTurn.setDraftedDice(dice);
        Message m = controller.controllerState.incrementDice();

        assertEquals(value, currentTurn.getDraftedDice().getValue());
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    @Test
    public void testDecrementDice() {
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        int value = 3;
        Dice dice = new Dice(DiceColors.BLUE, value);
        currentTurn.setDraftedDice(dice);
        controller.controllerState.decrementDice();

        assertEquals(value-1, currentTurn.getDraftedDice().getValue());

    }

    @Test
    public void testDecrementDiceWhenDiceValueIsOne() {
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        int value = 1;
        Dice dice = new Dice(DiceColors.BLUE, value);
        currentTurn.setDraftedDice(dice);
        Message m = controller.controllerState.decrementDice();

        assertEquals(value, currentTurn.getDraftedDice().getValue());
        assertEquals(ERROR_MESSAGE, m.getType());

    }

    @Test
    public void testChangeDiceValue() {
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        int value = 5;
        Dice dice = new Dice(DiceColors.BLUE, 3);
        currentTurn.setDraftedDice(dice);
        controller.controllerState.chooseDiceValue(value);

        assertEquals(value, currentTurn.getDraftedDice().getValue());
    }

    @Test
    public void testChangeDiceValueWhenValueNotAllowed() {
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        int value = 3;
        Dice dice = new Dice(DiceColors.BLUE, value);
        currentTurn.setDraftedDice(dice);
        Message m = controller.controllerState.chooseDiceValue(7);

        assertEquals(value, currentTurn.getDraftedDice().getValue());
        assertEquals(ERROR_MESSAGE, m.getType());
    }
}*/