package it.polimi.se2018.view;

import it.polimi.se2018.utils.message.Message;
import it.polimi.se2018.utils.Observer;

import java.util.logging.Logger;

public class GUIView extends View implements Observer {

    public GUIView(Logger logger) {
        super(logger);
    }

    @Override
    public boolean update(Message m) {
        return true;
    }
}
