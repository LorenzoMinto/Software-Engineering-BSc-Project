package it.polimi.se2018.networking.rmi;

import it.polimi.se2018.networking.ClientProxy;
import it.polimi.se2018.networking.NetworkingException;
import it.polimi.se2018.utils.Message;

import java.rmi.RemoteException;

public class RMIClientProxy implements ClientProxy {

    private RMIReceiverInterface receiver;

    private RMIReceiverInterface sender;

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
