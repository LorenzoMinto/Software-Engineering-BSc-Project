package it.polimi.se2018.view;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.ToolCard;
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

        //Launch of CLI
        launchConsoleReader();

        //Connection to server
        connect();
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

                    waitForMove();
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

        cleanConsole();

        print("Which move would you like to perform?");

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

    private void cleanConsole(){
        //TODO: implement this method
    }

    //TODO: inserisci metodi Event

    @Override
    void handleLeaveWaitingRoomMove() {
        super.handleLeaveWaitingRoomMove();
        waitForMove();
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

        print("These are dices of draftpool:");
        int index = 1;
        for(Dice dice : draftPoolDices){
            print(Integer.toString(index)+". "+dice);
            index++;
        }
        print("Insert the index of the dice you want to draft:");
        waitForConsoleInput(diceIndex -> {
            int diceIndexInt = Integer.parseInt(diceIndex) - 1;
            if(diceIndexInt>=0 && diceIndexInt<draftPoolDices.size()){
                sendMessage(new VCMessage(VCMessage.types.DRAFT_DICE_FROM_DRAFTPOOL,Message.fastMap("dice",draftPoolDices.get(diceIndexInt))));
            } else {
                print(INPUT_NOT_VALID);
            }

            waitForMove();
        });
    }

    @Override
    void handlePlaceDiceOnWindowPatternMove() {
        super.handlePlaceDiceOnWindowPatternMove();
        print("Insert the row number of the window pattern");
        waitForConsoleInput(rowString -> {
            int row = Integer.parseInt(rowString) - 1;
            print("Insert the col number of the window pattern");
            waitForConsoleInput(colString -> {
                int col = Integer.parseInt(colString) - 1;
                if(row < windowPattern.getNumberOfRows() && row >= 0 && col < windowPattern.getNumberOfColumns() && col>=0){
                    HashMap<String,Object> params = new HashMap<>();
                    params.put("row",row);
                    params.put("col",col);
                    sendMessage(new VCMessage(VCMessage.types.PLACE_DICE,params));
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
        int index = 1;
        for(ToolCard toolCard : drawnToolCards){
            print(Integer.toString(index)+". "+toolCard);
            index++;
        }
        waitForConsoleInput(toolcardIndexString -> {
            int toolcardIndex = Integer.parseInt(toolcardIndexString);
            sendMessage(new VCMessage(VCMessage.types.USE_TOOLCARD,Message.fastMap("toolcard",drawnToolCards.get(toolcardIndex))));
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
        print("Insert the value you want to assign to the drafted dice");
        waitForConsoleInput(diceValueString->{
            int diceValue = Integer.parseInt(diceValueString);
            if(diceValue<1 || diceValue>6){
                print(INPUT_NOT_VALID);
            } else {
                sendMessage(new VCMessage(VCMessage.types.CHOOSE_DICE_VALUE,Message.fastMap("value",diceValue)));
            }
            waitForMove();
        });
    }

    @Override
    void handleChooseDiceFromTrackMove() {
        super.handleChooseDiceFromTrackMove();
        print("Following the dices in the track");
        ArrayList<Dice> dices = new ArrayList<>();
        for(int i=0; i<track.size(); i++){
            print("TRACK SLOT #"+(i+1));
            for(Dice dice : track.getDicesFromSlotNumber(i)){
                dices.add(dice);
                print(dice.toString());
            }
        }
        print("Insert the number of slot you want to draft the dice from");
        waitForConsoleInput(trackSlotNumberString->{
            int trackSlotNumber = Integer.parseInt(trackSlotNumberString) - 1;
            print("Insert the index of the dice you want to pick:");
            int index = 1;
            for(Dice dice : track.getDicesFromSlotNumber(trackSlotNumber)){
                dices.add(dice);
                print(Integer.toString(index)+". "+dice);
                index++;
            }
            waitForConsoleInput(choosenDiceIndexString->{
                int choosenDiceIndex = Integer.parseInt(choosenDiceIndexString) - 1;

                HashMap<String,Object> params = new HashMap<>();
                params.put("slotNumber",trackSlotNumber);
                params.put("dice",track.getDicesFromSlotNumber(trackSlotNumber).get(choosenDiceIndex));

                sendMessage(new VCMessage(VCMessage.types.CHOOSE_DICE_FROM_TRACK,params));
                waitForMove();
            });
        });

    }

    @Override
    void handleMoveDiceMove() {
        super.handleMoveDiceMove();
        print("Insert the row number of the window pattern (origin)");
        waitForConsoleInput(rowString -> {
            int row = Integer.parseInt(rowString) - 1;
            print("Insert the row number of the window pattern (origin)");
            waitForConsoleInput(colString -> {
                int col = Integer.parseInt(colString) - 1;
                if(row < windowPattern.getNumberOfRows() && row >= 0 && col < windowPattern.getNumberOfColumns() && col>=0){

                    print("Insert the row number of the window pattern (destination)");
                    waitForConsoleInput(rowDestString -> {
                        int rowDest = Integer.parseInt(rowDestString) - 1;
                        print("Insert the row number of the window pattern (destination)");
                        waitForConsoleInput(colDestString -> {
                            int colDest = Integer.parseInt(colDestString) - 1;
                            if(rowDest < windowPattern.getNumberOfRows() && rowDest >= 0 && colDest < windowPattern.getNumberOfColumns() && colDest>=0){
                                HashMap<String,Object> params = new HashMap<>();
                                params.put("rowFrom",row);
                                params.put("colFrom",col);
                                params.put("rowTo",rowDest);
                                params.put("colTo",colDest);
                                sendMessage(new VCMessage(VCMessage.types.MOVE_DICE,params));
                            } else {
                                print(INPUT_NOT_VALID);
                            }
                            waitForMove();
                        });
                    });

                } else {
                    print(INPUT_NOT_VALID);
                }
            });
        });
    }

    @Override
    void handleJoinGameMove() {
        super.handleJoinGameMove();

        print("Insert your nickname");
        waitForConsoleInput(nickname->{
            setPlayer(nickname);
            sendMessage(new WaitingRoomMessage(WaitingRoomMessage.types.JOIN,Message.fastMap("nickname",nickname)));
        });
    }


    //EVENTS

    @Override
    void handleGameEndedEvent(Message m) {
        super.handleGameEndedEvent(m);
        print("Rankings: xxx");
        //TODO: print rankings
    }

    @Override
    void handleGiveWindowPatternsEvent(Message m) {
        super.handleGiveWindowPatternsEvent(m);

        //Print windowpattern
        print("Choose a window pattern from the followings:");
        int index = 1;
        for(WindowPattern windowPattern : drawnWindowPatterns){
            print(Integer.toString(index)+". "+windowPattern);
            index++;
        }
        waitForConsoleInput(s -> {
            int i = Integer.parseInt(s) - 1;
            if(i <= drawnWindowPatterns.size() && i >= 0){
                WindowPattern chosenWindowPattern = drawnWindowPatterns.get(i);
                sendMessage(new VCMessage(VCMessage.types.CHOOSE_WINDOW_PATTERN,Message.fastMap("windowpattern",chosenWindowPattern)));
            } else {
                print(INPUT_NOT_VALID);
            }

            waitForMove(); //remember that if the currentInputConsumer is the last operation to be formed, insert waitForMove().
        });
    }

    @Override
    void handleAddedEvent() {
        super.handleAddedEvent();
        waitForMove();
    }

    @Override
    void handleRemovedEvent() {
        super.handleRemovedEvent();
        waitForMove();
    }

    @Override
    void handleCVAcknowledgmentEvent(Message m){

    }

    @Override
    void handleInactivePlayerEvent(Message m){

    }

    @Override
    void handleBackToGameEvent(){

    }

    @Override
    void handleInactiveEvent(){

    }

    @Override
    void handleCVErrorEvent(Message m){

    }

    @Override
    void handleSetupEvent(Message m){

    }

    @Override
    void handleNewRoundEvent(Message m){

    }

    @Override
    void handleNewTurnEvent(Message m){

    }

    @Override
    void handleRankingsEvent(Message m){

    }

    @Override
    void handleChangedDraftPoolEvent(Message m){

    }

    @Override
    void handleYourTurnEvent(){

    }

    @Override
    void handleBadFormattedEvent(){

    }

    @Override
    void handleDeniedLimitEvent(){

    }

    @Override
    void handleDeniedNicknameEvent(){

    }

    @Override
    void handleDeniedPlayingEvent(){

    }

    @Override
    void handleUsedToolCardEvent(Message m){

    }







    @Override
    void showMessage(String message) {
        cleanConsole();
        print(message);
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
