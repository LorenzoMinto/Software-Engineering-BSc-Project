package it.polimi.se2018.view;

import it.polimi.se2018.controller.RankingRecord;
import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.PublicObjectiveCard;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.networking.ConnectionType;
import it.polimi.se2018.networking.NetworkingException;
import it.polimi.se2018.utils.ControllerBoundMessageType;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.Message;

import java.util.*;
import java.util.function.Consumer;

/**
 * Class for CLI implementation of View.
 *
 * @author Federico Haag
 */
public class CLIView extends View{

    /**
     * Scanner to read from console
     */
    private static final Scanner SCANNER = new Scanner(System.in);


    /*  CONSTANTS FOR CONSOLE MESSAGES
        Following constants are not commented one by one because they are as self explaining as needed.
        Major information can be found looking for their usage.
        Being private, they are used only in this file. So if a change is needed, just look for usages in this file.
    */
    private static final String INPUT_NOT_VALID = "Input not valid.";
    private static final String EXIT_FROM_READING_LOOP = "exit";
    private static final String CHOOSE_CONNECTION_TYPE = "Choose the type of connection technology you want to use:";
    private static final String INSERT_NAME_SERVER = "Insert name server";
    private static final String INSERT_PORT_NUMBER = "Insert port number";
    private static final String SHOW_MY_WINDOW_PATTERN = "Show my window pattern";
    private static final String SHOW_MY_PRIVATE_OBJECTIVE_CARD = "Show my private objective card";
    private static final String SHOW_PUBLIC_OBJECTIVE_CARDS = "Show public objective cards";
    private static final String SHOW_WINDOW_PATTERNS_OF_OTHER_PLAYERS = "Show window patterns of other players";
    private static final String ASK_FOR_MOVE = "Which move would you like to perform?";
    private static final String REMEMBER_DRAFTED_DICE = "Current drafted dice: ";
    private static final String WINDOW_PATTERN_OF = "Window pattern of ";
    private static final String PLAYERS_HAVE_NOT_ALREADY_RECEIVED_WINDOW_PATTERNS = "Players have not already received window patterns.";
    private static final String THERE_ARE_NO_PUBLIC_OBJECTIVE_CARDS_YET = "There are no public objective cards yet.";
    private static final String YOU_DO_NOT_HAVE_A_PRIVATE_OBJECTIVE_CARD_YET = "You do not have a private objective card yet.";
    private static final String YOU_DO_NOT_HAVE_A_WINDOW_PATTERN_YET = "You do not have a window pattern yet.";
    private static final String THESE_ARE_DICES_OF_DRAFTPOOL = "These are dices of draftpool:";
    private static final String INSERT_THE_INDEX_OF_THE_DICE_YOU_WANT_TO_DRAFT = "Insert the index of the dice you want to draft:";
    private static final String INSERT_THE_ROW_NUMBER_OF_THE_WINDOW_PATTERN = "Insert the row number of the window pattern";
    private static final String INSERT_THE_COL_NUMBER_OF_THE_WINDOW_PATTERN = "Insert the col number of the window pattern";
    private static final String INSERT_THE_VALUE_YOU_WANT_TO_ASSIGN_TO_THE_DRAFTED_DICE = "Insert the value you want to assign to the drafted dice";
    private static final String FOLLOWING_THE_DICES_IN_THE_TRACK = "Following the dices in the track";
    private static final String TRACK_SLOT_NUMBER = "TRACK SLOT #";
    private static final String INSERT_THE_NUMBER_OF_SLOT_YOU_WANT_TO_DRAFT_THE_DICE_FROM = "Insert the number of slot you want to draft the dice from";
    private static final String INSERT_THE_INDEX_OF_THE_DICE_YOU_WANT_TO_PICK = "Insert the index of the dice you want to pick:";
    private static final String INSERT_THE_ROW_NUMBER_OF_THE_WINDOW_PATTERN_ORIGIN = "Insert the row number of the window pattern (origin)";
    private static final String INSERT_THE_COL_NUMBER_OF_THE_WINDOW_PATTERN_ORIGIN = "Insert the col number of the window pattern (origin)";
    private static final String INSERT_THE_ROW_NUMBER_OF_THE_WINDOW_PATTERN_DESTINATION = "Insert the row number of the window pattern (destination)";
    private static final String INSERT_THE_COL_NUMBER_OF_THE_WINDOW_PATTERN_DESTINATION = "Insert the col number of the window pattern (destination)";
    private static final String INSERT_YOUR_NICKNAME = "Insert your nickname";
    private static final String CHOOSE_A_WINDOW_PATTERN_FROM_THE_FOLLOWINGS = "Choose a window pattern from the followings (tip: look at your private objective card):";
    private static final String INSERT_THE_INDEX_OF_THE_WINDOW_PATTERN_YOU_WANT_TO_CHOOSE = "Insert the index of the window pattern you want to choose:";
    private static final String ERROR_MESSAGE = "ERROR: ";
    private static final String YOU_CANT_WRITE_ON_CONSOLE_NOW = "You can't write on console now";
    private static final String FAILED_ESTABLISHING_CONNECTION = "Failed establishing connection";
    private static final String SHOW_TOOLCARDS = "Show toolcards";
    private static final String GET_FAVOUR_TOKENS_OF_PLAYERS = "Get favour tokens of players";
    private static final String THE_WINNER_IS = "The winner is: ";
    private static final String GLOBAL_RANKINGS_FOLLOWING = "Global rankings following:";
    private static final String YOU_ARE_QUITTING = "You are quitting...";
    private static final String QUIT_GAME = "Quit game";


