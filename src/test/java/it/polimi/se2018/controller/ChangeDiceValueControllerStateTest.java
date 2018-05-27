package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.message.Message;
import it.polimi.se2018.utils.message.VCMessage;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static it.polimi.se2018.utils.message.CVMessage.types.ERROR_MESSAGE;
import static org.junit.Assert.*;

public class ChangeDiceValueControllerStateTest {
    private Controller controller;
    private Properties prop;

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
        gprop.setProperty("timeoutLaunchingGame","30");
        gprop.setProperty("timeoutChoosingPatterns","30");
        gprop.setProperty("amountOfCouplesOfPatternsPerPlayer","4");

        controller = new Controller(game, gprop);

        Set<String> nicknames = new HashSet<>(Arrays.asList("johnnifer", "rubens"));

        WindowPatternManager wpmanager = new WindowPatternManager();
        WindowPattern wp = wpmanager.getCouplesOfPatterns(1).iterator().next();

        controller.launchGame(nicknames);

        for (Player p : controller.game.getPlayers()) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("windowPattern", wp);
            controller.handleMove(new VCMessage(VCMessage.types.CHOOSE_WINDOW_PATTERN, params, p.getID()));
        }

        prop = new Properties();
        prop.put("title", "title");
        prop.put("description", "desc");
        prop.put("neededTokens", "1");
        prop.put("tokensUsageMultiplier", "2");
        prop.put("imageURL", "imageURL");

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
}