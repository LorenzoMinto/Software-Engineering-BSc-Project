package it.polimi.se2018.networking.rmi;

import it.polimi.se2018.networking.Client;
import it.polimi.se2018.networking.NetworkingException;
import it.polimi.se2018.networking.SenderInterface;
import it.polimi.se2018.utils.Message;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Extends RMIReceiverInterface because can receive messages from Server,
 * so from the perspective of Server this class it is also a server
 *
 * @author Federico Haag
 */
public final class RMIClientGateway implements SenderInterface, RMIReceiverInterface {

    /**
     * The recipient of messages sent through this gateway
     */
    private RMIReceiverInterface recipient;

    /**
     * The client connected to this gateway
     */
    private Client client;

    /**
     * Reference to this instance exported
     */
    private RMIReceiverInterface sender;

    /**
     * Gateway constructor.
     * @param path path to the server
     * @param port port of the server to connect to
     * @param client client connected to this gateway
     * @throws NetworkingException if something fails connecting to the remote server
     */
    public RMIClientGateway(String path, int port, Client client) throws NetworkingException {
        try{
            this.recipient = (RMIReceiverInterface) Naming.lookup(path);
        } catch(Exception e){
            throw new NetworkingException("Failed looking for RMI name");
        }

        try{
            this.sender = (RMIReceiverInterface) UnicastRemoteObject.exportObject(this, port);
        } catch(Exception e){
            throw new NetworkingException("Failed exporting RMI object");
        }

        this.client = client;
    }

    @Override
    public void sendMessage(Message message) throws NetworkingException{
        try {
            this.recipient.receiveMessage(message,this.sender);
        } catch (RemoteException e) {
            throw new NetworkingException();
        }
    }

    @Override
    public void receiveMessage(Message message, RMIReceiverInterface sender){
        //IL THREAD VIENE CREATO PER DISACCOPPIARE LA CHIAMATA REMOTA DA QUELLA EFFETTIVA
        //TODO: assicurarsi che questa cosa, reduce dalla vecchia versione di networking, serva ancora.
        //TODO: nel caso andasse tolta, eliminare dall'UML la dipendenza di RMIClientGateway rispetto a Thread
        new Thread(()-> this.client.notify(message)).start();
    }
}
