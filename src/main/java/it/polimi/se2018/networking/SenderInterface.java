package it.polimi.se2018.networking;

import java.rmi.RemoteException;

public interface SenderInterface {

    void sendMessage(String message) throws RemoteException;
}
