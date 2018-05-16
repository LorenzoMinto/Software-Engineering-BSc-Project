package it.polimi.se2018.connection.rmi.clientRmi;


import it.polimi.se2018.connection.*;
import it.polimi.se2018.connection.rmi.serverRmi.ServerRMIImplementation;
import it.polimi.se2018.connection.rmi.serverRmi.ServerRMIInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class RMIClient implements LocalClientInterface {

    private ServerRMIInterface server;
    private RemoteClientInterface client;

    public RMIClient(RemoteClientInterface client) {
        this.client = client;
    }

    public void start() {
        try {

            this.server = (ServerRMIInterface) Naming.lookup("//localhost/MyServer");

            ClientRMIImplementation client = new ClientRMIImplementation(this.client);

            ClientRMIInterface remoteRef = (ClientRMIInterface) UnicastRemoteObject.exportObject(client, 0);

            server.addClient(remoteRef);

        } catch (MalformedURLException e) {
            System.err.println("URL non trovato!");
        } catch (RemoteException e) {
            System.err.println("Errore di connessione: " + e.getMessage() + "!");
        } catch (NotBoundException e) {
            System.err.println("Il riferimento passato non è associato a nulla!");
        }

    }

    public void stop(){
        //TODO: implement here
    }

    public void send(Message message) throws RemoteException {
        this.server.send(message);
    }

}
