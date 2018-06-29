package it.polimi.se2018.networking;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.networking.rmi.RMIServerGateway;
import it.polimi.se2018.networking.socket.SocketServerGateway;
import it.polimi.se2018.utils.*;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.utils.Message;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server
 * @author Federico Haag
 */
public class Server implements Observer, SenderInterface, ServerInterface {

    /**
     * Name of the default config file
     */
    private static final String DEFAULT_CONFIG_FILE_NAME = "default";


    /*  CONSTANTS FOR CONFIG PROPERTIES NAMES
        Following constants are not commented one by one because they are as self explaining as needed.
        Major information can be found looking for their usage.
        Being private, they are used only in this file. So if a change is needed, just look for usages in this file.
     */
    private static final String CONFIG_PROPERTY_MAX_NUMBER_OF_PLAYERS = "maxNumberOfPlayers";
    private static final String CONFIG_PROPERTY_NUMBER_OF_ROUNDS = "numberOfRounds";
    private static final String CONFIG_PROPERTY_NICKNAME = "nickname";
    private static final String CONFIG_PROPERTY_MIN_NUMBER_OF_PLAYERS = "minNumberOfPlayers";
    private static final String CONFIG_PROPERTY_TIMEOUT_LAUNCHING_GAME = "timeoutLaunchingGame";


    /*  CONSTANTS FOR LOGS
        Following constants are not commented one by one because they are as self explaining as needed.
        Major information can be found looking for their usage.
        Being private, they are used only in this file. So if a change is needed, just look for usages in this file.
     */
    private static final String EXCEPTION_SENDING_TO_CLIENTS_DURING_UPDATE = "Exception while sending a message from Server to Clients (asked by update call).";
    private static final String ERROR_SENDING_MESSAGE = "At least on message could not be sent from Client to Server. Message was: ";
    private static final String FIRST_PARAMETER_NAME_OF_THE_SERVER_IS_COMPULSORY = "First parameter (name of the server) is compulsory.";
    private static final String SECOND_PARAMETER_PORT_NUMBER_FOR_RMI_IS_COMPULSORY = "Second parameter (port number for RMI) is compulsory.";
    private static final String THIRD_PARAMETER_PORT_NUMBER_FOR_SOCKET_IS_COMPULSORY = "Third parameter (port number for SOCKET) is compulsory.";
    private static final String FOURTH_PARAMETER_MAX_NUMBER_OF_ATTEMPTS_IS_COMPULSORY = "Fourth parameter (max number of attempts) is compulsory.";
    private static final String SIXTH_PARAMETER_MAX_NUMBER_OF_ATTEMPTS_IS_COMPULSORY = "Sixth parameter (server hostname) is compulsory.";
    private static final String SEVENTH_PARAMETER_MAX_NUMBER_OF_ATTEMPTS_IS_COMPULSORY = "Seventh parameter (path for persistency) is compulsory.";
    private static final String STARTING_RMI = "Starting RMI...";
    private static final String FAILED_RMI_SETUP = "Failed RMI setup";
    private static final String STARTING_SOCKET = "Starting Socket...";
    private static final String SAGRADA_SERVER_IS_UP = "Sagrada Server is up.";
    private static final String CANT_LOAD_DEFAULT_CONFIG_FILE = "Can't load default config file.";
    private static final String ERROR_ANSWERING_TO_MESSAGE = "Error answering to message ";
    private static final String RECEIVED_MESSAGE = "Received message: ";
    private static final String ANSWERED_WITH = "Answered with: ";
    private static final String ATTEMPT = "Attempt #";
    private static final String COULD_NOT_SEND_THE_MESSAGE_DUE_TO_CONNECTION_ERROR_TO = "Could not send the message due to connection error to";
    private static final String THE_MESSAGE_WAS = "The message was";
    private static final String SUCCESSFULLY_SENT_MESSAGE_TO = "Successfully sent message to";


    /*  CONSTANTS FOR MESSAGES CONTENT
        Following constants are not commented one by one because they are as self explaining as needed.
        Major information can be found looking for their usage.
        Being private, they are used only in this file. So if a change is needed, just look for usages in this file.
     */
    private static final String GAME_IS_PLAYING = "GAME_IS_PLAYING";


