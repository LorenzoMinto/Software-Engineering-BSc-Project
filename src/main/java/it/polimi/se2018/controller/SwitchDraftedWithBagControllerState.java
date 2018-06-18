package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;

/**
 *  This is an implicit state that switches drafted dice with one random drafted from dicebag.
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class SwitchDraftedWithBagControllerState extends ImplicitControllerState {

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public SwitchDraftedWithBagControllerState(Controller controller) {
        super(controller);
    }

    /**
     * Puts the drafted dice of the current turn in the dice bag, draws another from the dice bag and
     * sets it as the drafted dice of the current turn.
     */
    @Override
    public void executeImplicitBehaviour() {
        DiceBag diceBag = controller.diceBag;
        Turn currentTurn = controller.game.getCurrentRound().getCurrentTurn();
        controller.diceBag.addDice(currentTurn.getDraftedDice());
        currentTurn.setDraftedDice(diceBag.getDices(1).get(0));
        controller.setControllerState(controller.stateManager.getNextState(this));
    }
}
