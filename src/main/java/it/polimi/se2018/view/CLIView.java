package it.polimi.se2018.view;

import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.networking.ConnectionType;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.message.Message;
import it.polimi.se2018.utils.message.VCMessage;
import it.polimi.se2018.utils.message.WaitingRoomMessage;

import java.util.*;
import java.util.function.Consumer;

public class CLIView extends View{

    private static final String INPUT_NOT_VALID = "Input not valid";
    private static final String EXIT_FROM_READING_LOOP = "exit";

    private static final Scanner SCANNER = new Scanner(System.in);

    private class ConsoleMove {
        private Move move;
        private Runnable action;

        private ConsoleMove(Move move, Runnable action) {
            this.move = move;
            this.action = action;
        }

        public String getDescription() {
            return move.getTextualREP();
        }

        public void run(){
            this.action.run();
        }
    }

    private Consumer<String> currentInputConsumer;

    public static void main(String[] args) {
        new CLIView();
    }

    public CLIView() {
        super();

        //Connection to server
        connect();

        //Launch of CLI
        launchConsoleReader();

        //CLI starts proposing moves (join game)
        waitForMove();
    }

    private void connect(){
        print("Insert 1. for RMI, 2. for socket");
        waitForConsoleInput(connectionTypeString -> {
            print("Insert name server");
            waitForConsoleInput(serverName -> {
                print("Insert port number");
                waitForConsoleInput(portNumberString -> {
                    int portNumber = Integer.parseInt(portNumberString);
                    ConnectionType connectionType = (connectionTypeString.equals("1")) ? ConnectionType.RMI : ConnectionType.SOCKET;
                    connectToRemoteServer(connectionType,serverName,portNumber);
                });
            });
        });
    }

    private void launchConsoleReader(){
        new Thread(()->{
            String text;
            do{
                text = SCANNER.nextLine();

                if(currentInputConsumer !=null){
                    currentInputConsumer.accept(text);
                } else {
                    print(INPUT_NOT_VALID);
                }
            } while(!text.equals(EXIT_FROM_READING_LOOP));
        }).start();
    }

    private void waitForMove(){

        //Create a LinkedHashMap to map string choices from console to moves
        LinkedHashMap<String,ConsoleMove> mapConsoleMoves = new LinkedHashMap<>();
        int index = 1;
        for(Move move : this.getPermissions()){
            mapConsoleMoves.put(Integer.toString(index), convertMoveToConsoleMove(move));
            index++;
        }

        //Print console moves
        for (Map.Entry<String, ConsoleMove> entry : mapConsoleMoves.entrySet()) {
            print(entry.getKey()+". "+entry.getValue().getDescription());
        }

        //Reads from console
        waitForConsoleInput(s -> {
            if(mapConsoleMoves.containsKey(s)){
                mapConsoleMoves.get(s).run();
            } else {
                print(INPUT_NOT_VALID);
            }
        });
    }

    private ConsoleMove convertMoveToConsoleMove(Move move){
        ConsoleMove consoleMove = null;
        switch (move) {
            case END_TURN:
                consoleMove = new ConsoleMove(move,this::handleEndTurnMove);
                break;
            case DRAFT_DICE_FROM_DRAFTPOOL:
                consoleMove = new ConsoleMove(move,this::handleDraftDiceFromDraftPoolMove);
                break;
            case PLACE_DICE_ON_WINDOWPATTERN:
                consoleMove = new ConsoleMove(move,this::handlePlaceDiceOnWindowPatternMove);
                break;
            case USE_TOOLCARD:
                consoleMove = new ConsoleMove(move,this::handleUseToolCardMove);
                break;
            case INCREMENT_DRAFTED_DICE:
                consoleMove = new ConsoleMove(move,this::handleIncrementDraftedDiceMove);
                break;
            case DECREMENT_DRAFTED_DICE:
                consoleMove = new ConsoleMove(move,this::handleDecrementDraftedDiceMove);
                break;
            case CHANGE_DRAFTED_DICE_VALUE:
                consoleMove = new ConsoleMove(move,this::handleChangeDraftedDiceValueMove);
                break;
            case CHOOSE_DICE_FROM_TRACK:
                consoleMove = new ConsoleMove(move,this::handleChooseDiceFromTrackMove);
                break;
            case MOVE_DICE:
                consoleMove = new ConsoleMove(move,this::handleMoveDiceMove);
                break;
            case JOIN_GAME:
                consoleMove = new ConsoleMove(move,this::handleJoinGameMove);
                break;
            case BACK_GAME:
                consoleMove = new ConsoleMove(move,this::handleBackGameMove);
                break;
            case LEAVE:
                consoleMove = new ConsoleMove(move,this::handleLeaveWaitingRoomMove);
                break;
        }
        return consoleMove;
    }

