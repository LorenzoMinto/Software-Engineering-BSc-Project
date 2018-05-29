
package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.message.Message;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;


public class ChooseFromTrackControllerStateTest {
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

        Set<String> nicknames = new HashSet<>(Arrays.asList("johnnifer", "rubens"));

        WindowPatternManager wpmanager = new WindowPatternManager();
        WindowPattern wp = wpmanager.getCouplesOfPatterns(1).iterator().next();

        controller.launchGame(nicknames);

        for (Player p : controller.game.getPlayers()) {
            game.assignWindowPatternToPlayer(wp, p.getID());
        }

        prop = new Properties();
        prop.put("id", "TaglierinaCircolare");
        prop.put("title", "title");
        prop.put("description", "desc");
        prop.put("neededTokens", "1");
        prop.put("tokensUsageMultiplier", "2");
        prop.put("imageURL", "imageURL");


        ToolCard toolCard = new ToolCard(prop, new HashMap<String, String>(), null);
        toolCard = game.getToolCard(toolCard);

        Message m = controller.controllerState.useToolCard(toolCard);

        Dice dice = controller.game.getCurrentRound().getDraftPool().getDices().get(0);
        m = controller.controllerState.draftDiceFromDraftPool(dice);

    }

    @Test
    public void testChooseDiceFromTrack() {
        List<Dice> dices = new ArrayList<>();
        Dice dice1 = new Dice(DiceColors.BLUE, 2);
        Dice dice2 = new Dice(DiceColors.RED, 3);
        dices.add(dice1);
        dices.add(dice2);

        controller.game.getTrack().processDices(dices);
        Message m = controller.controllerState.chooseDiceFromTrack(dice1, 0);
        //TODO: this will execute implicitly the next state, how to test intermediate behaviours?
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();

        assertEquals(dice1, currentTurn.getTrackChosenDice());
        assertFalse(controller.game.getTrack().getDicesFromSlotNumber(currentTurn.getSlotOfTrackChosenDice()).contains(currentTurn.getTrackChosenDice()));
    }

    @Test
    public void testChooseDiceFromTrackWhenDiceNotInTrack() {

    }
}
