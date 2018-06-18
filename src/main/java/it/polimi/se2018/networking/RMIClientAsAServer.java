package it.polimi.se2018.networking;

import it.polimi.se2018.utils.Message;

import java.rmi.RemoteException;

public class RMIClientAsAServer implements ClientAsAServer {

    private RMIReceiverInterface receiver;

    private RMIReceiverInterface sender;

    RMIClientAsAServer(RMIReceiverInterface sender, RMIReceiverInterface receiver) {
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
