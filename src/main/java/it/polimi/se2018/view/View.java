package it.polimi.se2018.view;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.Observable;

public abstract class View extends Observable {

    private Player player;

    public View() {
        //does nothing
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void showMessage(String message){}

    public void reportError(String message){}

    //TODO: view must assume permissions (see CVMessage types comments) in case of receiving CVMessage of INACTIVE or BACK_GAME. Otherwise permissions are usually sent by Model through MVMessage

    //TODO: view must ignore MVMessages if it is in "inactive status"

    //TODO: view must enter "inactive status" if receives INACTIVE message from controller (CVMessage)
    //TODO: view must enter "active status" if receives BACK_GAME message from controller (CVMessage)

    //NOTE: L'ultimo giocatore in ordine temporale che sceglie il wp causando l'inizio del gioco potrebbe vedere prima l'inizio del gioco e poi l'acknowledge del set del windowpattern

}
