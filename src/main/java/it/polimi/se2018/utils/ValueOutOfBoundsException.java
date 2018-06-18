package it.polimi.se2018.utils;

/**
 * The class {@code ValueOutOfBoundsException} is a form of {@link Exception}
 * that is thrown if a specified value is out of the bounds that are implicit
 * for that kind of object. For example if you try to get a dice from the 8th
 * row of a pattern that has just 4 rows, this exception will be thrown.
 *
 * @author Jacopo Pio Gargano
 * @author Lorenzo Minto
 */
public class ValueOutOfBoundsException extends IllegalArgumentException {

    /**
     * Message constructor. Do nothing else than calling super()
     * @param message the message containing details about the exception
     */
    public ValueOutOfBoundsException(String message){
        super(message);
        //do nothing else
    }

}
