package it.polimi.se2018.controller;

import it.polimi.se2018.model.Game;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;


public class ControllerTest {

    private static Controller controller;

    @BeforeClass
    public static void init(){

        Game game = new Game(4,4);
        Properties gprop = new Properties();
        gprop.setProperty("numberOfRounds","10");
        gprop.setProperty("numberOfDicesPerColor","18");
        gprop.setProperty("numberOfToolCards","12");
        gprop.setProperty("numberOfPublicObjectiveCards","2");
        gprop.setProperty("maxNumberOfPlayers","4");
        gprop.setProperty("minNumberOfPlayers","2");
        gprop.setProperty("timeoutLaunchingGame","30");
        gprop.setProperty("timeoutChoosingPatterns","30");
        gprop.setProperty("amountOfCouplesOfPatternsPerPlayer","4");

        controller = new Controller(game, gprop);
    }

    @Test
    public void setControllerState() {
    }

    @Test
    public void canUseSpecificToolCard() {
    }

    @Test
    public void setActiveToolCard() {
    }

    @Test
    public void resetActiveToolCard() {
    }

    @Test
    public void getActiveToolCard() {
    }

    @Test
    public void advanceGame() {
    }

    @Test
    public void endGame() {
    }

    @Test
    public void getRankingsAndScores() {
    }

    @Test
    public void getDefaultPlacementRule() {
    }

    @Test
    public void getConfigProperty() {
        assertEquals(4,controller.getConfigProperty("maxNumberOfPlayers"));
        assertEquals(10,controller.getConfigProperty("numberOfRounds"));
    }
}
