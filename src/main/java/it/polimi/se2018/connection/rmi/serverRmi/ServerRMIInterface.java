package it.polimi.se2018.connection.rmi.serverRmi;

import it.polimi.se2018.connection.ServerInterface;
import it.polimi.se2018.connection.rmi.clientRmi.ClientRMIInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRMIInterface extends Remote, ServerInterface {

    public void addClient(ClientRMIInterface client) throws RemoteException;
}
