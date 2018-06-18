package it.polimi.se2018.networking;

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

    private HashMap<RMIReceiverInterface,RMIClientAsAServer> map = new HashMap<>();

    RMIServerGateway(String name, int port, Server server) throws RemoteException, MalformedURLException {
        this.server = server;

        LocateRegistry.createRegistry(port);

        Naming.rebind(name, this);
    }

    @Override
    public void receiveMessage(Message message, RMIReceiverInterface sender){

        if(!map.containsKey(sender)){
            map.put(sender,new RMIClientAsAServer(this,sender));
        }

        server.parseInBoundMessage(message,map.get(sender));
    }

    //TODO: intellij consiglia di fare ovveride del metodo equals. Capire perchè.
}
