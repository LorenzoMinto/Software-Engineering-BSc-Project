package it.polimi.se2018.connection.rmi.clientRmi;

import it.polimi.se2018.connection.RemoteClientInterface;
import it.polimi.se2018.connection.Message;

import java.rmi.RemoteException;

public class ClientRMIImplementation implements ClientRMIInterface {

	private RemoteClientInterface client;

	public ClientRMIImplementation(RemoteClientInterface client) {
		this.client = client;
	}

	public void send(Message message) throws RemoteException {
		this.client.send(message);
	}

}
