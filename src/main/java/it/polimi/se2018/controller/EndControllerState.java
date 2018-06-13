package it.polimi.se2018.controller;

import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.Message;

import java.util.EnumSet;

import static it.polimi.se2018.utils.ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE;

/**
 * @author Lorenzo Minto
 */
public class EndControllerState extends ControllerState {

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public EndControllerState(Controller controller) {
        if (controller==null) { throw new IllegalArgumentException("Can't create a State Controller without a Controller");}
        this.controller = controller;
        this.defaultMessage = END_TURN_ONLY;
    }

    @Override
    public Message endCurrentTurn() {
        controller.advanceGame();
        //TODO: What to return here?!
        return new Message(ACKNOWLEDGMENT_MESSAGE, "Turn ended.");
    }

    @Override
    public EnumSet<Move> getStatePermissions() {
        return EnumSet.of(Move.END_TURN);
    }

}
