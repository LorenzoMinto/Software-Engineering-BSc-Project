package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.ControllerBoundMessageType;
import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.Move;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static it.polimi.se2018.model.GameStatus.ENDED;
import static it.polimi.se2018.utils.ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.ViewBoundMessageType.ERROR_MESSAGE;
import static org.junit.Assert.*;


/**
 * Test for {@link Controller} class
 * This class cannot be fully tested. Only simple behaviors are tested. Testing this class would mean simulating the game.
 *
 * Many of the methods and behaviors of {@link Controller} are tested by other tests.
 * Since the {@link Controller} follows the State Design Pattern, part of its behavior is mainly tested in the State tests.
 * The remaining behavior is not worth testing, as it would involve creating Messages of all types and exchanging them,
 * which in practice is playing the game.
 *
 * @author Jacopo Pio Gargano
 */
public class ControllerTest {

    private  Controller controller;
    private List<String> nicknames;

    @Before
    public void init(){

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

        nicknames = new ArrayList<>();
        nicknames.add("Johnnyfer");
        nicknames.add("Rubens");
        nicknames.add("Frida");
    }

    /**
     * Launches game and sets windowPatterns for players
     */
    private void launchGameAndSetWindowPatterns() {
        WindowPatternManager WPManager = new WindowPatternManager();
        WindowPattern wp = WPManager.getPairsOfPatterns(1).iterator().next();

        controller.launchGame(new HashSet<>(nicknames));

        for (Player player : controller.game.getPlayers()) {
            HashMap<String, Object> params = new HashMap<>();

            params.put("windowPattern", wp);
            params.put("move", Move.CHOOSE_WINDOW_PATTERN);
            controller.handleMoveMessage(new Message(ControllerBoundMessageType.MOVE, params, player.getID()));
        }
    }

    /**
     * Tests setting a specific controller state
     * @see Controller#setControllerState(ControllerState)
     */
    @Test
    public void testSetControllerState() {
        launchGameAndSetWindowPatterns();

        controller.setControllerState(controller.stateManager.getToolCardState());
        Message m = controller.controllerState.draftDiceFromDraftPool(new Dice(DiceColor.RED));
        assertEquals(ERROR_MESSAGE, m.getType());

        ToolCardManager manager = new ToolCardManager(new EmptyPlacementRule());
        ToolCard toolCard = null;
        while (toolCard == null || !toolCard.getTitle().equals("Eglomise Brush")) {
            toolCard = manager.getRandomToolCards(1).get(0);
        }
        m = controller.controllerState.useToolCard(toolCard);
        assertEquals(ACKNOWLEDGMENT_MESSAGE, m.getType());
    }

    /**
     * Tests the retrieval of the config properties
     * @see Controller#getConfigProperty(String)
     */
    @Test
    public void getConfigProperty() {


        assertEquals(4,controller.getConfigProperty("maxNumberOfPlayers"));
        assertEquals(10,controller.getConfigProperty("numberOfRounds"));
    }

    /**
     * Tests the game ends if there are only two players and one of them disconnects
     * @see Controller#playerLostConnection(String)
     */
    @Test
    public void testPlayerLostConnectionGameWithTwoPlayers(){
        nicknames.remove("Frida");
        launchGameAndSetWindowPatterns();

        controller.playerLostConnection(nicknames.get(0));

        assertEquals(2, controller.game.getPlayers().size());
        assertEquals(ENDED,controller.game.getStatus());
    }


    /**
     * Tests the game advances if there are three players and one of them disconnects
     * @see Controller#playerLostConnection(String)
     */
    @Test
    public void testPlayerLostConnection(){
        launchGameAndSetWindowPatterns();

        controller.playerLostConnection(nicknames.get(0));

        assertEquals(3, controller.game.getPlayers().size());
        assertEquals(1,controller.game.getCurrentRound().getCurrentTurn().getNumber());
    }

    /**
     * Tests the game advances correctly if a player disconnects and reconnects (three players game)
     * @see Controller#playerLostConnection(String)
     */
    @Test
    public void testPlayerReconnected(){
        launchGameAndSetWindowPatterns();

        controller.playerLostConnection(nicknames.get(0));
        controller.playerRestoredConnection(nicknames.get(0));

        assertEquals(3, controller.game.getPlayers().size());
        assertEquals(1,controller.game.getCurrentRound().getCurrentTurn().getNumber());
    }

    /**
     * Tests the game ends after 4 rounds
     * @see Controller#advanceGame()
     */
    @Test
    public void testRunAllGame(){
        nicknames.remove("Frida");
        launchGameAndSetWindowPatterns();

        for(int i=0; i < 2; i++){
            controller.handleMoveMessage(new Message(ControllerBoundMessageType.MOVE,Message.fastMap("move",Move.END_TURN),"Johnnyfer"));
            controller.handleMoveMessage(new Message(ControllerBoundMessageType.MOVE,Message.fastMap("move",Move.END_TURN),"Rubens"));
            controller.handleMoveMessage(new Message(ControllerBoundMessageType.MOVE,Message.fastMap("move",Move.END_TURN),"Rubens"));
            controller.handleMoveMessage(new Message(ControllerBoundMessageType.MOVE,Message.fastMap("move",Move.END_TURN),"Johnnyfer"));
            controller.handleMoveMessage(new Message(ControllerBoundMessageType.MOVE,Message.fastMap("move",Move.END_TURN),"Rubens"));
            controller.handleMoveMessage(new Message(ControllerBoundMessageType.MOVE,Message.fastMap("move",Move.END_TURN),"Johnnyfer"));
            controller.handleMoveMessage(new Message(ControllerBoundMessageType.MOVE,Message.fastMap("move",Move.END_TURN),"Johnnyfer"));
            controller.handleMoveMessage(new Message(ControllerBoundMessageType.MOVE,Message.fastMap("move",Move.END_TURN),"Rubens"));
        }

        assertEquals(3,controller.game.getCurrentRound().getNumber());
        assertEquals(3,controller.game.getCurrentRound().getCurrentTurn().getNumber());
        assertEquals(ENDED,controller.game.getStatus());
    }
}
