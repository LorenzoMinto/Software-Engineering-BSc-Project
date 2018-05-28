package it.polimi.se2018.networking;

import it.polimi.se2018.utils.message.Message;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Extends RMIReceiverInterface because can receive messages from Server,
 * so from the perspective of Server this class it is also a server
 *
 * @author Federico Haag
 */
public class RMIClientGateway implements SenderInterface, ReceiverInterface, Remote {

    private ReceiverInterface recipient;
    private ReceiverInterface client;
    private ReceiverInterface proxySender;


    RMIClientGateway(String path, int port, ReceiverInterface client) throws RemoteException {
        try{
            this.recipient = (ReceiverInterface) Naming.lookup(path);
        } catch(Exception e){
            throw new RemoteException("Failed looking for RMI name");
        }

        try{
            this.proxySender = (ReceiverInterface) UnicastRemoteObject.exportObject(this, port);
        } catch(Exception e){
            throw new RemoteException("Failed exporting RMI object");
        }

        this.client = client;
    }

    public void sendMessage(Message message) throws RemoteException{
        this.recipient.receiveMessage(message,this.proxySender);
    }

    public void receiveMessage(Message message, ReceiverInterface sender) throws RemoteException{
        this.client.receiveMessage(message,sender);
    }
}
