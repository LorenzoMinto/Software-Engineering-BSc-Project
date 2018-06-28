package it.polimi.se2018.utils;

/**
 * The class {@code BadBehaviourRuntimeException} is a form of {@link RuntimeException}
 * that is thrown when something that according the software logic should not happens
 * and the recover from the situation is impossible.
 *
 * This class is used to recognize behaviours that should not happens:
 * 1) Controller asks Model for something that should not (with reference to game status)
 * 2) Access to file system or connection fails in a way that is hard to handle
 *
 * @author Federico Haag
 */
public class BadBehaviourRuntimeException extends RuntimeException {
    /**
     * Basic constructor. Just calls super().
     */
    public BadBehaviourRuntimeException() {
        super();
        //do nothing else
    }

    /**
     * Constructor with message. Just calls super().
     *
     * @param message message sent within the exception
     */
    public BadBehaviourRuntimeException(String message) {
        super(message);
        //do nothing else
    }

    /**
     * Constructor with throwable. Just calls super().
     *
     * @param cause the cause of the exception
     */
    public BadBehaviourRuntimeException(Throwable cause) {
        super(cause);
    }
}
