package it.polimi.se2018.networking;

import it.polimi.se2018.networking.rmi.RMIClientGateway;
import it.polimi.se2018.networking.socket.SocketClientGateway;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.Observer;

import java.util.logging.*;

/**
 * Client
 * @author Federico Haag
 */
public class Client extends Observable implements SenderInterface {

    /**
     * Logger
     */
    private final Logger logger;

    /**
     * Max number of attempts sending a message before throwing a networking exception
     */
    private static final int MAX_NUMBER_OF_ATTEMPTS = 5;

    /**
     * Gateway for sending/receiving message to/from server
     */
    private final SenderInterface gateway;

    /**
     * If true, some debug messages are logged in the console
     */
    private final boolean debug;

    /**
     * Constructor for Client
     *
     * @param type type of connection
     * @param serverName name of the server
     * @param port port used for communication
     * @param view view that uses this client
     * @param debug boolean value for logging or not some debug messages
     */
    public Client(ConnectionType type, String serverName, int port, Observer view, boolean debug) {
        this.logger = createLogger();
        this.debug = debug;

        this.register(view);

        SenderInterface g = null;
        if (type == ConnectionType.RMI) {
            try {
                g = new RMIClientGateway(serverName, port, this);
            } catch (NetworkingException e){
                fail("NetworkingException catched: Failed connecting to RMI server.");
            }

        } else if (type == ConnectionType.SOCKET) {
            g = new SocketClientGateway(serverName, port, this);
            /*SocketClientGateway is a thread. So exception that could be thrown in it
             * are sent to this class (Client) throught the .fail() method. */
        }

        this.gateway = g;

        log("Started a Sagrada Client and connected to Sagrada Server as guest.");
    }

    /**
     * Creates and returns a logger with custom properties (writes on console)
     * @return a logger with custom properties (writes on console)
     */
    private Logger createLogger(){
        Logger newLogger = Logger.getLogger(Client.class.getName());
        newLogger.setUseParentHandlers(false);
        newLogger.setLevel(Level.INFO);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.INFO);
        handler.setFormatter(new SimpleFormatter(){
            private static final String FORMAT = "[CLIENT] %1$s %n";

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(FORMAT,lr.getMessage());
            }

        });
        newLogger.addHandler(handler);
        return newLogger;
    }

    /**
     * Write the given messange as a log
     * @param msg the message to be logged
     */
    private void log(String msg){
        if(this.debug){
            logger.info(msg);
        }
    }

    @Override
    public void sendMessage(Message message) throws NetworkingException {
        //Send message. Try sometimes if it fails. When maximum number of attempts is reached, go on next gateway

        int attempts = 0;
        boolean correctlySent = false;
        while(attempts< MAX_NUMBER_OF_ATTEMPTS && !correctlySent){
            attempts++;
            log("Attempt #"+attempts+": Sending message: "+message);

            try{
                gateway.sendMessage(message);
            } catch(NetworkingException e) {
                log("Attempt #" + attempts + ": Could not send the message due to connection error to: " + gateway + ". The message was: " + message);
                continue;
            }
            correctlySent = true;

            log("Attempt #"+attempts+": Successfully sent message to: " + gateway + ". The message was: "+message);
        }
        //Add failed gateway to a list that will be returned at the end of this method execution
        if(!correctlySent){ throw new NetworkingException("At least on message could not be sent from Client to Server. Message was: "+message); }
    }

    /**
     * Method used by client to notify that some kind of unhandlable failure that happened during their running     *
     * @param reason a string containing the explanation of the failure
     */
    public void fail(String reason){
        throw new BadBehaviourRuntimeException(reason);
    }
}
