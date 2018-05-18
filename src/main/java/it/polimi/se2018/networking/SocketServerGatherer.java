package it.polimi.se2018.networking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerGatherer extends Thread{

    private final ReceiverInterface server;
    private boolean acceptConns = true;
    private int portNumber;

    public SocketServerGatherer(Integer portNumber, ReceiverInterface server) {

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
            //TODO: gestire eccezione
        }
    }

    private void acceptConnection(ServerSocket socket) throws IOException{
        Socket clientSocket = socket.accept();

        //TODO: understand why never used but created
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        SocketServer socketServer = new SocketServer(clientSocket.getOutputStream());

        //Starts a thread listening for messages
        new Thread(new Runnable() {
            String message;
            @Override
            public void run() {
                try(BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))){
                    while ( (message = in.readLine()) != null ){
                        server.receiveMessage(message,socketServer);
                    }
                } catch(IOException e){
                    //TODO inserire eccezione
                }
            }
        }).start();
    }

    public void stopAcceptingConnections(){
        this.acceptConns = false;
    }
}
