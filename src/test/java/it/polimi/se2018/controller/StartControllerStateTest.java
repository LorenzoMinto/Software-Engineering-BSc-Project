package it.polimi.se2018.controller;

import it.polimi.se2018.model.Game;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

public class StartControllerStateTest {
    private Controller controller;

    @Before
    public void setUp() throws Exception {
        Game game = new Game(4,4);
        Properties prop = new Properties();
        prop.setProperty("numberOfDicesPerColor","18");
        prop.setProperty("numberOfToolCards","3");
        prop.setProperty("numberOfPublicObjectiveCards","2");
        controller = new Controller(game, prop);


    }

    @Test
    public void testDraftDiceFromDraftPool() {

    }

    @Test
    public void testUseToolcard() {

    }
}