package it.polimi.se2018.networking;

import it.polimi.se2018.utils.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIReceiverInterface extends ReceiverInterface, Remote {
    @Override
    void receiveMessage(Message message, ReceiverInterface sender) throws RemoteException, NetworkingException;
}
