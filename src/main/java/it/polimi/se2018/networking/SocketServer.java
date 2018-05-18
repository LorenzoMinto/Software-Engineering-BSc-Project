package it.polimi.se2018.networking;

import java.io.*;
import java.rmi.RemoteException;

public class SocketServer implements ReceiverInterface {

    private OutputStream stream;

    SocketServer(OutputStream stream) {
        this.stream = stream;
    }

    private void receiveMessage(String message) throws RemoteException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(stream));
        try {
            out.write(message + "\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveMessage(String message, ReceiverInterface sender) throws RemoteException {
        receiveMessage(message);
        //Just for compatibility reasons. Sender is not needed (and could be null): something related to RMI
    }
}
