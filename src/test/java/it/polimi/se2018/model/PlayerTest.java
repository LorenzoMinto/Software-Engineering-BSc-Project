package it.polimi.se2018.model;

import it.polimi.se2018.controller.ObjectiveCardManager;
import it.polimi.se2018.controller.WindowPatternManager;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

    User user;
    Player p;

    @Before
    public void setUp() throws Exception {
        user = new User(8,"nickname");
        ObjectiveCardManager ocm = new ObjectiveCardManager();
        p = new Player("nickname",ocm.getPrivateObjectiveCard());
    }

    @Test
    public void getFavorTokens() {
    }

    @Test
    public void getNickname() {
    }

    @Test
    public void setWindowPattern() {

    }

    @Test
    public void getPrivateObjectiveCard() {
    }

    @Test
    public void decreaseTokens() {
    }

    @Test
    public void getWindowPattern() {
    }

    @Test
    public void canUseToolCard() {
    }

    /*@Test
    public void getUser() {
        assertTrue(user.equals(p.getUser()));
    }*/

    @Test
    public void testEquals() {
    }

    @Test
    public void testHashCode() {
    }
}