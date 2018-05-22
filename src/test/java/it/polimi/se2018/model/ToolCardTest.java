package it.polimi.se2018.model;

import it.polimi.se2018.controller.*;
import it.polimi.se2018.utils.ConfigImporter;
import it.polimi.se2018.utils.NoConfigParamFoundException;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.*;

public class ToolCardTest {
    private ToolCard toolcard;

    private String id = "ID";
    private String title = "ID";
    private String description = "ID";
    private String imageURL = "ID";
    private int neededTokens = 1;
    private int tokenUsageMultiplier = 2;
    private HashMap<String,String> controllerStateRules;
    private PlacementRule rule;



    @Before
    public void setUp() throws Exception {
        rule = new ColorPlacementRuleDecorator(new EmptyPlacementRule());
        controllerStateRules = new HashMap<>();

        controllerStateRules.put("StartControllerState","DraftControllerState");
        controllerStateRules.put("DraftControllerState","ChangeDiceValueControllerState");
        controllerStateRules.put("ChangeDiceValueControllerState","EndControllerState");

        toolcard = new ToolCard(id, title, description, imageURL, neededTokens, tokenUsageMultiplier,
        controllerStateRules, rule);
    }

    @Test
    public void testUse() {
        int prevTokens = toolcard.getNeededTokens();
        toolcard.use();
        assertEquals(prevTokens*2, toolcard.getNeededTokens());
        toolcard.use();
        assertEquals(prevTokens*2, toolcard.getNeededTokens());
    }

    @Test
    public void testGetNeededTokens(){
        assertEquals(neededTokens, toolcard.getNeededTokens());
    }

    @Test
    public void testGetTokensUsed(){
        toolcard.use();
        assertEquals(1, toolcard.getTokensUsed());
    }

    @Test
    public void testGetPlacementRule(){
        assertEquals(rule, toolcard.getPlacementRule());
    }

    @Test
    public void testGetTitle(){
        assertEquals(title, toolcard.getTitle());
    }

    @Test
    public void testGetDescription(){
        assertEquals(description,toolcard.getDescription());
    }

    @Test
    public void testGetImageURL(){
        assertEquals(imageURL, toolcard.getImageURL());
    }

    @Test
    public void testNeedsDrafting(){
        assertTrue(toolcard.needsDrafting());
        //TODO: needs a negative (instantiate toolcard that doesn't need to draft and test.
    }

    @Test
    public void testCreateTestInstance() {
        assertNotNull(ToolCard.createTestInstance());
    }

    @Test
    public void testNextStateID(){
        Game game = new Game(2,3);
        Properties prop = new Properties();
        prop.setProperty("numberOfDicesPerColor","18");
        prop.setProperty("numberOfToolCards","3");
        prop.setProperty("numberOfPublicObjectiveCards","2");

        Controller controller = new Controller(game, prop);

        assertEquals("DraftControllerState", toolcard.nextStateID(new StartControllerState(controller)));
        assertEquals("ChangeDiceValueControllerState", toolcard.nextStateID(new DraftControllerState(controller)));
        assertEquals("EndControllerState", toolcard.nextStateID(new ChangeDiceValueControllerState(controller)));
    }

    @Test
    public void testEquals() {
        ToolCard toolCard2 = new ToolCard(id, title, description, imageURL, neededTokens, tokenUsageMultiplier,
                controllerStateRules, rule);

        assertTrue(toolCard2.equals(toolcard));
    }

    @Test
    public void testEqualsWhenNotEqual() {
        ToolCard toolCard3 = new ToolCard("ID2", "title2", "description2", "imageURL2", 1, 2,
                controllerStateRules, rule);

        assertFalse(toolCard3.equals(toolcard));
    }

    @Test
    public void testEqualsWhenGivenSameObject() {
        assertTrue(toolcard.equals(toolcard));
    }

    @Test
    public void testEqualsWhenGivenAnotherTypeOfObject() {
        assertFalse(toolcard.equals("this"));
    }

    @Test
    public void testHashCode() {
        assertNotNull(toolcard.hashCode());
    }

}