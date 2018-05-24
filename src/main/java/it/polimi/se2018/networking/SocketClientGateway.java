package it.polimi.se2018.networking;

import it.polimi.se2018.utils.message.Message;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;

public class SocketClientGateway extends Thread implements SenderInterface, ReceiverInterface {

    private BufferedWriter out;

    private ReceiverInterface client;
    private String hostName;
    private int portNumber;

    SocketClientGateway(String hostName, int portNumber, ReceiverInterface client) {
        this.client = client;
        this.hostName = hostName;
        this.portNumber = portNumber;

        this.start();
    }

    @Override
    public void sendMessage(Message message) throws RemoteException {
        try {
            //TODO: implementare serializzazione del messaggio
            String socketMessage = new String();

            this.out.write(socketMessage + "\n");
            this.out.flush();
        } catch (IOException e) {
            throw new RemoteException("Failed sending message from SocketClientGateway due to IOException");
        }
    }

    @Override
    public void receiveMessage(Message message, ReceiverInterface sender) throws RemoteException {
        client.receiveMessage(message,sender);
        //Client doesn't answer to server's messages so it is unnecessary sender
    }

    @Override
    public void run() {
        try(Socket echoSocket = new Socket(this.hostName, this.portNumber)){
            this.out = new BufferedWriter(new OutputStreamWriter((echoSocket.getOutputStream())));

            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            String socketMessage;
            while ( (socketMessage = in.readLine()) != null ){

                //TODO: implementare deserializzazione dell'oggetto
                //Message message = ...

                receiveMessage(message,new SocketServer(echoSocket.getOutputStream()));
            }

        } catch(Exception e){
            ((Client)this.client).fail("Exception thrown opening socket or reading from stream");
        }
    }
}
