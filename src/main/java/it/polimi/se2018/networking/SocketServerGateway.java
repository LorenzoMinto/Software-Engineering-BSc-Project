package it.polimi.se2018.networking;

import it.polimi.se2018.utils.Message;

import java.rmi.RemoteException;

/**
 * Gateway used by server to send messages.
 * From the server perspective, this class is the remote client to whom the server is sending messages.
 *
 * @author Federico Haag
 * @author Jacopo Pio Gargano
 */
public class SocketServerGateway implements ReceiverInterface {

    /**
     * Remote client that receives messages
     */
    private ReceiverInterface receiver;

    /**
     * Constructor for the gateway.
     *
     * @param portNumber port number on which the connection must be established
     * @param receiver remote client that receives messages
     */
    SocketServerGateway(Integer portNumber, ReceiverInterface receiver) {
        this.receiver = receiver;

        SocketServerGatherer socketServerGatherer = new SocketServerGatherer(portNumber,receiver);
        socketServerGatherer.start();
    }

    /**
     * Method called to send the message to the receiver.
     * It is called "receiveMessage" because, as stated in the general description, this class
     * is (from the server perspective) the remote client.
     * So: server "sends" the message, and client - so this gateway "receive" it.
     *
     * @param message the message to be sent
     * @param sender the sender of the message (server)
     * @throws RemoteException if something during the physical sending of the message goes wrong
     */
    public void receiveMessage(Message message, ReceiverInterface sender) throws RemoteException, NetworkingException {
        receiver.receiveMessage(message,sender);
    }
}
