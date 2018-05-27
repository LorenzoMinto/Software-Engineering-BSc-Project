package it.polimi.se2018.networking;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.utils.*;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.utils.message.*;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Observer, ReceiverInterface, SenderInterface{

    private static final String MAX_NUMBER_OF_PLAYERS = "maxNumberOfPlayers";

    private Timer timerForLaunchingGame;

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

    private HashMap<String,ReceiverInterface> waitingList = new HashMap<>();

    /**
     * List of gateways for communicating with clients
     */
    private final List<ReceiverInterface> gateways = new ArrayList<>();

    private HashMap<String,ReceiverInterface> playerIDToGatewayMap = new HashMap<>();

    private HashMap<ReceiverInterface, String> gatewayToPlayerIDMap = new HashMap<>();

    private enum ServerState {
        WAITING_ROOM,
        FORWARDING_TO_CONTROLLER
    }

    private ServerState serverState = ServerState.WAITING_ROOM;

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
                Integer.parseInt( properties.getProperty(MAX_NUMBER_OF_PLAYERS) )
        );
        game.register(this);

        return new Controller(game,properties);
    }

    @Override
    public void receiveMessage(Message message, ReceiverInterface sender) throws RemoteException {

        Message returnMessage;

        if(message instanceof WaitingRoomMessage){

            returnMessage = handleWaitingRoomMessage((WaitingRoomMessage)message,sender);

        } else if(message instanceof VCMessage) {

            //Add the Player reference to the message in order to let controller manage the move correctly
            VCMessage vcMessage = new VCMessage((VCMessage.types)message.getType(), message.getAllParams(), gatewayToPlayerIDMap.get(sender));
            //Send message to controller
            returnMessage = controller.handleMove(vcMessage);

        } else {
            returnMessage = new WaitingRoomMessage(WaitingRoomMessage.types.BAD_FORMATTED);
        }

        //Send answer message back to the sender
        sender.receiveMessage(returnMessage, this.proxyServer);
        if (LOGGER.isLoggable(Level.INFO)) { LOGGER.info("Received message: "+message+". Answered with: "+returnMessage+"."); }
    }

    private Message handleWaitingRoomMessage(WaitingRoomMessage message, ReceiverInterface client){
        if(serverState != ServerState.WAITING_ROOM){
            return new WaitingRoomMessage(WaitingRoomMessage.types.DENIED);
        }

        String nickname = (String) message.getParam("nickname");

        switch (message.getType()){
            case JOIN:
                return addInWaitingRoom(nickname,client);
            case LEAVE:
                return removeFromWaitingRoom(nickname,client);
            default:
                return new WaitingRoomMessage(WaitingRoomMessage.types.BAD_FORMATTED);
        }
    }

    private Message addInWaitingRoom(String nickname, ReceiverInterface client){
        Message message;

        if(waitingList.size() < controller.getConfigProperty(MAX_NUMBER_OF_PLAYERS)){
            waitingList.putIfAbsent(nickname,client);
            message = new WaitingRoomMessage(WaitingRoomMessage.types.ADDED);
        } else {
            //Should never happen because server status should change to FORWARDING_TO_CONTROLLER
            message = new WaitingRoomMessage(WaitingRoomMessage.types.DENIED);
        }

        if(waitingList.size() == controller.getConfigProperty(MAX_NUMBER_OF_PLAYERS)){
            launchGame();
        }

        if(waitingList.size() >= controller.getConfigProperty("minNumberOfPlayers")){
            startTimerForLaunchingGame();
        }

        return message;
    }

    private Message removeFromWaitingRoom(String nickname, ReceiverInterface client){
        if( waitingList.get(nickname) == client ){
            waitingList.remove(nickname);

            if(waitingList.size() < controller.getConfigProperty("minNumberOfPlayers")){
                cancelTimerForLaunchingGame();
            }

            return new WaitingRoomMessage(WaitingRoomMessage.types.REMOVED);
        } else {
            return new WaitingRoomMessage(WaitingRoomMessage.types.BAD_FORMATTED);
        }
    }

    private void launchGame(){
        //Forward future messages to controller and prevent that waitinglist is changed
        this.serverState = ServerState.FORWARDING_TO_CONTROLLER;
        //Add ReceiverInterfaces of players to gateways that will manage the bi-directional communication during game
        gateways.addAll(waitingList.values());
        //Map players id with gateway and vice versa
        playerIDToGatewayMap.putAll(waitingList);
        for(Map.Entry<String, ReceiverInterface> entry : playerIDToGatewayMap.entrySet()){
            gatewayToPlayerIDMap.put(entry.getValue(), entry.getKey());
        }
        //Send players to controller and let it actually starting the game
        controller.launchGame(waitingList.keySet());
    }

    private void startTimerForLaunchingGame(){
        cancelTimerForLaunchingGame();

        this.timerForLaunchingGame = new Timer();
        this.timerForLaunchingGame.schedule(new TimerTask() {
            @Override
            public void run() {
                launchGame();
            }
        },controller.getConfigProperty("timeoutLaunchingGame")*1000);
    }

    private void cancelTimerForLaunchingGame(){
        if(this.timerForLaunchingGame == null){
            return;
        }
        this.timerForLaunchingGame.cancel();
    }

    @Override
    public void sendMessage(Message m) throws RemoteException {
        boolean somethingFailed = false;
        List<ReceiverInterface> g;

        ViewBoundMessage message = (ViewBoundMessage) m;

        if(message.isBroadcast()){
            g = gateways;
        } else {
            g = new ArrayList<>();
            g.add(playerIDToGatewayMap.get(message.getReceivingPlayerID()));
        }

        for(ReceiverInterface o : g){
            int attempts = 0;
            boolean correctlySent = false;
            //Send message. Try sometimes if it fails. When maximum number of attempts is reached, go on next gateway
            while(attempts< MAX_NUMBER_OF_ATTEMPTS && !correctlySent) {
                attempts++;
                try {
                    o.receiveMessage(message, this.proxyServer);
                } catch (Exception e) {
                    LOGGER.warning("Attempt #" + attempts + ": Could not send the message due to connection error to: " + o + ". The message was: " + message);
                    continue;
                }
                correctlySent = true;

                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("Attempt #" + attempts + ": Successfully sent message to: " + o + ". The message was: " + message);
                }
            }
            //Add failed gateway to a list that will be returned at the end of this method execution
            if(!correctlySent){ somethingFailed=true; }
        }
        //Throws exception if at least one message failed to be sent. The caller will decide the severity of this problem
        if(somethingFailed) throw new RemoteException("At least on message could not be sent from Client to Server. Message was: "+message);
    }

    /**
     * Metodo per inviare messaggi ai client dalla console del server
     */
    private void listenForCommandsFromConsole(){
        LOGGER.info("Start listening for messages...");

        /*
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.print("Inserisci messaggio: ");
            String text = scanner.nextLine();

            try {
                sendMessage(...);
            } catch (RemoteException e) {
                LOGGER.severe("Exception while sending a message from the Server console");
            }

            if(text.equals("exit")){ break; }
        }
        */
    }

    @Override
    public boolean update(Message m) {
        boolean succeeded;
        try {
            sendMessage(m);
            succeeded = true;
        } catch (RemoteException e) {
            LOGGER.severe("Exception while sending a message from Server to Clients (asked by update call)");
            succeeded = false;
        }
        return succeeded;
    }

    void fail(String m){
        throw new BadBehaviourRuntimeException(m);
    }
}
