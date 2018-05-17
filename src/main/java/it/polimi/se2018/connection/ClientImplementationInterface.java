package it.polimi.se2018.connection;

import it.polimi.se2018.connection.message.Message;

import java.rmi.RemoteException;

public interface ClientImplementationInterface {
    public void sendToServer(Message message) throws RemoteException;
    public void receiveFromServer(Message message) throws RemoteException;
}
