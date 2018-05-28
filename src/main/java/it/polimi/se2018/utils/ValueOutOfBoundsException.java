package it.polimi.se2018.utils;

/**
 * @author Jacopo Pio Gargano
 * @author Lorenzo Minto
 */
public class ValueOutOfBoundsException extends IllegalArgumentException {

    public ValueOutOfBoundsException() {
        super();
    }

    public ValueOutOfBoundsException(String message){
        super(message);

    }

}
