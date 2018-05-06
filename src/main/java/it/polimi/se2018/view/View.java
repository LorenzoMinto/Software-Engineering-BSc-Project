package it.polimi.se2018.view;

import it.polimi.se2018.controller.ControllerInterface;
import it.polimi.se2018.model.Player;

public abstract class View {

    private Player player;

    protected ControllerInterface controller;

    public View(ControllerInterface controller, Player player) {

        this.player = player;
        this.controller = controller;
    }

    public void showMessage(String message){}

    public void reportError(String message){}

}
