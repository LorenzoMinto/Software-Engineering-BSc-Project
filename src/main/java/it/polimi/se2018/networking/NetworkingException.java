package it.polimi.se2018.networking;

/**
 * The class {@code NetworkingException} is a form of {@link Exception}
 * that is thrown if something related to connection goes wrong.
 * For example: sending and receiving of messages.
 *
 * @author Federico Haag
 */
public class NetworkingException extends Exception {
    public NetworkingException() {
        super();
        //nothing else
    }

    public NetworkingException(String message) {
        super(message);
        //nothing else
    }
}
