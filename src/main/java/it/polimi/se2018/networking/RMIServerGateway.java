package it.polimi.se2018.networking;

import it.polimi.se2018.utils.Message;

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

    private transient ReceiverInterface receiver;

    RMIServerGateway(String name, int port, ReceiverInterface receiver) throws RemoteException, NetworkingException {
        this.receiver = receiver;

        try {
            LocateRegistry.createRegistry(port);
        } catch(Exception ex) {
            ex.printStackTrace();
            throw new NetworkingException("Failed creating RMI registry");
        }

        try {
            Naming.rebind(name, this);
        } catch(Exception ex) {
            ex.printStackTrace();
            throw new NetworkingException("Failed creating binding rmi name");
        }
    }

    public void receiveMessage(Message message, ReceiverInterface sender) throws RemoteException, NetworkingException{
        receiver.receiveMessage(message,sender);
    }

    //TODO: intellij consiglia di fare ovveride del metodo equals. Capire perchè.
}
