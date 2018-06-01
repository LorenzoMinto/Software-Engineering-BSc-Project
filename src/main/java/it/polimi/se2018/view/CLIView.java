package it.polimi.se2018.view;

import it.polimi.se2018.model.*;
import it.polimi.se2018.networking.ConnectionType;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.message.Message;
import it.polimi.se2018.utils.message.NoSuchParamInMessageException;
import it.polimi.se2018.utils.message.VCMessage;
import it.polimi.se2018.utils.message.WaitingRoomMessage;

import java.util.*;

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
        writeToConsole("Inserisci il tuo nickname: ");
        String nickname = readFromConsole();
        return new WaitingRoomMessage( WaitingRoomMessage.types.JOIN, Message.fastMap("nickname",nickname) );
    }

    @Override
    Message handleGameEndedMove(LinkedHashMap<String, Integer> rankings) {
        int count=1;
        for (Map.Entry<String, Integer> entry : rankings.entrySet()) {
            if(count==1){
                writeToConsole("Il vincitore è "+entry.getKey()+" con "+entry.getValue()+" punti.");
            } else {
                writeToConsole(count+" classificato: "+entry.getKey()+" con "+entry.getValue()+" punti.");
            }
            count++;
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    Message handleGiveWindowPatterns(Message m) {
        List<WindowPattern> patterns;
        try {
            patterns = (List<WindowPattern>) m.getParam("patterns");
        } catch (NoSuchParamInMessageException e) {
            return null;
        }
        writeToConsole("Scegli uno fra i seguenti window pattern:");
        int index = 0;
        for(WindowPattern windowPattern : patterns){
            writeToConsole("OPZIONE #".concat(Integer.toString(index)).concat(System.lineSeparator()));
            writeToConsole(windowPattern.toString());
        }
        int choice = Integer.parseInt(readFromConsole());
        if(choice<patterns.size()){
            return new VCMessage(VCMessage.types.CHOOSE_WINDOW_PATTERN,Message.fastMap("windowpattern",patterns.get(choice)));

        } else {
            writeToConsole("Scelta non valida");

            return handleGiveWindowPatterns(m);
        }
    }

    @Override
    Message handleAddedWL() {
        writeToConsole("Sei stato aggiunto correttamente alla partita");
        return null;
    }

    @Override
    void notifyGameStarted() {
        printAllTheGameInfo();
    }

    @Override
    public void showMessage(String message) {
        writeToConsole(message);
    }

    @Override
    void notifyGameVariablesChanged() {
        //do nothing
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
            handleCurrentMove();
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

        String text;
        int choiceNumber;

        //Assign every move to a textual description and int identifier
        switch (move) {
            case END_TURN:
                text = "Concludi il turno";
                choiceNumber = 1;
                break;
            case DRAFT_DICE_FROM_DRAFTPOOL:
                text = "Pesca un dado dalla draftpool";
                choiceNumber = 2;
                break;
            case PLACE_DICE_ON_WINDOWPATTERN:
                text = "Posiziona un dado sul windowpattern";
                choiceNumber = 3;
                break;
            case USE_TOOLCARD:
                text = "Usa toolcard";
                choiceNumber = 4;
                break;
            case INCREMENT_DRAFTED_DICE:
                text = "Incrementa il valore del dado";
                choiceNumber = 5;
                break;
            case DECREMENT_DRAFTED_DICE:
                text = "Decrementa il valore del dado";
                choiceNumber = 6;
                break;
            case CHANGE_DRAFTED_DICE_VALUE:
                text = "Cambia il valore del dado";
                choiceNumber = 7;
                break;
            case CHOOSE_DICE_FROM_TRACK:
                text = "Scegli il dado dalla track";
                choiceNumber = 8;
                break;
            case MOVE_DICE:
                text = "Sposta il dado";
                choiceNumber = 9;
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

    private void printAllTheGameInfo(){
        //TODO
    }
}
