package it.polimi.se2018.networking.socket;

import it.polimi.se2018.networking.ServerInterface;
import it.polimi.se2018.utils.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Gatherer of new socket connections.
 * A thread is started for each new socket connection to read the input coming from it.
 *
 * @author Federico Haag
 * @author Jacopo Pio Gargano
 */
public final class SocketServerGatherer extends Thread{

    /**
     * String sent as content of fail() when something in accepting a new connection goes wrong.
     */
    private static final String ACCEPTING_CONNECTION_EXCEPTION = "Exception thrown accepting socket connections";

    /**
     * The server that is connected to this gatherer.
     */
    private final SocketReceiverInterface receiver;

    /**
     * Port number on which the socket connection is opened
     */
    private int portNumber;

    /**
     * Constructor for this class
     * @param portNumber port number on which the socket connection is opened
     * @param receiver the server that is connected to this gatherer
     */
    SocketServerGatherer(Integer portNumber, SocketReceiverInterface receiver) {
        this.receiver = receiver;
        this.portNumber = portNumber;
    }

    /**
     * Gathering loop
     */
    @Override
    public void run() {
        try(ServerSocket socket = new ServerSocket(portNumber)){

            while(true){
                Socket clientSocket = socket.accept();
                new Thread(() -> {
                    try {
                        acceptConnection(clientSocket);
                    } catch (IOException e) {
                        //no more socket connections will be accepted
                    }
                }).start();
            }

        } catch (Exception e){
            receiver.fail(ACCEPTING_CONNECTION_EXCEPTION);
        }
    }

    /**
     * Manages a new socket connection
     * @param clientSocket the socket connection
     * @throws IOException thrown if opening output stream fails
     */
    private void acceptConnection(Socket clientSocket) throws IOException{
        ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        outputStream.flush();

        ObjectInputStream in;
        SocketClientProxy socketClientAsAServer = null;
        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
            socketClientAsAServer = new SocketClientProxy(outputStream);

            //noinspection InfiniteLoopStatement
            while(true) {
                Message message;
                message = (Message) in.readObject();
                receiver.receiveMessage(message, socketClientAsAServer);
            }

        } catch (Exception e) {
            ((ServerInterface)receiver).lostSocketConnection(socketClientAsAServer);
        }
    }
}