    /**
     * Timer used to call launchGame() after a specified time
     */
    private Timer timerForLaunchingGame = new Timer();

    /**
     * Variable used to know if the timerForLaunchingGame is running or not
     */
    private boolean isTimerForLaunchingGameActive = false;

    /**
     * Logger class
     */
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    /**
     * How many attempts must be done before declaring sending of a message failed
     */
    private final int maxNumberOfAttempts;

    /**
     * Server IP
     */
    private final String serverIP;

    /**
     * Name of the server
     */
    private final String serverName;

    /**
     * Port number for RMI
     */
    private final int portNumberRMI;

    /**
     * Port number for Socket
     */
    private final int portNumberSOCKET;

    /**
     * File name of the config file
     */
    private final String configFileName;

    /**
     * List of players waiting for playing. Implemented as a map to store coupling of player id and respective client
     */
    private HashMap<String,ClientProxyInterface> waitingList = new HashMap<>();

    /**
     * List of gateways for communicating with clients
     */
    private final List<ClientProxyInterface> gateways = new ArrayList<>();

    /**
     * Map each player id to respective gateway
     */
    private HashMap<String,ClientProxyInterface> playerIDToGatewayMap = new HashMap<>();

    /**
     * Map each gateway to respective player id
     */
    private HashMap<ClientProxyInterface, String> gatewayToPlayerIDMap = new HashMap<>();

    /**
     * Enum representing the possibile server states
     */
    private enum ServerState {
        WAITING_ROOM,
        FORWARDING_TO_CONTROLLER
    }

    /**
     * State of the server to distinguish from waiting room
     * handling and forwarding to controller.
     */
    private ServerState serverState = ServerState.WAITING_ROOM;

    /**
     * Controller created by the server
     */
    private final Controller controller;

    /**
     * Gateways that are disconnected
     */
    private List<ClientProxyInterface> disconnectedGateways = new ArrayList<>();

    /**
     * Queue of all messages that were not sent due to connection problems
     */
    private HashMap<ClientProxyInterface,List<Message>> unSentMessages = new HashMap<>();

    /**
     * Path for persistency rankings file
     */
    private final String persistencyPath;

    /**
     * The main class for server in order to make it runnable.
     *
     * @param args arguments passed by command line
     */
    public static void main (String[] args) {

        String serverName;
        try{
            serverName = args[0];
        } catch (IndexOutOfBoundsException e){
            LOGGER.info(FIRST_PARAMETER_NAME_OF_THE_SERVER_IS_COMPULSORY);
            return;
        }

        int portNumberRMI;
        try{
            portNumberRMI = Integer.parseInt(args[1]);
        } catch (IndexOutOfBoundsException e){
            LOGGER.info(SECOND_PARAMETER_PORT_NUMBER_FOR_RMI_IS_COMPULSORY);
            return;
        }

        int portNumberSOCKET;
        try{
            portNumberSOCKET = Integer.parseInt(args[2]);
        } catch (IndexOutOfBoundsException e){
            LOGGER.info(THIRD_PARAMETER_PORT_NUMBER_FOR_SOCKET_IS_COMPULSORY);
            return;
        }

        int maxNumberOfAttempts;
        try{
            maxNumberOfAttempts = Integer.parseInt(args[3]);
        } catch (IndexOutOfBoundsException e){
            LOGGER.info(FOURTH_PARAMETER_MAX_NUMBER_OF_ATTEMPTS_IS_COMPULSORY);
            return;
        }

        String configFileName;
        try{
            configFileName = args[4];
        } catch (IndexOutOfBoundsException e){
            configFileName = DEFAULT_CONFIG_FILE_NAME;
        }

        String serverIP;
        try{
            serverIP = args[5];
        } catch (IndexOutOfBoundsException e){
            LOGGER.info(SIXTH_PARAMETER_MAX_NUMBER_OF_ATTEMPTS_IS_COMPULSORY);
            return;
        }

        String persistencyPath;
        try{
            persistencyPath = args[6];
        } catch (IndexOutOfBoundsException e){
            LOGGER.info(SEVENTH_PARAMETER_MAX_NUMBER_OF_ATTEMPTS_IS_COMPULSORY);
            return;
        }

        new Server(serverIP,serverName,portNumberRMI,portNumberSOCKET,maxNumberOfAttempts,configFileName,persistencyPath);
    }

