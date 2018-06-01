package it.polimi.se2018.view;

import it.polimi.se2018.utils.message.Message;
import javafx.application.Application;



public class GUIView extends View {


    public GUIView() {
        super();
        //TODO are args needed here?
        new Thread(()-> Application.launch(ViewGUI.class, "")).start();

    }

    @Override
    void askForMove() {

    }

    @Override
    Message handleEndTurnMove() {
        return null;
    }

    @Override
    Message handleDraftDiceFromDraftPoolMove() {
        return null;
    }

    @Override
    Message handlePlaceDiceOnWindowPatternMove() {
        return null;
    }

    @Override
    Message handleUseToolCardMove() {
        return null;
    }

    @Override
    Message handleIncrementDraftedDiceMove() {
        return null;
    }

    @Override
    Message handleDecrementDraftedDiceMove() {
        return null;
    }

    @Override
    Message handleChangeDraftedDiceValueMove() {
        return null;
    }

    @Override
    Message handleChooseDiceFromTrackMove() {
        return null;
    }

    @Override
    Message handleMoveDiceMove() {
        return null;
    }

    @Override
    Message handleJoinGameMove() {
        return null;
    }

    @Override
    void showMessage(String message) {

    }

    @Override
    public boolean update(Message m) {
        return true;
    }
}
