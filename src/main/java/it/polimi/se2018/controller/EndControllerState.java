package it.polimi.se2018.controller;

import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.Message;

import java.util.EnumSet;
import java.util.Set;

import static it.polimi.se2018.utils.ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE;

/**
 * This is the state during which the current player can only end the turn.
 * @author Lorenzo Minto
 */
public class EndControllerState extends ControllerState {

    /**
     * Message used as content of acknowledgment message in endCurrentTurn()
     */
    private static final String TURN_ENDED = "Turn ended.";

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public EndControllerState(Controller controller) {
        super(controller);
        this.defaultMessage = END_TURN_ONLY;
    }

    @Override
    public Message endCurrentTurn() {
        controller.advanceGame();
        return new Message(ACKNOWLEDGMENT_MESSAGE, TURN_ENDED);
    }

    @Override
    public Set<Move> getStatePermissions() {
        return EnumSet.of(Move.END_TURN);
    }

}
