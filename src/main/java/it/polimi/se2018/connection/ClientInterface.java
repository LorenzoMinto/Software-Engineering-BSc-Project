package it.polimi.se2018.connection;

import java.rmi.RemoteException;

public interface ClientInterface{

    void start() throws RemoteException;

    void stop();
}
