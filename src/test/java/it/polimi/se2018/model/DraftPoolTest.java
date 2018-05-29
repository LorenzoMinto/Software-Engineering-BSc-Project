package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Federico Haag
 */
public class DraftPoolTest {

    private static DraftPool draftPool;

    @Before
    public void setUp() {
        draftPool = new DraftPool();
    }

    @Test
    public void reroll() {
        Dice dice1 = new Dice(DiceColor.RED,3);
        Dice dice2 = new Dice(DiceColor.BLUE,1);
        draftPool.putDice(dice1);
        draftPool.putDice(dice2);
        boolean foundRed = false;
        boolean foundBlue = false;
        for (Dice d : draftPool.getDices()){
            if(d.getColor()== DiceColor.BLUE && d.getValue()==1){
                assertFalse(foundBlue);
                foundBlue = true;
            }
            if(d.getColor()== DiceColor.RED && d.getValue()==3){
                assertFalse(foundRed);
                foundRed = true;
            }
        }
        assertTrue(foundBlue);
        assertTrue(foundRed);
        draftPool.reroll();
        foundRed = false;
        foundBlue = false;
        for (Dice d : draftPool.getDices()){
            if(d.getColor()== DiceColor.BLUE && d.getValue()>=1 && d.getValue()<=6){
                assertFalse(foundBlue);
                foundBlue = true;
            }
            if(d.getColor()== DiceColor.RED && d.getValue()>=1 && d.getValue()<=6){
                assertFalse(foundRed);
                foundRed = true;
            }
        }
        assertTrue(foundBlue);
        assertTrue(foundRed);
    }

    @Test
    public void draftDice() {
        Dice dice = new Dice(DiceColor.RED,3);
        assertEquals(0,draftPool.getDices().size());
        draftPool.putDice(dice);
        assertEquals(1,draftPool.getDices().size());
        draftPool.draftDice(dice);
        assertEquals(0,draftPool.getDices().size());
    }

    @Test
    public void putAndGetDices() {
        Dice dice = new Dice(DiceColor.RED,3);
        assertEquals(0,draftPool.getDices().size());
        draftPool.putDice(dice);
        assertEquals(dice,draftPool.getDices().get(0));
        assertEquals(1,draftPool.getDices().size());
    }
}