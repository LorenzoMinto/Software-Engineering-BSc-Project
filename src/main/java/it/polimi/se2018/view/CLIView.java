package it.polimi.se2018.view;

import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.PublicObjectiveCard;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.networking.ConnectionType;
import it.polimi.se2018.utils.ControllerBoundMessageType;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.Message;

import java.util.*;
import java.util.function.Consumer;

public class CLIView extends View{

    private static final String INPUT_NOT_VALID = "Input not valid";
    private static final String EXIT_FROM_READING_LOOP = "exit";

    private static final Scanner SCANNER = new Scanner(System.in);

    private class ConsoleMove {
        private String description;
        private Runnable action;

        private ConsoleMove(String description, Runnable action) {
            this.description = description;
            this.action = action;
        }

        public String getDescription() {
            return description;
        }

        public void run(){
            this.action.run();
        }
    }

    private Consumer<String> currentInputConsumer;

    private boolean gameStarted = false;

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

        if(gameStarted){
            mapConsoleMoves.put(Integer.toString(index), new ConsoleMove("Show my window pattern",this::printWindowPattern));
            index++;
            mapConsoleMoves.put(Integer.toString(index), new ConsoleMove("Show my private objective card",this::printPrivateObjectiveCard));
            index++;
            mapConsoleMoves.put(Integer.toString(index), new ConsoleMove("Show public objective cards",this::printPublicObjectiveCards));
            index++;
            mapConsoleMoves.put(Integer.toString(index), new ConsoleMove("Show window patterns of other players",this::printOthersWindowPatterns));
        }

