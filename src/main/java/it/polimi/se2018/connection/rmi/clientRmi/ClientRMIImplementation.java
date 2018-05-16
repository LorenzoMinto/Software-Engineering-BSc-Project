package it.polimi.se2018.connection.rmi.clientRmi;

import it.polimi.se2018.connection.rmi.Message;

import java.rmi.RemoteException;

public class ClientRMIImplementation implements ClientRMIInterface {

	public void notify(Message message) throws RemoteException {
		System.out.println("Ho ricevuto il messaggio: " + message.getMessage());
	}

}
