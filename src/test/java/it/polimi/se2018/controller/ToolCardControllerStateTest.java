package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static it.polimi.se2018.utils.ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.ViewBoundMessageType.ERROR_MESSAGE;
import static org.junit.Assert.*;

/**
 * Test for {@link ToolCardControllerState} class
 *
 * @author Jacopo Pio Gargano
 */
public class ToolCardControllerStateTest {
    private Controller controller;
    private ToolCard toolCard;
    private WindowPattern wp;

    /**
     * Advances the Game in order to set the ControllerState to ToolCardControllerState
     */
    @Before
    public void setUpGameAndControllerToToolCardControllerState(){
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
        wp = WPManager.getPairsOfPatterns(1).iterator().next();
        while (!wp.getTitle().equals("Batllo") && !wp.getTitle().equals("Shadow Thief")){
            wp = WPManager.getPairsOfPatterns(1).iterator().next();
        }

        controller.launchGame(nicknames);

        for (Player p : controller.game.getPlayers()) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("windowPattern", wp);
            params.put("move", Move.CHOOSE_WINDOW_PATTERN);
            controller.handleMoveMessage(new Message(ControllerBoundMessageType.MOVE, params, p.getID()));
        }


        Properties toolCardProperties = new Properties();
        toolCardProperties.put("id", "id");
        toolCardProperties.put("title", "title");
        toolCardProperties.put("description", "desc");
        toolCardProperties.put("neededTokens", "1");
        toolCardProperties.put("tokensUsageMultiplier", "2");
        toolCardProperties.put("imageURL", "imageURL");

        toolCard = new ToolCard(toolCardProperties, new HashMap<>(), null, null);

        Dice diceToDraft = controller.game.getCurrentRound().getDraftPool().getDices().get(0);
        controller.controllerState.draftDiceFromDraftPool(diceToDraft);
        controller.controllerState.placeDice(1,4);
    }

    /**
     * Tests the impossibility of creating a {@link ToolCardControllerState} when controller is null
     * @see ToolCardControllerState#ToolCardControllerState(Controller)
     */
    @Test
    public void testConstructorWithNullController() {
        try {
            new ToolCardControllerState(null);
            fail();
        } catch (IllegalArgumentException e) { }
    }

    /**
     * Tests using a {@link ToolCard} that is in the drawn set of toolCards
     * Here testing the usage of a specific {@link ToolCard} that can be used when in this state
     * @see ToolCardControllerState#useToolCard(ToolCard) (Dice)
     */
    @Test
    public void testUseToolCard() {
        ToolCardManager manager = new ToolCardManager(new EmptyPlacementRule());
        ToolCard toolCard = null;
        while (toolCard == null || !toolCard.getTitle().equals("Eglomise Brush")) {
            toolCard = manager.getRandomToolCards(1).get(0);
        }

        Message m = controller.controllerState.useToolCard(toolCard);
        assertEquals(toolCard, controller.getActiveToolCard());
        assertTrue(controller.game.getCurrentRound().getCurrentTurn().hasUsedToolCard());
        assertEquals(ACKNOWLEDGMENT_MESSAGE, m.getType());
    }

    /**
     * Tests using a {@link ToolCard} that is not in the drawn set of toolCards
     * @see ToolCardControllerState#useToolCard(ToolCard) (Dice)
     */
    @Test
    public void testUseToolCardWhenNotInDrawnSet() {
        try {
            controller.controllerState.useToolCard(toolCard);
            fail();
        } catch (BadBehaviourRuntimeException e) { }

        assertNull(controller.getActiveToolCard());
        assertFalse(controller.game.getCurrentRound().getCurrentTurn().hasUsedToolCard());
    }

    /**
     * Tests using a {@link ToolCard} when already drafted
     * @see ToolCardControllerState#useToolCard(ToolCard) (Dice)
     */
    @Test
    public void testUseToolCardWhenNotAllowed() {
        Game game = new Game(10,4);
        Properties gameProperties = new Properties();
        gameProperties.setProperty("numberOfRounds","10");
        gameProperties.setProperty("numberOfDicesPerColor","18");
        //this is the only property compared with the above ones
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
        wp = WPManager.getPairsOfPatterns(1).iterator().next();

        controller.launchGame(nicknames);

        for (Player p : controller.game.getPlayers()) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("windowPattern", wp);
            params.put("move", Move.CHOOSE_WINDOW_PATTERN);
            controller.handleMoveMessage(new Message(ControllerBoundMessageType.MOVE, params, p.getID()));
        }

        controller.game.getCurrentRound().getCurrentTurn().setDraftedDice(new Dice(DiceColor.BLUE));

        Properties toolCardProperties = new Properties();
        toolCardProperties.put("id", "LensCutter");
        toolCardProperties.put("title", "title");
        toolCardProperties.put("description", "desc");
        toolCardProperties.put("neededTokens", "1");
        toolCardProperties.put("tokensUsageMultiplier", "2");
        toolCardProperties.put("imageURL", "imageURL");

        ToolCard toolCard = new ToolCard(toolCardProperties, new HashMap<>(), null, null);

        Message m = controller.controllerState.useToolCard(toolCard);

        assertNull(controller.getActiveToolCard());
        assertFalse(controller.game.getCurrentRound().getCurrentTurn().hasUsedToolCard());
        assertEquals(ERROR_MESSAGE, m.getType());
        try {
            assertNotEquals(controller.controllerState.defaultMessage, m.getParam("message"));
        } catch (NoSuchParamInMessageException e) {
            fail();
        }
    }

    /**
     * Tests ending the current turn in this state
     * @see ToolCardControllerState#endCurrentTurn()
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
     * @see ToolCardControllerState#getStatePermissions()
     */
    @Test
    public void testGetStatePermissions(){
        assertNotNull(controller.controllerState.getStatePermissions());
    }
}