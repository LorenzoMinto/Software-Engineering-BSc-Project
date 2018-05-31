package it.polimi.se2018.model;

import it.polimi.se2018.controller.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Test for {@link ToolCard} class
 *
 * @author Lorenzo Minto
 */
public class ToolCardTest {
    private ToolCard toolCard;

    private static String title = "title";
    private static String description = "description";
    private static String imageURL = "imageURL";
    private static Integer neededTokens = 1;
    private static Map<String,String> controllerStateRules;
    private static PlacementRule rule;
    private static Properties properties;


    /**
     * Initializing all needed variables for the tests
     */
    @BeforeClass
    public static void initializeVariables(){
        rule = new ColorPlacementRuleDecorator(new EmptyPlacementRule());
        controllerStateRules = new HashMap<>();

        controllerStateRules.put("StartControllerState","DraftControllerState");
        controllerStateRules.put("DraftControllerState","ChangeDiceValueControllerState");
        controllerStateRules.put("ChangeDiceValueControllerState","EndControllerState");

        properties = new Properties();
        properties.put("id", "ID");
        properties.put("title",title);
        properties.put("description",description);
        properties.put("imageURL",imageURL);
        properties.put("neededTokens", neededTokens.toString());
        properties.put("tokensUsageMultiplier", Integer.valueOf(2).toString());
    }

    /**
     * Initializing the ToolCard. This is done before each test
     */
    @Before
    public void initializeToolCard(){
        toolCard = new ToolCard(properties, controllerStateRules, rule);
    }

    /**
     * Tests the usage of the ToolCard, which has an effect on the tokens needed to use the toolCard
     */
    @Test
    public void testUse() {
        int prevTokens = toolCard.getNeededTokens();
        toolCard.use();
        assertEquals(prevTokens*2, toolCard.getNeededTokens());
        toolCard.use();
        assertEquals(prevTokens*2, toolCard.getNeededTokens());
    }

    /**
     * Tests the retrieval of the needed tokens to use a ToolCard
     */
    @Test
    public void testGetNeededTokens(){
        assertEquals(neededTokens.intValue(), toolCard.getNeededTokens());
    }

    /**
     * Tests the retrieval and the setting of the used tokens of a ToolCard
     */
    @Test
    public void testGetTokensUsed(){
        int prevTokens = toolCard.getNeededTokens();
        toolCard.use();
        assertEquals(prevTokens, toolCard.getTokensUsed());
    }

    /**
     * Tests the retrieval of the {@link PlacementRule} of a ToolCard
     */
    @Test
    public void testGetPlacementRule(){
        assertEquals(rule, toolCard.getPlacementRule());
    }

    /**
     * Tests the retrieval of the title of a ToolCard
     */
    @Test
    public void testGetTitle(){
        assertEquals(title, toolCard.getTitle());
    }

    /**
     * Tests the retrieval of the description of a ToolCard
     */
    @Test
    public void testGetDescription(){
        assertEquals(description, toolCard.getDescription());
    }

    /**
     * Tests the retrieval of the imageURL of a ToolCard
     */
    @Test
    public void testGetImageURL(){
        assertEquals(imageURL, toolCard.getImageURL());
    }


    /**
     * Tests the {@link ToolCard} needsDrafting property
     */
    @Test
    public void testNeedsDrafting(){
        assertTrue(toolCard.needsDrafting());
    }

    /**
     * Tests the {@link ToolCard} needsDrafting property when the ToolCard does not need drafting
     */
    @Test
    public void testDoesNotNeedDrafting(){
        Map<String, String> controllerStateRules = new HashMap<>();

        controllerStateRules.put("StartControllerState", "ChangeDiceValueControllerState");
        controllerStateRules.put("ChangeDiceValueControllerState","EndControllerState");

        toolCard = new ToolCard(properties, controllerStateRules, rule);

        assertFalse(toolCard.needsDrafting());
    }


    /**
     * Tests that the test instance of {@link ToolCard} is not null
     */
    @Test
    public void testCreateTestInstance() {
        assertNotNull(ToolCard.createTestInstance());
    }

    /**
     * Tests the retrieval of the next state ID of a {@link ToolCard}
     * @see ToolCard#nextStateID(ControllerState)
     */
    @Test
    public void testNextStateID(){
        Game game = new Game(2,3);
        Properties prop = new Properties();
        prop.setProperty("numberOfDicesPerColor","18");
        prop.setProperty("numberOfToolCards","3");
        prop.setProperty("numberOfPublicObjectiveCards","2");

        Controller controller = new Controller(game, prop);

        assertEquals("DraftControllerState", toolCard.nextStateID(new StartControllerState(controller)));
        assertEquals("ChangeDiceValueControllerState", toolCard.nextStateID(new DraftControllerState(controller)));
        assertEquals("EndControllerState", toolCard.nextStateID(new ChangeDiceValueControllerState(controller)));
    }

    /**
     * Tests the equals method of {@link ToolCard}
     * @see ToolCard#equals(Object)
     */
    @Test
    public void testEquals() {
        ToolCard toolCard2 = new ToolCard(properties, controllerStateRules, rule);

        assertTrue(toolCard.equals(toolCard2));
    }

    /**
     * Tests the equals method of {@link ToolCard} when two ToolCards are not equal
     * @see ToolCard#equals(Object)
     */
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

        assertFalse(toolCard3.equals(toolCard));
    }

    /**
     * Tests the equals method of {@link ToolCard} comparing the same object
     * @see ToolCard#equals(Object)
     */
    @Test
    public void testEqualsWhenGivenSameObject() {
        assertTrue(toolCard.equals(toolCard));
    }

    /**
     * Tests the equals method of {@link ToolCard} comparing two different classes
     * @see ToolCard#equals(Object)
     */
    @Test
    public void testEqualsWhenGivenAnotherTypeOfObject() {
        assertFalse(toolCard.equals("this"));
    }

    /**
     * Tests the hash code of a ToolCard is not null
     * @see ToolCard#hashCode()
     */
    @Test
    public void testHashCode() {
        assertNotNull(toolCard.hashCode());
    }

}