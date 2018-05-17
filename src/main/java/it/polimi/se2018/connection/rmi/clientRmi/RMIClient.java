package it.polimi.se2018.connection.rmi.clientRmi;


import it.polimi.se2018.connection.ClientInterface;
import it.polimi.se2018.connection.rmi.serverRmi.RMIServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class RMIClient implements ClientInterface{

    private RMIServerInterface remoteServer;
    private RMIClientImplementation clientImplementation;

    public void start() {
        try {

            remoteServer = (RMIServerInterface) Naming.lookup("//localhost/MyServer");

            clientImplementation = new RMIClientImplementation(remoteServer);

            RMIClientInterface remoteRef = (RMIClientInterface) UnicastRemoteObject.exportObject(clientImplementation, 0);

            remoteServer.addClient(remoteRef);

            clientImplementation.start();

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

}
