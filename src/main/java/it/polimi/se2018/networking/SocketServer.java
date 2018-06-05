package it.polimi.se2018.networking;

import it.polimi.se2018.utils.message.Message;

import java.io.*;
import java.rmi.RemoteException;

/**
 *
 * @author Federico Haag
 */
public class SocketServer implements ReceiverInterface {

    private ObjectOutputStream stream;

    SocketServer(ObjectOutputStream stream) {
        this.stream = stream;
    }

    private void receiveMessage(Message message) throws RemoteException {
        try {
            this.stream.writeObject(message);
        } catch (IOException e) {
            throw new RemoteException("IOException thrown writing in socket stream during receiveMessage method call");
        }
    }

    @Override
    public void receiveMessage(Message message, ReceiverInterface sender) throws RemoteException {
        receiveMessage(message);
        //Just for compatibility reasons. Sender is not needed (and could be null): something related to RMI
    }
}
