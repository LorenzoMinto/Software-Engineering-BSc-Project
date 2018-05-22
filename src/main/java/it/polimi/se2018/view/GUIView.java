package it.polimi.se2018.view;

import it.polimi.se2018.networking.message.Message;
import it.polimi.se2018.utils.Observer;

public class GUIView extends View implements Observer {

    public GUIView() {
        super();
    }


    @Override
    public boolean update(Message m) {
        return true;
    }
}
