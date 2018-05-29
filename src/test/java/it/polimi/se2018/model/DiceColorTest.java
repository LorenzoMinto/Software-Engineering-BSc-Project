package it.polimi.se2018.model;

import org.junit.Test;

import static it.polimi.se2018.model.DiceColor.*;
import static org.junit.Assert.*;

/**
 * @author Jacopo Pio Gargano
 */
public class DiceColorTest {

    private DiceColor color;


    @Test
    public void testToOneLetterSizeIsOne(){
        for (DiceColor color: DiceColor.values()) {
            
            String oneLetter = color.toOneLetter();

            assertEquals(1, oneLetter.length());
        }
    }

    @Test
    public void testToString(){
        color = RED;
        String colorString = color.toString();

        assertEquals("red", colorString);
    }



}