package it.polimi.se2018.networking;

import it.polimi.se2018.utils.BadBehaviourRuntimeException;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

//Extends RMIServerInterface because can receive messages from Server,
//so from the perspective of Server this class it is also a server
public class RMIClientGateway implements ClientInterface, RMIServerInterface{

    private RMIServerInterface recipient;
    private Observer client;
    private RMIServerInterface proxySender;


    RMIClientGateway(String path, int port, Observer client) {
        try{
            this.recipient = (RMIServerInterface) Naming.lookup(path);
            this.client = client;
            this.proxySender = (RMIServerInterface) UnicastRemoteObject.exportObject(this, port);
        } catch(Exception e){
            //TODO inserire network exception da throware fino a Client
        }
    }

    public void sendMessage(String message) throws RemoteException{
        this.recipient.receiveMessage(message,this.proxySender);
    }

    public void receiveMessage(String message, ServerInterface sender) throws RemoteException{
        this.client.update(message);
    }
}
