package it.polimi.se2018.networking;

import it.polimi.se2018.utils.Message;

/**
 * @author Federico Haag
 * @author Jacopo Pio Gargano
 */
public interface SenderInterface {

    /**
     * Send the given message. Who received the message depend on implementation of the method.
     * @param message message to send
     * @throws NetworkingException if something went wrong sending the message
     */
    void sendMessage(Message message) throws NetworkingException;
}
