package it.polimi.se2018.connection;

import it.polimi.se2018.connection.Socket.clientSocket.SocketClient;
import it.polimi.se2018.connection.rmi.clientRmi.RMIClient;

import java.rmi.RemoteException;
import java.util.Scanner;

public class Client{

    //initial view

    //ask player which kind of connection they want to establish

    //instantiate clientRMI or instantiate clientRMI

    private ClientInterface realClient;

    public Client() throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. RMI\n2. Socket");
        int choice = scanner.nextInt();

        if (choice == 2) {
            realClient = new SocketClient();
        } else if (choice == 1) {
            realClient = new RMIClient();
        }

        realClient.start();

    }

    public static void main(String[] args) throws RemoteException {
        new Client();
    }


}
