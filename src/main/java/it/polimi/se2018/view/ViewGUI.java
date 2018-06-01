package it.polimi.se2018.view;

import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.utils.message.CVMessage;
import it.polimi.se2018.utils.message.Message;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;

public class ViewGUI extends Application implements Observer {

    //LOGIN SCENE
    private TextField userTextField;
    private CheckBox rmiBox;
    private CheckBox socketBox;

    private int rmiOrSocket = 2;

    //SAGRADA SCENE
    private SagradaSceneController sagradaSceneController;

    private Scene sagradaScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Sagrada Login");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        Text scenetitle = new Text("Welcome to Sagrada");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 2);

        rmiBox = new CheckBox("RMI");
        grid.add(rmiBox, 0,3);
        rmiBox.setSelected(true);
        rmiBox.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                if (rmiBox.isSelected()) {
                    rmiOrSocket = 0;
                    socketBox.setSelected(false);
                } else {
                    rmiOrSocket = 2;
                }
            }
        });

        socketBox = new CheckBox("Socket");
        grid.add(socketBox, 1,3);
        socketBox.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                if (socketBox.isSelected()) {
                    rmiOrSocket = 1;
                    rmiBox.setSelected(false);
                } else {
                    rmiOrSocket = 2;
                }
            }
        });

        userTextField = new TextField("Rubens");
        grid.add(userTextField, 1, 2);

        Button btn = new Button("Log in to Sagrada");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                primaryStage.setScene(sagradaScene);
                primaryStage.show();
                sagradaSceneController.handleMessage(new CVMessage(CVMessage.types.ACKNOWLEDGMENT_MESSAGE,
                        "All good, welcome "+ userTextField.getText()+"."));
                sagradaSceneController.handleMessage(new CVMessage(CVMessage.types.ACKNOWLEDGMENT_MESSAGE,
                        rmiOrSocket==0 ? "RMI" : "Socket" + " selected."));
            }
        });
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 5);

        //grid.setGridLinesVisible(true); //this is useful to understand the layout
        Scene loginScene = new Scene(grid, 500, 275);
        primaryStage.setScene(loginScene);

        URL fxmlUrl = getClass().getClassLoader().getResource("fxml/SagradaScene.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);

        Parent root = fxmlLoader.load();
        sagradaScene = new Scene(root, primaryStage.getMaxWidth(),primaryStage.getMaxHeight());
        sagradaSceneController = fxmlLoader.getController();

        primaryStage.show();
    }


    @Override
    public boolean update(Message m) {
        //TODO: Implement handling here of ViewBoundMessages
        return false;
    }
}
