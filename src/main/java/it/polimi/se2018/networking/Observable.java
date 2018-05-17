package it.polimi.se2018.networking;

import java.rmi.RemoteException;

public interface Observable<T> {

    void notify(String message) throws RemoteException;

    void register(T observer);

    void deregister(T observer);
}
