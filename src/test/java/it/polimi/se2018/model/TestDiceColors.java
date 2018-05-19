package it.polimi.se2018.model;

import org.junit.Test;

import static it.polimi.se2018.model.DiceColors.*;
import static org.junit.Assert.*;

public class TestDiceColors {

    private DiceColors color;

    @Test
    public void testToOneLetter(){
        color = RED;
        String oneLetter = color.toOneLetter();

        assertEquals(1, oneLetter.length());
    }

    @Test
    public void testToString(){
        color = RED;
        String colorString = color.toString();

        assertEquals("red", colorString);
    }



}