    /*  CONSTANTS FOR MESSAGES PARAMS
        Following constants are not commented one by one because they are as self explaining as needed.
        Major information can be found looking for their usage.
        Being private, they are used only in this file. So if a change is needed, just look for usages in this file.
     */
    //Note: this strings are strictly connected with the ones used in Controller and Model. DO NOT CHANGE!
    private static final String PARAM_NICKNAME = "nickname";
    private static final String PARAM_WINDOW_PATTERN = "windowPattern";
    private static final String PARAM_ROW_FROM = "rowFrom";
    private static final String PARAM_COL_FROM = "colFrom";
    private static final String PARAM_ROW_TO = "rowTo";
    private static final String PARAM_COL_TO = "colTo";
    private static final String PARAM_SLOT_NUMBER = "slotNumber";
    private static final String PARAM_VALUE = "value";
    private static final String PARAM_ROW = "row";
    private static final String PARAM_COL = "col";
    private static final String PARAM_TOOL_CARD = "toolCard";
    private static final String PARAM_DICE = "dice";
    private static final String PARAM_MOVE = "move";


    // AUXILIARY CLASSES

    /**
     * A Console Move is a class representing an action that player can perform trough CLI.
     * It contains the description of the action (e.g. draft a dice).
     * Each console move performs an action stored in the runnable "action".
     * The class is immutable: description and action can obly be set through constructor.
     */
    private class ConsoleMove {
        /**
         * Description of the move
         */
        private String description;

        /**
         * Action to perform if the move is done
         */
        private Runnable action;

        /**
         * Constructor for a new ConsoleMove
         * @param description description of the move
         * @param action action to perform if the move is done
         */
        private ConsoleMove(String description, Runnable action) {
            this.description = description;
            this.action = action;
        }

        /**
         * Returns the description of the move.
         * @return the description of the move
         */
        public String getDescription() {
            return description;
        }

        /**
         * Execute the built-in action
         */
        void run(){
            this.action.run();
        }
    }


    // CLI PARAMS

    /**
     * Stores the action to be performed with the next console input that will be available.
     * It's aim is to enable CLI to change "consumer" on the fly without waiting for an actual input
     * as would be if scanner.nextLine() was used directly.
     */
    private Consumer<String> currentInputConsumer;

    /**
     * True if game is started. False if waiting for players.
     */
    private boolean gameStarted = false;

    /**
     * True if game is ended
     */
    private boolean gameEnded = false;

    /**
     * Main method to make runnable the class.
     * A new instance of CLIView is created.
     * @param args default param for main. no args are processed.
     */
    public static void main(String[] args) {
        new CLIView();
    }

    /**
     * Constructor of a new CLI
     */
    private CLIView() {
        super();

        cleanConsole();

        //Launch of CLI
        launchConsoleReader();

        //Connection to server
        connect();
    }


    // CLI HANDLING

    /**
     * Set currentInputConsumer to the consumer given as argument.
     * Actually it is a way for changing the handling of the next available input.
     * @param consumer the new consumer to be used for input processing
     */
    private void waitForConsoleInput(Consumer<String> consumer){
        this.currentInputConsumer = consumer;
    }

    /**
     * Launches a new thread that reads for user input on system.in.
     * When a new input is received, it is given to the currentInputConsumer.
     */
    private void launchConsoleReader(){
        new Thread(()->{
            String text;
            do{
                text = SCANNER.nextLine();

                if(currentInputConsumer !=null){
                    currentInputConsumer.accept(text);
                } else {
                    print(YOU_CANT_WRITE_ON_CONSOLE_NOW);
                }
            } while(!text.equals(EXIT_FROM_READING_LOOP));
        }).start();
    }

