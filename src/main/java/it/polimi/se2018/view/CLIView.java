package it.polimi.se2018.view;

import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.message.CVMessage;
import it.polimi.se2018.utils.message.MVMessage;
import it.polimi.se2018.utils.message.Message;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.utils.message.WaitingRoomMessage;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Scanner;

public class CLIView extends View {

    private static final Scanner scanner = new Scanner(System.in);

    private Move currentMove;

    public CLIView() {
        super();

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
            evaluateMove();

        } catch (ExitCommandException e) {
            abortMove();
        }

        console(); //Infinite loop until parseMoveChoise() -> ExitCommandException
    }

    private void askForMove() {
        //leggerà le permissions
        writeToConsole("Cosa vuoi fare? 1.Unirti al gioco");
    }

    private void parseMoveChoise() throws ExitCommandException {

        String input = readFromConsole(); //throws ExitCommandException
        switch(input){
            case "1":
                writeToConsole("Hai scelto di fare la mossa 1: unirti al gioco");
                currentMove = Move.JOIN_GAME;
                break;
            default:
                currentMove = Move.UNKNOWN;
                break;
        }
    }

    private void evaluateMove() throws ExitCommandException {
        //Check if currentMove is a valid one
        if(currentMove==null){ throw new BadBehaviourRuntimeException(); }

        Message message = null;

        switch (currentMove) {
            case UNKNOWN:
                writeToConsole("Non conosco questa mossa. Non so gestirla.");
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
        public ExitCommandException() {
            //do nothing
        }
    }

    private enum Move{
        UNKNOWN,
        JOIN_GAME
    }

    @Override
    public boolean update(Message m) {
        logger.info("Ricevuto: ".concat(m.getType().toString()));
        askForMove();

        /*if(m instanceof CVMessage){
            //switch for CVMessages
        } else if(m instanceof MVMessage){
            //switch for MVMessages
        } else if(m instanceof WaitingRoomMessage){

        } else {
            //
        }*/
        return true;
    }
}
