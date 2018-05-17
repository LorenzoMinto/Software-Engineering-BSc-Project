package it.polimi.se2018.networking;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client implements Observer, Observable<ClientInterface> {

    private final List<ClientInterface> gateways = new ArrayList<>();

    public enum NetoworkingCriteria {RMI,SOCKET};

    private Client(NetoworkingCriteria criteria) {

        ClientInterface server = null;

        switch(criteria){

            case RMI:
                server = new RMIClientGateway("//localhost/sagradaserver",1098,this);
                break;
            case SOCKET:
                server = new SocketClientGateway("localhost",1111,this);
                break;
        }

        register(server);

        System.out.println("Started a Sagrada Client and connected to SagradaServer as guest.");
        System.out.println("Remember that in order to receive message from server you have to send a message with content 'federico'");

        listenForMessagesFromConsole();
    }

    public static void main (String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("1. RMI\n2. Socket");
        int choice = scanner.nextInt();

        if (choice == 2) {
            new Client(NetoworkingCriteria.SOCKET);
        } else if (choice == 1) {
            new Client(NetoworkingCriteria.RMI);
        }
    }

    @Override
    public void notify(String m) throws RemoteException{
        for(ClientInterface o : gateways){
            try{
                o.sendMessage(m);
                System.out.println("Succesfully sent the message to: "+o);
            } catch(ConnectException e){
                //TODO gestire meglio questa eccezione
                System.out.println("Could not send the message due to connection error to: "+o);
            }
        }
    }

    @Override
    public void register(ClientInterface gateway) {
        gateways.add(gateway);
    }

    @Override
    public void deregister(ClientInterface gateway) {
        gateways.remove(gateway);
    }

    @Override
    public void update(String m) {
        System.out.println("Received message: "+m);
    }

    @Override
    public void update(String m, ServerInterface sender) throws RemoteException {
        //Client doesn't answer to server's messages
    }


    //Just for testing

    private void listenForMessagesFromConsole(){
        //Codice per inviare messaggio da riga di comando
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.print("Inserisci messaggio: ");
            String text = scanner.nextLine();

            if(text.equals("exit")){ return; }

            try {
                notify(text);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //Fine codice per inviare messaggio da riga di comando
    }
}