    /**
     * Connects client to server (asks user way of communication and data - address, port)
     */
    private void connect(){

        print(CHOOSE_CONNECTION_TYPE);
        int i = 1;
        for(ConnectionType cT : ConnectionType.values()){
            print(i+". "+cT);
            i++;
        }

        waitForConsoleInput(connectionTypeString -> {
            int connectionTypeInt;
            try {
                connectionTypeInt = Integer.parseInt(connectionTypeString) - 1;
            } catch(NumberFormatException e){
                cleanConsole();
                print(INPUT_NOT_VALID);
                connect();
                return;
            }
            if( connectionTypeInt >= ConnectionType.values().length){
                connect();
                return;
            }
            ConnectionType connectionType = ConnectionType.values()[connectionTypeInt];

            print(INSERT_NAME_SERVER);

            waitForConsoleInput(serverName -> {
                print(INSERT_PORT_NUMBER);
                waitForConsoleInput(portNumberString -> {
                    int portNumber;
                    try {
                        portNumber = Integer.parseInt(portNumberString);
                    } catch (NumberFormatException e){
                        cleanConsole();
                        print(INPUT_NOT_VALID);
                        connect();
                        return;
                    }

                    try {
                        connectToRemoteServer(connectionType,serverName,portNumber);
                    } catch (NetworkingException e) {
                        cleanConsole();
                        print(FAILED_ESTABLISHING_CONNECTION);
                        connect();
                        return;
                    }

                    waitForMove();
                });
            });
        });
    }

    /**
     * Creates a new handler for console input creating a map that contains all the available moves
     * and inserting it into a new instance of {@link CLIView#currentInputConsumer}.
     * This enables to generate dinamically indexes of choices (1. xxx 2. xxx).
     * This function actually doesn't hardcode any move option except for the standard ones:
     *      SHOW_MY_WINDOW_PATTERN
     *      SHOW_MY_PRIVATE_OBJECTIVE_CARD
     *      SHOW_PUBLIC_OBJECTIVE_CARDS
     *      SHOW_WINDOW_PATTERNS_OF_OTHER_PLAYERS
     */
    private void waitForMove(){
        if(this.gameEnded){
            return;
        }

        //Create a LinkedHashMap to map string choices from console to moves
        LinkedHashMap<String,ConsoleMove> mapConsoleMoves = new LinkedHashMap<>();
        int index = 1;
        for(Move move : this.getPermissions()){
            mapConsoleMoves.put(Integer.toString(index), convertMoveToConsoleMove(move));
            index++;
        }

        if(gameStarted){
            mapConsoleMoves.put(Integer.toString(index), new ConsoleMove(SHOW_MY_WINDOW_PATTERN,this::printWindowPattern));
            index++;
            mapConsoleMoves.put(Integer.toString(index), new ConsoleMove(SHOW_MY_PRIVATE_OBJECTIVE_CARD,this::printPrivateObjectiveCard));
            index++;
            mapConsoleMoves.put(Integer.toString(index), new ConsoleMove(SHOW_TOOLCARDS,this::handleShowToolCards));
            index++;
            mapConsoleMoves.put(Integer.toString(index), new ConsoleMove(SHOW_PUBLIC_OBJECTIVE_CARDS,this::printPublicObjectiveCards));
            index++;
            mapConsoleMoves.put(Integer.toString(index), new ConsoleMove(SHOW_WINDOW_PATTERNS_OF_OTHER_PLAYERS,this::printOthersWindowPatterns));
            index++;
            mapConsoleMoves.put(Integer.toString(index), new ConsoleMove(GET_FAVOUR_TOKENS_OF_PLAYERS,this::printFavourTokens));
            index++;
            mapConsoleMoves.put(Integer.toString(index), new ConsoleMove(QUIT_GAME,this::handleQuitMove));
        }

        if(this.draftedDice!=null){
            print(REMEMBER_DRAFTED_DICE +this.draftedDice);
        }

        print(ASK_FOR_MOVE);

        //Print console moves
        for (Map.Entry<String, ConsoleMove> entry : mapConsoleMoves.entrySet()) {
            print(entry.getKey()+". "+entry.getValue().getDescription());
        }

        //Reads from console
        waitForConsoleInput(s -> {
            if(mapConsoleMoves.containsKey(s)){
                mapConsoleMoves.get(s).run();
            } else {
                cleanConsole();
                print(INPUT_NOT_VALID);
                waitForMove();
            }
        });
    }

