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
    private ReceiverInterface client;
    private ReceiverInterface proxySender;


    RMIClientGateway(String path, int port, ReceiverInterface client) throws NetworkingException {
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

    public void sendMessage(Message message) throws RemoteException, NetworkingException{
        this.recipient.receiveMessage(message,this.proxySender);
    }

    public void receiveMessage(Message message, ReceiverInterface sender){
        //IL THREAD VIENE CREATO PER DISACCOPPIARE LA CHIAMATA REMOTA DA QUELLA EFFETTIVA
        new Thread(()->{
            try {
                this.client.receiveMessage(message,sender);
            } catch (NetworkingException | RemoteException e) {
                //can't happen because client is not remote
            }
        }).start();
    }
}
