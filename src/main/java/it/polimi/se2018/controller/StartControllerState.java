package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.Message;

import java.util.EnumSet;
import java.util.Set;

import static it.polimi.se2018.utils.ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.ViewBoundMessageType.ERROR_MESSAGE;

/**
 *  This is the initial state of each turn during which current player can draft a dice or use a toolcard.
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class StartControllerState extends ControllerState {

    /**
     * String used as content of acknowledgment message in draftDiceFromDraftPool()
     */
    private static final String DICE_DRAFTED = "Dice drafted.";

    /**
     * String used as content of error message in draftDiceFromDraftPool()
     */
    private static final String DICE_NOT_IN_DRAFT_POOL = "Dice not in DraftPool.";

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
    public StartControllerState(Controller controller) {
        super(controller);
        this.defaultMessage = NO_DICE_DRAFTED;
    }

    @Override
    public Message draftDiceFromDraftPool(Dice dice) {
        Round currentRound = controller.game.getCurrentRound();

        if (currentRound.getDraftPool().draftDice(dice)) {
            currentRound.getCurrentTurn().setDraftedDice(dice);
            controller.setControllerState(controller.stateManager.getPlaceState());
            return new Message(ACKNOWLEDGMENT_MESSAGE, DICE_DRAFTED);

        } else {
            return new Message(ERROR_MESSAGE, DICE_NOT_IN_DRAFT_POOL);
        }
    }

    @Override
    public Message useToolCard(ToolCard toolCard) {

        ToolCard gameToolCard = controller.game.getToolCard(toolCard);
        if (controller.setActiveToolCard(gameToolCard)) {
            controller.setControllerState(controller.stateManager.getNextState(this));
            return new Message(ACKNOWLEDGMENT_MESSAGE, TOOLCARD_ACTIVATED);
        } else {
            return new Message(ERROR_MESSAGE, CANT_USE_THIS_TOOL_CARD);
        }
    }

    @Override
    public Set<Move> getStatePermissions() {
        return EnumSet.noneOf(Move.class);
    }
}
