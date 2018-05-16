package it.polimi.se2018.connection.Socket.clientSocket;

import it.polimi.se2018.connection.Socket.Message;
import it.polimi.se2018.connection.Socket.serverSocket.ServerInterface;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientSocket {

    private static final int PORT = 1111;
    private static final String HOST = "localhost";

    public static void execute() {


        ServerInterface server = new NetworkHandler(HOST, PORT, new ClientImplementation() );

        // Avvio il loop per far inserire messaggi all'utente

        Scanner scanner = new Scanner(System.in);

        boolean loop = true;
        while ( loop ) {

            System.out.println("\nWrite a message: ");
            String text = scanner.nextLine();

            if ( text.equals("stop") )  {

                scanner.close();
                loop = false;

            } else {

                Message message = new Message(text);
                server.send(message);

            }

        }

        // stops the connection
        NetworkHandler nh = (NetworkHandler) server;
        nh.stopConnection();

    }
}
