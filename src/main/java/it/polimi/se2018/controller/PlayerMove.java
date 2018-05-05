package it.polimi.se2018.controller;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.view.View;


public abstract class PlayerMove {

    //TODO: check access permissions to the following attributes
    Player player;

    View view;

    public Player getPlayer() {
        return player;
    }

    public View getView() {
        return view;
    }
}