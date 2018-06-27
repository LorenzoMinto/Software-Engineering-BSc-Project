package it.polimi.se2018.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;

public class WaitingRoomView extends Pane {

    @FXML private Label nameLabel;
    @FXML private Label messageLabel;
    @FXML private HBox dynamicChoicesBox;
    @FXML private ListView<String> waitingRoomList;

    @FXML private Button exitButton;

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

        Platform.runLater(() -> {
            waitingRoomList.setItems(waitingPlayers);
            nameLabel.setText(username);
        });
    }

    public void setWaitingPlayers(List<String> players) {
        Platform.runLater(() -> {
            waitingPlayers.clear();
            waitingPlayers.addAll(players);
        });
    }

    public void forwardMessage(String m) {
        messageLabel.setText(m);
    }

    public void setExitHandler(EventHandler<ActionEvent> e) {
        exitButton.setOnAction(e);
    }

    public void addPermissions(Button p) {
        Platform.runLater(() -> {
            dynamicChoicesBox.getChildren().add(p);
        });
    }

    public void resetPermissions() {
        Platform.runLater(() -> {
            dynamicChoicesBox.getChildren().clear();
        });
    }
}
