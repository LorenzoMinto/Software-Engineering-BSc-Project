package it.polimi.se2018.connection.Socket.clientSocket;

import it.polimi.se2018.connection.*;

import java.rmi.RemoteException;

public class SocketClient implements LocalClientInterface {

    private static final int PORT = 1111;
    private static final String HOST = "localhost";

    private ServerInterface server;
    private RemoteClientInterface client;

    public SocketClient(RemoteClientInterface client) {
        this.client = client;
    }

    public void start() {
        this.server = new SocketNetworkHandler(HOST, PORT, new ClientImplementation(this.client) );
    }

    public void stop(){
        SocketNetworkHandler nh = (SocketNetworkHandler) server;
        nh.stopConnection();
    }

    public void send(Message message) throws RemoteException {
        this.server.send(message);
    }
}
