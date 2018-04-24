package it.polimi.se2018.model;

import java.util.Random;

public enum DiceColors {
    NOCOLOR,
    RED,
    YELLOW,
    GREEN,
    BLUE,
    PURPLE;

    public static DiceColors getRandomColor(){
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
