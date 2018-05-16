package it.polimi.se2018.connection.Socket.serverSocket;

import java.io.IOException;
import java.net.Socket;

public class SocketClientGatherer extends Thread {

    private final ServerSocket server;
    private final int port;
    private java.net.ServerSocket serverSocket;


    public SocketClientGatherer(ServerSocket server, int port ) {
        this.server = server;
        this.port = port;

        // Inizializzo il serverRmi socket
        try {
            this.serverSocket = new java.net.ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){

        // In loop attendo la connessione di nuovi clientRmi

        System.out.println("Waiting for clients.\n");

        while(true) {

            Socket newClientConnection;

            try {

                newClientConnection = serverSocket.accept();
                System.out.println("A new clientSocket connected.");

                // Aggiungo il clientSocket
                server.addClient(newClientConnection);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