    /**
     * Server constructor. Do the netwroking setup, creates controller and game
     */
    private Server(String serverIP, String serverName, int portNumberRMI, int portNumberSOCKET, int maxNumberOfAttempts, String configFileName, String persistencyPath) {
        LOGGER.setLevel(Level.ALL);
        this.serverIP = serverIP;
        this.serverName = serverName;
        this.portNumberRMI = portNumberRMI;
        this.portNumberSOCKET = portNumberSOCKET;
        this.configFileName = configFileName;
        this.maxNumberOfAttempts = maxNumberOfAttempts;
        this.persistencyPath = persistencyPath;

        setupNetworking();

        //Creates controller and game
        this.controller = createController();
        this.controller.register(this);

        new Pinging(this,ViewBoundMessageType.PING).start();
    }

    /**
     * Setup of networking starting RMI and Socket servers
     */
    private void setupNetworking() {

        System.setProperty("java.rmi.server.hostname",this.serverIP);

        try {
            LOGGER.info(STARTING_RMI);
            new RMIServerGateway(this.serverName,this.portNumberRMI,this);

        } catch (RemoteException | MalformedURLException e) {
            LOGGER.severe(FAILED_RMI_SETUP);
            return;
        }

        LOGGER.info(STARTING_SOCKET);
        new SocketServerGateway(this.portNumberSOCKET,this);

        LOGGER.info(SAGRADA_SERVER_IS_UP);
    }

    /**
     * Creates an returns the instance of a new controller.
     *
     * @return an returns the instance of a new controller
     */
    private Controller createController(){

        //Loads config parameters
        ConfigImporter configImporter = new ConfigImporter(this.configFileName);
        Properties properties;
        try{
            properties = configImporter.getProperties();
        } catch(NoConfigParamFoundException e){

            //Try with default configuration
            configImporter = new ConfigImporter();

            try{
                properties = configImporter.getProperties();
            } catch(NoConfigParamFoundException ex){

                throw new BadBehaviourRuntimeException(CANT_LOAD_DEFAULT_CONFIG_FILE);
            }
        }

        //Creates the game
        Game game = new Game(
                Integer.parseInt( properties.getProperty(CONFIG_PROPERTY_NUMBER_OF_ROUNDS) ),
                Integer.parseInt( properties.getProperty(CONFIG_PROPERTY_MAX_NUMBER_OF_PLAYERS) )
        );
        game.register(this);

        properties.put("persistencyPath",this.persistencyPath);

        return new Controller(game,properties,LOGGER);
    }

    /**
     * Handles inbound messages
     * @param message the received message
     * @param sender the sender of the message
     */
    public void handleInBoundMessage(Message message, ClientProxyInterface sender) {

        ControllerBoundMessageType type = (ControllerBoundMessageType) message.getType();

        Message returnMessage;

        if(message.isMove()){

            message.setPlayerID( gatewayToPlayerIDMap.get(sender) );

            if(message.isMove(Move.JOIN) || message.isMove(Move.LEAVE)){
                returnMessage = handleWaitingRoomMessage(message,sender);

            } else {
                returnMessage = controller.handleMoveMessage(message);
            }

        } else {

            returnMessage = (type==ControllerBoundMessageType.PING) ? null : new Message(ViewBoundMessageType.ERROR_MESSAGE);

        }

        if(returnMessage!=null){
            try{
                //Send answer message back to the sender
                sender.receiveMessage(returnMessage);

                //Notify all players
                if(returnMessage.getType()==ViewBoundMessageType.ADDED_TO_WR){
                    sendMessage(new Message(ViewBoundMessageType.PLAYER_ADDED_TO_WR,Message.fastMap("player",returnMessage.getPlayerID())));

                } else if(returnMessage.getType()==ViewBoundMessageType.REMOVED_FROM_WR){
                    sendMessage(new Message(ViewBoundMessageType.PLAYER_REMOVED_FROM_WR,Message.fastMap("player",returnMessage.getPlayerID())));
                }

            } catch (NetworkingException e){
                LOGGER.severe(ERROR_ANSWERING_TO_MESSAGE +message);
            }
        }

        //Log that message was handled
        if(message.getType()!=ControllerBoundMessageType.PING){logInfo(RECEIVED_MESSAGE + message + ". " + ANSWERED_WITH +returnMessage + ".");}
    }

