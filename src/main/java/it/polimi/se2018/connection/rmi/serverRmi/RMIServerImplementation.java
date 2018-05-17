package it.polimi.se2018.connection.rmi.serverRmi;


import it.polimi.se2018.connection.ClientImplementationInterface;
import it.polimi.se2018.connection.message.Message;
import it.polimi.se2018.connection.rmi.clientRmi.RMIClientInterface;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;


public class RMIServerImplementation extends UnicastRemoteObject implements RMIServerInterface {

    private static final long serialVersionUID = -7098548671967083832L;
    private List<RMIClientInterface> clients = new ArrayList<>();

    protected RMIServerImplementation() throws RemoteException {
        super(0);
    }


    @Override
    public void addClient(RMIClientInterface client) throws RemoteException {
        clients.add(client);
        System.out.println("RMIClient "+ (clients.indexOf(client)+1) + " connesso!");
    }

    @Override
    public void sendToClient(Message message, ClientImplementationInterface client){
        try {
            client.receiveFromServer(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveFromClient(Message message) {

        for (RMIClientInterface client: clients) {
            System.out.println("Ricevuto "+ message.getMessage());
            sendToClient(message,client);
        }
    }
}
