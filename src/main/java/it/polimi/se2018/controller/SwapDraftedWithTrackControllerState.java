package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;

/**
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class SwapDraftedWithTrackControllerState extends ImplicitControllerState {

    public SwapDraftedWithTrackControllerState(Controller controller) {
        super(controller);
    }

    @Override
    public void executeImplicitBehaviour() {
        Game game = controller.game;
        Turn turn = game.getCurrentRound().getCurrentTurn();
        game.getTrack().putDice(turn.getDraftedDice(), turn.getSlotOfTrackChosenDice());
        turn.setDraftedDice(turn.getTrackChosenDice());
        controller.setControllerState(controller.stateManager.getNextState(this));
    }
}
