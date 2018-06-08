package it.polimi.se2018.controller;

import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.message.CVMessage;

import java.util.EnumSet;
import java.util.Set;

import static it.polimi.se2018.utils.message.CVMessage.types.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.message.CVMessage.types.ERROR_MESSAGE;

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
    public CVMessage useToolCard(ToolCard toolcard) {
        if ( controller.setActiveToolCard(toolcard) ) {
            controller.setControllerState(controller.stateManager.getNextState(this));
            return new CVMessage(ACKNOWLEDGMENT_MESSAGE,"Toolcard activated.");

        } else {
            return new CVMessage(ERROR_MESSAGE,"Can't use this toolcard.");
        }
    }

    @Override
    public Set<Move> getStatePermissions() {
        return EnumSet.of(Move.USE_TOOLCARD);
    }
}
