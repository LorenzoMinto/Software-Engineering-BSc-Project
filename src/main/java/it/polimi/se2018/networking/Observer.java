package it.polimi.se2018.networking;

import java.rmi.RemoteException;

public interface Observer {

    void update(String message) throws RemoteException;

    void update(String message, ServerInterface sender) throws RemoteException;
}
