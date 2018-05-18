package it.polimi.se2018.networking;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;

public class SocketClientGateway extends Thread implements SenderInterface, ReceiverInterface {

    private BufferedWriter out;

    private ReceiverInterface client;
    private String hostName;
    private int portNumber;

    public SocketClientGateway(String hostName, int portNumber, ReceiverInterface client) {
        this.client = client;
        this.hostName = hostName;
        this.portNumber = portNumber;

        this.start();
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        try {
            this.out.write(message + "\n");
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveMessage(String message, ReceiverInterface sender) throws RemoteException {
        client.receiveMessage(message,sender);
        //Client doesn't answer to server's messages so it is unnecessary sender
    }

    @Override
    public void run() {
        try(Socket echoSocket = new Socket(this.hostName, this.portNumber)){
            this.out = new BufferedWriter(new OutputStreamWriter((echoSocket.getOutputStream())));

            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            String message;
            while ( (message = in.readLine()) != null ){
                receiveMessage(message,new SocketServer(echoSocket.getOutputStream()));
            }

        } catch(Exception e){
            e.printStackTrace();
            //TODO: creare networking exception da throware fino a Server
        }
    }
}
