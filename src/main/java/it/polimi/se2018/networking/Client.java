package it.polimi.se2018.networking;

import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.message.Message;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.utils.message.WaitingRoomMessage;
import it.polimi.se2018.view.CLIView;
import it.polimi.se2018.view.View;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.*;

/**
 *
 * @author Federico Haag
 */
public class Client extends Observable implements SenderInterface, ReceiverInterface {

    private final Logger logger;

    private static final int MAX_NUMBER_OF_ATTEMPTS = 5;

    private final List<SenderInterface> gateways = new ArrayList<>();

    private final String serverName;

    private final int port;

    private final ConnectionType type;

    public Client(ConnectionType type, String serverName, int port, Observer view, boolean debug) {
        this.logger = createLogger(debug);
        this.type = type;
        this.register(view);
        this.serverName = serverName;
        this.port = port;
        setup();
    }

    private void setup(){
        SenderInterface server = null;
        if (type == ConnectionType.RMI) {
            try {
                server = new RMIClientGateway(this.serverName, this.port, this);
            } catch (RemoteException e) {
                fail("Failed connecting to RMI server.");
            }

        } else if (type == ConnectionType.SOCKET) {
            server = new SocketClientGateway(this.serverName, this.port, this);
            /*SocketClientGateway is a thread. So exception that could be thrown in it
             * are sent to this class (Client) throught the .fail() method. */
        }

        addGateway(server);

        info("Started a Sagrada Client and connected to Sagrada Server as guest.");
    }

    private Logger createLogger(boolean debug){

        Level level = debug ? Level.FINE : Level.INFO;

        Logger newLogger = Logger.getLogger(Client.class.getName());
        newLogger.setUseParentHandlers(false);
        newLogger.setLevel(level);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(level);
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

    private void info(String msg){
        logger.info(msg);
    }

    private void fine(String msg){
        logger.fine(msg);
    }

    @Override
    public void receiveMessage(Message message, ReceiverInterface sender) throws RemoteException {
        notify(message);
    }

    @Override
    public void sendMessage(Message message) throws RemoteException {
        boolean somethingFailed = false;
        for(SenderInterface o : gateways){
            int attempts = 0;
            boolean correctlySent = false;
            //Send message. Try sometimes if it fails. When maximum number of attempts is reached, go on next gateway
            while(attempts< MAX_NUMBER_OF_ATTEMPTS && !correctlySent){
                attempts++;
                fine("Attempt #"+attempts+": Sending message: "+message);

                try{
                    o.sendMessage(message);
                } catch(Exception e){
                    e.printStackTrace();
                    fine("Attempt #"+attempts+": Could not send the message due to connection error to: "+o+". The message was: "+message);
                    continue;
                }
                correctlySent = true;

                fine("Attempt #"+attempts+": Successfully sent message to: "+o+". The message was: "+message);
            }
            //Add failed gateway to a list that will be returned at the end of this method execution
            if(!correctlySent){ somethingFailed=true; }
        }
        //Throws exception if at least one message failed to be sent. The caller will decide the severity of this problem
        if(somethingFailed) throw new RemoteException("At least on message could not be sent from Client to Server. Message was: "+message);
    }

    private void addGateway(SenderInterface gateway) {
        gateways.add(gateway);
    }

    void fail(String m){
        throw new BadBehaviourRuntimeException(m);
    }
}
