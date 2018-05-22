package it.polimi.se2018.utils;

public class EmptyListException extends IllegalArgumentException {

    public EmptyListException() {
        super();
    }

    public EmptyListException(String message){
        super(message);

    }

}