    /**
     * Reads the type of message and calls needed methods depending on that.
     * @param message the message received
     * @param sender the sender of the message
     * @return a message containing if the operation went good or not
     */
    private Message handleWaitingRoomMessage(Message message, ClientProxyInterface sender){
        if(serverState != ServerState.WAITING_ROOM){
            return new Message(ViewBoundMessageType.JOIN_WR_DENIED_PLAYING, GAME_IS_PLAYING);
        }

        String nickname;
        try {
            nickname = (String) message.getParam(CONFIG_PROPERTY_NICKNAME);
        } catch (NoSuchParamInMessageException e) {
            return new Message(ViewBoundMessageType.BAD_FORMATTED);
        }

        if(message.isMove(Move.JOIN)){
            return addInWaitingRoom(nickname,sender);

        } else if(message.isMove(Move.LEAVE)){
            return removeFromWaitingRoom(nickname,sender);

        } else {
            return null;
        }
    }

    /**
     * Adds the given couple nickname - client from the waiting room
     * @param nickname the nickname to add from the waiting room
     * @param client the client to add from the waiting room
     * @return a message containing if the operation went good or not
     */
    private Message addInWaitingRoom(String nickname, ClientProxyInterface client){
        Message message;

        if (nickname.equals("")) {
            return new Message(ViewBoundMessageType.ERROR_MESSAGE);
        }

        if(waitingList.size() < controller.getConfigProperty(CONFIG_PROPERTY_MAX_NUMBER_OF_PLAYERS)){
            if(!waitingList.containsKey(nickname)){
                waitingList.put(nickname,client);
                message = new Message(ViewBoundMessageType.ADDED_TO_WR,Message.fastMap("players",new ArrayList<>(waitingList.keySet())),nickname,EnumSet.of(Move.LEAVE));
            } else {
                message = new Message(ViewBoundMessageType.JOIN_WR_DENIED_NICKNAME);
            }
        } else {
            //Should never happen because server status should change to FORWARDING_TO_CONTROLLER
            message = new Message(ViewBoundMessageType.JOIN_WR_DENIED_LIMIT);
        }

        checkForLaunchingGame();

        return message;
    }

    /**
     * Remove the given couple nickname - client from the waiting room
     * @param nickname the nickname to remove from the waiting room
     * @param client the client to remove from the waiting room
     * @return a message containing if the operation went good or not
     */
    private Message removeFromWaitingRoom(String nickname, ClientProxyInterface client){
        if( waitingList.containsKey(nickname) && waitingList.get(nickname).equals(client) ){
            waitingList.remove(nickname);

            if(waitingList.size() < controller.getConfigProperty(CONFIG_PROPERTY_MIN_NUMBER_OF_PLAYERS)){
                cancelTimerForLaunchingGame();
            }

            return new Message(ViewBoundMessageType.REMOVED_FROM_WR,null,nickname,EnumSet.of(Move.JOIN));
        } else {
            return new Message(ViewBoundMessageType.BAD_FORMATTED);
        }
    }

    /**
     * Remove the given client from the waiting room
     * @param client the client to remove from the waiting room
     * @return a message containing if the operation went good or not
     */
    private boolean removeFromWaitingRoom(ClientProxyInterface client){
        for (Map.Entry<String, ClientProxyInterface> entry : waitingList.entrySet()) {
            if (entry.getValue().equals(client)) {
                waitingList.remove(entry.getKey());
                return true;
            }
        }
        return false;
    }

