package it.polimi.se2018.utils;

/**
 * The class {@code BadBehaviourRuntimeException} is a form of {@link RuntimeException}
 * that is thrown when something that according the software logic should not happens
 * and the recover from the situation is impossible.
 *
 * This class is used to recognize really "unexpeted" runtime exceptions during debugging
 * versus "exptected during coding phase" runtime exceptions.
 */
public class BadBehaviourRuntimeException extends RuntimeException {
    /**
     * Basic constructor. Just calls the super().
     */
    public BadBehaviourRuntimeException() {
        super();
        //do nothing else
    }

    /**
     * Constructor with message. Just calls the super().
     *
     * @param message message sent to the exception
     */
    public BadBehaviourRuntimeException(String message) {
        super(message);
        //do nothing else
    }
}
