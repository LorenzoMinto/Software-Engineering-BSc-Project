package it.polimi.se2018;

import it.polimi.se2018.controller.AcceptingPlayerException;
import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.model.*;
import it.polimi.se2018.view.CLIView;
import it.polimi.se2018.view.View;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        /*
        //TODO: load from config file
        int numberOfRounds = 10;
        int maxNumberOfPlayers = 4;
        int numberOfDicesPerColor = 18;

        Game game = new Game(numberOfRounds,maxNumberOfPlayers);
        Controller controller = new Controller(game,numberOfDicesPerColor);

        while( true ){ //TODO: wait for max number of players connecting or timeout
            int userID = 0;
            String username = "";
            String nickname = "";

            User user = new User(userID,username);

            try{
                Player player = controller.acceptPlayer(user,nickname);
                View view = new CLIView(controller,player);
            } catch (AcceptingPlayerException e){
                e.printStackTrace();
            }

            return; //here just to prevent infinite loop. it is not logically-correct. delete after implementing correctly while(true)
        }
        */

    }
}
