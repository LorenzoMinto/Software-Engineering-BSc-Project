package it.polimi.se2018.view;

import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.utils.message.Message;
import javafx.application.Application;

import java.util.LinkedHashMap;
import java.util.List;


public class GUIView extends View {


    public GUIView() {
        super();
        //TODO are args needed here?
        new Thread(()-> Application.launch(ViewGUI.class, "")).start();

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
    Message handleGameEndedMove(LinkedHashMap<String, Integer> rankings) {
        return null;
    }

    @Override
    Message handleGiveWindowPatterns(List<WindowPattern> patterns) {
        return null;
    }

    @Override
    Message handleAddedWL() {
        return null;
    }

    @Override
    void notifyHandlingOfMessageEnded() {

    }

    @Override
    void notifyHandlingOfMessageStarted() {

    }

    @Override
    void showMessage(String message) {

    }

    @Override
    void errorMessage(String message) {

    }

    @Override
    void notifyGameVariablesChanged() {

    }

    @Override
    void notifyGameVariablesChanged(boolean forceClean) {

    }

    @Override
    void notifyGameStarted() {

    }

    @Override
    public boolean update(Message m) {
        return true;
    }
}
