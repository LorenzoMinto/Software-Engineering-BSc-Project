package it.polimi.se2018.networking;

import java.rmi.RemoteException;

public class SocketServerGateway implements ReceiverInterface {

    private ReceiverInterface receiver;

    SocketServerGateway(Integer portNumber, ReceiverInterface receiver) throws RemoteException {
        this.receiver = receiver;

        SocketServerGatherer socketServerGatherer = new SocketServerGatherer(portNumber,receiver);
        socketServerGatherer.start();

        /*
        To stop connections acceptance:
        call socketServerGatherer.stopAcceptingConnections();
        */
    }

    public void receiveMessage(String message, ReceiverInterface sender) throws RemoteException {
        receiver.receiveMessage(message,sender);
    }
}
