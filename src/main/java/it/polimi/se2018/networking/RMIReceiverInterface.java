package it.polimi.se2018.networking;

import it.polimi.se2018.utils.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIReceiverInterface extends Remote {

    void receiveMessage(Message message, RMIReceiverInterface sender) throws RemoteException, NetworkingException;
}
