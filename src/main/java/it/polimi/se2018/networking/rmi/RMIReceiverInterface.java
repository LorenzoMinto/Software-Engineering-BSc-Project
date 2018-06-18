package it.polimi.se2018.networking.rmi;

import it.polimi.se2018.networking.NetworkingException;
import it.polimi.se2018.utils.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for RMI Receiver
 */
public interface RMIReceiverInterface extends Remote {

    /**
     * Received a message from the specified sender.
     * @param message the message received
     * @param sender the sender of the message
     * @throws NetworkingException if something receiving message went wrong due to connection problems
     * @throws RemoteException if something receiving message went wrong due to connection problems
     */
    void receiveMessage(Message message, RMIReceiverInterface sender) throws RemoteException, NetworkingException;
}
