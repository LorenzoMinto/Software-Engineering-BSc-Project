package it.polimi.se2018.view;

import it.polimi.se2018.utils.Move;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

public class WaitingRoomView extends Pane {

    @FXML private Label nameLabel;
    @FXML private Label messageLabel;
    @FXML private HBox dynamicChoicesBox;
    @FXML private ListView<String> waitingRoomList;

    public static final ObservableList waitingPlayers = FXCollections.observableArrayList();

    public WaitingRoomView(String username) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/WaitingRoomView.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        waitingRoomList.setItems(waitingPlayers);
    }

    public void addPlayer(String player) {
        waitingPlayers.add(player);
    }

    public void forwardMessage(String m) {
        messageLabel.setText(m);
    }

    public void updatePermissions(EnumSet<Move> permissions) {
        //TODO: assign action here to be passed to setOnAction so events are handled on SagradaScene
        if (permissions.isEmpty()) {
            Platform.runLater(() -> dynamicChoicesBox.getChildren().clear());
        } else {
            Platform.runLater(() -> dynamicChoicesBox.getChildren().clear());
            for (Move m: permissions) {
                Button button = new Button(m.getTextualREP());
                button.setId(m.toString());
                button.setOnAction(event -> checkID(m));
                Platform.runLater(() -> dynamicChoicesBox.getChildren().add(button));
            }
        }
    }

    private void checkID(Move m) {

    }
}
