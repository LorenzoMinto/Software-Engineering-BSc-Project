package it.polimi.se2018.networking;

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
public class SocketServerGatherer extends Thread{

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
     * Boolean value that is used to, eventually, stop the gathering loop.
     */
    private boolean acceptConnections = true;

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

            while(this.acceptConnections){
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
        new Thread(() -> {

            ObjectInputStream in;
            SocketServer socketServer;
            try {
                in = new ObjectInputStream(clientSocket.getInputStream());
                socketServer = new SocketServer(outputStream);
            } catch (IOException e) {
                return;
            }

            boolean c = true;
            while(c) {
                Message message;
                try {
                    message = (Message) in.readObject();
                    receiver.receiveMessage(message, socketServer);
                } catch( Exception e ){
                    receiver.fail(READING_STREAM_EXCEPTION);
                    c = false;
                }
            }
        }).start();
    }

    /**
     * Stop the gathering loop
     */
    public void stopAcceptingConnections(){
        //TODO: verificare se questo metodo è stato effettivamente utilizzato, e nel caso rimuoverlo
        this.acceptConnections = false;
    }
}
