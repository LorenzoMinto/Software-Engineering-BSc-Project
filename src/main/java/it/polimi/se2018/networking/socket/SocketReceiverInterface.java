package it.polimi.se2018.networking.socket;

import it.polimi.se2018.networking.ClientProxy;
import it.polimi.se2018.networking.NetworkingException;
import it.polimi.se2018.utils.Message;

public interface SocketReceiverInterface {

    void receiveMessage(Message message, ClientProxy sender) throws NetworkingException;

    void fail(String m);
}
