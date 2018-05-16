package it.polimi.se2018.connection.Socket.serverSocket;

import it.polimi.se2018.connection.RemoteClientInterface;
import it.polimi.se2018.connection.Message;
import it.polimi.se2018.connection.ViewMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class VirtualClient extends Thread implements RemoteClientInterface {

    private final ServerSocket server;

    private Socket clientConnection;

    public VirtualClient(ServerSocket server, Socket clientConnection) {

        this.server = server;
        this.clientConnection = clientConnection;
    }

    @Override
    public void run(){

        try {

            BufferedReader is = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));

            boolean loop = true;

            while(loop) {

                System.out.println("Waiting for messages.");

                String message = is.readLine();
                if ( message == null ) {
                    loop = false;
                } else {
                    System.out.println(message);
                    server.getImplementation().send(new ViewMessage(message));
                }

            }

            clientConnection.close();
            server.removeClient(this);

            System.out.println("Connection closed.");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void send(Message message) {

            OutputStreamWriter writer;

            try {
                writer = new OutputStreamWriter(clientConnection.getOutputStream());
                writer.write(message.getMessage());
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }


    }
}