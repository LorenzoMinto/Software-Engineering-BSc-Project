package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.ControllerBoundMessageType;
import it.polimi.se2018.utils.Message;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static it.polimi.se2018.utils.ViewBoundMessageType.ERROR_MESSAGE;
import static org.junit.Assert.*;

public class MoveControllerStateTest {
    private Controller controller;
    private Properties prop;

    private Dice redDice;
    private int r0 = 0;
    private int c0 = 0;
    private int r1 = 1;
    private int c1 = 1;

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
        gprop.setProperty("timeoutLaunchingGame","1000");
        gprop.setProperty("timeoutChoosingPatterns","1000");
        gprop.setProperty("amountOfCouplesOfPatternsPerPlayer","4");
        gprop.setProperty("timeoutPlayerMove","1000");

        controller = new Controller(game, gprop);

        Set<String> nicknames = new HashSet<>(Arrays.asList("johnnifer", "rubens"));

        Cell[][] pattern = new Cell[3][3];
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                pattern[i][j] = new Cell();
            }
        }
        redDice = new Dice(DiceColor.RED);
        pattern[r0][c0].setDice(redDice);
        WindowPattern wp = new WindowPattern("id", "title","", 5, pattern);

        controller.launchGame(nicknames);

        for (Player p : controller.game.getPlayers()) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("windowPattern", wp);
            controller.handleMove(new Message(ControllerBoundMessageType.CHOSEN_WINDOW_PATTERN, params, p.getID()));
        }

        prop = new Properties();
        prop.put("id", "PennelloPerEglomise");
        prop.put("title", "title");
        prop.put("description", "desc");
        prop.put("neededTokens", "1");
        prop.put("tokensUsageMultiplier", "2");
        prop.put("imageURL", "imageURL");

        ToolCard toolCard = new ToolCard(prop, new HashMap<>(), null);
        controller.controllerState.useToolCard(toolCard);
    }

    @Test
    public void testMoveDice() {
        controller.controllerState.moveDice(r0,c0,r1,c1);

        Player player = controller.game.getCurrentRound().getCurrentTurn().getPlayer();
        WindowPattern wp = player.getWindowPattern();

        assertNull(wp.getDiceOnCell(r0,c0));
        assertEquals(redDice, wp.getDiceOnCell(r1,c1));
    }

    @Test
    public void testMoveDiceWhenNoDiceInPosition() {
        Message m = controller.controllerState.moveDice(r1,c1,r0,c0);
        assertEquals(ERROR_MESSAGE, m.getType());
    }
}