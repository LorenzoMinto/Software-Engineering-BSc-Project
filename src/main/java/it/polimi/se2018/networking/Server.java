package it.polimi.se2018.networking;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.networking.message.Message;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.ConfigImporter;
import it.polimi.se2018.utils.NoConfigParamFoundException;
import it.polimi.se2018.utils.Observer;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server implements Observer, ReceiverInterface, SenderInterface{

    private ReceiverInterface proxyServer;

    private final List<ReceiverInterface> gateways = new ArrayList<>();

    private final Controller controller;

    public static void main (String[] args) {
        new Server();
    }

    private Server() {
        setupNetworking();

        this.controller = createController();
        this.controller.register(this);

        listenForCommandsFromConsole();
    }

    private void setupNetworking() {
        try {
            System.out.println("Starting RMI...");
            this.proxyServer = new RMIServerGateway("sagradaserver",this);

            System.out.println("Starting Socket...");
            new SocketServerGateway(1111,this);

            System.out.println("Sagrada Server is up.");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private Controller createController(){
        int numberOfRounds;
        int maxNumberOfPlayers;
        int numberOfDicesPerColor;
        int numberOfToolCards;
        int numberOfPublicObjCards;

        //Loads config parameters
        ConfigImporter configImporter = new ConfigImporter("default");
        boolean alreadyFailedLoading = false;

        while(true){
            try{
                numberOfRounds          = configImporter.getProperty("numberOfRounds");
                maxNumberOfPlayers      = configImporter.getProperty("maxNumberOfPlayers");
                numberOfDicesPerColor   = configImporter.getProperty("numberOfDicesPerColor");
                numberOfToolCards       = configImporter.getProperty("numberOfToolCards");
                numberOfPublicObjCards  = configImporter.getProperty("numberOfPublicObjectiveCards");
                break;
            } catch(NoConfigParamFoundException e) {
                if(alreadyFailedLoading) { throw new BadBehaviourRuntimeException("Can't load default config file"); }
                else {
                    alreadyFailedLoading = true;

                    //loads the default config file
                    configImporter = new ConfigImporter();
                }
            }
        }

        //Creates the game
        Game game = new Game(numberOfRounds,maxNumberOfPlayers);
        game.register(this);
        return new Controller(game,numberOfDicesPerColor,numberOfToolCards,numberOfPublicObjCards);
    }

    public void addGateway(ReceiverInterface gateway) {
        gateways.add(gateway);
    }

    public void removeGateway(ReceiverInterface gateway) {
        gateways.remove(gateway);
    }

    @Override
    public void receiveMessage(String message, ReceiverInterface sender) throws RemoteException {

        System.out.println("Received message: "+message);

        if(isJoinRequest(message)){

            if( canJoin(message) ){

                addGateway(sender);

                String replyMessage = "Welcome to SagradaServer. You are now an authorized client.";
                sender.receiveMessage(replyMessage,this.proxyServer);

                System.out.println("A new client has been authorized");

            } else {

                String replyMessage = "You can't join this server";
                sender.receiveMessage(replyMessage,this.proxyServer);

                System.out.println("A new client asked to join but refused");

            }

        }
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        for(ReceiverInterface c : gateways){
            try{
                c.receiveMessage(message,this.proxyServer);
                System.out.println("Message sent to "+c);

            } catch (ConnectException e){
                //TODO: gestire meglio questa eccezione
                System.out.println("The message was not sent to "+c+" due to connection error");
            }
        }
    }

    /*This method returns true if the message m contains something that
    enables who sent it to be added to the clients of this server*/
    private boolean canJoin(String m){
        return m.equals("federico");
    }

    //True if the message is a join request
    private boolean isJoinRequest(String m){
        return m.equals("federico"); //TODO:inserire quì il criterio corretto
    }




    //Just for testing

    private void listenForCommandsFromConsole(){
        //Codice per inviare messaggio da riga di comando
        System.out.println("Start listening for messages...");

        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.print("Inserisci messaggio: ");
            String text = scanner.nextLine();

            //Just for testing comunication
            try {
                sendMessage(text);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            //TODO: implementa quì metodi di servizio per debug

            if(text.equals("exit")){ break; }
        }
        //Fine codice per inviare messaggio da riga di comando
    }

    @Override
    public void update(Message m) {
        try {
            sendMessage(m.getMessage());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
