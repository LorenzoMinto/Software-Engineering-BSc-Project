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
     * String sent as content of fail() when something in reading from stream input goes wrong.
     */
    private static final String READING_STREAM_EXCEPTION = "Exception thrown reading socket input stream";

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

            //noinspection InfiniteLoopStatement
            while(true){
                acceptConnection(socket);
            }

        } catch (Exception e){
            receiver.fail(ACCEPTING_CONNECTION_EXCEPTION);
        }
    }

    /**
     * Method called for accepting a new connection. Waits for a new one and then starts an input reading thread.
     * @param socket the socket to monitor for new connections requests
     * @throws IOException if something in the acceptance process goes wrong due to IO problems
     */
    private void acceptConnection(ServerSocket socket) throws IOException{
        Socket clientSocket = socket.accept();

        ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        outputStream.flush();

        //Starts a thread listening for messages
        new ConnectionFixer(clientSocket,outputStream).start();
    }

    private class ConnectionFixer extends Thread {

        private final Socket clientSocket;
        private final ObjectOutputStream outputStream;
        private SocketClientProxy socketClientProxyBeforeConnectionDrop;

        ConnectionFixer(Socket clientSocket, ObjectOutputStream outputStream) {
            this.clientSocket=clientSocket;
            this.outputStream=outputStream;
            this.socketClientProxyBeforeConnectionDrop = null;
        }

        @Override
        public void run() {
            super.run();

            while(true) {

                ObjectInputStream in;
                SocketClientProxy socketClientAsAServer;
                try {
                    in = new ObjectInputStream(clientSocket.getInputStream());
                    socketClientAsAServer = new SocketClientProxy(outputStream);
                    if(socketClientProxyBeforeConnectionDrop==null){
                        socketClientProxyBeforeConnectionDrop=socketClientAsAServer;
                    }
                } catch (IOException e) {
                    return;
                }

                ((ServerInterface)receiver).restoredSocketConnection(socketClientProxyBeforeConnectionDrop,socketClientAsAServer);

                tryToFix(in,socketClientAsAServer);

                try {
                    sleep(500);
                } catch (InterruptedException e1) {
                    receiver.fail(READING_STREAM_EXCEPTION);
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

        private void tryToFix(ObjectInputStream in, SocketClientProxy socketClientAsAServer){
            boolean c = true;
            while (c) {
                Message message;
                try {
                    message = (Message) in.readObject();
                    receiver.receiveMessage(message, socketClientAsAServer);
                } catch (Exception e) {
                    ((ServerInterface)receiver).lostSocketConnection(socketClientAsAServer);
                    socketClientProxyBeforeConnectionDrop = socketClientAsAServer;
                    c = false;
                }
            }
        }
    }
}
