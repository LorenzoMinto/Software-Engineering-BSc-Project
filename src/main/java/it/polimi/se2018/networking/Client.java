package it.polimi.se2018.networking;

import it.polimi.se2018.networking.message.Message;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.view.CLIView;
import it.polimi.se2018.view.View;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Client implements Observer, SenderInterface, ReceiverInterface {

    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    private static final int MAX_NUMBER_OF_ATTEMPTS = 5;

    private final List<SenderInterface> gateways = new ArrayList<>();

    private final View view;

    public enum ConnectionType {RMI,SOCKET}

    private Client(ConnectionType type) {
        //Creates View and connects itself as observer
        this.view = new CLIView();
        this.view.register(this);

        SenderInterface server = null;
        if (type == ConnectionType.RMI) {

            try {
                server = new RMIClientGateway("//localhost/sagradaserver", 1098, this);
            } catch (RemoteException e) {
                LOGGER.severe("Failed connecting to RMI server.");
                //TODO: say it to view
                return;
            }

        } else if (type == ConnectionType.SOCKET) {
            server = new SocketClientGateway("localhost", 1111, this);
        }

        addGateway(server);

        LOGGER.info("Started a Sagrada Client and connected to Sagrada Server as guest.");
        LOGGER.info("Remember that in order to receive message from server you have to send a message with content 'federico'");

        listenForMessagesFromConsole();
    }

    public static void main (String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("1. RMI\n2. Socket");
        int choice = scanner.nextInt();

        ConnectionType type = ConnectionType.RMI;

        if (choice == 2) { type = ConnectionType.SOCKET; }
        else if (choice == 1) { type = ConnectionType.RMI; }

        new Client(type);
    }

    @Override
    public void receiveMessage(String message, ReceiverInterface sender) throws RemoteException {
        LOGGER.info(()->"Received message: "+message);
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        boolean somethingFailed = false;
        for(SenderInterface o : gateways){
            int attempts = 0;
            boolean correctlySent = false;
            //Send message. Try sometimes if it fails. When maximum number of attempts is reached, go on next gateway
            while(attempts< MAX_NUMBER_OF_ATTEMPTS && !correctlySent){
                attempts++;
                try{
                    o.sendMessage(message);
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

    @Override
    public boolean update(Message m) {
        try {
            sendMessage(m.getMessage());
        } catch (RemoteException e) {
            LOGGER.severe("Exception while sending a message from Client to Server (asked by update call)");
            return false;
        }
        return true;
    }


    private void addGateway(SenderInterface gateway) {
        gateways.add(gateway);
    }

    private void removeGateway(SenderInterface gateway) {
        gateways.remove(gateway);
    }


    //Just for testing

    private void listenForMessagesFromConsole(){
        //Codice per inviare messaggio da riga di comando
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.print("Inserisci messaggio:\n");
            String text = scanner.next();

            if(text.equals("exit")){ return; }

            try {
                sendMessage(text);
            } catch (RemoteException e) {
                LOGGER.severe("Exception while sending a message from the Client console");
            }
        }
        //Fine codice per inviare messaggio da riga di comando
    }

    void fail(String m){
        throw new BadBehaviourRuntimeException(m);
    }
}
