
package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.message.Message;
import it.polimi.se2018.utils.message.VCMessage;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static it.polimi.se2018.utils.message.CVMessage.types.ERROR_MESSAGE;
import static org.junit.Assert.*;


public class ChooseFromTrackControllerStateTest {
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
        prop.put("id", "TaglierinaCircolare");
        prop.put("title", "title");
        prop.put("description", "desc");
        prop.put("neededTokens", "1");
        prop.put("tokensUsageMultiplier", "2");
        prop.put("imageURL", "imageURL");


        ToolCard toolCard = new ToolCard(prop, new HashMap<>(), null);
        toolCard = game.getToolCard(toolCard);

        Message m = controller.controllerState.useToolCard(toolCard);

        Dice dice = controller.game.getCurrentRound().getDraftPool().getDices().get(0);
        m = controller.controllerState.draftDiceFromDraftPool(dice);
    }

    @Test
    public void testChooseDiceFromTrack() {
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        Dice draftedDice = currentTurn.getDraftedDice();

        List<Dice> dices = new ArrayList<>();
        Dice dice1 = new Dice(DiceColors.BLUE, 2);
        Dice dice2 = new Dice(DiceColors.RED, 3);
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
        Dice dice1 = new Dice(DiceColors.BLUE, 2);
        Message m = controller.controllerState.chooseDiceFromTrack(dice1, slotNumber);
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    @Test
    public void testChooseDiceFromTrackWhenDiceNotInTrackSlot() {
        List<Dice> dices = new ArrayList<>();
        Dice dice1 = new Dice(DiceColors.BLUE, 2);
        Dice dice2 = new Dice(DiceColors.RED, 3);
        dices.add(dice1);

        int slotNumber = 0;

        controller.game.getTrack().processDices(dices);

        Message m = controller.controllerState.chooseDiceFromTrack(dice2, slotNumber);
        assertEquals(ERROR_MESSAGE, m.getType());
    }
}
