package it.polimi.se2018.networking;

import it.polimi.se2018.utils.Message;

/**
 * Interface for Client Proxies.
 */
public interface ClientProxyInterface {

    /**
     * Send to the client the given message
     * @param message message to be sent to the client
     * @throws NetworkingException if something during sending message goes wrong
     */
    void receiveMessage(Message message) throws NetworkingException;
}
