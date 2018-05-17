package it.polimi.se2018.connection;

import it.polimi.se2018.connection.message.Message;

import java.rmi.RemoteException;

public interface ServerImplementationInterface {
    public void sendToClient(Message message, ClientImplementationInterface client) throws RemoteException;

    public void receiveFromClient(Message message) throws RemoteException;

}
