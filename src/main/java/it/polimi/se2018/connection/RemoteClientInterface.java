package it.polimi.se2018.connection;

import java.rmi.RemoteException;

public interface RemoteClientInterface {
    public void send(Message message) throws RemoteException;
}
