package it.polimi.se2018.networking;

import java.rmi.RemoteException;

public interface ClientInterface {

    void sendMessage(String message) throws RemoteException;
}
