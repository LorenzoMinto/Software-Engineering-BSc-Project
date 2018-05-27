package it.polimi.se2018.controller;

import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.User;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;
/*
public class ControllerTest {

    private static Controller controller;

    @BeforeClass
    public static void init(){

        Properties properties = new Properties();
        properties.put("numberOfRounds","10");
        properties.put("maxNumberOfPlayers","2"); //non cambiare
        properties.put("numberOfDicesPerColor","18");
        properties.put("numberOfToolCards","3");
        properties.put("numberOfPublicObjectiveCards","3");

        Game game = new Game(
                Integer.parseInt( properties.getProperty("numberOfRounds") ),
                Integer.parseInt( properties.getProperty("maxNumberOfPlayers") )
        );
        controller = new Controller(game,properties);
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
    public void acceptPlayer() {

        try {
            controller.acceptPlayer(new User(0,"myusername"),"mynickname");
        } catch (AcceptingPlayerException e) {
            fail();
        }

        try {
            controller.acceptPlayer(new User(0,"myusername"),"mynickname");
        } catch (AcceptingPlayerException e) {

            try {
                controller.acceptPlayer(new User(1,"mysecondusername"),"mysecondusername");
            } catch (AcceptingPlayerException ex) {
                fail();
            }

            try {
                controller.acceptPlayer(new User(2,"mythirdusername"),"mythirdusername");
            } catch (AcceptingPlayerException ex) {
                return;
            }
        }

        fail();

    }

    @Test
    public void getConfigProperty() {
        assertEquals(2,controller.getConfigProperty("maxNumberOfPlayers"));
    }
}*/