    /**
     * Launch the game if 4 players are in waiting room and manages the TimerForLaunchingGame
     */
    private void checkForLaunchingGame(){
        if(waitingList.size() == controller.getConfigProperty(CONFIG_PROPERTY_MAX_NUMBER_OF_PLAYERS)){
            //The game can be launched. Eventual timer is stopped. Game is launched.
            cancelTimerForLaunchingGame();
            launchGame();
        } else if(waitingList.size() >= controller.getConfigProperty(Server.CONFIG_PROPERTY_MIN_NUMBER_OF_PLAYERS)){
            //The game can be launched. If timer was not already started, it is started now.
            if(!this.isTimerForLaunchingGameActive){ startTimerForLaunchingGame(); }
        } else {
            //Players are not enough for starting game. If timer was started, now it is stopped
            if(this.isTimerForLaunchingGameActive){ cancelTimerForLaunchingGame(); }
        }
    }

    /**
     * Start the TimerForLaunchingGame.
     * Before calling this method, check that isTimerForLaunchingGameActive is false.
     */
    private void startTimerForLaunchingGame(){
        this.isTimerForLaunchingGameActive = true;
        this.timerForLaunchingGame = new Timer();
        this.timerForLaunchingGame.schedule(new TimerTask() {
            @Override
            public void run() {
                launchGame();
            }
        },(long)(controller.getConfigProperty(CONFIG_PROPERTY_TIMEOUT_LAUNCHING_GAME)*1000));
    }

    /**
     * Stop the TimerForLaunchingGame.
     * Before calling this method, check that isTimerForLaunchingGameActive is true.
     */
    private void cancelTimerForLaunchingGame(){
        this.isTimerForLaunchingGameActive = false;
        this.timerForLaunchingGame.cancel();
    }

    /**
     * Sets the server state to FORWARDING_TO_CONTROLLER in order to forward
     * future messages to controller instance (for move handling). It also
     * sets the gateways for handling bidirectional communication server to/from client
     * and register in bidirectional map the coupling of players with respective client interface
     * and vice versa.
     */
    private void launchGame(){
        //Forward future messages to controller and prevent that waitinglist is changed
        this.serverState = ServerState.FORWARDING_TO_CONTROLLER;
        //Add ReceiverInterfaces of players to gateways that will manage the bi-directional communication during game
        gateways.addAll(waitingList.values());
        //Map players id with gateway and vice versa, plus creates unSentMessages map
        playerIDToGatewayMap.putAll(waitingList);
        for(Map.Entry<String, ClientProxyInterface> entry : playerIDToGatewayMap.entrySet()){
            gatewayToPlayerIDMap.put(entry.getValue(), entry.getKey());
            unSentMessages.put(entry.getValue(),new ArrayList<>());
        }
        //Send players to controller and let it actually starting the game
        controller.launchGame(waitingList.keySet());
    }

    /**
     * Rreturns the gateway(s) to send the given message
     * @param message message to analyze
     * @return the list of gateways to send to the message
     */
    private List<ClientProxyInterface> getGateway(Message message){
        List<ClientProxyInterface> g;
        if(message.getPlayerID()==null){ //Means that message is broadcast
            g = this.serverState == ServerState.WAITING_ROOM ? new ArrayList<>(waitingList.values()) : gateways;
        } else {
            g = new ArrayList<>();
            g.add(playerIDToGatewayMap.get(message.getPlayerID()));
        }
        return g;
    }

    @Override
    public void sendMessage(Message message) throws NetworkingException {
        boolean somethingFailed = false;
        List<ClientProxyInterface> g = getGateway(message);


        for(ClientProxyInterface o : g){
            int attempts = 0;
            boolean correctlySent = false;

            if(disconnectedGateways.contains(o)){
                unSentMessages.get(o).add(message);
                continue;
            }

            //Send message. Try sometimes if it fails. When maximum number of attempts is reached, go on next gateway
            while(attempts< maxNumberOfAttempts && !correctlySent) {
                attempts++;
                try {
                    o.receiveMessage(message);
                } catch (NetworkingException e) {
                    LOGGER.warning(ATTEMPT + attempts + ": " + COULD_NOT_SEND_THE_MESSAGE_DUE_TO_CONNECTION_ERROR_TO + ": " + o + ". " + THE_MESSAGE_WAS + ": " + message);
                    continue;
                }

                correctlySent = true;

                if (message.getType()!=ViewBoundMessageType.PING) {
                    logInfo(ATTEMPT + attempts + ": " + SUCCESSFULLY_SENT_MESSAGE_TO + ": " + o + ". " + THE_MESSAGE_WAS + ": " + message);
                }
            }
            //Add failed gateway to a list that will be returned at the end of this method execution


            if(!correctlySent){
                handleSendMessageError(o);
                somethingFailed=true;
            }
        }
        //Throws exception if at least one message failed to be sent. The caller will decide the severity of this problem
        if(somethingFailed) throw new NetworkingException(ERROR_SENDING_MESSAGE +message);
    }

