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
        DiceColors randomColor;

        do{
            randomColor = values()[random.nextInt(values().length)];
        } while (randomColor!=DiceColors.NOCOLOR);

        return randomColor;
    }

    @Override
    public String toString() {
        switch(this) {
            case NOCOLOR: return "_";
            case RED: return "R";
            case YELLOW: return "Y";
            case GREEN: return "G";
            case BLUE: return "B";
            case PURPLE: return "P";
            default: throw new IllegalArgumentException();
        }
    }

    public int toInt(){
        switch(this) {
            case NOCOLOR: return 0;
            case RED: return 1;
            case YELLOW: return 2;
            case GREEN: return 3;
            case BLUE: return 4;
            case PURPLE: return 5;
            default: throw new IllegalArgumentException();
        }
    }
}
