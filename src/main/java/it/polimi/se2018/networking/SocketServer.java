package it.polimi.se2018.networking;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.rmi.RemoteException;

public class SocketServer implements ReceiverInterface {

    private OutputStream stream;

    SocketServer(OutputStream stream) {
        this.stream = stream;
    }

    private void receiveMessage(String message) throws RemoteException {
        PrintWriter out = new PrintWriter(stream, true);
        out.println(message);
    }

    @Override
    public void receiveMessage(String message, ReceiverInterface sender) throws RemoteException {
        receiveMessage(message);
        //Just for compatibility reasons. Sender is not needed (and could be null): something related to RMI
    }
}
