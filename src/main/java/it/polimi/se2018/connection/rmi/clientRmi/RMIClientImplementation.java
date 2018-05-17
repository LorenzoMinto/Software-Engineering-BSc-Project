package it.polimi.se2018.connection.rmi.clientRmi;

import it.polimi.se2018.connection.message.Message;
import it.polimi.se2018.connection.message.ViewMessage;
import it.polimi.se2018.connection.rmi.serverRmi.RMIServerInterface;

import java.rmi.RemoteException;
import java.util.Scanner;

public class RMIClientImplementation extends Thread implements RMIClientInterface{

    private RMIServerInterface remoteServer;

    public RMIClientImplementation(RMIServerInterface remoteServer) {
        this.remoteServer = remoteServer;
    }

    @Override
    public void run(){
        Scanner scanner = new Scanner(System.in);
        boolean active = true;
        while(active){
            System.out.println("Inserire un messaggio:");
            String text = scanner.nextLine();

            Message message = new ViewMessage(text);


            sendToServer(message);
        }
        scanner.close();
    }


    @Override
    public void sendToServer(Message message) {
        try {
            this.remoteServer.receiveFromClient(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void receiveFromServer(Message message) {
        System.out.println("Ho ricevuto il messaggio: " + message.getMessage());
	}

}
