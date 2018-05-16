package it.polimi.se2018.connection.Socket.serverSocket;

import it.polimi.se2018.connection.Message;
import it.polimi.se2018.connection.RemoteClientInterface;
import it.polimi.se2018.connection.ServerInterface;

import java.rmi.RemoteException;

public class ServerImplementation implements ServerInterface {

    private final ServerSocket server;

    public ServerImplementation( ServerSocket server) {
        this.server = server;
    }

    public void send ( Message message ) throws RemoteException{

        System.out.println("Broadcasting: "+message.getMessage());

        for( RemoteClientInterface client: server.getClients() ) {
            client.send(message);
        }
    }
}
