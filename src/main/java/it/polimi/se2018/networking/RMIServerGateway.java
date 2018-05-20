package it.polimi.se2018.networking;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServerGateway extends UnicastRemoteObject implements ReceiverInterface, Remote {

    private static final int PORT = 1099; // porta di default
    private transient ReceiverInterface receiver;

    RMIServerGateway(String name, ReceiverInterface receiver) throws RemoteException {
        this.receiver = receiver;

        try {
            LocateRegistry.createRegistry(PORT);
        } catch(Exception ex) {
            throw new RemoteException("Failed creating RMI registry");
        }

        try {
            Naming.rebind(name, this);
        } catch(Exception ex) {
            throw new RemoteException("Failed creating binding rmi name");
        }
    }

    public void receiveMessage(String message, ReceiverInterface sender) throws RemoteException{
        receiver.receiveMessage(message,sender);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj); //TODO:verificare se implementare qualcosa quì
    }

    @Override
    public int hashCode() {
        return super.hashCode(); //TODO:verificare se implementare qualcosa quì
    }
}
