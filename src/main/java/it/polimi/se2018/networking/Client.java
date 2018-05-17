package it.polimi.se2018.networking;

import it.polimi.se2018.connection.message.Message;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.view.CLIView;
import it.polimi.se2018.view.View;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client implements Observer, SenderInterface, ReceiverInterface {

    private final List<SenderInterface> gateways = new ArrayList<>();

    private final View view;

    @Override
    public void receiveMessage(String message, ReceiverInterface sender) throws RemoteException {
        System.out.println("Received message: "+message);
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        for(SenderInterface o : gateways){
            try{
                o.sendMessage(message);
                System.out.println("Succesfully sent the message to: "+o);
            } catch(ConnectException e){
                //TODO gestire meglio questa eccezione
                System.out.println("Could not send the message due to connection error to: "+o);
            }
        }
    }

    @Override
    public void update(Message m) {
        try {
            sendMessage(m.getMessage());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public enum ConnectionType {RMI,SOCKET};

    private Client(ConnectionType type) {

        //Creates View and connects itself as observer
        this.view = new CLIView();
        this.view.register(this);

        SenderInterface server = null;
        if (type == ConnectionType.RMI) {
            server = new RMIClientGateway("//localhost/sagradaserver", 1098, this);

        } else if (type == ConnectionType.SOCKET) {
            server = new SocketClientGateway("localhost", 1111, this);
        }

        addGateway(server);

        System.out.println("Started a Sagrada Client and connected to SagradaServer as guest.");
        System.out.println("Remember that in order to receive message from server you have to send a message with content 'federico'");

        listenForMessagesFromConsole();
    }

    public static void main (String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("1. RMI\n2. Socket");
        int choice = scanner.nextInt();

        if (choice == 2) {
            new Client(ConnectionType.SOCKET);
        } else if (choice == 1) {
            new Client(ConnectionType.RMI);
        }
    }

    public void addGateway(SenderInterface gateway) {
        gateways.add(gateway);
    }

    public void removeGateway(SenderInterface gateway) {
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
                e.printStackTrace();
            }
        }
        //Fine codice per inviare messaggio da riga di comando
    }
}
