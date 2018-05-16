package it.polimi.se2018.connection.rmi.serverRmi;

import it.polimi.se2018.connection.rmi.Message;
import it.polimi.se2018.connection.rmi.clientRmi.ClientRMIInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;


/*
 * IMPORTANTE:
 * L'interfaccia che definisce la classe da esportare deve:
 * - Estendere l'interfaccia Remote
 * - Essere pubblica
 * - Tutti i metodi devono lanciare l'eccezione RemoteException
 */
public interface ServerRMIInterface extends Remote {
	
    void addClient(ClientRMIInterface client) throws RemoteException;
	
    void send(Message message) throws RemoteException;

}
