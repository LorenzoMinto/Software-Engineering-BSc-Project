package it.polimi.se2018.connection.rmi.serverRmi;


import it.polimi.se2018.connection.rmi.Message;
import it.polimi.se2018.connection.rmi.clientRmi.ClientRMIInterface;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ServerRMIImplementation extends UnicastRemoteObject implements
        ServerRMIInterface {
	
	private List<ClientRMIInterface> clients = new ArrayList<>();

	protected ServerRMIImplementation() throws RemoteException {
		super(0);		
	}

	private static final long serialVersionUID = -7098548671967083832L;

	public void addClient(ClientRMIInterface client) throws RemoteException {
		clients.add(client);
        System.out.println("ClientRMI "+ (clients.indexOf(client)+1) + " connesso!");
	}

	public void send(Message message) throws RemoteException {
		Iterator<ClientRMIInterface> clientIterator = clients.iterator();
		while(clientIterator.hasNext()){
			System.out.println(message.getMessage());
			try{
				clientIterator.next().notify(message);
			}catch(ConnectException e) {
				clientIterator.remove();	
				System.out.println("ClientRMI rimosso!");
			}			
		}	
	}

}
