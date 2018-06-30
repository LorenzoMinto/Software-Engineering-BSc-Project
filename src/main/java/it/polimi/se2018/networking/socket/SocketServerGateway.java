package it.polimi.se2018.networking.socket;

import it.polimi.se2018.networking.ClientProxyInterface;
import it.polimi.se2018.networking.ServerInterface;
import it.polimi.se2018.networking.NetworkingException;
import it.polimi.se2018.networking.Server;
import it.polimi.se2018.utils.Message;

/**
 * Gateway used by server to send messages.
 * From the server perspective, this class is the remote client to whom the server is sending messages.
 *
 * @author Federico Haag
 * @author Jacopo Pio Gargano
 */
public final class SocketServerGateway implements SocketReceiverInterface, ServerInterface {

    /**
     * Remote client that receives messages
     */
    private Server server;

    /**
     * Constructor for the gateway.
     *
     * @param portNumber port number on which the connection must be established
     * @param server remote client that receives messages
     */
    public SocketServerGateway(Integer portNumber, Server server) {
        this.server = server;

        SocketServerGatherer socketServerGatherer = new SocketServerGatherer(portNumber,this);
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
     */
    @Override
    public void receiveMessage(Message message, SocketClientProxy sender) throws NetworkingException {
        server.handleInBoundMessage(message,sender);
    }

    @Override
    public void fail(String reason) {
        this.server.fail(reason);
    }

    @Override
    public void lostSocketConnection(ClientProxyInterface sender) {
        this.server.lostSocketConnection(sender);
    }

    @Override
    public void restoredSocketConnection(ClientProxyInterface previous, ClientProxyInterface next) {
        this.server.restoredSocketConnection(previous,next);
    }
}
