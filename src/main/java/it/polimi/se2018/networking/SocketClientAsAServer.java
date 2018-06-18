package it.polimi.se2018.networking;

import it.polimi.se2018.utils.Message;

import java.io.*;

/**
 *
 * @author Federico Haag
 */
public class SocketClientAsAServer implements ClientAsAServer {

    private ObjectOutputStream stream;

    SocketClientAsAServer(ObjectOutputStream stream) {
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
