package it.polimi.se2018.networking;

import it.polimi.se2018.utils.Message;

public interface ClientAsAServer {
    void receiveMessage(Message message) throws NetworkingException;
}
