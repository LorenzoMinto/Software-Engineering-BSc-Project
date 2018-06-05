package it.polimi.se2018.utils;

/**
 * The class {@code EmptyListException} is a form of {@link Exception}
 * that is thrown if a list that may be used in a method execution
 * it is empty even if it should not.
 *
 * @author Jacopo Pio Gargano
 */
public class EmptyListException extends IllegalArgumentException {

    public EmptyListException(String message){
        super(message);
    }

}