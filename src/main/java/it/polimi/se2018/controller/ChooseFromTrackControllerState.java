package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

/**
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class ChooseFromTrackControllerState extends ControllerState {

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public ChooseFromTrackControllerState(Controller controller) {
        this.controller = controller;
        this.defaultMessage = MIDDLE_OF_EFFECT;
    }

    @Override
    public void chooseDiceFromTrack(Dice dice, int slotNumber, View view) {
        Game game = controller.game;
        if (game.getTrack().takeDice(dice, slotNumber)) {
            game.getCurrentRound().getCurrentTurn().setTrackChosenDice(dice);
            game.getCurrentRound().getCurrentTurn().setSlotOfTrackChosenDice(slotNumber);
            controller.setControllerState(controller.stateManager.getNextState(this));
        }

    }
}
