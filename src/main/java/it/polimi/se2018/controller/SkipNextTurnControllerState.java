package it.polimi.se2018.controller;

import it.polimi.se2018.model.Round;

/**
 * Implicit state that as implicit behaviour removes next turn of current player.
 * @author Lorenzo Minto
 */
public class SkipNextTurnControllerState extends ImplicitControllerState {

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public SkipNextTurnControllerState(Controller controller) {
        super(controller);
    }

    /**
     * Eliminates the current player's next turn from current round.
     */
    @Override
    public void executeImplicitBehaviour() {
        Round currentRound = controller.game.getCurrentRound();
        if (currentRound.removeNextTurnOfPlayer(currentRound.getCurrentTurn().getPlayer())) {
            controller.setControllerState(controller.stateManager.getNextState(this));
        }
    }
}
