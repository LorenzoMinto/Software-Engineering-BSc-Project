package it.polimi.se2018.networking;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ReceiverInterface extends Remote {

    void receiveMessage(String message, ReceiverInterface sender) throws RemoteException;
}
