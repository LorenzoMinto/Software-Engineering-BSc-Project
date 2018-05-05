package it.polimi.se2018.controller;

import it.polimi.se2018.model.Player;


public abstract class PlayerMove {

    Player player;

    View view;

    public abstract void perform(ControllerState controllerState);

    public Player getPlayer() {
        return player;
    }

}
