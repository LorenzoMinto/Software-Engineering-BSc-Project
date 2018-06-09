package it.polimi.se2018.controller;

import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.Message;

import java.util.EnumSet;
import java.util.Set;

import static it.polimi.se2018.utils.ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.ViewBoundMessageType.ERROR_MESSAGE;

/**
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class ToolCardControllerState extends ControllerState {

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public ToolCardControllerState(Controller controller) {
        this.controller = controller;
        this.defaultMessage = TOOLCARD_ONLY;
    }

    @Override
    public Message useToolCard(ToolCard toolCard) {
        if ( controller.setActiveToolCard(toolCard) ) {
            controller.setControllerState(controller.stateManager.getNextState(this));
            return new Message(ACKNOWLEDGMENT_MESSAGE,"Toolcard activated.");

        } else {
            return new Message(ERROR_MESSAGE,"Can't use this toolCard.");
        }
    }

    @Override
    public Set<Move> getStatePermissions() {
        return EnumSet.of(Move.USE_TOOLCARD);
    }
}