    /**
     * Map every Move to the respectful ConsoleMove.
     * @param move the move to map to console move
     * @return the move converted to respectful consolemove.
     */
    private ConsoleMove convertMoveToConsoleMove(Move move){
        ConsoleMove consoleMove = null;
        switch (move) {
            case END_TURN:
                consoleMove = new ConsoleMove(move.getTextualREP(),this::handleEndTurnMove);
                break;
            case DRAFT_DICE_FROM_DRAFTPOOL:
                consoleMove = new ConsoleMove(move.getTextualREP(),this::handleDraftDiceFromDraftPoolMove);
                break;
            case PLACE_DICE_ON_WINDOWPATTERN:
                consoleMove = new ConsoleMove(move.getTextualREP(),this::handlePlaceDiceOnWindowPatternMove);
                break;
            case USE_TOOLCARD:
                consoleMove = new ConsoleMove(move.getTextualREP(),this::handleUseToolCardMove);
                break;
            case INCREMENT_DRAFTED_DICE:
                consoleMove = new ConsoleMove(move.getTextualREP(),this::handleIncrementDraftedDiceMove);
                break;
            case DECREMENT_DRAFTED_DICE:
                consoleMove = new ConsoleMove(move.getTextualREP(),this::handleDecrementDraftedDiceMove);
                break;
            case CHANGE_DRAFTED_DICE_VALUE:
                consoleMove = new ConsoleMove(move.getTextualREP(),this::handleChangeDraftedDiceValueMove);
                break;
            case CHOOSE_DICE_FROM_TRACK:
                consoleMove = new ConsoleMove(move.getTextualREP(),this::handleChooseDiceFromTrackMove);
                break;
            case RETURN_DICE_TO_DRAFTPOOL:
                consoleMove = new ConsoleMove(move.getTextualREP(),this::handleReturnDiceToDraftpoolMove);
                break;
            case MOVE_DICE:
                consoleMove = new ConsoleMove(move.getTextualREP(),this::handleMoveDiceMove);
                break;
            case END_EFFECT:
                consoleMove = new ConsoleMove(move.getTextualREP(),this::handleEndEffectMove);
                break;
            case JOIN:
                consoleMove = new ConsoleMove(move.getTextualREP(),this::handleJoinGameMove);
                break;
            case BACK_GAME:
                consoleMove = new ConsoleMove(move.getTextualREP(),this::handleBackGameMove);
                break;
            case LEAVE:
                consoleMove = new ConsoleMove(move.getTextualREP(),this::handleLeaveWaitingRoomMove);
                break;
            case QUIT:
                consoleMove = new ConsoleMove(move.getTextualREP(),this::handleQuitMove);
                break;
            default:
                break;

        }
        return consoleMove;
    }


    /**
     * Print given text on console.
     * @param text to be printed on console.
     */
    private void print(String text){
        System.out.println(text);
    }

    /**
     * Cleans the console screen
     */
    private void cleanConsole(){
        print("");
        print("");
        print("");
    }

    @Override
    void showInformation(String message) {
        cleanConsole();
        print(message);
    }

    @Override
    void showError(String message) {
        print(ERROR_MESSAGE +message);
        waitForMove();
    }

    @Override
    void ack(String text){
        showInformation(text);
        waitForMove();
    }


    // SPECIAL CLI PRINTING METHODS

    /**
     * Prints on console the window pattern of other players
     */
    private void printOthersWindowPatterns() {
        if(this.windowPatterns!=null && !this.windowPatterns.isEmpty()){
            int index = -1;
            for(WindowPattern windowPattern : windowPatterns){
                index++;
                if(windowPattern.getID().equals(this.windowPattern.getID())) {
                    continue;
                }
                print(WINDOW_PATTERN_OF +players.get(index)); //assumes that windowPatterns and players are in the same order
                print(windowPattern.toString());
            }
        } else {
            print(PLAYERS_HAVE_NOT_ALREADY_RECEIVED_WINDOW_PATTERNS);
        }
        waitForMove();
    }

    /**
     * Prints on console favour tokens
     */
    private void printFavourTokens(){
        int index = 0;
        for(Integer favourToken : this.playersFavourTokens){
            print(this.players.get(index)+": "+favourToken);
            index++;
        }
        waitForMove();
    }

    /**
     * Prints on console public objective cards
     */
    private void printPublicObjectiveCards() {
        if(this.drawnPublicObjectiveCards!=null && !this.drawnPublicObjectiveCards.isEmpty() ){
            for(PublicObjectiveCard publicObjectiveCard : drawnPublicObjectiveCards){
                print(publicObjectiveCard.toString());
            }
        } else {
            print(THERE_ARE_NO_PUBLIC_OBJECTIVE_CARDS_YET);
        }
        waitForMove();
    }

    /**
     * Prints the private objective card of the view's player
     */
    private void printPrivateObjectiveCard() {
        if(this.privateObjectiveCard==null){
            print(YOU_DO_NOT_HAVE_A_PRIVATE_OBJECTIVE_CARD_YET);
        } else {
            print(this.privateObjectiveCard.toString());
        }
        waitForMove();
    }

