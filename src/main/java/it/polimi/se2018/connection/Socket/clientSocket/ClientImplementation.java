package it.polimi.se2018.connection.Socket.clientSocket;


import it.polimi.se2018.connection.RemoteClientInterface;
import it.polimi.se2018.connection.Message;

import java.rmi.RemoteException;

public class ClientImplementation implements RemoteClientInterface {

    private RemoteClientInterface client;

    public ClientImplementation(RemoteClientInterface client) {
        this.client = client;
    }

    public void send (Message message ) throws RemoteException {
        this.client.send(message);
    }

}
