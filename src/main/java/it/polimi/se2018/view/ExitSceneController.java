package it.polimi.se2018.view;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller class for ExitScene fxml scene. Displays exit screen after the game has been quit.
 *
 * @author Lorenzo Minto
 */
public class ExitSceneController {

    @FXML private Label messageLabel;


    @FXML public void handleWindowClosing() {
        Stage stage = (Stage) messageLabel.getScene().getWindow();
        stage.close();
    }

    public void setMessage(String message) {
        this.messageLabel.setText(message);
    }

}
