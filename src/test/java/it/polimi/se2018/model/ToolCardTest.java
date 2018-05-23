package it.polimi.se2018.model;

import it.polimi.se2018.controller.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * @author Lorenzo Minto
 */
public class ToolCardTest {
    private ToolCard toolcard;

    private static String id = "ID";
    private static String title = "title";
    private static String description = "description";
    private static String imageURL = "imageURL";
    private static Integer neededTokens = 1;
    private static Integer tokenUsageMultiplier = 2;
    private static HashMap<String,String> controllerStateRules;
    private static PlacementRule rule;
    private static Properties properties;



    @BeforeClass
    public static void initializeVariables(){
        rule = new ColorPlacementRuleDecorator(new EmptyPlacementRule());
        controllerStateRules = new HashMap<>();

        controllerStateRules.put("StartControllerState","DraftControllerState");
        controllerStateRules.put("DraftControllerState","ChangeDiceValueControllerState");
        controllerStateRules.put("ChangeDiceValueControllerState","EndControllerState");

        properties = new Properties();
        properties.put("id",id);
        properties.put("title",title);
        properties.put("description",description);
        properties.put("imageURL",imageURL);
        properties.put("neededTokens", neededTokens.toString());
        properties.put("tokensUsageMultiplier", tokenUsageMultiplier.toString());
    }

    @Before
    public void setUp(){
        toolcard = new ToolCard(properties, controllerStateRules, rule);
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
        assertEquals(neededTokens.intValue(), toolcard.getNeededTokens());
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
        ToolCard toolCard2 = new ToolCard(properties, controllerStateRules, rule);

        assertTrue(toolCard2.equals(toolcard));
    }

    @Test
    public void testEqualsWhenNotEqual() {

        Properties properties2 = new Properties();

        properties2.put("id","ID2");
        properties2.put("title","title2");
        properties2.put("description","description2");
        properties2.put("imageURL","imageURL2");
        properties2.put("neededTokens", "1");
        properties2.put("tokensUsageMultiplier", "2");

        ToolCard toolCard3 = new ToolCard(properties2, controllerStateRules, rule);

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