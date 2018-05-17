package it.polimi.se2018.connection.rmi.serverRmi;

import it.polimi.se2018.connection.ServerImplementationInterface;
import it.polimi.se2018.connection.message.Message;
import it.polimi.se2018.connection.rmi.clientRmi.RMIClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerInterface extends Remote, ServerImplementationInterface {

    public void addClient(RMIClientInterface client) throws RemoteException;

}