    /**
     * Prints the window pattern of the view's player
     */
    private void printWindowPattern() {
        if(this.windowPattern==null){
            print(YOU_DO_NOT_HAVE_A_WINDOW_PATTERN_YET);
        } else {
            print(this.windowPattern.toString());
        }
        waitForMove();
    }

    /**
     * Prints toolcards
     */
    private void printToolCards(){
        int index = 1;
        for(ToolCard toolCard : drawnToolCards){
            print(Integer.toString(index)+". "+toolCard);
            index++;
        }
    }

    // HANDLING OF MOVES (PERFORMED BY THE VIEW'S PLAYER)

    @Override
    void handleReturnDiceToDraftpoolMove(){
        super.handleReturnDiceToDraftpoolMove();
        waitForMove();
    }


    @Override
    void handleLeaveWaitingRoomMove() {
        super.handleLeaveWaitingRoomMove();
        waitForMove();
    }

    @Override
    void handleQuitMove(){
        super.handleQuitMove();
        print(YOU_ARE_QUITTING);
        this.currentInputConsumer = null;
    }

    @Override
    void handleBackGameMove() {
        super.handleBackGameMove();
        waitForMove();
    }

    @Override
    void handleEndTurnMove() {
        super.handleEndTurnMove();
        waitForMove();
    }

    @Override
    void handleDraftDiceFromDraftPoolMove() {
        super.handleDraftDiceFromDraftPoolMove();

        print(THESE_ARE_DICES_OF_DRAFTPOOL);
        int index = 1;
        for(Dice dice : draftPoolDices){
            print(Integer.toString(index)+". "+dice);
            index++;
        }
        print(INSERT_THE_INDEX_OF_THE_DICE_YOU_WANT_TO_DRAFT);
        waitForConsoleInput(diceIndex -> {
            int diceIndexInt;
            try {
                diceIndexInt = Integer.parseInt(diceIndex) - 1;
            } catch(NumberFormatException e){
                cleanConsole();
                print(INPUT_NOT_VALID);
                handleDraftDiceFromDraftPoolMove();
                return;
            }

            if(diceIndexInt>=0 && diceIndexInt<draftPoolDices.size()){
                try {
                    HashMap<String,Object> params = new HashMap<>();
                    params.put(PARAM_DICE,draftPoolDices.get(diceIndexInt));
                    params.put(PARAM_MOVE,Move.DRAFT_DICE_FROM_DRAFTPOOL);
                    notifyGame(new Message(ControllerBoundMessageType.MOVE,params));
                } catch (NetworkingException e) {
                    print(e.getMessage());
                }
            } else {
                print(INPUT_NOT_VALID);
            }

            waitForMove();
        });
    }

    @Override
    void handlePlaceDiceOnWindowPatternMove() {
        super.handlePlaceDiceOnWindowPatternMove();
        printWindowPattern();
        print(INSERT_THE_ROW_NUMBER_OF_THE_WINDOW_PATTERN);
        waitForConsoleInput(rowString -> {
            int row;
            try {
                row = Integer.parseInt(rowString) - 1;
            } catch(NumberFormatException e){
                cleanConsole();
                print(INPUT_NOT_VALID);
                handlePlaceDiceOnWindowPatternMove();
                return;
            }
            print(INSERT_THE_COL_NUMBER_OF_THE_WINDOW_PATTERN);
            waitForConsoleInput(colString -> {
                int col;
                try{
                    col = Integer.parseInt(colString) - 1;
                } catch(NumberFormatException e){
                    cleanConsole();
                    print(INPUT_NOT_VALID);
                    handlePlaceDiceOnWindowPatternMove();
                    return;
                }
                if(row < windowPattern.getNumberOfRows() && row >= 0 && col < windowPattern.getNumberOfColumns() && col>=0){
                    HashMap<String,Object> params = new HashMap<>();
                    params.put(PARAM_ROW,row);
                    params.put(PARAM_COL,col);
                    params.put(PARAM_MOVE,Move.PLACE_DICE_ON_WINDOWPATTERN);
                    try {
                        notifyGame(new Message(ControllerBoundMessageType.MOVE,params));
                    } catch (NetworkingException e) {
                        print(e.getMessage());
                    }
                } else {
                    print(INPUT_NOT_VALID);
                }
                waitForMove();
            });
        });
    }

    @Override
    void handleUseToolCardMove() {
        super.handleUseToolCardMove();
        printToolCards();
        waitForConsoleInput(toolCardIndexString -> {
            int toolCardIndex;
            try {
                toolCardIndex = Integer.parseInt(toolCardIndexString) - 1;
            } catch (NumberFormatException e){
                cleanConsole();
                print(INPUT_NOT_VALID);
                waitForMove();
                return;
            }
            if(toolCardIndex>=drawnToolCards.size() || toolCardIndex<0){
                print(INPUT_NOT_VALID);
                waitForMove();
                return;
            }

            try {
                HashMap<String,Object> params = new HashMap<>();
                params.put(PARAM_TOOL_CARD,drawnToolCards.get(toolCardIndex));
                params.put(PARAM_MOVE,Move.USE_TOOLCARD);
                notifyGame(new Message(ControllerBoundMessageType.MOVE,params));
            } catch (NetworkingException e) {
                print(e.getMessage());
            }
            waitForMove();
        });
    }

