package it.polimi.se2018.networking;

import java.rmi.RemoteException;
import java.util.Scanner;

public class Server implements Observer{

    Observable proxyServer;

    public Server() {
        try {
            this.proxyServer = new RMIServerGateway("sagradaserver",this);
            System.out.println("Started Sagrada Server");

            listenForMessagesFromConsole();

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void main (String[] args) {
        new Server();
    }

    public void update(String m) throws RemoteException {
        System.out.println("Received message: "+m);
    }

    /*This method returns true if the message m contains something that
    enables who sent it to be added to the clients of this server*/
    public boolean canJoin(String m){
        return m.equals("federico"); //TODO:inserire quì il criterio corretto
    }

    //Just for testing

    private void listenForMessagesFromConsole(){
        //Codice per inviare messaggio da riga di comando
        Scanner scanner = new Scanner(System.in);
        do{
            System.out.print("Inserisci messaggio: ");
            String text = scanner.nextLine();

            try {
                proxyServer.notify(text);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } while(true);
        //Fine codice per inviare messaggio da riga di comando
    }
}
