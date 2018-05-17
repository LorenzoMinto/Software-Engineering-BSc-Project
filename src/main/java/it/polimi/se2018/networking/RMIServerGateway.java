package it.polimi.se2018.networking;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RMIServerGateway extends UnicastRemoteObject implements RMIServerInterface {

    private static final int PORT = 1099; // porta di default
    private transient Server receiver;

    RMIServerGateway(String name, Server receiver) throws RemoteException {
        this.receiver = receiver;

        try {
            LocateRegistry.createRegistry(PORT);
            Naming.rebind(name, this);
        } catch(Exception ex) {
            //TODO: creare networking exception da throware fino a Server
        }
    }

    public void receiveMessage(String message, ServerInterface sender) throws RemoteException{
        receiver.update(message,sender);
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