    @Override
    void handleIncrementDraftedDiceMove() {
        super.handleIncrementDraftedDiceMove();
        waitForMove();
    }

    @Override
    void handleDecrementDraftedDiceMove() {
        super.handleDecrementDraftedDiceMove();
        waitForMove();
    }

    @Override
    void handleChangeDraftedDiceValueMove() {
        super.handleChangeDraftedDiceValueMove();
        print(INSERT_THE_VALUE_YOU_WANT_TO_ASSIGN_TO_THE_DRAFTED_DICE);
        waitForConsoleInput(diceValueString->{
            int diceValue;
            try {
                diceValue = Integer.parseInt(diceValueString);
            } catch(NumberFormatException e){
                cleanConsole();
                print(INPUT_NOT_VALID);
                handleChangeDraftedDiceValueMove();
                return;
            }
            if(diceValue<1 || diceValue>6){
                print(INPUT_NOT_VALID);
            } else {
                try {
                    HashMap<String,Object> params = new HashMap<>();
                    params.put(PARAM_MOVE,Move.CHANGE_DRAFTED_DICE_VALUE);
                    params.put(PARAM_VALUE,diceValue);
                    notifyGame(new Message(ControllerBoundMessageType.MOVE,params));
                } catch (NetworkingException e) {
                    print(e.getMessage());
                }
            }
            waitForMove();
        });
    }

    @Override
    void handleChooseDiceFromTrackMove() {
        super.handleChooseDiceFromTrackMove();
        print(FOLLOWING_THE_DICES_IN_THE_TRACK);
        for(int i=0; i<track.size(); i++){
            print(TRACK_SLOT_NUMBER +(i+1));
            for(Dice dice : track.getDicesFromSlotNumber(i)){
                print(dice.toString());
            }
        }
        print(INSERT_THE_NUMBER_OF_SLOT_YOU_WANT_TO_DRAFT_THE_DICE_FROM);
        waitForConsoleInput(trackSlotNumberString->{
            int trackSlotNumber;
            try {
                trackSlotNumber = Integer.parseInt(trackSlotNumberString) - 1;
            } catch (NumberFormatException e){
                cleanConsole();
                print(INPUT_NOT_VALID);
                waitForMove();
                return;
            }
            print(INSERT_THE_INDEX_OF_THE_DICE_YOU_WANT_TO_PICK);
            int index = 1;
            for(Dice dice : track.getDicesFromSlotNumber(trackSlotNumber)){
                print(Integer.toString(index)+". "+dice);
                index++;
            }
            waitForConsoleInput(choosenDiceIndexString->{
                int chosenDiceIndex;
                try {
                    chosenDiceIndex = Integer.parseInt(choosenDiceIndexString) - 1;
                } catch(NumberFormatException e){
                    cleanConsole();
                    print(INPUT_NOT_VALID);
                    connect();
                    waitForMove();
                    return;
                }
                HashMap<String,Object> params = new HashMap<>();
                params.put(PARAM_SLOT_NUMBER,trackSlotNumber);
                params.put(PARAM_DICE,track.getDicesFromSlotNumber(trackSlotNumber).get(chosenDiceIndex));
                params.put(PARAM_MOVE,Move.CHOOSE_DICE_FROM_TRACK);

                try {
                    notifyGame(new Message(ControllerBoundMessageType.MOVE,params));
                } catch (NetworkingException e) {
                    print(e.getMessage());
                }
                waitForMove();
            });
        });

    }

    /**
     * Method created to decrease cognitive complexity of handleMoveDiceMove(). Step 1/4
     */
    private void askForRowWP(){
        print(INSERT_THE_ROW_NUMBER_OF_THE_WINDOW_PATTERN_ORIGIN);
        waitForConsoleInput(rowString -> {
            int row;
            try {
                row = Integer.parseInt(rowString) - 1;
            } catch (NumberFormatException e){
                cleanConsole();
                print(INPUT_NOT_VALID);
                waitForMove();
                return;
            }
            askForColWP(row);
        });
    }

