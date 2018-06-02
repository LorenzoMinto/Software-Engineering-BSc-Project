package it.polimi.se2018.view;

import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.networking.Client;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.message.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.File;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class SagradaSceneController extends View implements Initializable {
    private Client client;

    @FXML private TextArea playerTerminal;
    @FXML private HBox dynamicChoicesPane;
    @FXML private Pane cardsCarouselCardHBox;
    @FXML private ImageView cardsCarouselCardImageView;

    File file = new File("src/main/resources/images/toolcard1.png");
    Image toolcard = new Image(file.toURI().toString());


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cardsCarouselCardImageView.setImage(toolcard);
        cardsCarouselCardImageView.setPreserveRatio(true);
        cardsCarouselCardImageView.fitWidthProperty().bind(cardsCarouselCardHBox.widthProperty());
        cardsCarouselCardImageView.fitHeightProperty().bind(cardsCarouselCardHBox.heightProperty());
    }

    @Override
    void askForMove() {
        new Thread(new Runnable() {
            @Override public void run() {
                if (getPermissions().isEmpty()) {
                    //add message? No move available at the moment
                } else {
                    Set<Move> permissions = getPermissions();
                    //TODO: each move should have a literal representation, not hardcoded here
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            dynamicChoicesPane.getChildren().clear();
                            for (Move m : permissions) {
                                Button button = new Button(m.toString());
                                button.setId(m.toString());
                                button.setOnAction(event -> checkID(button));
                                dynamicChoicesPane.getChildren().add(button);
                            }
                        }
                    });
                }
            }
        }).start();
    }

    private void checkID(Button button){
        //TODO: Button action handling here -> will correspond to the start of a move.
        switch (button.getId()) {
            case "DRAFT_DICE_FROM_DRAFTPOOL":

                break;
            default:
                break;
        }
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
        printOnConsole("You have joined the waiting room!");
        return null;
    }

    @Override
    void notifyHandlingOfMessageEnded() {

    }

    @Override
    void showMessage(String message) {
        printOnConsole(message);
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

    public void setClient(Client c) {
        this.client = c;
    }

    protected void printOnConsole(String s) {
        String ss = "\n"+s;
        playerTerminal.appendText(ss);
    }

}
