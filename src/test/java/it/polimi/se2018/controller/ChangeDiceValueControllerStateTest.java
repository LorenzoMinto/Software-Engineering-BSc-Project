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
 * Test for {@link ChangeDiceValueControllerState} class
 *
 * @author Lorenzo Minto
 * @author Jacopo Pio Gargano
 */
public class ChangeDiceValueControllerStateTest {
    private Controller controller;

    /**
     * Advances the Game in order to set the ControllerState to ChangeDiceValueControllerState
     */
    @Before
    public void setUpGameAndControllerToChangeDiceValueState(){
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

        WindowPatternManager WPManager = new WindowPatternManager();
        WindowPattern wp = WPManager.getPairsOfPatterns(1).iterator().next();

        controller.launchGame(nicknames);

        for (Player p : controller.game.getPlayers()) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("windowPattern", wp);
            controller.handleMove(new Message(ControllerBoundMessageType.CHOSEN_WINDOW_PATTERN, params, p.getID()));
        }


        Properties prop = new Properties();
        prop.put("id", "FluxRemover");
        prop.put("title", "title");
        prop.put("description", "desc");
        prop.put("neededTokens", "1");
        prop.put("tokensUsageMultiplier", "2");
        prop.put("imageURL", "imageURL");

        ToolCard toolCard = new ToolCard(prop, new HashMap<>(), null);
        controller.controllerState.useToolCard(toolCard);
        Dice dice = controller.game.getCurrentRound().getDraftPool().getDices().get(0);

        controller.controllerState.draftDiceFromDraftPool(dice);
    }

    /**
     * Tests the impossibility of creating a {@link ChangeDiceValueControllerState} when controller is null
     * @see ChangeDiceValueControllerState#ChangeDiceValueControllerState(Controller)
     */
    @Test
    public void testConstructorWithNullController() {
        try {
            new ChangeDiceValueControllerState(null);
            fail();
        } catch (IllegalArgumentException e) { }
    }

    /**
     * Tests changing the value of the drafted dice
     * @see ChangeDiceValueControllerState#chooseDiceValue(int)
     */
    @Test
    public void testChangeDiceValue() {
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        int value = 5;
        Dice dice = new Dice(DiceColor.BLUE, 3);
        currentTurn.setDraftedDice(dice);
        Message m =controller.controllerState.chooseDiceValue(value);

        assertEquals(value, currentTurn.getDraftedDice().getValue());
        assertEquals(ACKNOWLEDGMENT_MESSAGE, m.getType());
    }

    /**
     * Tests changing the value of the drafted dice to an illegal value
     * @see ChangeDiceValueControllerState#chooseDiceValue(int)
     */
    @Test
    public void testChangeDiceValueWhenIllegalValue() {
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        int value = 3;
        Dice dice = new Dice(DiceColor.BLUE, value);
        currentTurn.setDraftedDice(dice);
        Message m = controller.controllerState.chooseDiceValue(7);

        assertEquals(value, currentTurn.getDraftedDice().getValue());
        assertEquals(ERROR_MESSAGE, m.getType());
        try {
            assertNotEquals(controller.controllerState.defaultMessage, m.getParam("message"));
        } catch (NoSuchParamInMessageException e) {
            fail();
        }
    }

    /**
     * Tests ending the current turn in this state
     * @see ChangeDiceValueControllerState#endCurrentTurn()
     */
    @Test
    public void testEndCurrentTurn(){
        Message m = controller.controllerState.endCurrentTurn();
        assertEquals(1,controller.game.getCurrentRound().getCurrentTurn().getNumber());
        assertEquals(ACKNOWLEDGMENT_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of drafting a dice from the draftPool in this state
     * @see ChangeDiceValueControllerState#draftDiceFromDraftPool(Dice)
     */
    @Test
    public void testDraftDiceFromDraftPool(){
        Message m = controller.controllerState.draftDiceFromDraftPool(new Dice(DiceColor.RED));
        assertEquals(ERROR_MESSAGE, m.getType());
    }
    
    /**
     * Tests the impossibility of placing a dice in this state
     * @see ChangeDiceValueControllerState#placeDice(int, int)
     */
    @Test
    public void testPlaceDice(){
        Message m = controller.controllerState.placeDice(0,0);
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of using a toolCard in this state
     * @see ChangeDiceValueControllerState#useToolCard(ToolCard) 
     */
    @Test
    public void testUseToolCard(){
        Message m = controller.controllerState.useToolCard(ToolCard.createTestInstance());
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of choosing a dice from track in this state
     * @see ChangeDiceValueControllerState#chooseDiceFromTrack(Dice, int)
     */
    @Test
    public void testChooseDiceFromTrack(){
        Message m = controller.controllerState.chooseDiceFromTrack(new Dice(DiceColor.RED), 1);
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of moving a dice in this state
     * @see ChangeDiceValueControllerState#moveDice(int, int, int, int)
     */
    @Test
    public void testMoveDice(){
        Message m = controller.controllerState.moveDice(0,0,1,1);
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of incrementing a dice value in this state
     * @see ChangeDiceValueControllerState#incrementDice()
     */
    @Test
    public void testIncrementDice(){
        Message m = controller.controllerState.incrementDice();
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of decrementing a dice value in this state
     * @see ChangeDiceValueControllerState#decrementDice()
     */
    @Test
    public void testDecrementDice(){
        Message m = controller.controllerState.decrementDice();
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of ending a toolCard effect in this state
     * @see ChangeDiceValueControllerState#endToolCardEffect()
     */
    @Test
    public void testEndToolCardEffect(){
        Message m = controller.controllerState.endToolCardEffect();
        assertEquals(ERROR_MESSAGE, m.getType());
    }
    
    
    
}