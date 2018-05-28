package it.polimi.se2018.utils;

/**
 *
 * @author Jacopo Pio Gargano
 */
public class EmptyListException extends IllegalArgumentException {

    public EmptyListException() {
        super();
    }

    public EmptyListException(String message){
        super(message);

    }

}