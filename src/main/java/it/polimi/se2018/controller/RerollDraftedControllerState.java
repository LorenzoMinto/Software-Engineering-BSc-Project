package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;

/**
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class RerollDraftedControllerState extends ImplicitControllerState {

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public RerollDraftedControllerState(Controller controller) {
        super(controller);
    }


    /**
     * Re-rolls the drafted dice in the current turn.
     */
    @Override
    public void executeImplicitBehaviour() {
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        Dice draftedDice = currentTurn.getDraftedDice();
        draftedDice.roll();
        currentTurn.setDraftedDice(draftedDice);
        controller.setControllerState(controller.stateManager.getNextState(this));
    }
}
