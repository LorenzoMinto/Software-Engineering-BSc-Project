package it.polimi.se2018.networking;

import it.polimi.se2018.utils.Message;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Federico Haag
 */
public class RMIServerGateway extends UnicastRemoteObject implements RMIReceiverInterface {

    private transient Server server;

    RMIServerGateway(String name, int port, Server server) throws RemoteException, MalformedURLException {
        this.server = server;

        LocateRegistry.createRegistry(port);

        Naming.rebind(name, this);
    }

    @Override
    public void receiveMessage(Message message, ReceiverInterface sender) throws NetworkingException{
        server.parseInBoundMessage(message,sender);
    }

    //TODO: intellij consiglia di fare ovveride del metodo equals. Capire perchè.
}