    private void waitForConsoleInput(Consumer<String> consumer){
        this.currentInputConsumer = consumer;
    }

    private void print(String text){
        System.out.println(text);
    }

    @Override
    void handleLeaveWaitingRoomMove() {
        sendMessage(new WaitingRoomMessage(WaitingRoomMessage.types.LEAVE,Message.fastMap("nickname",this.playerID)));
        waitForMove();
    }

    @Override
    void handleBackGameMove() {
        sendMessage(new VCMessage(VCMessage.types.BACK_GAMING,null,this.playerID));
        waitForMove();
    }

    @Override
    void handleEndTurnMove() {
        sendMessage(new VCMessage(VCMessage.types.END_TURN,null,this.playerID));
        waitForMove();
    }

    @Override
    void handleDraftDiceFromDraftPoolMove() {

    }

    @Override
    void handlePlaceDiceOnWindowPatternMove() {

    }

    @Override
    void handleUseToolCardMove() {

    }

    @Override
    void handleIncrementDraftedDiceMove() {

    }

    @Override
    void handleDecrementDraftedDiceMove() {

    }

    @Override
    void handleChangeDraftedDiceValueMove() {

    }

    @Override
    void handleChooseDiceFromTrackMove() {

    }

    @Override
    void handleMoveDiceMove() {

    }

    @Override
    void handleJoinGameMove() {
        print("Insert your nickname");
        String nickname = SCANNER.nextLine();
        this.playerID = nickname;
        sendMessage(new WaitingRoomMessage(WaitingRoomMessage.types.JOIN,Message.fastMap("nickname",nickname)));
    }

    @Override
    void handleGameEndedEvent(LinkedHashMap<String, Integer> rankings) {
        //TODO: print rankings
    }

    @Override
    void handleGiveWindowPatternsEvent(List<WindowPattern> patterns) {
        //Print windowpattern
        int index = 1;
        for(WindowPattern windowPattern : patterns){
            print(Integer.toString(index)+". "+windowPattern);
            index++;
        }
        waitForConsoleInput(s -> {
            int i = Integer.parseInt(s) - 1;
            if(i <= patterns.size() && i >= 0){
                WindowPattern chosenWindowPattern = patterns.get(Integer.parseInt(s));
                sendMessage(new VCMessage(VCMessage.types.CHOOSE_WINDOW_PATTERN,Message.fastMap("windowpattern",chosenWindowPattern)));
            } else {
                print(INPUT_NOT_VALID);
            }

            waitForMove(); //remember that if the currentInputConsumer is the last operation to be formed, insert waitForMove().
        });
    }

    @Override
    void handleAddedEvent() {
        waitForMove();
    }

    @Override
    void handleRemovedEvent() {
        waitForMove();
    }

    @Override
    void showMessage(String message) {
        print(message);
        waitForMove();
    }

    @Override
    void errorMessage(String message) {
        print("ERROR: "+message);
        waitForMove();
    }

    @Override
    void notifyGameVariablesChanged() {
        //do nothing
    }

    @Override
    void notifyGameStarted() {
        print("The game is started!");
    }

    @Override
    void notifyPermissionsChanged() {
        //do nothing
    }
}
