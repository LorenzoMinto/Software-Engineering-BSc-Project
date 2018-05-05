package it.polimi.se2018.view;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.PlayerMove;
import it.polimi.se2018.model.ChangeMessage;
import it.polimi.se2018.utils.Observable;

public abstract class View extends Observable<Controller> {
    public View() {
    }

    public void handleMove(PlayerMove move){}

    public void showMessage(String message){}

    public void reportError(String message){}

    public void update(ChangeMessage message){}

}
