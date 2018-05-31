package it.polimi.se2018.view;

import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.message.CVMessage;
import it.polimi.se2018.utils.message.MVMessage;
import it.polimi.se2018.utils.message.Message;
import it.polimi.se2018.utils.message.WaitingRoomMessage;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Scanner;

public class CLIView extends View {

    private static final Scanner scanner = new Scanner(System.in);

    private Move currentMove;

    private EnumSet<Move> permissions;

    private HashMap<Move,String> movesToCLICommands = new HashMap<>();
    private HashMap<Integer,Move> cliCommandsToMoves = new HashMap<>();

    public CLIView() {
        super();

        //Set the textual description and integer identifier for all moves
        Arrays.stream(Move.values()).forEach(this::mapMoveWithPermission);

        this.logger.fine("Ciao sono la CLI");

        new Thread(this::console).start();
    }

    private void console(){

        askForMove();

        try {
            parseMoveChoise();

        } catch (ExitCommandException e) {
            writeToConsole("Console closed.");
            return;
        }

        try {
            handleMove();

        } catch (ExitCommandException e) {
            abortMove();
        }

        console(); //Infinite loop until parseMoveChoise() -> ExitCommandException
    }

    private void askForMove() {

        String messaggio;

        if(permissions.isEmpty()){
            messaggio = "Nessuna mossa eseguibile, per il momento.";
        } else {
            String options = permissions.stream().map(move -> movesToCLICommands.get(move)).reduce((x,y) -> x.concat(System.lineSeparator()).concat(y)).get();
            messaggio = "Scegli la mossa che vuoi eseguire.".concat(System.lineSeparator()).concat(options);
        }

        writeToConsole(messaggio);
    }

    private void parseMoveChoise() throws ExitCommandException {

        int input = Integer.parseInt(readFromConsole()); //throws ExitCommandException

        currentMove = cliCommandsToMoves.get(input);
    }

    private void handleMove() throws ExitCommandException {
        //Check if currentMove is a valid one
        if(currentMove==null){
            writeToConsole("Mossa non riconosciuta");
            return;
        }

        Message message = null;

        //Here the code that handles each move. Each case can contain multiple writeToConsole and / or readFromConsole
        switch (currentMove) {
            case END_TURN:
                break;
            case DRAFT_DICE_FROM_DRAFTPOOL:
                break;
            case PLACE_DICE_ON_WINDOWPATTERN:
                break;
            case USE_TOOLCARD:
                break;
            case INCREMENT_DRAFTED_DICE:
                break;
            case DECREMENT_DRAFTED_DICE:
                break;
            case CHANGE_DRAFTED_DICE_VALUE:
                break;
            case CHOOSE_DICE_FROM_TRACK:
                break;
            case MOVE_DICE:
                break;
            case JOIN_GAME:
                writeToConsole("Inserisci il tuo nickname: ");
                String nickname = readFromConsole();
                message = new WaitingRoomMessage( WaitingRoomMessage.types.JOIN, Message.fastMap("nickname",nickname) );
                break;
        }

        //Send message (if created) to server (through client)
        if(message!=null){
            notify(message);
        }
    }

    private void abortMove() {
        currentMove = null;
        writeToConsole("Move aborted");
    }

    private String readFromConsole() throws ExitCommandException {
        String text = scanner.nextLine();
        if(text.equals("exit")){ throw new ExitCommandException(); }
        return text;
    }

    private void writeToConsole(String m){
        this.logger.info(m);
    }


    private class ExitCommandException extends Exception{
        ExitCommandException() {
            //do nothing
        }
    }

    @Override
    public boolean update(Message m) {
        logger.info(()->"Ricevuto: "+m.getType().toString());

        handleReceivedMessage(m);

        askForMove();

        return true;
    }

    private void mapMoveWithPermission(Move move){

        String text = "";
        int choiceNumber = 0;

        //Assign every move to a textual description and int identifier
        switch (move) {
            case END_TURN:
                text = "";
                choiceNumber = 0;
                break;
            case DRAFT_DICE_FROM_DRAFTPOOL:
                text = "";
                choiceNumber = 0;
                break;
            case PLACE_DICE_ON_WINDOWPATTERN:
                text = "";
                choiceNumber = 0;
                break;
            case USE_TOOLCARD:
                text = "";
                choiceNumber = 0;
                break;
            case INCREMENT_DRAFTED_DICE:
                text = "";
                choiceNumber = 0;
                break;
            case DECREMENT_DRAFTED_DICE:
                text = "";
                choiceNumber = 0;
                break;
            case CHANGE_DRAFTED_DICE_VALUE:
                text = "";
                choiceNumber = 0;
                break;
            case CHOOSE_DICE_FROM_TRACK:
                text = "";
                choiceNumber = 0;
                break;
            case MOVE_DICE:
                text = "";
                choiceNumber = 0;
                break;
            case JOIN_GAME:
                text = "Unisciti al gioco";
                choiceNumber = 10;
                break;
        }

        movesToCLICommands.put(move,Integer.toString(choiceNumber).concat(". ").concat(text));
        cliCommandsToMoves.put(choiceNumber,move);
    }

    private void handleReceivedMessage(Message m) {

        EnumSet<Move> p = (EnumSet<Move>) m.getPermissions();
        if(p!=null && !p.isEmpty()){
            this.permissions = p;
        }//else keep same permissions

        if(m instanceof CVMessage){
            switch ((CVMessage.types) m.getType()) {
                case ERROR_MESSAGE:
                    break;
                case ACKNOWLEDGMENT_MESSAGE:
                    break;
                case INACTIVE_PLAYER:
                    break;
                case BACK_TO_GAME:
                    break;
                case INACTIVE:
                    break;
                case GIVE_WINDOW_PATTERNS:
                    break;
                case GAME_ENDED:
                    break;
            }
        } else if(m instanceof MVMessage){
            switch ((MVMessage.types) m.getType()) {
                case SETUP:
                    break;
                case NEXT_ROUND:
                    break;
                case NEW_TURN:
                    break;
                case USED_TOOLCARD:
                    break;
                case RANKINGS:
                    break;
                case WINDOWPATTERN:
                    break;
                case DRAFTPOOL:
                    break;
            }
        } else if(m instanceof WaitingRoomMessage){
            switch ((WaitingRoomMessage.types) m.getType()) {
                case BAD_FORMATTED:
                    break;
                case DENIED:
                    break;
                case JOIN:
                    break;
                case ADDED:
                    break;
                case LEAVE:
                    break;
                case REMOVED:
                    break;
            }
        } else {
            //do nothing
        }
    }
}
