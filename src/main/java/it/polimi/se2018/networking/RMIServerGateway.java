package it.polimi.se2018.networking;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RMIServerGateway extends UnicastRemoteObject implements RMIServerInterface, Observable<ServerInterface> {

    private static final int PORT = 1099; // porta di default
    private transient Server receiver;

    private final transient List<ServerInterface> gateways = new ArrayList<>();

    RMIServerGateway(String name, Server receiver) throws RemoteException {
        this.receiver = receiver;

        try {
            LocateRegistry.createRegistry(PORT);
            Naming.rebind(name, this);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void receiveMessage(String message, ServerInterface sender) throws RemoteException{

        receiver.update(message);

        if(receiver.canJoin(message)){

            register(sender);

            sender.receiveMessage("Welcome to SagradaServer. You are now an authorized client.",this);
            receiver.update("A new client has been authorized");
        }
    }

    public void notify(String message) throws RemoteException{
        for(ServerInterface c : gateways){
            c.receiveMessage(message,this);
        }
    }

    public void register(ServerInterface gateway) {
        gateways.add(gateway);
    }

    public void deregister(ServerInterface gateway) {
        gateways.remove(gateway);
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
