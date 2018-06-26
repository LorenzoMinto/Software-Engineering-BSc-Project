package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.Message;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static it.polimi.se2018.utils.ViewBoundMessageType.ACKNOWLEDGMENT_MESSAGE;
import static it.polimi.se2018.utils.ViewBoundMessageType.ERROR_MESSAGE;

/**
 *  This is the state during which, due to a toolcard, current player can move a dice from
 *  a cell of the own windowpattern to another cell of the own windowpattern.
 *
 *  @author Lorenzo Minto
 *  @author Federico Haag (refactor)
 */
public class MoveControllerState extends ControllerState {

    /**
     * String used as content of acknowledgment message in moveDice()
     */
    private static final String MOVE_MADE = "Move made.";

    /**
     * String used as content of error message in moveDice()
     */
    private static final String CANT_MAKE_THIS_MOVE = "Can't make this move.";

    /**
     * String used as content of acknowledgment message in endToolCardEffect()
     */
    private static final String TOOL_CARD_EFFECT_ENDED = "ToolCard effect ended.";

    /**
     * String used as content of error message in endToolCardEffect()
     */
    private static final String CANT_END_TOOLCARD_EFFECT = "Can't end the ToolCard effect now.";

    /**
     * String used as content of error message in moveDice()
     */
    private static final String NO_DICE_ON_CELL = "There is no dice on the selected cell";

    /**
     * Class constructor.
     *
     * @param controller the controller of which this class is going to act as a state.
     */
    public MoveControllerState(Controller controller) {
        super(controller);
        this.defaultMessage = MIDDLE_OF_EFFECT;
    }

    @Override
    public Message moveDice(int rowFrom, int colFrom, int rowTo, int colTo) {
        Game game = controller.game;
        Turn currentTurn = game.getCurrentRound().getCurrentTurn();
        WindowPattern pattern = currentTurn.getPlayer().getWindowPattern();

        Dice diceOnHold = pattern.removeDiceFromCell(rowFrom, colFrom);
        boolean isAllowed = controller.placementRule.isMoveAllowed(pattern, diceOnHold, rowTo, colTo);
        try{
            pattern.putDiceOnCell(diceOnHold, rowFrom, colFrom);
        }catch (IllegalArgumentException e){
            return new Message(ERROR_MESSAGE, NO_DICE_ON_CELL);
        }

        if (isAllowed && pattern.moveDiceFromCellToCell(rowFrom, colFrom, rowTo, colTo)) {

            controller.movesCounter += 1;

            List<Integer> possibleMovesCount = new ArrayList<>(controller.getActiveToolCard().getPossibleMovesCountSet());

            //if the moves counter is less than the maximum number of moves ask for another move
            if (possibleMovesCount.isEmpty() || controller.movesCounter < possibleMovesCount.get(possibleMovesCount.size()-1)) {
                controller.setControllerState(controller.stateManager.getNextState(this));
            } else {
                controller.setControllerState(controller.stateManager.getEndToolCardEffectControllerState());
            }
            return new Message(ACKNOWLEDGMENT_MESSAGE, MOVE_MADE);
        } else {
            return new Message(ERROR_MESSAGE, CANT_MAKE_THIS_MOVE);
        }
    }

    @Override
    public Message endToolCardEffect() {
        if (controller.getActiveToolCard().getPossibleMovesCountSet().contains(controller.movesCounter)) {
            this.controller.resetActiveToolCard();
            if (controller.game.getCurrentRound().getCurrentTurn().hasDraftedAndPlaced()) {
                controller.setControllerState(controller.stateManager.getEndControllerState());
            } else {
                controller.setControllerState(controller.stateManager.getDraftControllerState());
            }
            return new Message(ACKNOWLEDGMENT_MESSAGE, TOOL_CARD_EFFECT_ENDED);
        } else {
            return new Message(ERROR_MESSAGE, CANT_END_TOOLCARD_EFFECT);
        }
    }

    @Override
    public Set<Move> getStatePermissions() {
        return EnumSet.of(Move.MOVE_DICE, Move.END_EFFECT);
    }
}
