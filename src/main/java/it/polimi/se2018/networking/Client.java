package it.polimi.se2018.networking;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client implements Observer, Observable<ClientInterface> {

    private final List<ClientInterface> gateways = new ArrayList<>();

    public Client() {
        ClientInterface server = new RMIClientGateway("sagradaserver",this);

        register(server);

        System.out.println("Started a Sagrada Client and connected to SagradaServer as guest.");
        System.out.println("Remember that in order to receive message from server you have to send a message with content 'federico'");

        listenForMessagesFromConsole();
    }

    public static void main (String[] args) {
        new Client();
    }

    @Override
    public void notify(String m) throws RemoteException{
        for(ClientInterface o : gateways){
            o.sendMessage(m);
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


    //Just for testing

    private void listenForMessagesFromConsole(){
        //Codice per inviare messaggio da riga di comando
        Scanner scanner = new Scanner(System.in);
        do{
            System.out.print("Inserisci messaggio: ");
            String text = scanner.nextLine();

            try {
                notify(text);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } while(true);
        //Fine codice per inviare messaggio da riga di comando
    }
}
