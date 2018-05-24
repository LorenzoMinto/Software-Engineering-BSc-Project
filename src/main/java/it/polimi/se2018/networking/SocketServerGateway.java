package it.polimi.se2018.networking;

import it.polimi.se2018.utils.message.Message;

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

    public void receiveMessage(Message message, ReceiverInterface sender) throws RemoteException {
        receiver.receiveMessage(message,sender);
    }
}
