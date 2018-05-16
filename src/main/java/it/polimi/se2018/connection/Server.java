package it.polimi.se2018.connection;


import it.polimi.se2018.connection.Socket.serverSocket.ServerSocket;
import it.polimi.se2018.connection.rmi.serverRmi.ServerRMI;
import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.model.Game;

public class Server {

    private static ServerRMI serverRMI;
    private static ServerSocket serverSocket;
    private static Controller controller;


    public static void main(String[] args){
        serverRMI = new ServerRMI();
        serverRMI.execute();

        serverSocket = new ServerSocket();


        controller = new Controller(new Game(10,4),
                18, 10, 10 );
    }

}
