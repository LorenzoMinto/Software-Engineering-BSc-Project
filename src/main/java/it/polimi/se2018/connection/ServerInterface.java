package it.polimi.se2018.connection;

import it.polimi.se2018.connection.rmi.clientRmi.ClientRMIInterface;

import java.rmi.RemoteException;

public interface ServerInterface {

    public void send(Message message) throws RemoteException;
}
