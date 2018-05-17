package it.polimi.se2018.connection.Socket.serverSocket;


import it.polimi.se2018.connection.message.ViewMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketServerMessageReceiver extends Thread {

    private Socket socket;

    private SocketServerImplementation server;

    public SocketServerMessageReceiver(Socket socket, SocketServerImplementation server) {

        this.socket = socket;
        this.server = server;
    }


    //ascolta i messaggi dal client
    @Override
    public void run () {

        System.out.println("Listening for messages from the Client. ");

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean loop = true;
        while ( loop && !this.socket.isClosed() ) {

            try {

                String message;
                while( (message = br.readLine()) != "stop") {
                    this.server.receiveFromClient(new ViewMessage(message));
                }

                server.receiveFromClient(new ViewMessage("Stopping Connection!"));
                loop = false;
                stopConnection();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }




    public synchronized void stopConnection(){

        if ( !socket.isClosed() ) {
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Connection closed.");
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
