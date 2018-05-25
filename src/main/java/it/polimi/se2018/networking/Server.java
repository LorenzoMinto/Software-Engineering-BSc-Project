package it.polimi.se2018.networking;

import it.polimi.se2018.controller.AcceptingPlayerException;
import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.User;
import it.polimi.se2018.utils.message.Message;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.ConfigImporter;
import it.polimi.se2018.utils.NoConfigParamFoundException;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.utils.message.NetworkMessage;

import java.rmi.RemoteException;
import java.util.*;
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

    private HashMap<Player,ReceiverInterface> gatewayPlayerMap = new HashMap<>();

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
    public void receiveMessage(Message message, ReceiverInterface sender) throws RemoteException {

        LOGGER.info(()->"Received message: "+message);

        if(isJoinRequest(message)){

            Player player;
            User user = (User) message.getParam("user");
            String nickname = (String) message.getParam("nickname");
            try{
                player = controller.acceptPlayer(user,nickname);

                addGateway(sender);

                gatewayPlayerMap.put(player,sender);

                sender.receiveMessage(new NetworkMessage(NetworkMessage.types.CONNECTED), this.proxyServer);
                LOGGER.info("A new client has been authorized");

            } catch(AcceptingPlayerException e){
                sender.receiveMessage(new NetworkMessage(NetworkMessage.types.REFUSED),this.proxyServer);
                LOGGER.info("A new client asked to join but refused");
            }
        }
    }

    @Override
    public void sendMessage(Message message) throws RemoteException {
        boolean somethingFailed = false;
        List<ReceiverInterface> g;

        if(message.isBroadcast()){
            g = gateways;
        } else {
            g = new ArrayList<>();
            g.add(gatewayPlayerMap.get(message.getRecipientPlayer()));
        }

        for(ReceiverInterface o : g){
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
    private boolean canJoin(Message m){
        return m.equals("federico");
    } //TODO:inserire quì il criterio corretto


    /**
     * Returns true if the message is a join request
     * @param m the message received
     * @return True if the message is a join request
     */
    private boolean isJoinRequest(Message m){
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

            //TODO: implementa quì metodi di servizio per debug
            /*try {
                sendMessage(text);
            } catch (RemoteException e) {
                LOGGER.severe("Exception while sending a message from the Server console");
            }
            */

            if(text.equals("exit")){ break; }
        }
        //Fine codice per inviare messaggio da riga di comando
    }

    @Override
    public boolean update(Message m) {
        try {
            sendMessage(m);
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
