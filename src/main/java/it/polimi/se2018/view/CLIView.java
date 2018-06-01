package it.polimi.se2018.view;

import it.polimi.se2018.networking.ConnectionType;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.message.Message;
import it.polimi.se2018.utils.message.WaitingRoomMessage;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Scanner;

public class CLIView extends View {

    private static final Scanner scanner = new Scanner(System.in);

    private EnumMap<Move,String> movesToCLICommands = new EnumMap<>(Move.class);
    private HashMap<Integer,Move> cliCommandsToMoves = new HashMap<>();

    public static void main (String[] args) { new CLIView(); }

    private CLIView() {
        super();

        //Set the textual description and integer identifier for all moves
        Arrays.stream(Move.values()).forEach(this::mapMoveWithPermission);

        askForConnectionType();

        new Thread(this::console).start();
    }

    private void askForConnectionType(){
        writeToConsole("1. Per giocare con RMI. 2. Per giocare con Socket.");
        if(readFromConsole().equals("1")){
            connectToRemoteServer(ConnectionType.RMI);
        } else {
            connectToRemoteServer(ConnectionType.SOCKET);
        }
    }

    @Override
    Message handleEndTurnMove() {
        return null;
    }

    @Override
    Message handleDraftDiceFromDraftPoolMove() {
        return null;
    }

    @Override
    Message handlePlaceDiceOnWindowPatternMove() {
        return null;
    }

    @Override
    Message handleUseToolCardMove() {
        return null;
    }

    @Override
    Message handleIncrementDraftedDiceMove() {
        return null;
    }

    @Override
    Message handleDecrementDraftedDiceMove() {
        return null;
    }

    @Override
    Message handleChangeDraftedDiceValueMove() {
        return null;
    }

    @Override
    Message handleChooseDiceFromTrackMove() {
        return null;
    }

    @Override
    Message handleMoveDiceMove() {
        return null;
    }

    @Override
    Message handleJoinGameMove() {
        showMessage("Inserisci il tuo nickname: ");
        String nickname = readFromConsole();
        return new WaitingRoomMessage( WaitingRoomMessage.types.JOIN, Message.fastMap("nickname",nickname) );
    }

    @Override
    public void showMessage(String message) {
        writeToConsole(message);
    }

    @Override
    void askForMove() {
        String messaggio;
        if(getPermissions().isEmpty()){
            messaggio = "Nessuna mossa eseguibile, per il momento.";
        } else {
            String options =    getPermissions()
                                .stream()
                                .map(move -> movesToCLICommands.get(move))
                                .reduce((x,y) -> x.concat(System.lineSeparator()).concat(y))
                                .orElse("");

            messaggio = "Scegli la mossa che vuoi eseguire.".concat(System.lineSeparator()).concat(options);
        }

        writeToConsole(messaggio);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void console(){

        do {
            askForMove();
            parseMoveChoiceFromConsole();
            handleMove();
        } while (true);
    }

    /**
     * Depending on the value inserted in console, recognize the move to perform
     *
     * @see CLIView#mapMoveWithPermission(Move)
     */
    private void parseMoveChoiceFromConsole() {
        setCurrentMove(cliCommandsToMoves.get(Integer.parseInt(readFromConsole())));
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
            default:
                //if cases are updated with Move.class, should never enter here
                throw new BadBehaviourRuntimeException();
        }

        movesToCLICommands.put(move,Integer.toString(choiceNumber).concat(". ").concat(text));
        cliCommandsToMoves.put(choiceNumber,move);
    }

    private String readFromConsole() {
        return scanner.nextLine();
    }

    private void writeToConsole(String m){
        this.logger.info(m);
    }
}
