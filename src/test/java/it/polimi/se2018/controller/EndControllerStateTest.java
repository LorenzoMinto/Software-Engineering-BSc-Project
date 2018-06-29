package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.ControllerBoundMessageType;
import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.Move;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static it.polimi.se2018.utils.ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.ViewBoundMessageType.ERROR_MESSAGE;
import static org.junit.Assert.*;

/**
 * Test for {@link EndControllerState} class
 *
 * @author Jacopo Pio Gargano
 */
public class EndControllerStateTest {
    private Controller controller;

    private ToolCard toolCard;

    /**
     * Advances the Game in order to set the ControllerState to EndControllerState
     */
    @Before
    public void setUpGameAndControllerToEndControllerState(){
        Game game = new Game(10,4);
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

        Properties properties = new Properties();

        properties.put("id","ID1");
        properties.put("title","title1");
        properties.put("description","description1");
        properties.put("imageURL","imageURL1");
        properties.put("neededTokens", "1");
        properties.put("tokensUsageMultiplier", "2");

        toolCard = new ToolCard(properties, new HashMap<>(), null, new HashSet<>());

        controller.controllerState = controller.stateManager.getEndControllerState();
    }

    /**
     * Tests the impossibility of creating a {@link EndControllerState} when controller is null
     * @see EndControllerState#EndControllerState(Controller)
     */
    @Test
    public void testConstructorWithNullController() {
        try {
            new EndControllerState(null);
            fail();
        } catch (IllegalArgumentException e) { }
    }

    /**
     * Tests ending the current turn in this state
     * @see EndControllerState#endCurrentTurn()
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
        Message m = controller.controllerState.useToolCard(toolCard);
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
     * Tests the impossibility of moving a dice in this state
     * @see ControllerState#moveDice(int, int, int, int)
     */
    @Test
    public void testMoveDice(){
        Message m = controller.controllerState.moveDice(0,0,1,1);
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
     * @see EndControllerState#getStatePermissions()
     */
    @Test
    public void testGetStatePermissions(){
        assertNotNull(controller.controllerState.getStatePermissions());
    }

}