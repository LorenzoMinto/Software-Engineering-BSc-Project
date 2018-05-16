package it.polimi.se2018.connection.Socket.serverSocket;

import it.polimi.se2018.connection.Socket.Message;
import it.polimi.se2018.connection.Socket.clientSocket.ClientInterface;

public class ServerImplementation implements ServerInterface {

    private final ServerSocket server;

    public ServerImplementation( ServerSocket server) {
        this.server = server;
    }

    public void send ( Message message ) {

        System.out.println("Broadcasting: "+message.getMessage());

        for( ClientInterface client: server.getClients() ) {
            client.notify(message);

        }
    }
}
