package it.polimi.se2018.connection;


import it.polimi.se2018.connection.Socket.serverSocket.ServerSocket;
import it.polimi.se2018.connection.rmi.serverRmi.ServerRMI;
import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.ConfigImporter;
import it.polimi.se2018.utils.NoConfigParamFoundException;

public class Server {

    private final Controller controller;

    public Server() {
        int numberOfRounds;
        int maxNumberOfPlayers;
        int numberOfDicesPerColor;
        int numberOfToolCards;
        int numberOfPublicObjCards;

        //Loads config parameters
        ConfigImporter configImporter = new ConfigImporter("default");
        boolean alreadyFailedLoading = false;

        while(true){
            try{
                numberOfRounds          = configImporter.getProperty("numberOfRounds");
                maxNumberOfPlayers      = configImporter.getProperty("maxNumberOfPlayers");
                numberOfDicesPerColor   = configImporter.getProperty("numberOfDicesPerColor");
                numberOfToolCards       = configImporter.getProperty("numberOfToolCards");
                numberOfPublicObjCards  = configImporter.getProperty("numberOfPublicObjectiveCards");
                break;
            } catch(NoConfigParamFoundException e) {
                if(alreadyFailedLoading) { throw new BadBehaviourRuntimeException("Can't load default config file"); }
                else {
                    alreadyFailedLoading = true;

                    //loads the default config file
                    configImporter = new ConfigImporter();
                }
            }
        }

        //Creates the game
        Game game = new Game(numberOfRounds,maxNumberOfPlayers);
        this.controller = new Controller(game,numberOfDicesPerColor,numberOfToolCards,numberOfPublicObjCards);

        //Starts client gatherers
        new ServerRMI();
        new ServerSocket();
    }

    public static void main(String[] args){
        new Server();
    }

}
