package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.Message;

import java.util.EnumSet;
import java.util.Set;

import static it.polimi.se2018.utils.ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.ViewBoundMessageType.ERROR_MESSAGE;

/**
 *  This is the state in which the current player can place a dice on the own windowpattern.
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class PlaceControllerState extends ControllerState {

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public PlaceControllerState(Controller controller) {
        if (controller==null) { throw new IllegalArgumentException("Can't create a State Controller without a Controller");}
        this.controller = controller;
        this.defaultMessage = PLACE_DICE;
    }

    @Override
    public Message placeDice(int row, int col) {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();
        WindowPattern pattern = currentTurn.getPlayer().getWindowPattern();

        //TODO: rimuovere questo sout di debug
        System.out.println("Going to try to place dice " + currentTurn.getDraftedDice() + " at " + row + " " + col + " on WP:" + currentTurn.getPlayer().getWindowPattern().toString() + "with PR: " + controller.placementRule.getClass().getSimpleName());

        if (controller.placementRule.isMoveAllowed(pattern, currentTurn.getDraftedDice(), row, col)
                && pattern.putDiceOnCell(currentTurn.getDraftedDice(), row, col)) {
            currentTurn.resetDraftedDice();
            if (controller.getActiveToolCard() != null) {
                controller.setControllerState(controller.stateManager.getNextState(this));
                return new Message(ACKNOWLEDGMENT_MESSAGE,"Dice placed!");
            } else {
                if (currentTurn.hasUsedToolCard()) {
                    controller.setControllerState(controller.stateManager.getEndControllerState());
                } else {
                    controller.setControllerState(controller.stateManager.getToolCardState());
                }
                return new Message(ACKNOWLEDGMENT_MESSAGE,"Dice placed!");
            }
        } else {
            return new Message(ERROR_MESSAGE,"Move is illegal.");
        }
    }

    @Override
    public Set<Move> getStatePermissions() {
        return EnumSet.of(Move.PLACE_DICE_ON_WINDOWPATTERN);
    }
}
