package it.polimi.se2018.networking.rmi;

import it.polimi.se2018.networking.Server;
import it.polimi.se2018.utils.Message;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 *
 * @author Federico Haag
 */
public class RMIServerGateway extends UnicastRemoteObject implements RMIReceiverInterface {

    private transient Server server;

    private HashMap<RMIReceiverInterface,RMIClientProxy> map = new HashMap<>();

    public RMIServerGateway(String name, int port, Server server) throws RemoteException, MalformedURLException {
        this.server = server;

        LocateRegistry.createRegistry(port);

        Naming.rebind(name, this);
    }

    @Override
    public void receiveMessage(Message message, RMIReceiverInterface sender){

        if(!map.containsKey(sender)){
            map.put(sender,new RMIClientProxy(this,sender));
        }

        server.parseInBoundMessage(message,map.get(sender));
    }

    //TODO: intellij consiglia di fare ovveride del metodo equals. Capire perchè.
}
