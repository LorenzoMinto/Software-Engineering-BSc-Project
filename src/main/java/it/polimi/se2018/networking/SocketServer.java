package it.polimi.se2018.networking;

import it.polimi.se2018.utils.Message;

import java.io.*;
import java.rmi.RemoteException;

/**
 *
 * @author Federico Haag
 */
public class SocketServer implements ReceiverInterface {

    private ObjectOutputStream stream;

    SocketServer(ObjectOutputStream stream) throws IOException {
        this.stream = stream;
    }

    private void receiveMessage(Message message) throws RemoteException, NetworkingException {
        try {
            this.stream.writeObject(message);
        } catch (IOException e) {
            throw new NetworkingException("IOException thrown writing in socket stream during receiveMessage method call");
        }
    }

    @Override
    public void receiveMessage(Message message, ReceiverInterface sender) throws NetworkingException {
        try {
            receiveMessage(message);
        } catch (RemoteException e) {
            throw new NetworkingException();
            //TODO: rimuovere
        }
        //Just for compatibility reasons. Sender is not needed (and could be null): something related to RMI
        //TODO: questa cosa non va bene
    }
}
