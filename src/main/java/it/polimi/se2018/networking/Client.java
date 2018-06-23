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

    /*  CONSTANTS FOR LOGS' MESSAGES
        Following constants are not commented one by one because they are as self explaining as needed.
        Major information can be found looking for their usage.
        Being private, they are used only in this file. So if a change is needed, just look for usages in this file.
    */
    private static final String ACKNOWLEDGEMENT_MESSAGE_CONSTRUCTOR = "Started a Sagrada Client and connected to Sagrada Server as guest.";
    private static final String ATTEMPT = "Attempt #";
    private static final String SENDING_MESSAGE = " sending message ";
    private static final String THE_MESSAGE_WAS = "The message was: ";
    private static final String COULD_NOT_SEND_THE_MESSAGE_DUE_TO_CONNECTION_ERROR = " could not send the message due to connection error to: ";
    private static final String SUCCESSFULLY_SENT_MESSAGE = " successfully sent message to: ";

    /**
     * Format of of logs
     */
    private static final String LOGGER_FORMAT = "[CLIENT] %1$s %n";

    /**
     * String used as message of NetworkingException in case of failure sending messages
     */
    private static final String AT_LEAST_A_MESSAGE_NOT_SET = "At least on message could not be sent from Client to Server. Message was: ";

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
                fail("Caught NetworkingException: Failed connecting to RMI server.");
            }

        } else if (type == ConnectionType.SOCKET) {
            g = new SocketClientGateway(serverName, port, this);
            /*SocketClientGateway is a thread. So exception that could be thrown in it
             * are sent to this class (Client) throught the .fail() method. */
        }

        this.gateway = g;

        log(ACKNOWLEDGEMENT_MESSAGE_CONSTRUCTOR);
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

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(LOGGER_FORMAT,lr.getMessage());
            }

        });
        newLogger.addHandler(handler);
        return newLogger;
    }

    /**
     * Write the given message as a log
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
            log(ATTEMPT + attempts + SENDING_MESSAGE +message);

            try{
                gateway.sendMessage(message);
            } catch(NetworkingException e) {
                log(ATTEMPT + attempts + COULD_NOT_SEND_THE_MESSAGE_DUE_TO_CONNECTION_ERROR + gateway + ". " + THE_MESSAGE_WAS + message);
                continue;
            }
            correctlySent = true;

            log(ATTEMPT + attempts + SUCCESSFULLY_SENT_MESSAGE + gateway + ". " + THE_MESSAGE_WAS + message);
        }
        //Add failed gateway to a list that will be returned at the end of this method execution
        if(!correctlySent){ throw new NetworkingException(AT_LEAST_A_MESSAGE_NOT_SET +message); }
    }

    /**
     * Method used by client to notify that some kind of unhandlable failure that happened during their running     *
     * @param reason a string containing the explanation of the failure
     */
    public void fail(String reason){
        throw new BadBehaviourRuntimeException(reason);
    }
}
