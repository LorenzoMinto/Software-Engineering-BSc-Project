package it.polimi.se2018.connection.rmi.clientRmi;

import it.polimi.se2018.connection.rmi.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRMIInterface extends Remote {
	
	public void notify(Message message) throws RemoteException;
	
}