        if(this.draftedDice!=null){
            print("Remember you have drafted a dice ("+this.draftedDice+") that is waiting to be placed.");
        }

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
            case MOVE_DICE:
                consoleMove = new ConsoleMove(move.getTextualREP(),this::handleMoveDiceMove);
                break;
            case END_EFFECT:
                consoleMove = new ConsoleMove(move.getTextualREP(),this::handleEndEffectMove);
                break;
            case JOIN_GAME:
                consoleMove = new ConsoleMove(move.getTextualREP(),this::handleJoinGameMove);
                break;
            case BACK_GAME:
                consoleMove = new ConsoleMove(move.getTextualREP(),this::handleBackGameMove);
                break;
            case LEAVE:
                consoleMove = new ConsoleMove(move.getTextualREP(),this::handleLeaveWaitingRoomMove);
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
        print("");
        print("");
        print("");
        print("");
        print("");
        print("");
        print("");
        print("");
        //TODO: implement better this method
    }



    private void printOthersWindowPatterns() {
        if(this.windowPatterns!=null && !this.windowPatterns.isEmpty()){
            int index = 0;
            for(WindowPattern windowPattern : windowPatterns){
                print("Window pattern of "+players.get(index)); //assumes that windowPatterns and players are in the same order
                print(windowPattern.toString());
                index++;
            }
        } else {
            print("Players have not already received window patterns.");
        }
        waitForMove();
    }

    private void printPublicObjectiveCards() {
        if(this.drawnPublicObjectiveCards!=null && !this.drawnPublicObjectiveCards.isEmpty() ){
            for(PublicObjectiveCard publicObjectiveCard : drawnPublicObjectiveCards){
                print(publicObjectiveCard.toString());
            }
        } else {
            print("There are no public objective cards yet.");
        }
        waitForMove();
    }

    private void printPrivateObjectiveCard() {
        if(this.privateObjectiveCard==null){
            print("You do not have a private objective card yet.");
        } else {
            print(this.privateObjectiveCard.toString());
        }
        waitForMove();
    }

    private void printWindowPattern() {
        if(this.windowPattern==null){
            print("You do not have a window pattern yet.");
        } else {
            print(this.windowPattern.toString());
        }
        waitForMove();
    }




    @Override
    void handleEndEffectMove(){
        super.handleEndEffectMove();
        waitForMove();
    }

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
                sendMessage(new Message(ControllerBoundMessageType.DRAFT_DICE_FROM_DRAFTPOOL,Message.fastMap("dice",draftPoolDices.get(diceIndexInt))));
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
                    sendMessage(new Message(ControllerBoundMessageType.PLACE_DICE,params));
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
            sendMessage(new Message(ControllerBoundMessageType.USE_TOOLCARD,Message.fastMap("toolcard",drawnToolCards.get(toolcardIndex))));
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
                sendMessage(new Message(ControllerBoundMessageType.CHOOSE_DICE_VALUE,Message.fastMap("value",diceValue)));
            }
            waitForMove();
        });
    }

    @Override
    void handleChooseDiceFromTrackMove() {
        super.handleChooseDiceFromTrackMove();
        print("Following the dices in the track");
        for(int i=0; i<track.size(); i++){
            print("TRACK SLOT #"+(i+1));
            for(Dice dice : track.getDicesFromSlotNumber(i)){
                print(dice.toString());
            }
        }
        print("Insert the number of slot you want to draft the dice from");
        waitForConsoleInput(trackSlotNumberString->{
            int trackSlotNumber = Integer.parseInt(trackSlotNumberString) - 1;
            print("Insert the index of the dice you want to pick:");
            int index = 1;
            for(Dice dice : track.getDicesFromSlotNumber(trackSlotNumber)){
                print(Integer.toString(index)+". "+dice);
                index++;
            }
            waitForConsoleInput(choosenDiceIndexString->{
                int choosenDiceIndex = Integer.parseInt(choosenDiceIndexString) - 1;

                HashMap<String,Object> params = new HashMap<>();
                params.put("slotNumber",trackSlotNumber);
                params.put("dice",track.getDicesFromSlotNumber(trackSlotNumber).get(choosenDiceIndex));

                sendMessage(new Message(ControllerBoundMessageType.CHOOSE_DICE_FROM_TRACK,params));
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
                                sendMessage(new Message(ControllerBoundMessageType.MOVE_DICE,params));
                            } else {
                                print(INPUT_NOT_VALID);
                            }
                            waitForMove();
                        });
                    });

                } else {
                    print(INPUT_NOT_VALID);
                    waitForMove();
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
            sendMessage(new Message(ControllerBoundMessageType.JOIN_WR,Message.fastMap("nickname",nickname)));
        });
    }


    //EVENTS

    @Override
    void handleGameEndedEvent(Message m) {
        super.handleGameEndedEvent(m);
    }

    @Override
    void handleGiveWindowPatternsEvent(Message m) {
        super.handleGiveWindowPatternsEvent(m);

        //Print windowPattern
        print("Choose a window pattern from the followings:");
        int index = 1;
        for(WindowPattern windowPattern : drawnWindowPatterns){
            print(Integer.toString(index)+". "+windowPattern);
            index++;
        }
        print("Insert the index of the window pattern you want to choose:");
        waitForConsoleInput(s -> {
            int i = Integer.parseInt(s) - 1;
            if(i <= drawnWindowPatterns.size() && i >= 0){
                WindowPattern chosenWindowPattern = drawnWindowPatterns.get(i);
                sendMessage(new Message(ControllerBoundMessageType.CHOSEN_WINDOW_PATTERN,Message.fastMap("windowPattern",chosenWindowPattern)));
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
        super.handleCVAcknowledgmentEvent(m);
    }

    @Override
    void handleInactivePlayerEvent(Message m){
        super.handleInactivePlayerEvent(m);
    }

    @Override
    void handleBackToGameEvent(){
        super.handleBackToGameEvent();
    }

    @Override
    void handleInactiveEvent(){
        super.handleInactiveEvent();
    }

    @Override
    void handleCVErrorEvent(Message m){
        super.handleCVErrorEvent(m);
    }

    @Override
    void handleSetupEvent(Message m){
        super.handleSetupEvent(m);
        this.gameStarted = true;
    }

    @Override
    void handleNewRoundEvent(Message m){
        super.handleNewRoundEvent(m);
    }

    @Override
    void handleNewTurnEvent(Message m){
        super.handleNewTurnEvent(m);
    }

    @Override
    void handleRankingsEvent(Message m){
        super.handleRankingsEvent(m);
    }


    @Override
    void handleUpdatedWindowPatternEvent(Message m){
        super.handleUpdatedWindowPatternEvent(m);
    }

    @Override
    void handleChangedDraftPoolEvent(Message m){
        super.handleChangedDraftPoolEvent(m);
    }

    @Override
    void handleYourTurnEvent(){
        super.handleYourTurnEvent();
        waitForMove();
    }

    @Override
    void handleBadFormattedEvent(){
        super.handleBadFormattedEvent();
    }

    @Override
    void handleDeniedLimitEvent(){
        super.handleDeniedLimitEvent();
    }

    @Override
    void handleDeniedNicknameEvent(){
        super.handleDeniedNicknameEvent();
    }

    @Override
    void handleDeniedPlayingEvent(){
        super.handleDeniedPlayingEvent();
    }

    @Override
    void handleUsedToolCardEvent(Message m){
        super.handleUsedToolCardEvent(m);
    }

    @Override
    void handleSlotOfTrackChosenDice(Message m){
        super.handleSlotOfTrackChosenDice(m);
    }

    @Override
    void handleTrackChosenDiceEvent(Message m){
        super.handleTrackChosenDiceEvent(m);
    }

    @Override
    void handleDraftedDiceEvent(Message m){
        super.handleDraftedDiceEvent(m);
        waitForMove();
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
    void ack(String text){
        showMessage(text);
        waitForMove();
    }

    @Override
    void notifyNewRound(){
        super.notifyNewRound();
        //do nothing else
    }

    @Override
    void notifyNewTurn(){
        super.notifyNewTurn();
        waitForMove();
    }

    @Override
    void notifyGameVariablesChanged() {
        super.notifyGameVariablesChanged();
        //do nothing else
    }

    @Override
    void notifyGameStarted() {
        super.notifyGameStarted();
        //do nothing else
    }

    @Override
    void notifyPermissionsChanged() {
        super.notifyPermissionsChanged();
        //do nothing else
    }
}