    /**
     * Method created to decrease cognitive complexity of sendMessage()
     * @param o the gateway where the sending message call failed
     */
    private void handleSendMessageError(ClientProxyInterface o){
        if(this.serverState==ServerState.WAITING_ROOM){
            removeFromWaitingRoom(o);
        } else {
            handleDisconnectedGateway(o);
        }
    }

    /**
     * Handles the disconnecting of a client
     * @param gateway the disconnected client's gateway
     */
    private void handleDisconnectedGateway(ClientProxyInterface gateway){
        this.disconnectedGateways.add(gateway);
        this.controller.playerLostConnection(gatewayToPlayerIDMap.get(gateway));

        new Thread(()->{
            while(true){
                boolean restored = false;
                try {
                    gateway.receiveMessage(new Message(ViewBoundMessageType.PING));
                    this.disconnectedGateways.remove(gateway);
                    restored = true;
                } catch (NetworkingException e) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e1) {
                        Thread.currentThread().interrupt();
                    }
                }

                if(restored){
                    resendUnSentMessages(gateway);
                    return;
                }
            }
        }).start();
    }

    /**
     * Method created to lower cognitive compexity of handleDisconnectedGateway().
     * Try to send messages that were not sent of a given gateway
     * @param gateway the gateway to look into for un sent messages
     */
    private void resendUnSentMessages(ClientProxyInterface gateway){
        for(Message message : this.unSentMessages.get(gateway)){
            try {
                sendMessage(message);
            } catch (NetworkingException e) {
                return;
            }
        }
    }

    @Override
    public boolean update(Message m) {
        boolean succeeded;
        try {
            sendMessage(m);
            succeeded = true;
        } catch (NetworkingException e) {
            LOGGER.severe(EXCEPTION_SENDING_TO_CLIENTS_DURING_UPDATE);
            succeeded = false;
        }
        return succeeded;
    }

    /**
     * Method used by server threads that have to notify server of some kind
     * of unhandlable failure that happened during their running.
     *
     * @param reason a string containing the explanation of the failure
     */
    public void fail(String reason){
        throw new BadBehaviourRuntimeException(reason);
    }

    @Override
    public void lostSocketConnection(ClientProxyInterface sender) {
        if (serverState == ServerState.WAITING_ROOM) {

            removeFromWaitingRoom(sender);

        } else if(serverState == ServerState.FORWARDING_TO_CONTROLLER && gatewayToPlayerIDMap.containsKey(sender)){
            this.disconnectedGateways.add(sender);
            controller.playerLostConnection(gatewayToPlayerIDMap.get(sender));
        }
    }

    @Override
    public void restoredSocketConnection(ClientProxyInterface previous, ClientProxyInterface next) {
        if(serverState == ServerState.FORWARDING_TO_CONTROLLER && gatewayToPlayerIDMap.containsKey(previous)){
            String playerID = gatewayToPlayerIDMap.get(previous);
            gatewayToPlayerIDMap.remove(previous);
            gatewayToPlayerIDMap.put(next,playerID);
            playerIDToGatewayMap.remove(playerID);
            playerIDToGatewayMap.put(playerID,next);
            this.disconnectedGateways.remove(previous);
            controller.playerRestoredConnection(playerID);
        }
    }

    /**
     * Log message on console
     * @param text message to log
     */
    private void logInfo(String text){
        if (LOGGER.isLoggable(Level.INFO)) { LOGGER.info(text); }
    }
}
