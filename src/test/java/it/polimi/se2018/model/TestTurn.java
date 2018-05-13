package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestTurn {

    private Turn turn;

    private static User user;
    private static PrivateObjectiveCard privateObjectiveCard;
    private static Player player;
    private static Dice dice;
    private static ToolCard toolCard;
    private static String playerName;



    @BeforeClass
    public static void initializeVariables() {
        user = new User(1, "username");
        privateObjectiveCard = new PrivateObjectiveCard(null,null, null, DiceColors.RED);
        playerName = "player";
        TestTurn.player = new Player(user, playerName, privateObjectiveCard);
        dice = new Dice(DiceColors.RED);
        toolCard = ToolCard.createTestInstance();

    }

    @Before
    public void initializeTurnWithPlayer(){
        turn = new Turn(0, player);
    }

    //to be run with setDraftedDice test
    @Test
    public void testHasActuallyDrafted(){
        turn.setDraftedDice(dice);
        assertTrue(turn.hasDrafted());
    }

    @Test
    public void testHasNotDrafted(){
        assertNull(turn.getDraftedDice());
        assertFalse(turn.hasDrafted());
    }

    @Test
    public void testHasDraftedAndPlaced(){
        turn.setDraftedDice(dice);
//        assertNotNull(turn.getDraftedDice());
        turn.setDraftedAndPlaced();
        assertTrue(turn.hasDraftedAndPlaced());
    }

    @Test
    public void testHasDraftedAndPlacedWithoutDrafting(){
        try{
            assertNull(turn.getDraftedDice());
            turn.setDraftedAndPlaced();
            fail();
        }catch (IllegalStateException e){}
    }

    @Test
    public void testHasActuallyUsedToolCard(){
        turn.setUsedToolCard(toolCard);
        assertTrue(turn.hasUsedToolCard());
    }

    @Test
    public void testHasNotUsedToolCard(){
        assertFalse(turn.hasUsedToolCard());
    }

    @Test
    public void testSetNullUsedToolCard(){
        try {
            turn.setUsedToolCard(null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    @Test
    public void getSlotOfTrackChosenDice(){
        try {
            turn.getSlotOfTrackChosenDice();
            fail();
        }catch (RuntimeException e){}
    }

    @Test
    public void testSetSlotOfTrackChosenDice(){
        turn.setSlotOfTrackChosenDice(1);
        assertEquals(1,turn.getSlotOfTrackChosenDice());
    }

    @Test
    public void testSetSlotOfTrackChosenDiceWithNegativeValue(){
        try{
            turn.setSlotOfTrackChosenDice(-1);
            fail();
        }catch (IllegalArgumentException e){}
    }

    @Test
    public void testSetSlotOfTrackChosenDiceWithWrongValue(){
        try{
            turn.setSlotOfTrackChosenDice(10);
            fail();
        }catch (IllegalArgumentException e){}
    }

    @Test
    public void testSetDraftedDice(){
        turn.setDraftedDice(dice);
        assertNotNull(turn.getDraftedDice());
    }

    @Test
    public void testSetNullDraftedDice(){
        try {
            turn.setDraftedDice(null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    @Test
    public void testSetTrackChosenDice(){
        turn.setTrackChosenDice(dice);
        assertNotNull(turn.getTrackChosenDice());
    }

    @Test
    public void testSetNullTrackChosenDice(){
        try {
            turn.setTrackChosenDice(null);
            fail();
        }catch (IllegalArgumentException e){}
    }

    @Test
    public void testIsCurrentPlayer(){
        Player playerTest = turn.getPlayer();
        assertTrue(turn.isCurrentPlayer(playerTest));
    }

    @Test
    public void testIsCurrentPlayerSameName(){
        Player playerTest = new Player(user, playerName, privateObjectiveCard);
        assertTrue(turn.isCurrentPlayer(playerTest));
    }

    @Test
    public void testDifferentPlayerIsCurrentPlayer(){
        Player playerTest = new Player(user, "differentName", privateObjectiveCard);
        assertFalse(turn.isCurrentPlayer(playerTest));
    }

    @Test
    public void testNullPlayerIsCurrentPlayer(){
        Player playerTest = null;
        try{
            turn.isCurrentPlayer(null);
            fail();
        }catch (IllegalArgumentException e){}
    }



}