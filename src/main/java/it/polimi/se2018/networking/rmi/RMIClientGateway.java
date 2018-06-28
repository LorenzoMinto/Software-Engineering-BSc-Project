package it.polimi.se2018.networking.rmi;

import it.polimi.se2018.networking.Client;
import it.polimi.se2018.networking.ClientInterface;
import it.polimi.se2018.networking.NetworkingException;
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
public final class RMIClientGateway implements ClientInterface, RMIReceiverInterface {

    /**
     * When connection drops, this class try to fix it resetting params. This operation
     * is made in infinite loop until connection is restored.
     * This int value is milliseconds of wait between one reset and the next one.
     */
    private static final int WAIT_TIME_FOR_RETRYING_CONNECTION_FIX = 1000;

    /**
     * String used as message of NetworkingException in case of failure reading for rmi name
     */
    private static final String FAILED_LOOKING_FOR_RMI_NAME = "Failed looking for RMI name";

    /**
     * String used as message of NetworkingException in case of failure exporting rmi object
     */
    private static final String FAILED_EXPORTING_RMI_OBJECT = "Failed exporting RMI object";

    /**
     * String used as reason of failing in case fixing connection thread is interrupted
     */
    private static final String FIXING_CONNECTION_INTERRUPTED = "FixingConnection interrupted";

    /**
     * The receiver of messages sent through this gateway
     */
    private RMIReceiverInterface receiver;

    /**
     * The client connected to this gateway
     */
    private Client client;

    /**
     * Reference to this instance exported
     */
    private RMIReceiverInterface sender;

    /**
     * RMI path of receiver
     */
    private final String path;

    /**
     * RMI port of sender
     */
    private final int port;

    /**
     * Gateway constructor.
     * @param path path to the server
     * @param port port of the server to connect to
     * @param client client connected to this gateway
     * @throws NetworkingException if something fails connecting to the remote server
     */
    public RMIClientGateway(String path, int port, Client client) throws NetworkingException {
        this.path = path;
        this.port = port;
        this.client = client;

        resetRecipient();
        resetSender();
    }

    /**
     * Sets the receiver of the gateway based on the path given during class construction
     * @throws NetworkingException if something during setting goes wrong
     */
    private void resetRecipient() throws NetworkingException {
        try{
            this.receiver = (RMIReceiverInterface) Naming.lookup(path);
        } catch(Exception e){
            throw new NetworkingException(FAILED_LOOKING_FOR_RMI_NAME);
        }
    }

    /**
     * Sets the sender of the gateway based on the port given during class construction
     * @throws NetworkingException if something during setting goes wrong
     */
    private void resetSender() throws NetworkingException{
        try{
            this.sender = (RMIReceiverInterface) UnicastRemoteObject.exportObject(this, this.port);
        } catch(Exception e){
            throw new NetworkingException(FAILED_EXPORTING_RMI_OBJECT);
        }
    }

    @Override
    public void sendMessage(Message message) throws NetworkingException{
        try {
            this.receiver.receiveMessage(message,this.sender);
        } catch (RemoteException e) {
            throw new NetworkingException();
        }
    }

    @Override
    public void fixConnection() {
        new Thread(() -> {
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    resetRecipient();
                    this.client.setConnectionAvailable(true);
                    return;
                } catch (NetworkingException e) {
                    try {
                        Thread.sleep(WAIT_TIME_FOR_RETRYING_CONNECTION_FIX);
                    } catch (InterruptedException e1) {
                        this.client.fail(FIXING_CONNECTION_INTERRUPTED);
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }).start();
    }

    @Override
    public void receiveMessage(Message message, RMIReceiverInterface sender){
        //Il thread viene creato per disaccoppiare la chiamata remota da quella effettiva
        new Thread(()-> this.client.notify(message)).start();
    }
}
