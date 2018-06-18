package it.polimi.se2018.networking;

import it.polimi.se2018.utils.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Federico Haag
 * @author Jacopo Pio Gargano
 */
public interface ReceiverInterface {

    void receiveMessage(Message message, ReceiverInterface sender) throws RemoteException, NetworkingException;
}
