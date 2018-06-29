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
 * Test for {@link PlaceControllerState} class
 *
 * @author Lorenzo Minto
 * @author Jacopo Pio Gargano
 */
public class PlaceControllerStateTest {
    private Controller controller;

    private Dice dice;
    private static final int r0 = 2;
    private static final int c0 = 2;

    /**
     * Advances the Game in order to set the ControllerState to PlaceControllerState
     */
    @Before
    public void setUpGameAndControllerToPlaceControllerState(){
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

        Cell[][] pattern = new Cell[3][3];
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                pattern[i][j] = new Cell();
            }
        }
        pattern[r0][c0].setDice(new Dice(DiceColor.RED));
        WindowPattern wp = new WindowPattern("id", "title", "",5, pattern);

        controller.launchGame(nicknames);

        for (Player p : controller.game.getPlayers()) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("windowPattern", wp);
            params.put("move", Move.CHOOSE_WINDOW_PATTERN);
            controller.handleMoveMessage(new Message(ControllerBoundMessageType.MOVE, params, p.getID()));
        }

        dice = game.getCurrentRound().getDraftPool().getDices().get(0);
    }

    /**
     * Tests the impossibility of creating a {@link PlaceControllerState} when controller is null
     * @see PlaceControllerState#PlaceControllerState(Controller)
     */
    @Test
    public void testConstructorWithNullController() {
        try {
            new PlaceControllerState(null);
            fail();
        } catch (IllegalArgumentException e) { }
    }

    /**
     * Tests placing a dice on an empty {@link Cell}
     * @see PlaceControllerState#placeDice(int, int)
     */
    @Test
    public void testPlaceDice() {
        controller.controllerState.draftDiceFromDraftPool(dice);
        Message m = controller.controllerState.placeDice(0,0);
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        WindowPattern wp = currentTurn.getPlayer().getWindowPattern();
        assertEquals(dice, wp.getDiceOnCell(0,0));
        assertNull(currentTurn.getDraftedDice());

        assertEquals(ACKNOWLEDGMENT_MESSAGE, m.getType());
    }

    /**
     * Tests placing a dice on a not allowed {@link Cell}
     * @see PlaceControllerState#placeDice(int, int)
     * @see BorderPlacementRuleDecorator
     */
    @Test
    public void testPlaceDiceAtIllegalPosition() {
        controller.controllerState.draftDiceFromDraftPool(dice);
        Message m = controller.controllerState.placeDice(1,1);
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        WindowPattern wp = currentTurn.getPlayer().getWindowPattern();
        assertEquals(dice, currentTurn.getDraftedDice());
        assertNull(wp.getDiceOnCell(1,1));

        assertEquals(ERROR_MESSAGE, m.getType());
        try {
            assertNotEquals(controller.controllerState.defaultMessage, m.getParam("message"));
        } catch (NoSuchParamInMessageException e) {
            fail();
        }
    }

    /**
     * Tests the impossibility of placing a dice on a cell that already has a dice
     * @see PlaceControllerState#placeDice(int, int)
     */
    @Test
    public void testPlaceDiceOnCellWithDice(){
        controller.controllerState.draftDiceFromDraftPool(dice);
        Message m = controller.controllerState.placeDice(r0,c0);
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        WindowPattern wp = currentTurn.getPlayer().getWindowPattern();
        assertEquals(dice, currentTurn.getDraftedDice());
        assertNull(wp.getDiceOnCell(1,1));

        assertEquals(ERROR_MESSAGE, m.getType());
        try {
            assertNotEquals(controller.controllerState.defaultMessage, m.getParam("message"));
        } catch (NoSuchParamInMessageException e) {
            fail();
        }
    }

    @Test
    public void testReturnDiceToDraftPool(){
        ToolCardManager manager = new ToolCardManager(new EmptyPlacementRule());
        ToolCard toolCard = null;
        while (toolCard == null || !toolCard.getTitle().equals("Flux Brush")) {
            toolCard = manager.getRandomToolCards(1).get(0);
        }

        controller.controllerState.useToolCard(toolCard);
        controller.controllerState.draftDiceFromDraftPool(dice);
        Message m = controller.controllerState.returnDiceToDraftPool();
        assertEquals(ACKNOWLEDGMENT_MESSAGE, m.getType());
        m = controller.controllerState.placeDice(r0,c0);
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests ending the current turn in this state
     * @see PlaceControllerState#endCurrentTurn()
     */
    @Test
    public void testEndCurrentTurn(){
        controller.controllerState.draftDiceFromDraftPool(dice);
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
        controller.controllerState.draftDiceFromDraftPool(dice);
        //Now actually in PlaceControllerState
        Message m = controller.controllerState.draftDiceFromDraftPool(dice);
        assertEquals(ERROR_MESSAGE, m.getType());
    }
    
    /**
     * Tests the impossibility of using a toolCard in this state
     * @see ControllerState#useToolCard(ToolCard)
     */
    @Test
    public void testUseToolCard(){
        Properties toolCardProperties = new Properties();
        toolCardProperties.put("id", "EglomiseBrush");
        toolCardProperties.put("title", "title");
        toolCardProperties.put("description", "desc");
        toolCardProperties.put("neededTokens", "1");
        toolCardProperties.put("tokensUsageMultiplier", "2");
        toolCardProperties.put("imageURL", "imageURL");

        ToolCard toolCard = new ToolCard(toolCardProperties, new HashMap<>(), null, null);

        controller.controllerState.draftDiceFromDraftPool(dice);
        Message m = controller.controllerState.useToolCard(toolCard);
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of choosing a dice from track in this state
     * @see ControllerState#chooseDiceFromTrack(Dice, int)
     */
    @Test
    public void testChooseDiceFromTrack(){
        controller.controllerState.draftDiceFromDraftPool(dice);
        Message m = controller.controllerState.chooseDiceFromTrack(dice, 1);
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of moving a dice in this state
     * @see ControllerState#moveDice(int, int, int, int)
     */
    @Test
    public void testMoveDice(){
        controller.controllerState.draftDiceFromDraftPool(dice);
        Message m = controller.controllerState.moveDice(0,0,1,1);
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of incrementing a dice value in this state
     * @see ControllerState#incrementDice()
     */
    @Test
    public void testIncrementDice(){
        controller.controllerState.draftDiceFromDraftPool(dice);
        Message m = controller.controllerState.incrementDice();
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of decrementing a dice value in this state
     * @see ControllerState#decrementDice()
     */
    @Test
    public void testDecrementDice(){
        controller.controllerState.draftDiceFromDraftPool(dice);
        Message m = controller.controllerState.decrementDice();
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of choosing a dice value in this state
     * @see ControllerState#chooseDiceValue(int)
     */
    @Test
    public void testChooseDiceValue(){
        controller.controllerState.draftDiceFromDraftPool(dice);
        Message m = controller.controllerState.chooseDiceValue(1);
        assertEquals(ERROR_MESSAGE, m.getType());
    }


    /**
     * Tests the impossibility of ending a toolCard effect in this state
     * @see ControllerState#endToolCardEffect()
     */
    @Test
    public void testEndToolCardEffect(){
        controller.controllerState.draftDiceFromDraftPool(dice);
        Message m = controller.controllerState.endToolCardEffect();
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Testing the retrieval of the state permissions
     * @see PlaceControllerState#getStatePermissions()
     */
    @Test
    public void testGetStatePermissions(){
        assertNotNull(controller.controllerState.getStatePermissions());
    }
}