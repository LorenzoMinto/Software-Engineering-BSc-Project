package it.polimi.se2018.connection.Socket.clientSocket;

import it.polimi.se2018.connection.*;
import it.polimi.se2018.connection.message.Message;

public class SocketClient implements ClientInterface{

    private static final int PORT = 1111;
    private static final String HOST = "localhost";

    private SocketClientMessageReceiver socketMessageReceiver;
    private SocketClientImplementation clientImplementation;

    public void start() {

        clientImplementation = new SocketClientImplementation();

        socketMessageReceiver = new SocketClientMessageReceiver(HOST, PORT, clientImplementation);
        socketMessageReceiver.start();
        clientImplementation.setSocket(socketMessageReceiver.getSocket());
        clientImplementation.start();

    }

    public void stop(){
        SocketClientMessageReceiver nh = (SocketClientMessageReceiver) socketMessageReceiver;
        nh.stopConnection();
    }

}
