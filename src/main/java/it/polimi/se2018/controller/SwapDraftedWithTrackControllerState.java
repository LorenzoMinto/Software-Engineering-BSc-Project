package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.BadDiceReferenceException;

/**
 *  This is an implicit state that switch drafted dice with selected dice from track.
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class SwapDraftedWithTrackControllerState extends ImplicitControllerState {

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public SwapDraftedWithTrackControllerState(Controller controller) {
        super(controller);
    }

    /**
     * Swaps the drafted dice of the current turn with the dice chosen from the track of the current turn.
     */
    @Override
    public void executeImplicitBehaviour() {
        Game game = controller.game;
        Turn turn = game.getCurrentRound().getCurrentTurn();

        try {
            game.getTrack().takeDice(turn.getTrackChosenDice(), turn.getSlotOfTrackChosenDice());
        } catch (BadDiceReferenceException e) {
            throw new BadBehaviourRuntimeException();
        }
        game.getTrack().putDice(turn.getDraftedDice(), turn.getSlotOfTrackChosenDice());

        turn.setDraftedDice(turn.getTrackChosenDice());
        turn.resetTrackChosenDice();
        controller.setControllerState(controller.stateManager.getNextState(this));
    }
}
