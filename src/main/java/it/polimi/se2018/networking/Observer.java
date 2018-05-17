package it.polimi.se2018.networking;

import java.rmi.RemoteException;

public interface Observer {

    void update(String m) throws RemoteException;
}
