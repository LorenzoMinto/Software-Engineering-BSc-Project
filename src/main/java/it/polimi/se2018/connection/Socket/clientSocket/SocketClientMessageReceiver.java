package it.polimi.se2018.connection.Socket.clientSocket;


import it.polimi.se2018.connection.ClientImplementationInterface;
import it.polimi.se2018.connection.message.ViewMessage;

import java.io.*;
import java.net.Socket;

public class SocketClientMessageReceiver extends Thread {

    private Socket socket;
    private BufferedReader is;

    private ClientImplementationInterface client;

    public SocketClientMessageReceiver(String host, int port, ClientImplementationInterface client) {

        try {
            this.socket =  new Socket( host, port);

            System.out.println("Connected.");

            this.is = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.client = client;

        } catch (IOException e) {
            System.out.println("Connection Error.");
            e.printStackTrace();
        }

    }


    //ascolta i messaggi dal server
    @Override
    public void run () {

        System.out.println("Listening for messages from the Server. ");

        boolean loop = true;
        while ( loop && !this.socket.isClosed() ) {

            try {

                String message = this.is.readLine();

                if ( message == null ) {
                    loop = false;
                    this.stopConnection();

                } else {
                    client.receiveFromServer(new ViewMessage(message));
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    public synchronized void stopConnection () {

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
