package it.polimi.se2018.view;

import it.polimi.se2018.utils.message.Message;
import it.polimi.se2018.utils.message.NoSuchParamInMessageException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class SagradaSceneController {
    @FXML private Button john;
    @FXML private TextArea playerTerminal;

    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        System.out.println("Johnny and rubens");
    }

    protected void handleMessage(Message message) {
        String msg;
        try {
            msg = "\n"+ message.getParam("message");
        } catch (NoSuchParamInMessageException e) {
            msg = "\nSomething went wrong in the communication with the Server. Empty Message.";
        }
        playerTerminal.appendText(msg);
    }

}
