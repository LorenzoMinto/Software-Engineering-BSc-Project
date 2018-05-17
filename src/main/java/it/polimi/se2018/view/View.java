package it.polimi.se2018.view;

import it.polimi.se2018.controller.ControllerInterface;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;

public abstract class View extends Observable implements Observer {

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



}
