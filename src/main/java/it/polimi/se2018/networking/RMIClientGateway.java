package it.polimi.se2018.networking;

import it.polimi.se2018.utils.Message;

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
public class RMIClientGateway implements SenderInterface, RMIReceiverInterface {

    private ReceiverInterface recipient;
    private Client client;
    private ReceiverInterface proxySender;


    RMIClientGateway(String path, int port, Client client) throws NetworkingException {
        try{
            this.recipient = (RMIReceiverInterface) Naming.lookup(path);
        } catch(Exception e){
            e.printStackTrace();
            throw new NetworkingException("Failed looking for RMI name");
        }

        try{
            this.proxySender = (RMIReceiverInterface) UnicastRemoteObject.exportObject(this, port);
        } catch(Exception e){
            e.printStackTrace();
            throw new NetworkingException("Failed exporting RMI object");
        }

        this.client = client;
    }

    public void sendMessage(Message message) throws NetworkingException{
        try {
            this.recipient.receiveMessage(message,this.proxySender);
        } catch (RemoteException e) {
            throw new NetworkingException();
        }
    }

    public void receiveMessage(Message message, ReceiverInterface sender){
        //IL THREAD VIENE CREATO PER DISACCOPPIARE LA CHIAMATA REMOTA DA QUELLA EFFETTIVA
        new Thread(()->{
            this.client.notify(message);
        }).start();
    }
}
