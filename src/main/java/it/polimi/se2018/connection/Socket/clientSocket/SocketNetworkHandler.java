package it.polimi.se2018.connection.Socket.clientSocket;

import it.polimi.se2018.connection.RemoteClientInterface;
import it.polimi.se2018.connection.Message;
import it.polimi.se2018.connection.ServerInterface;
import it.polimi.se2018.connection.ViewMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketNetworkHandler extends Thread implements ServerInterface {

    private Socket socket;
    private BufferedReader is;

    private RemoteClientInterface client;

    public SocketNetworkHandler(String host, int port, RemoteClientInterface client) {

        try {
            this.socket =  new Socket( host, port);

            System.out.println("Connected.");

            this.is = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.client = client;

            this.start();

        } catch (IOException e) {
            System.out.println("Connection Error.");
            e.printStackTrace();
        }

    }

    @Override
    public void run () {

        System.out.println("Listening for messages from the ServerRMI. ");

        boolean loop = true;
        while ( loop && !this.socket.isClosed() ) {

            try {

                String message = this.is.readLine();

                if ( message == null ) {
                    loop = false;
                    this.stopConnection();

                } else {
                    client.send(new ViewMessage(message));
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public synchronized void send ( Message message ) {

        try {

            OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());

            // qui passiamo la stringa, ma potete passare anche l'oggetto Message
            // serializzato e deserializzarlo lato ServerRMI ( online troverete come fare )

            writer.write(message.getMessage());
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
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
}