    /**
     * Method created to decrease cognitive complexity of handleMoveDiceMove(). Step 2/4
     */
    private void askForColWP(int row){
        print(INSERT_THE_COL_NUMBER_OF_THE_WINDOW_PATTERN_ORIGIN);
        waitForConsoleInput(colString -> {
            int col;
            try{
                col = Integer.parseInt(colString) - 1;
            } catch (NumberFormatException e){
                cleanConsole();
                print(INPUT_NOT_VALID);
                waitForMove();
                return;
            }
            if(row < windowPattern.getNumberOfRows() && row >= 0 && col < windowPattern.getNumberOfColumns() && col>=0){

                askForRowDestWP(row,col);

            } else {
                print(INPUT_NOT_VALID);
                waitForMove();
            }
        });
    }

    /**
     * Method created to decrease cognitive complexity of handleMoveDiceMove(). Step 3/4
     */
    private void askForRowDestWP(int row, int col){
        print(INSERT_THE_ROW_NUMBER_OF_THE_WINDOW_PATTERN_DESTINATION);
        waitForConsoleInput(rowDestString -> {
            int rowDest;
            try{
                rowDest = Integer.parseInt(rowDestString) - 1;
            } catch (NumberFormatException e){
                cleanConsole();
                print(INPUT_NOT_VALID);
                waitForMove();
                return;
            }
            askForColDestWP(row,col,rowDest);
        });
    }

    /**
     * Method created to decrease cognitive complexity of handleMoveDiceMove(). Step 4/4
     */
    private void askForColDestWP(int row, int col, int rowDest){
        print(INSERT_THE_COL_NUMBER_OF_THE_WINDOW_PATTERN_DESTINATION);
        waitForConsoleInput(colDestString -> {
            int colDest;
            try{
                colDest = Integer.parseInt(colDestString) - 1;
            } catch (NumberFormatException e){
                cleanConsole();
                print(INPUT_NOT_VALID);
                waitForMove();
                return;
            }
            if(rowDest < windowPattern.getNumberOfRows() && rowDest >= 0 && colDest < windowPattern.getNumberOfColumns() && colDest>=0){
                HashMap<String,Object> params = new HashMap<>();
                params.put(PARAM_ROW_FROM,row);
                params.put(PARAM_COL_FROM,col);
                params.put(PARAM_ROW_TO,rowDest);
                params.put(PARAM_COL_TO,colDest);
                params.put(PARAM_MOVE,Move.MOVE_DICE);
                try {
                    notifyGame(new Message(ControllerBoundMessageType.MOVE,params));
                } catch (NetworkingException e) {
                    print(e.getMessage());
                }
            } else {
                print(INPUT_NOT_VALID);
            }
            waitForMove();
        });
    }

    @Override
    void handleMoveDiceMove() {
        super.handleMoveDiceMove();
        askForRowWP();
    }

    @Override
    void handleJoinGameMove() {
        super.handleJoinGameMove();

        print(INSERT_YOUR_NICKNAME);
        waitForConsoleInput(nickname->{
            setPlayer(nickname);
            try {
                HashMap<String,Object> params = new HashMap<>();
                params.put(PARAM_MOVE,Move.JOIN);
                params.put(PARAM_NICKNAME,nickname);
                notifyGame(new Message(ControllerBoundMessageType.MOVE,params));
            } catch (NetworkingException e) {
                print(e.getMessage());
            }
        });
    }

    /**
     * Print toolcards and then wait for new move
     */
    private void handleShowToolCards(){
        printToolCards();
        waitForMove();
    }

    // HANDLING OF EVENTS. EVENTS ARE BASICALLY MESSAGES RECEIVED FROM SERVER.

    @Override
    void handleConnectionRestoredEvent(){
        super.handleConnectionRestoredEvent();
        if(this.handlingMessage==null){
            waitForMove();
        }
    }

    @Override
    void handleConnectionLostEvent(Message m){
        super.handleConnectionLostEvent(m);
        this.currentInputConsumer = null;
        removeHandlingMessage(m);
    }

    @Override
    void handleAbortedEvent(){
        super.handleAbortedEvent();
        this.currentInputConsumer = null;
    }

    @Override
    void handleGiveWindowPatternsEvent(Message m) {
        super.handleGiveWindowPatternsEvent(m);

        //Print windowPattern
        print(CHOOSE_A_WINDOW_PATTERN_FROM_THE_FOLLOWINGS);
        int index = 1;
        for(WindowPattern windowPattern : drawnWindowPatterns){
            print(Integer.toString(index)+". "+windowPattern);
            index++;
        }
        print("Your private objective card is:");
        print(this.privateObjectiveCard.toString());

        print(INSERT_THE_INDEX_OF_THE_WINDOW_PATTERN_YOU_WANT_TO_CHOOSE);
        waitForConsoleInput(s -> {
            int i;
            try{
                i = Integer.parseInt(s) - 1;
            } catch (NumberFormatException e){
                cleanConsole();
                print(INPUT_NOT_VALID);
                waitForMove();
                return;
            }
            if(i <= drawnWindowPatterns.size() && i >= 0){
                WindowPattern chosenWindowPattern = drawnWindowPatterns.get(i);
                try {
                    HashMap<String,Object> params = new HashMap<>();
                    params.put(PARAM_MOVE,Move.CHOOSE_WINDOW_PATTERN);
                    params.put(PARAM_WINDOW_PATTERN,chosenWindowPattern);
                    notifyGame(new Message(ControllerBoundMessageType.MOVE,params));
                } catch (NetworkingException e) {
                    print(e.getMessage());
                    return;
                }
            } else {
                print(INPUT_NOT_VALID);
            }
            removeHandlingMessage(m);
            waitForMove();
        });
    }

