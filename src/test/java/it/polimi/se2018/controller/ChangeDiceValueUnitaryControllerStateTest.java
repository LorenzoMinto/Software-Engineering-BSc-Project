package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.ControllerBoundMessageType;
import it.polimi.se2018.utils.Message;

import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.NoSuchParamInMessageException;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static it.polimi.se2018.utils.ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.ViewBoundMessageType.ERROR_MESSAGE;
import static org.junit.Assert.*;

/**
 * Test for {@link ChangeDiceValueUnitaryControllerState} class
 *
 * @author Lorenzo Minto
 * @author Jacopo Pio Gargano
 */
public class ChangeDiceValueUnitaryControllerStateTest {
    private Controller controller;

    private ToolCard toolCard;

    /**
     * Advances the Game in order to set the ControllerState to ChangeDiceValueUnitaryControllerState
     */
    @Before
    public void setUpGameAndControllerToChangeDiceValueUnitaryControllerState(){
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
        gameProperties.setProperty("persistencyPath","globalrankings.xml");

        controller = new Controller(game, gameProperties);

        Set<String> nicknames = new HashSet<>(Arrays.asList("Johnnyfer", "Rubens"));

        WindowPatternManager WPManager = new WindowPatternManager();
        WindowPattern wp = WPManager.getPairsOfPatterns(1).iterator().next();

        controller.launchGame(nicknames);

        for (Player p : controller.game.getPlayers()) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("windowPattern", wp);
            params.put("move", Move.CHOOSE_WINDOW_PATTERN);
            controller.handleMoveMessage(new Message(ControllerBoundMessageType.MOVE, params, p.getID()));
        }

        Properties toolCardProperties = new Properties();
        toolCardProperties.put("id", "GrozingPliers");
        toolCardProperties.put("title", "title");
        toolCardProperties.put("description", "desc");
        toolCardProperties.put("neededTokens", "1");
        toolCardProperties.put("tokensUsageMultiplier", "2");
        toolCardProperties.put("imageURL", "imageURL");

        toolCard = new ToolCard(toolCardProperties, new HashMap<>(), null, null);
        controller.controllerState.useToolCard(toolCard);
        Dice dice = controller.game.getCurrentRound().getDraftPool().getDices().get(0);

