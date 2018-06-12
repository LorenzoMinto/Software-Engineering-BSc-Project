
package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.ControllerBoundMessageType;
import it.polimi.se2018.utils.Message;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static it.polimi.se2018.utils.ViewBoundMessageType.ERROR_MESSAGE;
import static org.junit.Assert.*;


public class ChooseFromTrackControllerStateTest {
    private Controller controller;

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
        controller = new Controller(game, gameProperties);

        Set<String> nicknames = new HashSet<>(Arrays.asList("Johnnyfer", "Rubens"));

        WindowPatternManager manager = new WindowPatternManager();
        WindowPattern wp = manager.getPairsOfPatterns(1).iterator().next();

        controller.launchGame(nicknames);

        for (Player p : controller.game.getPlayers()) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("windowPattern", wp);
            controller.handleMove(new Message(ControllerBoundMessageType.CHOSEN_WINDOW_PATTERN, params, p.getID()));
        }

        Properties prop = new Properties();
        prop.put("id", "TaglierinaCircolare");
        prop.put("title", "title");
        prop.put("description", "desc");
        prop.put("neededTokens", "1");
        prop.put("tokensUsageMultiplier", "2");
        prop.put("imageURL", "imageURL");


        ToolCard toolCard = new ToolCard(prop, new HashMap<>(), null);
        toolCard = game.getToolCard(toolCard);

        controller.controllerState.useToolCard(toolCard);

        Dice dice = controller.game.getCurrentRound().getDraftPool().getDices().get(0);
        controller.controllerState.draftDiceFromDraftPool(dice);
    }

    @Test
    public void testChooseDiceFromTrack() {
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        Dice draftedDice = currentTurn.getDraftedDice();

        List<Dice> dices = new ArrayList<>();
        Dice dice1 = new Dice(DiceColor.BLUE, 2);
        Dice dice2 = new Dice(DiceColor.RED, 3);
        dices.add(dice1);
        dices.add(dice2);

        int slotNumber = 0;

        controller.game.getTrack().processDices(dices);
        Message m = controller.controllerState.chooseDiceFromTrack(dice1, slotNumber);

        assertNull(currentTurn.getTrackChosenDice());
        assertFalse(controller.game.getTrack().getDicesFromSlotNumber(slotNumber).contains(dice1));
        assertTrue(controller.game.getTrack().getDicesFromSlotNumber(slotNumber).contains(draftedDice));
        assertEquals(dice1, currentTurn.getDraftedDice());
    }

    @Test
    public void testChooseDiceFromTrackWhenTrackSlotDoesNotExist() {
        int slotNumber = 0;
        Dice dice1 = new Dice(DiceColor.BLUE, 2);
        Message m = controller.controllerState.chooseDiceFromTrack(dice1, slotNumber);
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    @Test
    public void testChooseDiceFromTrackWhenDiceNotInTrackSlot() {
        List<Dice> dices = new ArrayList<>();
        Dice dice1 = new Dice(DiceColor.BLUE, 2);
        Dice dice2 = new Dice(DiceColor.RED, 3);
        dices.add(dice1);

        int slotNumber = 0;

        controller.game.getTrack().processDices(dices);

        Message m = controller.controllerState.chooseDiceFromTrack(dice2, slotNumber);
        assertEquals(ERROR_MESSAGE, m.getType());
    }
}
