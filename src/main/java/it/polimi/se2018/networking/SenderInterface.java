package it.polimi.se2018.networking;

import it.polimi.se2018.utils.Message;

/**
 * @author Federico Haag
 * @author Jacopo Pio Gargano
 */
public interface SenderInterface {

    void sendMessage(Message message) throws NetworkingException;
}
