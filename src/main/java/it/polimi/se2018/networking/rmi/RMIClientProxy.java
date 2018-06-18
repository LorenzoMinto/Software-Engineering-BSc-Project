package it.polimi.se2018.networking.rmi;

import it.polimi.se2018.networking.ClientProxyInterface;
import it.polimi.se2018.networking.NetworkingException;
import it.polimi.se2018.utils.Message;

import java.rmi.RemoteException;

/**
 * Proxy of an RMI client.
 *
 * @author Federico Haag
 */
public final class RMIClientProxy implements ClientProxyInterface {

    /**
     * The former client
     */
    private RMIReceiverInterface receiver;

    /**
     * The sender of the message (former server)
     */
    private RMIReceiverInterface sender;

    /**
     * Constructor of the proxy
     * @param sender the sender of the message (former server)
     * @param receiver the former client
     */
    RMIClientProxy(RMIReceiverInterface sender, RMIReceiverInterface receiver) {
        this.receiver = receiver;
        this.sender = sender;
    }

    @Override
    public void receiveMessage(Message message) throws NetworkingException {
        try {
            this.receiver.receiveMessage(message,this.sender);
        } catch (RemoteException e) {
            throw new NetworkingException();
        }
    }
}