        controller.controllerState.draftDiceFromDraftPool(dice);
    }

    /**
     * Tests the impossibility of creating a {@link ChangeDiceValueUnitaryControllerState} when controller is null
     * @see ChangeDiceValueUnitaryControllerState#ChangeDiceValueUnitaryControllerState(Controller)
     */
    @Test
    public void testConstructorWithNullController() {
        try {
            new ChangeDiceValueUnitaryControllerState(null);
            fail();
        } catch (IllegalArgumentException e) { }
    }
    
    /**
     * Tests incrementing the drafted dice value
     * @see ChangeDiceValueUnitaryControllerState#incrementDice()
     */
    @Test
    public void testIncrementDice() {
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        int value = 3;
        Dice dice = new Dice(DiceColor.BLUE, value);
        currentTurn.setDraftedDice(dice);
        Message m = controller.controllerState.incrementDice();

        assertEquals(value+1, currentTurn.getDraftedDice().getValue());
        assertEquals(ACKNOWLEDGMENT_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of incrementing the drafted dice value when its value is 6
     * @see ChangeDiceValueUnitaryControllerState#incrementDice()
     */
    @Test
    public void testIncrementDiceWhenDiceValueIsSix() {
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        int value = 6;
        Dice dice = new Dice(DiceColor.BLUE, value);
        currentTurn.setDraftedDice(dice);
        Message m = controller.controllerState.incrementDice();

        assertEquals(value, currentTurn.getDraftedDice().getValue());
        assertEquals(ERROR_MESSAGE, m.getType());
        try {
            assertNotEquals(controller.controllerState.defaultMessage, m.getParam("message"));
        } catch (NoSuchParamInMessageException e) {
            fail();
        }
    }

    /**
     * Tests decrementing the drafted dice value
     * @see ChangeDiceValueUnitaryControllerState#decrementDice()
     */
    @Test
    public void testDecrementDice() {
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        int value = 3;
        Dice dice = new Dice(DiceColor.BLUE, value);
        currentTurn.setDraftedDice(dice);
        Message m = controller.controllerState.decrementDice();

        assertEquals(value-1, currentTurn.getDraftedDice().getValue());
        assertEquals(ACKNOWLEDGMENT_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of decrementing the drafted dice value when its value is 1
     * @see ChangeDiceValueUnitaryControllerState#decrementDice() 
     */
    @Test
    public void testDecrementDiceWhenDiceValueIsOne() {
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        int value = 1;
        Dice dice = new Dice(DiceColor.BLUE, value);
        currentTurn.setDraftedDice(dice);
        Message m = controller.controllerState.decrementDice();

        assertEquals(value, currentTurn.getDraftedDice().getValue());
        assertEquals(ERROR_MESSAGE, m.getType());

    }

    /**
     * Tests ending the current turn in this state
     * @see ChangeDiceValueUnitaryControllerState#endCurrentTurn()
     */
    @Test
    public void testEndCurrentTurn(){
        Message m = controller.controllerState.endCurrentTurn();
        assertEquals(1,controller.game.getCurrentRound().getCurrentTurn().getNumber());
        assertEquals(ACKNOWLEDGMENT_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of drafting a dice from the draftPool in this state
     * @see ChangeDiceValueUnitaryControllerState#draftDiceFromDraftPool(Dice)
     */
    @Test
    public void testDraftDiceFromDraftPool(){
        Message m = controller.controllerState.draftDiceFromDraftPool(new Dice(DiceColor.RED));
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of placing a dice in this state
     * @see ChangeDiceValueUnitaryControllerState#placeDice(int, int)
     */
    @Test
    public void testPlaceDice(){
        Message m = controller.controllerState.placeDice(0,0);
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of using a toolCard in this state
     * @see ChangeDiceValueUnitaryControllerState#useToolCard(ToolCard)
     */
    @Test
    public void testUseToolCard(){
        Message m = controller.controllerState.useToolCard(toolCard);
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of choosing a dice value in this state
     * @see ChangeDiceValueUnitaryControllerState#chooseDiceValue(int)
     */
    @Test
    public void testChooseDiceValue(){
        Message m = controller.controllerState.chooseDiceValue(1);
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of choosing a dice from track in this state
     * @see ChangeDiceValueUnitaryControllerState#chooseDiceFromTrack(Dice, int)
     */
    @Test
    public void testChooseDiceFromTrack(){
        Message m = controller.controllerState.chooseDiceFromTrack(new Dice(DiceColor.RED), 1);
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of moving a dice in this state
     * @see ChangeDiceValueUnitaryControllerState#moveDice(int, int, int, int)
     */
    @Test
    public void testMoveDice(){
        Message m = controller.controllerState.moveDice(0,0,1,1);
        assertEquals(ERROR_MESSAGE, m.getType());
    }
    
    /**
     * Tests the impossibility of ending a toolCard effect in this state
     * @see ChangeDiceValueUnitaryControllerState#endToolCardEffect()
     */
    @Test
    public void testEndToolCardEffect(){
        Message m = controller.controllerState.endToolCardEffect();
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of returning a dice to the draftpool in this state
     * @see ControllerState#endToolCardEffect()
     */
    @Test
    public void testReturnDiceToDraftPool(){
        Message m = controller.controllerState.returnDiceToDraftPool();
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Testing the retrieval of the state permissions
     * @see ChangeDiceValueUnitaryControllerState#getStatePermissions()
     */
    @Test
    public void testGetStatePermissions(){
        assertNotNull(controller.controllerState.getStatePermissions());
    }
}