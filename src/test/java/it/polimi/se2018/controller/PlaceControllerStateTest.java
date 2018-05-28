package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.message.Message;
import it.polimi.se2018.utils.message.VCMessage;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static it.polimi.se2018.utils.message.CVMessage.types.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.message.CVMessage.types.ERROR_MESSAGE;
import static org.junit.Assert.*;

public class PlaceControllerStateTest {
    private Controller controller;
    private Properties prop;

    private Dice dice;

    @Before
    public void setUp() throws Exception {
        Game game = new Game(4,4);
        Properties gprop = new Properties();
        gprop.setProperty("numberOfRounds","10");
        gprop.setProperty("numberOfDicesPerColor","18");
        gprop.setProperty("numberOfToolCards","3");
        gprop.setProperty("numberOfPublicObjectiveCards","2");
        gprop.setProperty("maxNumberOfPlayers","4");
        gprop.setProperty("minNumberOfPlayers","2");
        gprop.setProperty("timeoutLaunchingGame","30");
        gprop.setProperty("timeoutChoosingPatterns","30");
        gprop.setProperty("amountOfCouplesOfPatternsPerPlayer","4");

        controller = new Controller(game, gprop);

        Set<String> nicknames = new HashSet<>(Arrays.asList("johnnifer", "rubens"));

        Cell[][] pattern = new Cell[3][3];
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                pattern[i][j] = new Cell();
            }
        }
        WindowPattern wp = new WindowPattern("id", "title", 5, pattern);

        controller.launchGame(nicknames);

        for (Player p : controller.game.getPlayers()) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("windowPattern", wp);
            controller.handleMove(new VCMessage(VCMessage.types.CHOOSE_WINDOW_PATTERN, params, p.getID()));
        }

        prop = new Properties();
        prop.put("id", "id");
        prop.put("title", "title");
        prop.put("description", "desc");
        prop.put("neededTokens", "1");
        prop.put("tokensUsageMultiplier", "2");
        prop.put("imageURL", "imageURL");

        dice = game.getCurrentRound().getDraftPool().getDices().get(0);
        Message m = controller.controllerState.draftDiceFromDraftPool(dice);
    }

    @Test
    public void testPlaceDice() {
        Message m = controller.controllerState.placeDice(0,0);
        System.out.println(m.getParam("message"));
        assertEquals(ACKNOWLEDGMENT_MESSAGE, m.getType());
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        WindowPattern wp = currentTurn.getPlayer().getWindowPattern();
        assertEquals(dice, wp.getDiceOnCell(0,0));
        assertNull(currentTurn.getDraftedDice());
    }

    @Test
    public void testPlaceDiceAtIllegalPosition() {
        Message m = controller.controllerState.placeDice(1,1);
        assertEquals(ERROR_MESSAGE, m.getType());
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        WindowPattern wp = currentTurn.getPlayer().getWindowPattern();
        assertEquals(dice, currentTurn.getDraftedDice());
        assertNull(wp.getDiceOnCell(1,1));
    }
}