package it.polimi.se2018.networking;

import it.polimi.se2018.utils.message.Message;

import java.io.*;
import java.rmi.RemoteException;

/**
 *
 * @author Federico Haag
 */
public class SocketServer implements ReceiverInterface {

    private OutputStream stream;

    SocketServer(OutputStream stream) {
        this.stream = stream;
    }

    private void receiveMessage(Message message) throws RemoteException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(stream));
        try {
            out.write(message + "\n");
            out.flush();
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
