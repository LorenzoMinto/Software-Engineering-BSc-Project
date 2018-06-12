package it.polimi.se2018.model;

import org.junit.Test;

import static it.polimi.se2018.model.DiceColor.*;
import static org.junit.Assert.*;

/**
 * Test for {@link DiceColor} class
 *
 * @author Jacopo Pio Gargano
 */
public class DiceColorTest {


    /**
     * Tests the retrieval of a random color. The retrieved color must not be no color
     * @see DiceColor#getRandomColor()
     */
    @Test
    public void testGetRandomColor(){
        DiceColor color = DiceColor.getRandomColor();
        assertNotNull(color);
        assertNotEquals(DiceColor.NOCOLOR, color);
    }

    /**
     * Tests the toOneLetter method. Size of the returned string must be 1 and must be uppercase
     * @see DiceColor#toOneLetter()
     */
    @Test
    public void testToOneLetterSizeIsOne(){
        for (DiceColor color: DiceColor.values()) {
            
            String oneLetter = color.toOneLetter();

            assertEquals(1, oneLetter.length());
            assertEquals(oneLetter.toUpperCase(), oneLetter);
        }
    }

    /**
     * Tests the toString method of {@link DiceColor}
     * @see DiceColor#toString()
     */
    @Test
    public void testToString(){
        String colorToString = RED.toString();

        assertEquals("red", colorToString);
    }



}