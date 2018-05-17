package it.polimi.se2018.connection.Socket.clientSocket;


import it.polimi.se2018.connection.ClientImplementationInterface;
import it.polimi.se2018.connection.message.Message;
import it.polimi.se2018.connection.message.ViewMessage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClientImplementation extends Thread implements ClientImplementationInterface {

    private Socket socket;

    @Override
    public void run(){
        Scanner scanner = new Scanner(System.in);
        boolean active = true;
        while(active){
            System.out.println("Inserire un messaggio:");
            String text = scanner.nextLine();

            Message message = new ViewMessage(text);

            sendToServer(message);
        }
        scanner.close();
    }

    public void sendToServer(Message message){
        try {

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            bw.write(message.getMessage() + "\n");
            bw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void receiveFromServer(Message message) {
        System.out.println("Received: " + message.getMessage());
    }
}
