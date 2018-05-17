package it.polimi.se2018.networking;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RMIServerGateway extends UnicastRemoteObject implements RMIServerInterface, Observable<ServerInterface> {

    private static final int PORT = 1099; // porta di default
    private Server receiver;

    private final List<ServerInterface> gateways = new ArrayList<>();

    public RMIServerGateway(String name, Server receiver) throws RemoteException {
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
}
