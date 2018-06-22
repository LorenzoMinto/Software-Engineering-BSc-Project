package it.polimi.se2018.model;

import it.polimi.se2018.controller.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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
     * Initializes variables for the tests
     */
    @BeforeClass
    public static void setUp(){
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
     * Initializes the ToolCard before each test
     * @see ToolCard#ToolCard(Properties, Map, PlacementRule, Set)
     * */
    @Before
    public void initializeToolCard(){
        toolCard = new ToolCard(properties, controllerStateRules, rule, null);
    }

    /**
     * Tests the class constructor
     */
    @Test
    public void testConstructor(){
        assertNotNull(toolCard);
    }

    /**
     * Tests the usage of the ToolCard, which has an effect on the tokens needed to use the toolCard
     * @see ToolCard#use()
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
     * @see ToolCard#getNeededTokens()
     */
    @Test
    public void testGetNeededTokens(){
        assertEquals(neededTokens.intValue(), toolCard.getNeededTokens());
    }

    /**
     * Tests the retrieval and the setting of the used tokens of a ToolCard
     * @see ToolCard#getUsedTokens()
     */
    @Test
    public void testGetUsedTokens(){
        int prevTokens = toolCard.getNeededTokens();
        toolCard.use();
        assertEquals(prevTokens, toolCard.getUsedTokens());
    }

    /**
     * Tests the retrieval of the {@link PlacementRule} of a ToolCard
     * @see ToolCard#getPlacementRule()
     */
    @Test
    public void testGetPlacementRule(){
        assertEquals(rule, toolCard.getPlacementRule());
    }

    /**
     * Tests the retrieval of the title of a ToolCard
     * @see ToolCard#getTitle()
     */
    @Test
    public void testGetTitle(){
        assertEquals(title, toolCard.getTitle());
    }

    /**
     * Tests the retrieval of the description of a ToolCard
     * @see ToolCard#getDescription()
     */
    @Test
    public void testGetDescription(){
        assertEquals(description, toolCard.getDescription());
    }

    /**
     * Tests the retrieval of the imageURL of a ToolCard
     * @see ToolCard#getImageURL()
     */
    @Test
    public void testGetImageURL(){
        assertEquals(imageURL, toolCard.getImageURL());
    }


    /**
     * Tests the {@link ToolCard} needsDrafting property
     * @see ToolCard#needsDrafting()
     */
    @Test
    public void testNeedsDrafting(){
        assertTrue(toolCard.needsDrafting());
    }

    /**
     * Tests the {@link ToolCard} needsDrafting property when the ToolCard does not need drafting
     * @see ToolCard#needsDrafting()
     */
    @Test
    public void testDoesNotNeedDrafting(){
        Map<String, String> controllerStateRules = new HashMap<>();

        controllerStateRules.put("StartControllerState", "ChangeDiceValueControllerState");
        controllerStateRules.put("ChangeDiceValueControllerState","EndControllerState");

        toolCard = new ToolCard(properties, controllerStateRules, rule, null);

        assertFalse(toolCard.needsDrafting());
    }

    /**
     * Tests the retrieval of the next state ID of a {@link ToolCard}
     * @see ToolCard#nextStateID(ControllerState)
     */
    @Test
    public void testNextStateID(){
        Game game = new Game(2,3);
        Properties toolCardProperties = new Properties();
        toolCardProperties.setProperty("numberOfDicesPerColor","18");
        toolCardProperties.setProperty("numberOfToolCards","3");
        toolCardProperties.setProperty("numberOfPublicObjectiveCards","2");

        Controller controller = new Controller(game, toolCardProperties);

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
        ToolCard toolCard2 = new ToolCard(properties, controllerStateRules, rule, null);

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

        ToolCard toolCard3 = new ToolCard(properties2, controllerStateRules, rule, null);

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