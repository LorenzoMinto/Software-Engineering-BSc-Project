package it.polimi.se2018.view;

import it.polimi.se2018.controller.ControllerInterface;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.Observer;

public abstract class View implements Observer {

    private Player player;

    protected ControllerInterface controller;

    public View(ControllerInterface controller) {

        this.controller = controller;
    }

    public void showMessage(String message){}

    public void reportError(String message){}

    public Player getPlayer() {
        return player;
    }

}
