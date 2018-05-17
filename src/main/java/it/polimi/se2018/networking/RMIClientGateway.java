package it.polimi.se2018.networking;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

//Extends RMIReceiverInterface because can receive messages from Server,
//so from the perspective of Server this class it is also a server
public class RMIClientGateway implements SenderInterface, ReceiverInterface, Remote {

    private ReceiverInterface recipient;
    private ReceiverInterface client;
    private ReceiverInterface proxySender;


    RMIClientGateway(String path, int port, ReceiverInterface client) {
        try{
            this.recipient = (ReceiverInterface) Naming.lookup(path);
            this.client = client;
            this.proxySender = (ReceiverInterface) UnicastRemoteObject.exportObject(this, port);
        } catch(Exception e){
            //TODO inserire network exception da throware fino a Client
        }
    }

    public void sendMessage(String message) throws RemoteException{
        this.recipient.receiveMessage(message,this.proxySender);
    }

    public void receiveMessage(String message, ReceiverInterface sender) throws RemoteException{
        this.client.receiveMessage(message,sender);
    }
}
