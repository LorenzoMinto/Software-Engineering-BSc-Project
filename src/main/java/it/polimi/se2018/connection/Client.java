package it.polimi.se2018.connection;

import it.polimi.se2018.connection.Socket.clientSocket.SocketClient;
import it.polimi.se2018.connection.rmi.clientRmi.RMIClient;

import java.rmi.RemoteException;
import java.util.Scanner;

public class Client implements RemoteClientInterface{

    //initial view

    //ask player which kind of connection they want to establish

    //instantiate clientRMI or instantiate clientRMI

    private LocalClientInterface realClient;

    public Client() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. RMI\n2. Socket");
        int choice = scanner.nextInt();

        if (choice == 2){
            realClient = new SocketClient(this);
        }else if(choice == 1) {
            realClient = new RMIClient(this);
        }

        realClient.start();

        boolean active = true;
        while(active){
            System.out.println("Inserire un messaggio:");
            String text = scanner.nextLine();

            Message message = new ViewMessage(text);


            try {
                send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        scanner.close();
    }

    public static void main(String[] args) {
        new Client();
    }

    public void send(Message message) throws RemoteException{
        this.realClient.send(message);
    }

    public void receive(Message message){

    }

}
