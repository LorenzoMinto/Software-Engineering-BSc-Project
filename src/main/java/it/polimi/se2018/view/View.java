package it.polimi.se2018.view;

import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.utils.message.CVMessage;
import it.polimi.se2018.utils.message.MVMessage;
import it.polimi.se2018.utils.message.Message;
import it.polimi.se2018.utils.message.WaitingRoomMessage;

import java.util.EnumSet;
import java.util.Set;
import java.util.logging.*;

public abstract class View extends Observable implements Observer {

    private String playerID;

    final Logger logger;

    private Move currentMove;

    private EnumSet<Move> permissions;

    public View() {
        logger = createLogger();
    }

    public String getPlayerID() {
        return playerID;
    }

    Move getCurrentMove() {
        return currentMove;
    }

    public Set<Move> getPermissions() {
        return permissions;
    }

    public void setPlayer(String playerID) {
        this.playerID = playerID;
    }

    void setCurrentMove(Move currentMove) {
        this.currentMove = currentMove;
    }

    public void setPermissions(Set<Move> permissions) {
        this.permissions = (EnumSet<Move>)permissions;
    }

    abstract void askForMove();

    void handleMove() {
        //Check if currentMove is a valid one
        if(getCurrentMove()==null){
            showMessage("Mossa non riconosciuta");
            return;
        }

        Message message = null;

        //Here the code that handles each move. Each case can contain multiple writeToConsole and / or readFromConsole
        switch (getCurrentMove()) {
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
        }

        //Send message (if created) to server (through client)
        if(message!=null){
            notify(message);
        }
    }

    abstract Message handleEndTurnMove();

    abstract Message handleDraftDiceFromDraftPoolMove();

    abstract Message handlePlaceDiceOnWindowPatternMove();

    abstract Message handleUseToolCardMove();

    abstract Message handleIncrementDraftedDiceMove();

    abstract Message handleDecrementDraftedDiceMove();

    abstract Message handleChangeDraftedDiceValueMove();

    abstract Message handleChooseDiceFromTrackMove();

    abstract Message handleMoveDiceMove();

    abstract Message handleJoinGameMove();

    void receiveMessage(Message m) {

        EnumSet<Move> p = (EnumSet<Move>) m.getPermissions();
        if(p!=null && !p.isEmpty()){
            setPermissions(p);
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

    abstract void showMessage(String message);

    //UTILS
    private Logger createLogger(){
        Logger newLogger = Logger.getLogger(View.class.getName());
        newLogger.setUseParentHandlers(false);
        newLogger.setLevel(Level.FINE);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.FINE);
        handler.setFormatter(new SimpleFormatter(){
            private static final String FORMAT = "[VIEW] %1$s %n";

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(FORMAT,lr.getMessage());
            }

        });
        newLogger.addHandler(handler);
        return newLogger;
    }

    //TODO: view must assume permissions (see CVMessage types comments) in case of receiving CVMessage of INACTIVE or BACK_GAME. Otherwise permissions are usually sent by Model through MVMessage

    //TODO: view must ignore MVMessages if it is in "inactive status"

    //TODO: view must enter "inactive status" if receives INACTIVE message from controller (CVMessage)
    //TODO: view must enter "active status" if receives BACK_GAME message from controller (CVMessage)

    //NOTE: L'ultimo giocatore in ordine temporale che sceglie il wp causando l'inizio del gioco potrebbe vedere prima l'inizio del gioco e poi l'acknowledge del set del windowpattern

    //TODO: riceve un messaggio MVMessage di tipo NEW_TURN e se il player della view è uguale a param:currentPlayer allora imposta le permissions, sennò setta permissions vuote fino ad un nuovo NEW_TURN

}
