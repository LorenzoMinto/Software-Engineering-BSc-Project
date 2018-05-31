package it.polimi.se2018.networking;

import it.polimi.se2018.utils.message.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Federico Haag
 * @author Jacopo Pio Gargano
 */
public class SocketServerGatherer extends Thread{

    private final ReceiverInterface server;
    private boolean acceptConns = true;
    private int portNumber;

    SocketServerGatherer(Integer portNumber, ReceiverInterface server) {

        this.server = server;
        this.portNumber = portNumber;
    }

    @Override
    public void run() {
        try(ServerSocket socket = new ServerSocket(portNumber)){

            while(this.acceptConns){
                acceptConnection(socket);
            }

        } catch (Exception e){
            ((Server)this.server).fail("Exception thrown accepting socket connections");
        }
    }

    private void acceptConnection(ServerSocket socket) throws IOException{
        Socket clientSocket = socket.accept();

        ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        outputStream.flush();

        //Starts a thread listening for messages
        new Thread(() -> {

            ObjectInputStream in;
            try {
                in = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                return;
            }

            boolean c = true;
            while(c) {
                Message message;
                try {
                    message = (Message) in.readObject();
                    server.receiveMessage(message, new SocketServer(outputStream));
                } catch( Exception e ){
                    e.printStackTrace();
                    ((Server)server).fail("Exception thrown reading socket input stream");
                    c = false;
                }
            }
        }).start();
    }

    public void stopAcceptingConnections(){
        this.acceptConns = false;
    }
}
