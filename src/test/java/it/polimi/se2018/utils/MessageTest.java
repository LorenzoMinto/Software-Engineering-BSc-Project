package it.polimi.se2018.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class MessageTest {

    private Message message;
    private static final String playerID = "playerID";

    @Before
    public void setUp(){
        message = new Message(ViewBoundMessageType.SETUP);
    }

    /**
     * Tests the setter and getter for the playerID field of {@link Message}
     * @see Message#getPlayerID()
     * @see Message#setPlayerID(String)
     */
    @Test
    public void testSetPlayerID(){
        message.setPlayerID(playerID);
        assertEquals(playerID, message.getPlayerID());
    }

    /**
     * Tests the setter and getter for the permissions field of {@link Message}
     * @see Message#getPermissions() ()
     * @see Message#setPermissions(Set)
     */
    @Test
    public void testPermissions(){
        EnumSet<Move> permissions = EnumSet.of(Move.DRAFT_DICE_FROM_DRAFTPOOL, Move.USE_TOOLCARD, Move.END_TURN);
        message.setPermissions(permissions);
        assertEquals(permissions, message.getPermissions());
    }

    /**
     * Tests the retrieval of a specific parameter of the message
     * @see Message#setParams(Map)
     * @see Message#getParam(String)
     */
    @Test
    public void testParameters(){

        Map<String, Object> params = new HashMap<>();
        params.put("id", playerID);

        message.setParams(params);
        try {
            assertEquals(playerID, message.getParam("id"));
        } catch (NoSuchParamInMessageException e) {
            fail();
        }
    }

    /**
     * Tests the retrieval of a specific parameter of the message when parameters are passed in the constructor
     * @see Message#Message(Enum, Map)
     * @see Message#getParam(String)
     */
    @Test
    public void testParametersWithConstructor(){

        Map<String, Object> params = new HashMap<>();
        params.put("id", playerID);

        message = new Message(ControllerBoundMessageType.USE_TOOLCARD, params);
        try {
            assertEquals(playerID, message.getParam("id"));
        } catch (NoSuchParamInMessageException e) {
            fail();
        }
    }

    /**
     * Tests the retrieval of a parameter that is not in the message
     * @see Message#getParam(String)
     */
    @Test
    public void testGetParameterNotInMessage(){

        Map<String, Object> params = new HashMap<>();
        params.put("id", playerID);

        message.setParams(params);
        try {
            assertEquals(playerID, message.getParam("title"));
            fail();
        } catch (NoSuchParamInMessageException e) {}
    }

    @Test
    public void testFastMessage(){
        String messageString = "HELLO!";
        message = new Message(ControllerBoundMessageType.USE_TOOLCARD, messageString);
        try {
            assertEquals(messageString, message.getParam("message") );
        } catch (NoSuchParamInMessageException e) {
            fail();
        }
    }

    @Test
    public void testFastMap(){
        message = new Message(ControllerBoundMessageType.USE_TOOLCARD, Message.fastMap("id",playerID));
        try {
            assertEquals(playerID, message.getParam("id") );
        } catch (NoSuchParamInMessageException e) {
            fail();
        }
    }



}