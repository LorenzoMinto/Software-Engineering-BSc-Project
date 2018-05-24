package it.polimi.se2018.view;

import it.polimi.se2018.utils.message.Message;
import it.polimi.se2018.utils.Observer;

public class CLIView extends View implements Observer {

    public CLIView() {
        super();
    }

    @Override
    public boolean update(Message m) {
        return true;
    }
}