    @Override
    void handleAddedEvent(Message m) {
        super.handleAddedEvent(m);
        removeHandlingMessage(m);
        waitForMove();
    }

    @Override
    void handleGameEndedEvent(){
        super.handleGameEndedEvent();
        this.gameEnded = true;
        this.currentInputConsumer = null;
    }

    @Override
    void handleRemovedEvent(Message m) {
        super.handleRemovedEvent(m);
        removeHandlingMessage(m);
        waitForMove();
    }

    @Override
    void handleBackToGameEvent(Message m){
        super.handleBackToGameEvent(m);
        removeHandlingMessage(m);
        waitForMove();
    }

    @Override
    void handleInactiveEvent(Message m){
        super.handleInactiveEvent(m);
        removeHandlingMessage(m);
        waitForMove();
    }

    @Override
    void handleSetupEvent(Message m){
        super.handleSetupEvent(m);
        this.gameStarted = true;
        removeHandlingMessage(m);
    }

    @Override
    void handleRankingsEvent(Message m){
        super.handleRankingsEvent(m);

        for(RankingRecord rankingRecord : this.rankings){
            print(rankingRecord.toString());
        }

        print("");

        print(THE_WINNER_IS + getWinnerID());

        print("");
        print("");

        print(GLOBAL_RANKINGS_FOLLOWING);

        for(RankingRecord rankingRecord : this.globalRankings){
            print(rankingRecord.toString());
        }

        removeHandlingMessage(m);
    }


    @Override
    void handleUpdatedWindowPatternEvent(Message m){
        super.handleUpdatedWindowPatternEvent(m);
        removeHandlingMessage(m);
        waitForMove();
    }

    @Override
    void handleChangedDraftPoolEvent(Message m){
        super.handleChangedDraftPoolEvent(m);
        removeHandlingMessage(m);
        waitForMove();
    }

    @Override
    void handleYourTurnEvent(Message m){
        super.handleYourTurnEvent(m);
        removeHandlingMessage(m);
        waitForMove();
    }

    @Override
    void handleBadFormattedEvent(Message m){
        super.handleBadFormattedEvent(m);
        removeHandlingMessage(m);
        waitForMove();
    }

    @Override
    void handleDeniedLimitEvent(Message m){
        super.handleDeniedLimitEvent(m);
        removeHandlingMessage(m);
        waitForMove();
    }

    @Override
    void handleDeniedNicknameEvent(Message m){
        super.handleDeniedNicknameEvent(m);
        removeHandlingMessage(m);
        waitForMove();
    }

    @Override
    void handleDeniedPlayingEvent(Message m){
        super.handleDeniedPlayingEvent(m);
        removeHandlingMessage(m);
        waitForMove();
    }

    @Override
    void handleUsedToolCardEvent(Message m){
        super.handleUsedToolCardEvent(m);
        removeHandlingMessage(m);
        waitForMove();
    }

    @Override
    void handleSlotOfTrackChosenDiceEvent(Message m){
        super.handleSlotOfTrackChosenDiceEvent(m);
        removeHandlingMessage(m);
        waitForMove();
    }

    @Override
    void handleTrackChosenDiceEvent(Message m){
        super.handleTrackChosenDiceEvent(m);
        removeHandlingMessage(m);
        waitForMove();
    }

    @Override
    void handleDraftedDiceEvent(Message m){
        super.handleDraftedDiceEvent(m);
        removeHandlingMessage(m);
        waitForMove();
    }

    @Override
    void handleChangedTrackEvent(Message m){
        super.handleChangedTrackEvent(m);
        removeHandlingMessage(m);
        waitForMove();
    }


    /* HANDLING NOTIFY METHODS.
        Notify methods are called in view (super class) to notify classes that extends it (CLI and GUI)
        of changes that occured during the execution of some view's code that may need some special
        treatment from them.
     */

    @Override
    void notifyNewTurn(){
        super.notifyNewTurn();
        waitForMove();
    }
}
