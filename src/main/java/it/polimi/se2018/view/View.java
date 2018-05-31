package it.polimi.se2018.view;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;

import java.util.logging.*;

public abstract class View extends Observable implements Observer {

    private String playerID;

    final Logger logger;

    public View() {
        logger = createLogger();
    }

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

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayer(String playerID) {
        this.playerID = playerID;
    }

    public void showMessage(String message){}

    public void reportError(String message){}

    //TODO: view must assume permissions (see CVMessage types comments) in case of receiving CVMessage of INACTIVE or BACK_GAME. Otherwise permissions are usually sent by Model through MVMessage

    //TODO: view must ignore MVMessages if it is in "inactive status"

    //TODO: view must enter "inactive status" if receives INACTIVE message from controller (CVMessage)
    //TODO: view must enter "active status" if receives BACK_GAME message from controller (CVMessage)

    //NOTE: L'ultimo giocatore in ordine temporale che sceglie il wp causando l'inizio del gioco potrebbe vedere prima l'inizio del gioco e poi l'acknowledge del set del windowpattern

    //TODO: riceve un messaggio MVMessage di tipo NEW_TURN e se il player della view è uguale a param:currentPlayer allora imposta le permissions, sennò setta permissions vuote fino ad un nuovo NEW_TURN

}
