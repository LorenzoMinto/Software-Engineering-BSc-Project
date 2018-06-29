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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;

/**
 * Custom Pane that displays the Server's waiting room and that enables the player to interact with it.
 *
 * @author Lorenzo Minto
 */
public class WaitingRoomView extends Pane {

    /**
     * Label that displays the current player's chosen username
     */
    @FXML private Label nameLabel;
    /**
     * Label that displays the Server's response to the user's interactions.
     */
    @FXML private Label messageLabel;
    /**
     * HBox that wraps all the button for the interactions possible at the moment
     */
    @FXML private HBox dynamicChoicesBox;
    /**
     * ListView that shows the players currently in the Server's waiting room
     */
    @FXML private ListView<String> waitingRoomList;

    /**
     * Button that allows the player to exit the Server's waiting room
     */
    @FXML private Button exitButton;

    /**
     * Textfield that allows the user to input the wanted username
     */
    @FXML private TextField usernameTextfield;

    /**
     * Observable list of players waiting in the Server's waiting room
     */
    private static final ObservableList waitingPlayers = FXCollections.observableArrayList();

    /**
     * Class constructor
     */
    WaitingRoomView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/WaitingRoomView.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            forwardMessage(exception.getMessage());
        }

        Platform.runLater(() -> waitingRoomList.setItems(waitingPlayers));

    }

    /**
     * Sets the waiting players in the observable list
     *
     * @param players the players waiting in the waiting room
     */
    public void setWaitingPlayers(List<String> players) {
        Platform.runLater(() -> {
            waitingPlayers.clear();
            waitingPlayers.addAll(players);
        });
    }

    /**
     * Displays the message string in the message label
     *
     * @param m message string to be shown
     */
    public void forwardMessage(String m) {
        messageLabel.setText(m);
    }

    /**
     * Sets the even handler to be called when the exit button in pressed
     *
     * @param e the even handler
     */
    public void setExitHandler(EventHandler<ActionEvent> e) {
        exitButton.setOnAction(e);
    }

    /**
     * Adds a possible interaction to the dynamicChoicesBox
     *
     * @param p the interaction permission
     */
    public void addPermissions(Button p) {
        Platform.runLater(() -> dynamicChoicesBox.getChildren().add(p));
    }

    /**
     * Empties the possible interactions in the dynamicChoicesBox
     */
    public void resetPermissions() {
        Platform.runLater(() -> dynamicChoicesBox.getChildren().clear());
    }

    /**
     * Returns the text inside the usernameTextfield
     *
     * @return the text inside the usernameTextfield
     */
    public String getUsername() { return usernameTextfield.getText(); }
}
