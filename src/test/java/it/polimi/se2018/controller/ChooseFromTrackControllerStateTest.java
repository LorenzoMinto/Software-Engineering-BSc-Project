
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
 * Test for {@link ChooseFromTrackControllerState} class
 *
 * @author Lorenzo Minto
 * @author Jacopo Pio Gargano
 */
public class ChooseFromTrackControllerStateTest {
    private Controller controller;

    private ToolCard toolCard;

    /**
     * Advances the Game in order to set the ControllerState to ChooseFromTrackControllerState
     */
    @Before
    public void setUpGameAndControllerToChooseFromTrackControllerState(){
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
        toolCardProperties.put("id", "LensCutter");
        toolCardProperties.put("title", "title");
        toolCardProperties.put("description", "desc");
        toolCardProperties.put("neededTokens", "1");
        toolCardProperties.put("tokensUsageMultiplier", "2");
        toolCardProperties.put("imageURL", "imageURL");


        toolCard = new ToolCard(toolCardProperties, new HashMap<>(), null, null);
        toolCard = game.getToolCard(toolCard);

        controller.controllerState.useToolCard(toolCard);

        Dice dice = controller.game.getCurrentRound().getDraftPool().getDices().get(0);
        controller.controllerState.draftDiceFromDraftPool(dice);
    }

    /**
     * Tests the impossibility of creating a {@link ChooseFromTrackControllerState} when controller is null
     * @see ChooseFromTrackControllerState#ChooseFromTrackControllerState(Controller)
     */
    @Test
    public void testConstructorWithNullController() {
        try {
            new ChooseFromTrackControllerState(null);
            fail();
        } catch (IllegalArgumentException e) { }
    }

    /**
     * Tests choosing a dice from the {@link Track}
     * @see ChooseFromTrackControllerState#chooseDiceFromTrack(Dice, int)
     */
    @Test
    public void testChooseDiceFromTrack() {
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        Dice draftedDice = currentTurn.getDraftedDice();

        List<Dice> dices = new ArrayList<>();
        Dice dice1 = new Dice(DiceColor.BLUE, 2);
        Dice dice2 = new Dice(DiceColor.RED, 3);

        if(draftedDice.equals(dice1)){
            dice1 = new Dice(DiceColor.YELLOW,3);
        }

        dices.add(dice1);
        dices.add(dice2);

        int slotNumber = 0;
        controller.game.getTrack().processDices(dices);

        Message m = controller.controllerState.chooseDiceFromTrack(dice1, slotNumber);
        assertEquals(ACKNOWLEDGMENT_MESSAGE, m.getType());

        assertNull(currentTurn.getTrackChosenDice());
        assertFalse(controller.game.getTrack().getDicesFromSlotNumber(slotNumber).contains(dice1));
        assertTrue(controller.game.getTrack().getDicesFromSlotNumber(slotNumber).contains(draftedDice));
        assertEquals(dice1, currentTurn.getDraftedDice());
    }

    /**
     * Tests the impossibility of choosing a dice from the {@link Track} when the selected {@link TrackSlot} does not exist
     * @see ChooseFromTrackControllerState#chooseDiceFromTrack(Dice, int)
     */
    @Test
    public void testChooseDiceFromTrackWhenTrackSlotDoesNotExist() {
        int slotNumber = 0;
        Dice dice1 = new Dice(DiceColor.BLUE, 2);
        Message m = controller.controllerState.chooseDiceFromTrack(dice1, slotNumber);
        assertEquals(ERROR_MESSAGE, m.getType());
        try {
            assertNotEquals(controller.controllerState.defaultMessage, m.getParam("message"));
        } catch (NoSuchParamInMessageException e) {
            fail();
        }
    }

    /**
     * Tests the impossibility of choosing a dice from the {@link Track} when the selected dice is not in the selected {@link TrackSlot}
     * @see ChooseFromTrackControllerState#chooseDiceFromTrack(Dice, int)
     */
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
        try {
            assertNotEquals(controller.controllerState.defaultMessage, m.getParam("message"));
        } catch (NoSuchParamInMessageException e) {
            fail();
        }
    }

    /**
     * Tests ending the current turn in this state
     * @see ChooseFromTrackControllerState#endCurrentTurn()
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
     * @see ChooseFromTrackControllerState#getStatePermissions()
     */
    @Test
    public void testGetStatePermissions(){
        assertNotNull(controller.controllerState.getStatePermissions());
    }
}
