package it.polimi.se2018.networking;

import it.polimi.se2018.utils.message.Message;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;

/**
 *
 * @author Federico Haag
 */
public class SocketClientGateway extends Thread implements SenderInterface, ReceiverInterface {

    private ObjectOutputStream out;

    private ReceiverInterface client;
    private String hostName;
    private int portNumber;

    private volatile boolean running = false;

    SocketClientGateway(String hostName, int portNumber, ReceiverInterface client) {
        this.client = client;
        this.hostName = hostName;
        this.portNumber = portNumber;

        this.start();
    }

    @Override
    public void sendMessage(Message message) throws RemoteException {

        while(!running){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        try {
            this.out.reset();
            this.out.writeObject(message);
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
            this.out = new ObjectOutputStream(echoSocket.getOutputStream());
            this.out.flush();

            ObjectInputStream in = null;
            in = new ObjectInputStream(echoSocket.getInputStream());
            this.running = true;
            while(true){
                receiveMessage((Message) in.readObject(),null);
            }

        } catch(Exception e){
            e.printStackTrace();
            ((Client)this.client).fail("Exception thrown opening socket or reading from stream");
        }
    }
}
