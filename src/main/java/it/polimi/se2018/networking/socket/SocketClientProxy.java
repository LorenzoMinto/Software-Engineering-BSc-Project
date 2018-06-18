package it.polimi.se2018.networking.socket;

import it.polimi.se2018.networking.*;
import it.polimi.se2018.utils.Message;

import java.io.*;

/**
 * Proxy of a socket client.
 *
 * @author Federico Haag
 */
public final class SocketClientProxy implements ClientProxyInterface {

    /**
     * Stream were to write to send a message to the former client
     */
    private ObjectOutputStream stream;

    /**
     * Constructor of the proxy
     * @param stream the stream were to write to send a message to the former client
     */
    SocketClientProxy(ObjectOutputStream stream) {
        this.stream = stream;
    }

    @Override
    public void receiveMessage(Message message) throws NetworkingException {
        try {
            this.stream.writeObject(message);
        } catch (IOException e) {
            throw new NetworkingException("IOException thrown writing in socket stream during receiveMessage method call");
        }
    }
}
