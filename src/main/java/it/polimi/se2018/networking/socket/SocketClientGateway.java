package it.polimi.se2018.networking.socket;

import it.polimi.se2018.networking.*;
import it.polimi.se2018.utils.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Gateway used by client to send messages to server.
 *
 * @author Federico Haag
 */
public final class SocketClientGateway extends Thread implements ClientInterface, SocketReceiverInterface {

    /**
     * When connection drops, this class try to fix it resetting params. This operation
     * is made in infinite loop until connection is restored.
     * This int value is milliseconds of wait between one reset and the next one.
     */
    private static final int WAIT_TIME_FOR_RETRYING_CONNECTION_FIX = 1000;

    /**
     * String used as NetworkingException message when an IOException happens sending a message
     */
    private static final String FAILED_SENDING_MESSAGE = "Failed sending message from SocketClientGateway due to IOException";

    /**
     * String used as BadBehaviourRuntimeException message (called through fail()) when something throws an exception in run()
     */
    private static final String EXCEPTION_THROWN_OPENING_SOCKET_OR_READING_FROM_STREAM = "Exception thrown opening socket or reading from stream";

    /**
     * Output stream for sending messages to server
     */
    private ObjectOutputStream out;

    /**
     * The client that sends messages through this gateway
     */
    private Client client;

    /**
     * Host name of server
     */
    private String hostName;

    /**
     * Port number of the server
     */
    private int portNumber;

    /**
     * Boolean that is true when the socket is formerly opened and connection is working
     */
    private volatile boolean running = false;

    /**
     * Gateway constructor.
     *
     * @param hostName host name of server
     * @param portNumber port number of the server
     * @param client the client that sends messages through this gateway
     */
    public SocketClientGateway(String hostName, int portNumber, Client client) {
        this.client = client;
        this.hostName = hostName;
        this.portNumber = portNumber;

        this.start();
    }

    @Override
    public void sendMessage(Message message) throws NetworkingException {

        //Waits that socket is open and connection correctly established
        while(!running){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        try {
            this.out.reset();
            this.out.writeObject(message);
        } catch (IOException e) {
            throw new NetworkingException(FAILED_SENDING_MESSAGE);
        }
    }

    @Override
    public void fixConnection() {
        /*  Connection should already be in fixing mode because
            run() loop of this thread, should have catched
            the Socket Exception before the sending of message.
         */
    }

    @Override
    public void receiveMessage(Message message, SocketClientProxy sender) {

        client.notify(message); //client doesn't directly answer to server's messages so it is unnecessary sender
    }

    @Override
    public void fail(String reason) {
        this.client.fail(reason);
    }

    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while(true){
            try (Socket echoSocket = new Socket(this.hostName, this.portNumber)) {
                this.out = new ObjectOutputStream(echoSocket.getOutputStream());
                this.out.flush();

                ObjectInputStream in;
                in = new ObjectInputStream(echoSocket.getInputStream());
                this.running = true;

                this.client.setConnectionAvailable(true);

                //noinspection InfiniteLoopStatement
                while (true) {
                    receiveMessage((Message) in.readObject(), null);
                }

            } catch (Exception e) {
                this.client.setConnectionAvailable(false);
                //will retry to fix the error re running the previous code

            }

            try {
                sleep(WAIT_TIME_FOR_RETRYING_CONNECTION_FIX);
            } catch (InterruptedException e1) {
                this.client.fail(EXCEPTION_THROWN_OPENING_SOCKET_OR_READING_FROM_STREAM);
                Thread.currentThread().interrupt();
            }
        }
    }
}