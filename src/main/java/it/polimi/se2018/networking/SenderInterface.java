package it.polimi.se2018.networking;

import it.polimi.se2018.utils.message.Message;

import java.rmi.RemoteException;

/**
 * @author Federico Haag
 * @author Jacopo Pio Gargano
 */
public interface SenderInterface {

    void sendMessage(Message message) throws NetworkException;
}
