package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.ControllerBoundMessageType;
import it.polimi.se2018.utils.Message;

import it.polimi.se2018.utils.NoSuchParamInMessageException;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static it.polimi.se2018.utils.ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.ViewBoundMessageType.ERROR_MESSAGE;
import static org.junit.Assert.*;

/**
 * Test for {@link MoveControllerState} class
 *
 * @author Lorenzo Minto
 * @author Jacopo Pio Gargano
 */
public class MoveControllerStateTest {
    private Controller controller;

    private Dice redDice;
    private static final int r0 = 0;
    private static final int c0 = 0;
    private static final int r1 = 1;
    private static final int c1 = 1;
    private static final int r2 = 2;
    private static final int c2 = 2;

    /**
     * Advances the Game in order to set the ControllerState to MoveControllerState
     */
    @Before
    public void setUpGameAndControllerToMoveControllerState(){
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

        Cell[][] pattern = new Cell[3][3];
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                pattern[i][j] = new Cell();
            }
        }
        redDice = new Dice(DiceColor.RED);
        pattern[r0][c0].setDice(redDice);
        pattern[r2][c2].setDice(redDice);
        WindowPattern wp = new WindowPattern("id", "title","", 5, pattern);

        controller.launchGame(nicknames);

        for (Player p : controller.game.getPlayers()) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("windowPattern", wp);
            controller.handleMove(new Message(ControllerBoundMessageType.CHOSEN_WINDOW_PATTERN, params, p.getID()));
        }

        Properties toolCardProperties = new Properties();
        toolCardProperties.put("id", "EglomiseBrush");
        toolCardProperties.put("title", "title");
        toolCardProperties.put("description", "desc");
        toolCardProperties.put("neededTokens", "1");
        toolCardProperties.put("tokensUsageMultiplier", "2");
        toolCardProperties.put("imageURL", "imageURL");

        ToolCard toolCard = new ToolCard(toolCardProperties, new HashMap<>(), null);
        controller.controllerState.useToolCard(toolCard);
    }

    /**
     * Tests the impossibility of creating a {@link MoveControllerState} when controller is null
     * @see MoveControllerState#MoveControllerState(Controller)
     */
    @Test
    public void testConstructorWithNullController() {
        try {
            new MoveControllerState(null);
            fail();
        } catch (IllegalArgumentException e) { }
    }

    /**
     * Tests moving a dice from an initial cell to another cell
     * @see MoveControllerState#moveDice(int, int, int, int)
     */
    @Test
    public void testMoveDice() {
        Message m = controller.controllerState.moveDice(r0,c0,r1,c1);

        Player player = controller.game.getCurrentRound().getCurrentTurn().getPlayer();
        WindowPattern wp = player.getWindowPattern();

        assertNull(wp.getDiceOnCell(r0,c0));
        assertEquals(redDice, wp.getDiceOnCell(r1,c1));
        assertEquals(ACKNOWLEDGMENT_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of moving a dice from an initial cell with no dice to another cell
     * @see MoveControllerState#moveDice(int, int, int, int)
     */
    @Test
    public void testMoveDiceWhenNoDiceInPosition() {
        Message m = controller.controllerState.moveDice(r1,c1,r0,c0);
        assertEquals(ERROR_MESSAGE, m.getType());
        try {
            assertNotEquals(controller.controllerState.defaultMessage, m.getParam("message"));
        } catch (NoSuchParamInMessageException e) {
            fail();
        }
    }

    /**
     * Tests the impossibility of moving a dice from an initial cell to another cell with a dice already on it
     * @see MoveControllerState#moveDice(int, int, int, int)
     */
    @Test
    public void testMoveDiceToCellWithDice() {
        Message m = controller.controllerState.moveDice(r1,c1,r2,c2);
        assertEquals(ERROR_MESSAGE, m.getType());
        try {
            assertNotEquals(controller.controllerState.defaultMessage, m.getParam("message"));
        } catch (NoSuchParamInMessageException e) {
            fail();
        }
    }

    /**
     * Tests ending the current turn in this state
     * @see MoveControllerState#endCurrentTurn()
     */
    @Test
    public void testEndCurrentTurn(){
        Message m = controller.controllerState.endCurrentTurn();
        assertEquals(1,controller.game.getCurrentRound().getCurrentTurn().getNumber());
        assertEquals(ACKNOWLEDGMENT_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of drafting a dice from the draftPool in this state
     * @see ControllerState#draftDiceFromDraftPool(Dice)
     */
    @Test
    public void testDraftDiceFromDraftPool(){
        Message m = controller.controllerState.draftDiceFromDraftPool(new Dice(DiceColor.RED));
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of placing a dice in this state
     * @see ControllerState#placeDice(int, int)
     */
    @Test
    public void testPlaceDice(){
        Message m = controller.controllerState.placeDice(0,0);
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of using a toolCard in this state
     * @see ControllerState#useToolCard(ToolCard)
     */
    @Test
    public void testUseToolCard(){
        Message m = controller.controllerState.useToolCard(ToolCard.createTestInstance());
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of choosing a dice from track in this state
     * @see ControllerState#chooseDiceFromTrack(Dice, int)
     */
    @Test
    public void testChooseDiceFromTrack(){
        Message m = controller.controllerState.chooseDiceFromTrack(new Dice(DiceColor.RED), 1);
        assertEquals(ERROR_MESSAGE, m.getType());
    }
    
    /**
     * Tests the impossibility of incrementing a dice value in this state
     * @see ControllerState#incrementDice()
     */
    @Test
    public void testIncrementDice(){
        Message m = controller.controllerState.incrementDice();
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of decrementing a dice value in this state
     * @see ControllerState#decrementDice()
     */
    @Test
    public void testDecrementDice(){
        Message m = controller.controllerState.decrementDice();
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of choosing a dice value in this state
     * @see ControllerState#chooseDiceValue(int)
     */
    @Test
    public void testChooseDiceValue(){
        Message m = controller.controllerState.chooseDiceValue(1);
        assertEquals(ERROR_MESSAGE, m.getType());
    }


    /**
     * Tests the impossibility of ending a toolCard effect in this state
     * @see ControllerState#endToolCardEffect()
     */
    @Test
    public void testEndToolCardEffect(){
        Message m = controller.controllerState.endToolCardEffect();
        assertEquals(ERROR_MESSAGE, m.getType());
    }
}