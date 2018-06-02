package it.polimi.se2018.controller;

import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.message.CVMessage;

import java.util.EnumSet;

import static it.polimi.se2018.utils.message.CVMessage.types.ACKNOWLEDGMENT_MESSAGE;

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
        this.controller = controller;
        this.defaultMessage = END_TURN_ONLY;
    }

    @Override
    public CVMessage endCurrentTurn() {
        controller.advanceGame();
        //TODO: What to return here?!
        return new CVMessage(ACKNOWLEDGMENT_MESSAGE, "Turn ended.");
    }

    @Override

    public EnumSet<Move> getStatePermissions() {
        return EnumSet.of(Move.END_TURN);
    }

}
