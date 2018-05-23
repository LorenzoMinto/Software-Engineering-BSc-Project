package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.networking.message.CVMessage;
import it.polimi.se2018.networking.message.Message;

/**
 *
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class DraftControllerState extends ControllerState {


    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public DraftControllerState(Controller controller) {
        this.controller = controller;
        this.defaultMessage = ONLY_DRAFT_AND_PLACE;
    }

    @Override
    public Message draftDiceFromDraftPool(Dice dice) {
        Game game = controller.game;
        Round currentRound = game.getCurrentRound();

        if (currentRound.getDraftPool().draftDice(dice)) {
            currentRound.getCurrentTurn().setDraftedDice(dice);
        }

        if (controller.getActiveToolCard() != null) {
            controller.setControllerState(controller.stateManager.getNextState(this));
        } else {
            controller.setControllerState(controller.stateManager.getPlaceState());
        }
        return new CVMessage("Dice drafted.");
    }

    @Override
    public Message placeDice(int row, int col) {
        return NO_DICE_DRAFTED;
    }
}
