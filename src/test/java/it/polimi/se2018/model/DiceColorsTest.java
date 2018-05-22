package it.polimi.se2018.model;

import org.junit.Test;

import static it.polimi.se2018.model.DiceColors.*;
import static org.junit.Assert.*;

/**
 * @author Jacopo Pio Gargano
 */
public class DiceColorsTest {

    private DiceColors color;


    @Test
    public void testToOneLetterSizeIsOne(){
        for (DiceColors color: DiceColors.values()) {
            
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