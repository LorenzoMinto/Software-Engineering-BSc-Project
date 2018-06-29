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
 * Test for {@link MoveControllerState} class
 *
 * @author Lorenzo Minto
 * @author Jacopo Pio Gargano
 */
public class MoveControllerStateTest {
    private Controller controller;

    private ToolCard toolCard;
    private ToolCard toolCardMoveCounter;
    private ToolCard toolCardEndToolCardEffect;

    private Dice blueDice;

    private static final int r0 = 0;
    private static final int c0 = 0;
    private static final int r1 = 1;
    private static final int c1 = 1;
    private static final int r2 = 2;
    private static final int c2 = 2;
    private Dice diceToDraft;

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
        gameProperties.setProperty("persistencyPath","globalrankings.xml");

        controller = new Controller(game, gameProperties);

        Set<String> nicknames = new HashSet<>(Arrays.asList("Johnnyfer", "Rubens"));

        Cell[][] pattern = new Cell[3][3];
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                pattern[i][j] = new Cell();
            }
        }
        Dice redDice = new Dice(DiceColor.RED, 2);
        blueDice = new Dice(DiceColor.BLUE, 3);
        pattern[r0][c0].setDice(redDice);
        pattern[r2][c2].setDice(blueDice);
        WindowPattern wp = new WindowPattern("id", "title","", 5, pattern);

        controller.launchGame(nicknames);

        for (Player p : controller.game.getPlayers()) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("windowPattern", wp);
            params.put("move", Move.CHOOSE_WINDOW_PATTERN);
            controller.handleMoveMessage(new Message(ControllerBoundMessageType.MOVE, params, p.getID()));
        }

        Properties toolCardProperties = new Properties();
        toolCardProperties.put("id", "EglomiseBrush");
        toolCardProperties.put("title", "title");
        toolCardProperties.put("description", "desc");
        toolCardProperties.put("neededTokens", "1");
        toolCardProperties.put("tokensUsageMultiplier", "2");
        toolCardProperties.put("imageURL", "imageURL");

        toolCard = new ToolCard(toolCardProperties, new HashMap<>(), null, null);

        ToolCardManager manager = new ToolCardManager(new EmptyPlacementRule());

        while (toolCardMoveCounter == null || !toolCardMoveCounter.getTitle().equals("Tap Wheel")) {
            toolCardMoveCounter = manager.getRandomToolCards(1).get(0);
        }

        manager = new ToolCardManager(new EmptyPlacementRule());

        while (toolCardEndToolCardEffect == null || !toolCardEndToolCardEffect.getTitle().equals("Eglomise Brush")) {
            toolCardEndToolCardEffect = manager.getRandomToolCards(1).get(0);
        }

        diceToDraft = controller.game.getCurrentRound().getDraftPool().getDices().get(0);

    }

    /**
     * Tests the impossibility of creating a {@link MoveControllerState} when controller is null
     * @see MoveControllerState#MoveControllerState(Controller)
     */
    @Test
    public void testConstructorWithNullController() {
        controller.controllerState.useToolCard(toolCard);

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
        controller.controllerState.useToolCard(toolCard);

        Message m = controller.controllerState.moveDice(r2,c2,r0,c1);

        Player player = controller.game.getCurrentRound().getCurrentTurn().getPlayer();
        WindowPattern wp = player.getWindowPattern();

        assertNull(wp.getDiceOnCell(r2,c2));
        assertEquals(blueDice, wp.getDiceOnCell(r0,c1));
        assertEquals(ACKNOWLEDGMENT_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of moving a dice from an initial cell with no dice to another cell
     * @see MoveControllerState#moveDice(int, int, int, int)
     */
    @Test
    public void testMoveDiceWhenNoDiceInPosition() {
        controller.controllerState.useToolCard(toolCard);

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
        controller.controllerState.useToolCard(toolCard);

        Message m = controller.controllerState.moveDice(r1,c1,r2,c2);
        assertEquals(ERROR_MESSAGE, m.getType());
        try {
            assertNotEquals(controller.controllerState.defaultMessage, m.getParam("message"));
        } catch (NoSuchParamInMessageException e) {
            fail();
        }
    }

    /**
     * Tests the ending a toolCard effect in this state
     * @see ControllerState#endToolCardEffect()
     */
    @Test
    public void testEndToolCardEffect(){
        List<Dice> dices = new ArrayList<>();
        dices.add(new Dice(DiceColor.RED, 1));
        controller.game.getTrack().processDices(dices);
        controller.controllerState.useToolCard(toolCardMoveCounter);
        controller.controllerState.chooseDiceFromTrack(dices.get((0)), 0);
        Message m = controller.controllerState.endToolCardEffect();
        assertEquals(ACKNOWLEDGMENT_MESSAGE, m.getType());
    }

    /**
     * Tests the ending a toolCard effect in this state when already drafted and placed
     * @see ControllerState#endToolCardEffect()
     */
    @Test
    public void testEndToolCardEffectHasDraftedAndPlaced(){
        controller.controllerState.draftDiceFromDraftPool(diceToDraft);
        controller.controllerState.placeDice(r0,c1);

        List<Dice> dices = new ArrayList<>();
        dices.add(new Dice(DiceColor.RED, 1));
        controller.game.getTrack().processDices(dices);
        controller.controllerState.useToolCard(toolCardMoveCounter);
        controller.controllerState.chooseDiceFromTrack(dices.get((0)), 0);
        Message m = controller.controllerState.endToolCardEffect();
        assertEquals(ACKNOWLEDGMENT_MESSAGE, m.getType());

        diceToDraft = controller.game.getCurrentRound().getDraftPool().getDices().get(0);
        m = controller.controllerState.draftDiceFromDraftPool(diceToDraft);
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests ending the current turn in this state
     * @see MoveControllerState#endCurrentTurn()
     */
    @Test
    public void testEndCurrentTurn(){
        controller.controllerState.useToolCard(toolCard);

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
        controller.controllerState.useToolCard(toolCard);

        Message m = controller.controllerState.draftDiceFromDraftPool(new Dice(DiceColor.RED));
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of placing a dice in this state
     * @see ControllerState#placeDice(int, int)
     */
    @Test
    public void testPlaceDice(){
        controller.controllerState.useToolCard(toolCard);

        Message m = controller.controllerState.placeDice(0,0);
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of using a toolCard in this state
     * @see ControllerState#useToolCard(ToolCard)
     */
    @Test
    public void testUseToolCard(){
        controller.controllerState.useToolCard(toolCard);

        Message m = controller.controllerState.useToolCard(toolCard);
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of choosing a dice from track in this state
     * @see ControllerState#chooseDiceFromTrack(Dice, int)
     */
    @Test
    public void testChooseDiceFromTrack(){
        controller.controllerState.useToolCard(toolCard);

        Message m = controller.controllerState.chooseDiceFromTrack(new Dice(DiceColor.RED), 1);
        assertEquals(ERROR_MESSAGE, m.getType());
    }
    
    /**
     * Tests the impossibility of incrementing a dice value in this state
     * @see ControllerState#incrementDice()
     */
    @Test
    public void testIncrementDice(){
        controller.controllerState.useToolCard(toolCard);

        Message m = controller.controllerState.incrementDice();
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of decrementing a dice value in this state
     * @see ControllerState#decrementDice()
     */
    @Test
    public void testDecrementDice(){
        controller.controllerState.useToolCard(toolCard);

        Message m = controller.controllerState.decrementDice();
        assertEquals(ERROR_MESSAGE, m.getType());
    }

    /**
     * Tests the impossibility of choosing a dice value in this state
     * @see ControllerState#chooseDiceValue(int)
     */
    @Test
    public void testChooseDiceValue(){
        controller.controllerState.useToolCard(toolCard);

        Message m = controller.controllerState.chooseDiceValue(1);
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
     * @see MoveControllerState#getStatePermissions()
     */
    @Test
    public void testGetStatePermissions(){
        assertNotNull(controller.controllerState.getStatePermissions());
    }
}