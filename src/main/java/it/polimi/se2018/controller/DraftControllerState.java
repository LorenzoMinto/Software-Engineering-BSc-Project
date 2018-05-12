package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

/**
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class DraftControllerState extends ControllerState {

    public DraftControllerState(Controller controller) {
        this.controller = controller;
        this.defaultMessage = ONLY_DRAFT_AND_PLACE;
    }

    @Override
    public void draftDiceFromDraftPool(Dice dice, View view) {
        Game game = controller.game;
        Round currentRound = game.getCurrentRound();

        if (currentRound.getDraftPool().draftDice(dice)) {
            currentRound.getCurrentTurn().setDraftedDice(dice);
        }

        //TODO: activeToolcard needs to be set to null after effect expires
        if (controller.getActiveToolCard() != null) {
            controller.setControllerState(controller.stateManager.getNextState(this));
        } else {
            controller.setControllerState(controller.stateManager.getPlaceState());
        }
    }

    @Override
    public void placeDice(int row, int col, View view) {
        view.showMessage(DRAFT_FIRST);
    }
}
