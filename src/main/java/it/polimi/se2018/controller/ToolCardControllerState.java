package it.polimi.se2018.controller;

import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.Message;

import java.util.EnumSet;
import java.util.Set;

import static it.polimi.se2018.utils.ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.ViewBoundMessageType.ERROR_MESSAGE;

/**
 *  This is the state in which current player can use a toolcard.
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class ToolCardControllerState extends ControllerState {

    /**
     * String used as content of acknowledgment message in useToolCard()
     */
    private static final String TOOLCARD_ACTIVATED = "Toolcard activated.";

    /**
     * String used as content of error message in useToolCard()
     */
    private static final String CANT_USE_THIS_TOOL_CARD = "Can't use this toolCard.";

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public ToolCardControllerState(Controller controller) {
        super(controller);
        this.defaultMessage = TOOLCARD_ONLY;
    }

    @Override
    public Message useToolCard(ToolCard toolCard) {
        ToolCard gameToolcard = controller.game.getToolCard(toolCard);
        if ( controller.setActiveToolCard(gameToolcard) ) {
            controller.setControllerState(controller.stateManager.getNextState(this));
            return new Message(ACKNOWLEDGMENT_MESSAGE, TOOLCARD_ACTIVATED);

        } else {
            return new Message(ERROR_MESSAGE, CANT_USE_THIS_TOOL_CARD);
        }
    }

    @Override
    public Set<Move> getStatePermissions() {
        return EnumSet.of(Move.USE_TOOLCARD, Move.END_TURN);
    }
}
