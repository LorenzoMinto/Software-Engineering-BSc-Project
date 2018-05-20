package it.polimi.se2018.networking;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.networking.message.Message;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.ConfigImporter;
import it.polimi.se2018.utils.NoConfigParamFoundException;
import it.polimi.se2018.utils.Observer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Observer, ReceiverInterface, SenderInterface{

    /**
     * Logger class
     */
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    /**
     * How many attempts must be done before declaring sending of a message failed
     */
    private static final int MAX_NUMBER_OF_ATTEMPTS = 5;

    /**
     * Server representation for RMI
     */
    private ReceiverInterface proxyServer;

    /**
     * List of gateways for communicating with clients
     */
    private final List<ReceiverInterface> gateways = new ArrayList<>();

    /**
     * Controller created by the server
     */
    private final Controller controller;

    public static void main (String[] args) {
        new Server();
    }

    /**
     * Server constructor. Do the netwroking setup, creates controller and game
     */
    private Server() {
        LOGGER.setLevel(Level.ALL);

        setupNetworking();

        //Creates controller and game
        this.controller = createController();
        this.controller.register(this);

        listenForCommandsFromConsole();
    }

    /**
     * Setup of networking starting RMI and Socket servers
     */
    private void setupNetworking() {
        try {
            LOGGER.info("Starting RMI...");
            this.proxyServer = new RMIServerGateway("sagradaserver",this);

        } catch (RemoteException e) {
            LOGGER.severe("Failed RMI setup");
            return;
        }

        try{
            LOGGER.info("Starting Socket...");
            new SocketServerGateway(1111,this);

        } catch (RemoteException e) {
            LOGGER.severe("Failed Socket setup");
            return;
        }

        LOGGER.info("Sagrada Server is up.");
    }

    /**
     * Creates an returns the instance of a new controller
     * @return an returns the instance of a new controller
     */
    private Controller createController(){

        //Loads config parameters
        ConfigImporter configImporter = new ConfigImporter("default");
        Properties properties;
        try{
            properties = configImporter.getProperties();
        } catch(NoConfigParamFoundException e){

            //Try with default configuration
            configImporter = new ConfigImporter();

            try{
                properties = configImporter.getProperties();
            } catch(NoConfigParamFoundException ex){

                throw new BadBehaviourRuntimeException("Can't load default config file");
            }
        }

        //Creates the game
        Game game = new Game(
                Integer.parseInt( properties.getProperty("numberOfRounds") ),
                Integer.parseInt( properties.getProperty("maxNumberOfPlayers") )
        );
        game.register(this);

        return new Controller(game,properties);
    }

    /**
     * Add the specified gateway from the list of gateways
     * @param gateway the gateway to add
     */
    private void addGateway(ReceiverInterface gateway) {
        gateways.add(gateway);
    }

    /**
     * Remove the specified gateway from the list of gateways
     * @param gateway the gateway to remove
     */
    private void removeGateway(ReceiverInterface gateway) {
        gateways.remove(gateway);
    }

    @Override
    public void receiveMessage(String message, ReceiverInterface sender) throws RemoteException {

        LOGGER.info(()->"Received message: "+message);

        if(isJoinRequest(message)){

            if( canJoin(message) ){

                addGateway(sender);

                String replyMessage = "Welcome to SagradaServer. You are now an authorized client.";
                sender.receiveMessage(replyMessage,this.proxyServer);

                LOGGER.info("A new client has been authorized");

            } else {

                String replyMessage = "You can't join this server";
                sender.receiveMessage(replyMessage,this.proxyServer);

                LOGGER.info("A new client asked to join but refused");

            }

        }
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        boolean somethingFailed = false;
        for(ReceiverInterface o : gateways){
            int attempts = 0;
            boolean correctlySent = false;
            //Send message. Try sometimes if it fails. When maximum number of attempts is reached, go on next gateway
            while(attempts< MAX_NUMBER_OF_ATTEMPTS && !correctlySent){
                attempts++;
                try{
                    o.receiveMessage(message,this.proxyServer);
                } catch(Exception e){
                    LOGGER.warning("Attempt #"+attempts+": Could not send the message due to connection error to: "+o+". The message was: "+message);
                    continue;
                }
                correctlySent = true;
                LOGGER.info("Attempt #"+attempts+": Successfully sent message to: "+o+". The message was: "+message);
            }
            //Add failed gateway to a list that will be returned at the end of this method execution
            if(!correctlySent){ somethingFailed=true; }
        }
        //Throws exception if at least one message failed to be sent. The caller will decide the severity of this problem
        if(somethingFailed) throw new RemoteException("At least on message could not be sent from Client to Server. Message was: "+message);
    }


    /**
     * This method returns true if the message contains something that
     * enables who sent it to be added to the clients of this server
     * @param m the message received
     * @return true if the message contains something that enables who sent it to be added to the clients of this server
     */
    private boolean canJoin(String m){
        return m.equals("federico");
    }


    /**
     * Returns true if the message is a join request
     * @param m the message received
     * @return True if the message is a join request
     */
    private boolean isJoinRequest(String m){
        return m.equals("federico"); //TODO:inserire quì il criterio corretto
    }




    //Just for testing

    private void listenForCommandsFromConsole(){
        //Codice per inviare messaggio da riga di comando
        LOGGER.info("Start listening for messages...");

        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.print("Inserisci messaggio: ");
            String text = scanner.nextLine();

            //Just for testing comunication
            try {
                sendMessage(text);
            } catch (RemoteException e) {
                LOGGER.severe("Exception while sending a message from the Server console");
            }

            //TODO: implementa quì metodi di servizio per debug

            if(text.equals("exit")){ break; }
        }
        //Fine codice per inviare messaggio da riga di comando
    }

    @Override
    public boolean update(Message m) {
        try {
            sendMessage(m.getMessage());
        } catch (RemoteException e) {
            LOGGER.severe("Exception while sending a message from Server to Clients (asked by update call)");
            return false;
        }

        return true;
    }

    void fail(String m){
        throw new BadBehaviourRuntimeException(m);
    }
}
