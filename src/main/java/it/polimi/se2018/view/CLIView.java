package it.polimi.se2018.view;

import it.polimi.se2018.model.*;
import it.polimi.se2018.networking.ConnectionType;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.message.Message;
import it.polimi.se2018.utils.message.VCMessage;
import it.polimi.se2018.utils.message.WaitingRoomMessage;

import java.util.*;

public class CLIView extends View {

    private static final Scanner scanner = new Scanner(System.in);
    private static final String BAD_DATA_INSERTED = "Inserimento errato.";

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
        String choice = readFromConsole();

        writeToConsole("Inserisci l'indirizzo del server:");
        String serverName = readFromConsole();
        if(serverName.equals("")){
            writeToConsole(BAD_DATA_INSERTED);
            askForConnectionType();
            return;
        }

        writeToConsole("Inserisci la porta da utilizzare:");

        int port;
        try{
            port = Integer.parseInt(readFromConsole());
        } catch (NumberFormatException e){
            writeToConsole(BAD_DATA_INSERTED);
            askForConnectionType();
            return;
        }

        ConnectionType type = choice.equals("1") ? ConnectionType.RMI : ConnectionType.SOCKET;

        connectToRemoteServer(type,serverName,port);
    }

    @Override
    Message handleEndTurnMove() {

        return new VCMessage(VCMessage.types.END_TURN);
    }

    @Override
    Message handleDraftDiceFromDraftPoolMove() {
        writeToConsole("Di seguito i dadi disponibili");
        int index = 0;
        for(Dice dice : this.draftPoolDices){
            writeToConsole("Dice #"+index+": "+dice);
        }
        writeToConsole("Inserisci il numero rispettivo al dado che vuoi pescare");
        try{
            index = Integer.parseInt(readFromConsole());
        } catch (NumberFormatException e){
            writeToConsole(BAD_DATA_INSERTED);
            return handleDraftDiceFromDraftPoolMove();
        }
        if(index>=draftPoolDices.size()){
            writeToConsole(BAD_DATA_INSERTED);
            return handleDraftDiceFromDraftPoolMove();
        }

        return new VCMessage(VCMessage.types.DRAFT_DICE_FROM_DRAFTPOOL,Message.fastMap("dice",draftPoolDices.get(index)));
    }

    @Override
    Message handlePlaceDiceOnWindowPatternMove() {
        writeToConsole("This is your window pattern:"+this.windowPattern);
        writeToConsole("Inserisci la riga dove vuoi inserire il dado:");
        int row;
        try{
            row = Integer.parseInt(readFromConsole());
        } catch( NumberFormatException e ){
            writeToConsole(BAD_DATA_INSERTED);
            return handlePlaceDiceOnWindowPatternMove();
        }

        writeToConsole("Inserisci la colonna dove vuoi inserire il dado:");
        int col;
        try{
            col = Integer.parseInt(readFromConsole());
        } catch( NumberFormatException e ){
            writeToConsole(BAD_DATA_INSERTED);
            return handlePlaceDiceOnWindowPatternMove();
        }

        HashMap<String,Object> params = new HashMap<>();
        params.put("row",row);
        params.put("col",col);
        return new VCMessage(VCMessage.types.PLACE_DICE,params);
    }

    @Override
    Message handleUseToolCardMove() {
        writeToConsole("Di seguito le toolcards disponibili");
        int index = 0;
        for(ToolCard toolCard : drawnToolCards){
            writeToConsole("ToolCard #"+index+": "+toolCard);
        }
        writeToConsole("Inserisci il numero rispettivo alla toolcard che vuoi usare");

        try{
            index = Integer.parseInt(readFromConsole());
        } catch( NumberFormatException e){
            writeToConsole(BAD_DATA_INSERTED);
            return handleUseToolCardMove();
        }

        if(index>=drawnToolCards.size()){
            return handleUseToolCardMove();
        }

        return new VCMessage(VCMessage.types.USE_TOOLCARD,Message.fastMap("toolcard",drawnToolCards.get(index)));
    }

    @Override
    Message handleIncrementDraftedDiceMove() {

        return new VCMessage(VCMessage.types.INCREMENT_DICE,Message.fastMap("dice",draftedDice));
    }

    @Override
    Message handleDecrementDraftedDiceMove() {

        return new VCMessage(VCMessage.types.DECREMENT_DICE,Message.fastMap("dice",draftedDice));
    }

    @Override
    Message handleChangeDraftedDiceValueMove() {
        writeToConsole("Il valore corrente del dado è: "+this.draftedDice.getValue());
        writeToConsole("Inserisci il nuovo valore che vuoi attribuire");
        int value;
        try{
            value = Integer.parseInt(readFromConsole());
        } catch(NumberFormatException e){
            return handleChangeDraftedDiceValueMove();
        }
        return new VCMessage(VCMessage.types.CHOOSE_DICE_VALUE,Message.fastMap("value",value));
    }

    @Override
    Message handleChooseDiceFromTrackMove() {
        writeToConsole("Di seguito il contenuto attuale della track");
        int slotNumber = 0;
        for(TrackSlot trackSlot: track){
            writeToConsole("Slot #"+slotNumber+": "+trackSlot);
            slotNumber++;
        }
        writeToConsole("Da quale slot vuoi prelevare il dado?");

        try{
            slotNumber = Integer.parseInt(readFromConsole());
        } catch( NumberFormatException e ){
            writeToConsole(BAD_DATA_INSERTED);
            return handleChooseDiceFromTrackMove();
        }

        if(slotNumber>=track.size()){
            writeToConsole(BAD_DATA_INSERTED);
            return handleChooseDiceFromTrackMove();
        }

        int diceNumber = 0;
        for(Dice dice : track.getDicesFromSlotNumber(slotNumber)){
            writeToConsole("Inserisci "+diceNumber+" per prendere il dado "+dice);
            diceNumber++;
        }

        try{
            diceNumber = Integer.parseInt(readFromConsole());
        } catch ( NumberFormatException e ){
            writeToConsole(BAD_DATA_INSERTED);
            return handleChooseDiceFromTrackMove();
        }

        if(diceNumber>=track.getDicesFromSlotNumber(slotNumber).size()){
            writeToConsole(BAD_DATA_INSERTED);
            return handleChooseDiceFromTrackMove();
        }

        HashMap<String,Object> params = new HashMap<>();
        params.put("slotNumber",slotNumber);
        params.put("dice",diceNumber);

        return new VCMessage(VCMessage.types.CHOOSE_DICE_FROM_TRACK,params);
    }

    @Override
    Message handleMoveDiceMove() {
        //TODO: implementa questo metodo
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
    Message handleGiveWindowPatterns(List<WindowPattern> patterns) {
        writeToConsole("Scegli uno fra i seguenti window pattern:");
        int index = 0;
        for(WindowPattern windowPattern : patterns){
            writeToConsole("OPZIONE #".concat(Integer.toString(index)).concat(System.lineSeparator()));
            writeToConsole(windowPattern.toString());
        }
        int choice;
        try {
            choice = Integer.parseInt(readFromConsole());
        } catch (NumberFormatException e){
            writeToConsole(BAD_DATA_INSERTED);
            return handleGiveWindowPatterns(patterns);
        }

        if(choice<patterns.size()){
            return new VCMessage(VCMessage.types.CHOOSE_WINDOW_PATTERN,Message.fastMap("windowpattern",patterns.get(choice)));

        } else {
            writeToConsole("Scelta non valida");

            return handleGiveWindowPatterns(patterns);
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
    public void errorMessage(String message) {
        showMessage("[[[ERRORE]]] "+message);
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
    void notifyGameVariablesChanged(boolean forceClean) {
        if(forceClean){
            cleanConsole();
        }
    }

    private void cleanConsole() {
        //TODO: implementa questo metodo
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
        int choice;
        try{
            choice = Integer.parseInt(readFromConsole());
        } catch(NumberFormatException e){
            parseMoveChoiceFromConsole();
            return;
        }

        setCurrentMove(cliCommandsToMoves.get(choice));
    }

    private void handleCurrentMove() {
        //Check if currentMove is a valid one
        if(currentMove==null){
            showMessage("Mossa non riconosciuta");
            return;
        }

        Message message = null;

        //Here the code that handles each move. Each case can contain multiple writeToConsole and / or readFromConsole
        switch (currentMove) {
            case END_TURN:
                message = handleEndTurnMove();
                break;
            case DRAFT_DICE_FROM_DRAFTPOOL:
                message = handleDraftDiceFromDraftPoolMove();
                break;
            case PLACE_DICE_ON_WINDOWPATTERN:
                message = handlePlaceDiceOnWindowPatternMove();
                break;
            case USE_TOOLCARD:
                message = handleUseToolCardMove();
                break;
            case INCREMENT_DRAFTED_DICE:
                message = handleIncrementDraftedDiceMove();
                break;
            case DECREMENT_DRAFTED_DICE:
                message = handleDecrementDraftedDiceMove();
                break;
            case CHANGE_DRAFTED_DICE_VALUE:
                message = handleChangeDraftedDiceValueMove();
                break;
            case CHOOSE_DICE_FROM_TRACK:
                message = handleChooseDiceFromTrackMove();
                break;
            case MOVE_DICE:
                message = handleMoveDiceMove();
                break;
            case JOIN_GAME:
                message = handleJoinGameMove();
                break;
            case BACK_GAME:
                message = new VCMessage(VCMessage.types.BACK_GAMING);
                break;
            case NAVIGATE_INFOS:
                printAllTheGameInfo();
                break;
            default:
                //if cases are updated with Move.class, should never enter here
                throw new BadBehaviourRuntimeException();
        }

        //Send message (if created) to server (through client)
        if(message!=null){
            sendMessage(message);
        }
    }

    private void mapMoveWithPermission(Move move){

        String text;
        int choiceNumber;

        //Assign every move to a textual description and int identifier
        switch (move) {
            case BACK_GAME:
                text = "Rientra nel gioco";
                choiceNumber = 11;
                break;
            case NAVIGATE_INFOS:
                text = "Naviga nelle informazioni di gioco";
                choiceNumber = 0;
                break;
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
        //TODO: implementa questo metodo
    }
}
