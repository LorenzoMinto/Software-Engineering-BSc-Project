package it.polimi.se2018.connection;

import it.polimi.se2018.connection.Socket.clientSocket.ClientSocket;
import it.polimi.se2018.connection.rmi.clientRmi.ClientRMI;

import java.util.Scanner;

public class Client {

    //initial view

    //ask player which kind of connection they want to establish

    //instantiate clientRMI or instantiate clientRMI

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. RMI\n2. Socket");
        int choice = scanner.nextInt();

        if (choice == 2){
            ClientSocket clientSocket = new ClientSocket();
            clientSocket.execute();
        }else if(choice == 1) {
            ClientRMI clientRMI = new ClientRMI();
            clientRMI.execute();
        }
    }



}
