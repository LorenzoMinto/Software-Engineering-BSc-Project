package it.polimi.se2018.networking.socket;

import it.polimi.se2018.networking.NetworkingException;
import it.polimi.se2018.utils.Message;

/**
 * Interface for Socket Receiver
 *
 * @author Federico Haag
 */
public interface SocketReceiverInterface{

    /**
     * Received a message from the specified sender.
     * @param message the message received
     * @param sender the sender of the message
     * @throws NetworkingException if something receiving message went wrong due to connection problems
     */
    void receiveMessage(Message message, SocketClientProxy sender) throws NetworkingException;

    /**
     * Method used by server threads that have to notify server of some kind
     * of unhandlable failure that happened during their running.
     *
     * @param reason the string containing the explanation of the failure
     */
    void fail(String reason);
}
