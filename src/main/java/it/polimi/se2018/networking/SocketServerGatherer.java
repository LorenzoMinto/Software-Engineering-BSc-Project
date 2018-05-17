package it.polimi.se2018.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerGatherer extends Thread{

    private final Server server;
    private boolean acceptConns = true;
    private int portNumber;

    public SocketServerGatherer(Integer portNumber, Server server) {

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

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        SocketServer socketServer = new SocketServer(clientSocket.getOutputStream());

        //Starts a thread listening for messages
        new Thread(new Runnable() {
            String message;
            @Override
            public void run() {
                try(BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))){
                    while ( (message = in.readLine()) != null ){
                        server.update(message,socketServer);
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
