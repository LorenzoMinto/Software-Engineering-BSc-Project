package it.polimi.se2018;

/**
 * Hello world!
 *
 * @author Federico Haag
 */
public class App {
    public static void main( String[] args ){

        /*

        System.out.println( "Hello World!" );

        int numberOfRounds;
        int maxNumberOfPlayers;
        int numberOfDicesPerColor;
        int numberOfToolCards;
        int numberOfPublicObjCards;

        //Loads config parameters
        ConfigImporter configImporter = new ConfigImporter();
        boolean alreadyFailedLoading = false;

        while(true){
            try{
                numberOfRounds          = (Integer) configImporter.getProperty("numberOfRounds");
                maxNumberOfPlayers      = (Integer) configImporter.getProperty("maxNumberOfPlayers");
                numberOfDicesPerColor   = (Integer) configImporter.getProperty("numberOfDicesPerColor");
                numberOfToolCards       = (Integer) configImporter.getProperty("numberOfToolCards");
                numberOfPublicObjCards  = (Integer) configImporter.getProperty("numberOfPublicObjectiveCards");
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
        Controller controller = new Controller(game,numberOfDicesPerColor,numberOfToolCards,numberOfPublicObjCards);

        //TODO: continue implementation here..


        while( true ){
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
