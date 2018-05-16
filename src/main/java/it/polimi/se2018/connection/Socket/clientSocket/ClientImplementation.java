package it.polimi.se2018.connection.Socket.clientSocket;


import it.polimi.se2018.connection.Socket.Message;

public class ClientImplementation implements ClientInterface{

    public void notify ( Message message ) {

        System.out.println("Received: " + message.getMessage());

    }

}
