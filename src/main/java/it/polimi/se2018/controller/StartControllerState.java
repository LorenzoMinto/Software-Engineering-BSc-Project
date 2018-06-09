package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.Message;

import java.util.EnumSet;

import static it.polimi.se2018.utils.ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.ViewBoundMessageType.ERROR_MESSAGE;

/**
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class StartControllerState extends ControllerState {

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public StartControllerState(Controller controller) {
        if (controller==null) { throw new IllegalArgumentException("Can't create a State Controller without a Controller");}
        this.controller = controller;
        this.defaultMessage = NO_DICE_DRAFTED;
    }

    @Override
    public Message draftDiceFromDraftPool(Dice dice) {
        Round currentRound = controller.game.getCurrentRound();

        if (currentRound.getDraftPool().draftDice(dice)) {
            currentRound.getCurrentTurn().setDraftedDice(dice);
            controller.setControllerState(controller.stateManager.getPlaceState());
            return new Message(ACKNOWLEDGMENT_MESSAGE,"Dice drafted.");

        } else {
            return new Message(ERROR_MESSAGE,"Dice not in the draft pool.");
        }
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
    public EnumSet<Move> getStatePermissions() {
        return EnumSet.of(Move.DRAFT_DICE_FROM_DRAFTPOOL, Move.USE_TOOLCARD, Move.END_TURN);
    }
}
