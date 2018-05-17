package it.polimi.se2018.networking;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server implements Observer, Observable<ServerInterface>{

    private ServerInterface proxyServer;

    private final transient List<ServerInterface> gateways = new ArrayList<>();

    private Server() {
        try {
            System.out.println("Starting RMI...");
            this.proxyServer = new RMIServerGateway("sagradaserver",this);

            System.out.println("Starting Socket...");
            new SocketServerGateway(1111,this);

            System.out.println("Sagrada Server is up.");

            System.out.println("Start listening for messages...");
            listenForMessagesFromConsole();

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void main (String[] args) {
        new Server();
    }

    @Override
    public void notify(String message) throws RemoteException{
        for(ServerInterface c : gateways){
            try{
                c.receiveMessage(message,this.proxyServer);
                System.out.println("Message sent to "+c);

            } catch (ConnectException e){
                //TODO: gestire meglio questa eccezione
                System.out.println("The message was not sent to "+c+" due to connection error");
            }
        }
    }

    @Override
    public void register(ServerInterface gateway) {
        gateways.add(gateway);
    }

    @Override
    public void deregister(ServerInterface gateway) {
        gateways.remove(gateway);
    }


    @Override
    public void update(String message) throws RemoteException {

        System.out.println("Received message: "+message);
    }

    @Override
    public void update(String message,ServerInterface sender) throws RemoteException {

        update(message);

        if(isJoinRequest(message)){

            if( canJoin(message) ){

                register(sender);

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

    /*This method returns true if the message m contains something that
    enables who sent it to be added to the clients of this server*/
    public boolean canJoin(String m){
        return m.equals("federico");
    }

    //True if the message is a join request
    public boolean isJoinRequest(String m){
        return m.equals("federico"); //TODO:inserire quì il criterio corretto
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
