package it.polimi.se2018.networking;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

//Extends RMIServerInterface because can receive messages from Server,
//so from the perspective of Server this class it is also a server
public class RMIClientGateway implements ClientInterface, RMIServerInterface{

    private RMIServerInterface recipient;
    private Observer client;
    private RMIServerInterface proxySender;

    private static final int PORT = 1098;
    private static final String PATH = "rmi://127.0.0.1/";

    public RMIClientGateway(String recipient, Observer client) {
        try{
            this.recipient = (RMIServerInterface) Naming.lookup(PATH+recipient);
            this.client = client;
            this.proxySender = (RMIServerInterface) UnicastRemoteObject.exportObject(this, PORT);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) throws RemoteException{
        this.recipient.receiveMessage(message,this.proxySender);
    }

    public void receiveMessage(String message, ServerInterface sender) throws RemoteException{
        this.client.update(message);
    }
}
