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
    void handleLeaveWaitingRoomMove() {

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
    void handleGameEndedEvent(LinkedHashMap<String, Integer> rankings) {

    }

    @Override
    void handleGiveWindowPatternsEvent(List<WindowPattern> patterns) {

    }

    @Override
    void notifyHandlingOfMessageEnded() {

    }

    @Override
    void notifyHandlingOfMessageStarted() {

    }

    @Override
    void handleAddedEvent() {

    }

    @Override
    void handleRemovedEvent() {

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

    @Override
    void notifyPermissionsChanged() {

    }
}
