package it.polimi.se2018.model;

import it.polimi.se2018.utils.ValueOutOfBoundsException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static it.polimi.se2018.model.DiceColors.*;
import static org.junit.Assert.*;



/**
 * @author Federico Haag
 * @author Jacopo Pio Gargano
 */
public class DiceTest {

    private Dice dice;

    @Before
    public  void initializeDice(){
        dice = new Dice(GREEN,2);
    }

    @Test
    public void testConstructor(){
        dice = new Dice(GREEN, 2);
    }

    @Test
    public void testConstructorWithNoColor(){
        try{
            dice = new Dice(NOCOLOR);
            fail();
        }catch (IllegalArgumentException e){}
    }

    @Test
    public void testConstructorWithValueOutOfBounds(){
        try{
            dice = new Dice(GREEN, 7);
            fail();
        }catch (ValueOutOfBoundsException e){}

        try{
            dice = new Dice(GREEN, -2);
            fail();
        }catch (ValueOutOfBoundsException e){}
    }

    @Test
    public void testSetValue() {
        int value = 5;
        dice.setValue(value);

        assertEquals(value,dice.getValue());
    }

    @Test
    public void testSetIllegalValue(){
        try{
            dice.setValue(7);
            fail();
        }catch (ValueOutOfBoundsException e){}

        try{
            dice.setValue(-3);
            fail();
        }catch (ValueOutOfBoundsException e){}
    }

    @Test
    public void testRollOver() {
        for(int i=1; i <= 6; i++){
            dice.setValue(i);
            dice.rollOver();
            assertEquals(7-i, dice.getValue());
        }
    }

    @Test
    public void testIncrementValue() {
        dice.setValue(5);
        dice.incrementValue();

        assertEquals(6,dice.getValue());
    }

    @Test
    public void testIncrementValueFromMaxValue() {
        dice.setValue(6);

        assertFalse(dice.incrementValue());
    }

    @Test
    public void testDecrementValue() {
        dice.setValue(2);
        dice.decrementValue();

        assertEquals(1,dice.getValue());
    }

    @Test
    public void testDecrementValueFromMin() {
        dice.setValue(1);

        assertFalse(dice.decrementValue());
    }

    @Test
    public void testToString() {
        dice.setValue(2);
        assertEquals("2:G",dice.toString());
    }

    @Test
    public void testEquals() {
        assertTrue(dice.equals(dice));
        assertTrue(dice.equals(dice.copy()));
        assertFalse(dice.equals(new Cell()));
    }

    @Test
    public void testHashCode() {
        assertNotNull(dice.hashCode());
    }
}