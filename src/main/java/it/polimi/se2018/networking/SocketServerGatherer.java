package it.polimi.se2018.networking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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

        SocketServer socketServer = new SocketServer(clientSocket.getOutputStream());

        //Starts a thread listening for messages
        new Thread(new Runnable() {
            String socketMessage;
            @Override
            public void run() {
                try(BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))){
                    while ( (socketMessage = in.readLine()) != null ){
                        //TODO: implementare deserializzazione dell'oggetto
                        server.receiveMessage(message,socketServer);
                    }
                } catch(IOException e){
                    ((Server)server).fail("IOException thrown reading socket input stream");
                }
            }
        }).start();
    }

    public void stopAcceptingConnections(){
        this.acceptConns = false;
    }
}
