package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;


public class MoveControllerState implements ControllerState {
    Controller controller;

    public MoveControllerState(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void draftDiceFromDraftPool(Dice dice, View view) {
        view.showMessage("Can't do. You are in the middle of a ToolCard effect");
    }

    @Override
    public void placeDice(int row, int col, View view) {
        view.showMessage("Can't do. You are in the middle of a ToolCard effect");
    }

    @Override
    public void useToolCard(Player player, ToolCard toolcard, View view) {
        view.showMessage("Can't do. You are in the middle of a ToolCard effect");
    }

    @Override
    public void chooseDiceFromTrack(Dice dice, int slotNumber, View view) {
        view.showMessage("Can't do. You are in the middle of a ToolCard effect");
    }

    @Override
    public void moveDice(int rowFrom, int colFrom, int rowTo, int colTo, View view) {
        //TODO: PlacementRule needs to be enforced
        Game game = controller.game;
        Turn currentTurn = game.currentRound.currentTurn;
        if (currentTurn.currentPlayer.getWindowPattern().moveDiceFromCellToCell(rowFrom, colFrom, rowTo, colTo)) {
            view.showMessage("Move made.");
            controller.movesCounter += 1;
            if (controller.movesCounter < 2) {
                controller.setControllerState(controller.getActiveToolCard().nextState(this));
            } else {
                controller.setControllerState(controller.getDraftControllerState());
            }
        } else {
            view.showMessage("Can't make this move.");
        }
    }

    @Override
    public void incrementDice(View view) {
        view.showMessage("Can't do. You are in the middle of a ToolCard effect");
    }

    @Override
    public void decrementDice(View view) {
        view.showMessage("Can't do. You are in the middle of a ToolCard effect");
    }

    @Override
    public void chooseDiceValue(int value, View view) {
        view.showMessage("Can't do. You are in the middle of a ToolCard effect");
    }

    @Override
    public void executeImplicitBehaviour(View view) {

    }
}
