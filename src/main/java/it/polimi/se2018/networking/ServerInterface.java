package it.polimi.se2018.networking;

import java.rmi.RemoteException;

public interface ServerInterface {

    void receiveMessage(String message, ServerInterface sender) throws RemoteException;
}
