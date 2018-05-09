package it.polimi.se2018.controller;

/**
 * The class {@code AcceptingPlayerException} is a form of {@link Exception}
 * that is used to notify the application that something in the player
 * accepting process (by default done by {@link Controller}) has failed.
 *
 * @author Federico Haag
 */
public class AcceptingPlayerException extends Exception {
    public AcceptingPlayerException() {
        super();
    }

    public AcceptingPlayerException(String message) {
        super(message);
    }
}
