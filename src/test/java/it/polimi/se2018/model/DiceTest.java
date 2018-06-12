package it.polimi.se2018.model;

import it.polimi.se2018.utils.ValueOutOfBoundsException;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static it.polimi.se2018.model.DiceColor.*;
import static org.junit.Assert.*;



/**
 * Test for {@link Dice} class
 *
 * @author Federico Haag
 * @author Jacopo Pio Gargano
 */
public class DiceTest {

    private Dice dice;

    /**
     * Initializes the dice
     */
    @Before
    public  void initializeDice(){
        dice = new Dice(GREEN,2);
    }

    /**
     * Tests the creation of a generic dice
     * @see Dice#Dice(DiceColor, int)
     */
    @Test
    public void testConstructor(){
        dice = new Dice(GREEN, 2);
    }

    /**
     * Tests the impossibility of creating a dice with no color
     * @see Dice#Dice(DiceColor)
     */
    @Test
    public void testConstructorWithNoColor(){
        try{
            dice = new Dice(NOCOLOR);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Tests the impossibility of creating a dice with value out of bounds [1,6]
     * @see Dice#Dice(DiceColor, int)
     */
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

    /**
     * Tests setting the value of a dice
     * @see Dice#getValue()
     */
    @Test
    public void testSetAndGetValue() {
        int value = 5;
        dice.setValue(value);

        assertEquals(value,dice.getValue());
    }

    /**
     * Tests setting the value of a dice to a value out of bounds [1,6]
     * @see Dice#setValue(int)
     */
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

    /**
     * Tests the rollOver method
     * @see Dice#rollOver()
     */
    @Test
    public void testRollOver() {
        for(int i=1; i <= 6; i++){
            dice.setValue(i);
            dice.rollOver();
            assertEquals(7-i, dice.getValue());
        }
    }

    /**
     * Tests the increment of the value of a dice
     * @see Dice#getValue()
     */
    @Test
    public void testIncrementValue() {
        dice.setValue(5);
        dice.incrementValue();

        assertEquals(6,dice.getValue());
    }

    /**
     * Tests the increment of the value of a dice when the value is already the maximum value in bounds [1,6]
     * @see Dice#incrementValue()
     */
    @Test
    public void testIncrementValueFromMaxValue() {
        dice.setValue(6);

        assertFalse(dice.incrementValue());
    }

    /**
     * Tests the decrement of the value of a dice
     * @see Dice#decrementValue()
     */
    @Test
    public void testDecrementValue() {
        dice.setValue(2);
        dice.decrementValue();

        assertEquals(1,dice.getValue());
    }

    /**
     * Tests the decrement of the value of a dice when the value is already the minimum value in bounds [1,6]
     * @see Dice#decrementValue()
     */
    @Test
    public void testDecrementValueFromMin() {
        dice.setValue(1);

        assertFalse(dice.decrementValue());
    }

    /**
     * Tests the toString method of {@link Dice}
     * @see Dice#toString()
     */
    @Test
    public void testToString() {
        dice.setValue(2);
        assertEquals("G2",dice.toString());
    }

    /**
     * Tests the equals method of {@link Dice}
     * @see Dice#equals(Object)
     */
    @Test
    public void testEquals() {
        Dice dice1 = new Dice(GREEN, 2);

        assertTrue(dice.equals(dice1));
    }

    /**
     * Tests the equals method of {@link Dice} when two Dice are not equal
     * @see Dice#equals(Object)
     */
    @Test
    public void testEqualsWhenNotEqual() {
        Dice dice1 = new Dice(RED, 4);

        assertFalse(dice.equals(dice1));
    }

    /**
     * Tests the equals method of {@link Dice} comparing the same object
     * @see Dice#equals(Object)
     */
    @Test
    public void testEqualsSameObject() {
        assertTrue(dice.equals(dice));
    }

    /**
     * Tests the equals method of {@link Dice} comparing two different classes
     * @see Dice#equals(Object)
     */
    @Test
    public void testEqualsObjectOfDifferentType() {
        assertFalse(dice.equals("this"));
    }


    /**
     * Tests the hash code of a dice is not null
     * @see Dice#hashCode()
     */
    @Test
    public void testHashCode() {
        assertNotNull(dice.hashCode());
    }

    /**
     * Tests the copy method of {@link Dice}
     * @see Dice#copy()
     */
    @Test
    public void testCopy(){
        assertNotNull(dice.copy());
    }

}