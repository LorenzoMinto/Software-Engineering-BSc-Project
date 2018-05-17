package it.polimi.se2018.connection.Socket.serverSocket;

import it.polimi.se2018.connection.ClientImplementationInterface;
import it.polimi.se2018.connection.ServerImplementationInterface;
import it.polimi.se2018.connection.message.Message;
import it.polimi.se2018.connection.message.ViewMessage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketServerImplementation extends Thread implements ServerImplementationInterface{


    private SocketServer server;

    public SocketServerImplementation(SocketServer server){
        this.server = server;
    }


    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean active = true;
        while(active){
            System.out.println("Inserire un messaggio:");
            String text = scanner.nextLine();

            Message message = new ViewMessage(text);

            sendToClient(message, null);
            System.out.println("WEWE");
        }
        scanner.close();
    }

    public void sendToClient(Message message, ClientImplementationInterface client){

        System.out.println("Broadcasting: "+message.getMessage());

        for(SocketServerMessageReceiver messageReceiver: server.getMessageReceivers() ) {
            try{
                Socket socket = messageReceiver.getSocket();
                BufferedWriter br = new BufferedWriter(new OutputStreamWriter((socket.getOutputStream())));
                br.write(message.getMessage() + "\n");
                br.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public void receiveFromClient(Message message) {
        System.out.println("Received: " + message.getMessage());
        sendToClient(message,null);
    }


}
