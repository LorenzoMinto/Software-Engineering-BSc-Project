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
 * Gateway used by server to send messages.
 * From the server perspective, this class is the remote client to whom the server is sending messages.
 *
 * @author Federico Haag
 */
public final class RMIServerGateway extends UnicastRemoteObject implements RMIReceiverInterface {

    /**
     * The server connected to this gateway
     */
    private transient Server server;

    /**
     * Map used to convert a RMIReceiverInterface into a RMIClientProxy
     */
    private HashMap<RMIReceiverInterface,RMIClientProxy> map = new HashMap<>();

    /**
     * Constructor of the gateway.
     * @param name name to be binded to this gateway during rmi exporting
     * @param port port to which open the connection
     * @param server the server connected to this gateway
     * @throws RemoteException if something opening connection went wrong
     * @throws MalformedURLException if the given name/port are bad fomatted
     */
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

        server.handleInBoundMessage(message,map.get(sender));
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
