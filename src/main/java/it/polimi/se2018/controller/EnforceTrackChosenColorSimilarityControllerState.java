package it.polimi.se2018.controller;


import it.polimi.se2018.model.ColorRestrictionPlacementRuleDecorator;
import it.polimi.se2018.model.Dice;

/**
 * This is an implicit state that enforces the constraint on any following die move that the dice to be moved be
 * of the same color of the die chosen from the track.
 *  @author Lorenzo Minto
 */

public class EnforceTrackChosenColorSimilarityControllerState extends ImplicitControllerState {

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public EnforceTrackChosenColorSimilarityControllerState(Controller controller) {
        super(controller);
    }

    /**
     * Restricts the movable dice on the window pattern to the ones of the same color as the track chosen dice.
     */
    @Override
    public void executeImplicitBehaviour() {
        Dice trackChoseDice = controller.game.getCurrentRound().getCurrentTurn().getTrackChosenDice();
        controller.placementRule = new ColorRestrictionPlacementRuleDecorator(controller.placementRule, trackChoseDice.getColor());
        controller.setControllerState(controller.stateManager.getNextState(this));
    }
}