package it.polimi.se2018.view;

import it.polimi.se2018.networking.Client;
import it.polimi.se2018.networking.ConnectionType;
import it.polimi.se2018.utils.message.Message;
import it.polimi.se2018.utils.message.WaitingRoomMessage;
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
import java.rmi.RemoteException;

public class ViewGUI extends Application {

    private Client client;

    //LOGIN SCENE
    private TextField userTextField;
    private CheckBox rmiBox;
    private CheckBox socketBox;
    private TextField portTextField;
    private TextField serverNameTextField;

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
                    socketBox.setSelected(false);
                }
            }
        });

        socketBox = new CheckBox("Socket");
        grid.add(socketBox, 1,3);
        socketBox.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                if (socketBox.isSelected()) {
                    rmiBox.setSelected(false);
                }
            }
        });

        Label portLabel = new Label("Port:");
        grid.add(portLabel, 0,5);

        portTextField = new TextField("0");
        grid.add(portTextField, 1,5);

        Label serverLabel = new Label("Server name:");
        grid.add(serverLabel, 0,4);

        serverNameTextField = new TextField("//localhost/sagradaserver");
        grid.add(serverNameTextField, 1,4);

        userTextField = new TextField("Johnnyfer");
        grid.add(userTextField, 1, 2);

        Button btn = new Button("Log in to Sagrada");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            //start the client and the game screen. This should be done only once in the whole application. SagradaScene becomes permanent.
            @Override
            public void handle(ActionEvent e) {
                client = new Client(rmiBox.isSelected() ? ConnectionType.RMI : ConnectionType.SOCKET,
                        serverNameTextField.getText(), Integer.parseInt(portTextField.getText()), sagradaSceneController, false);
                sagradaSceneController.setClient(client);
                try {
                    //TODO: Remote exception handling should not be here, client should handle and return other kind of exception
                    client.sendMessage(new WaitingRoomMessage(WaitingRoomMessage.types.JOIN, Message.fastMap("nickname",userTextField.getText())));
                } catch (RemoteException r) {
                    System.out.println("Error sending join message.");
                }
                primaryStage.setScene(sagradaScene);
                primaryStage.show();
            }
        });
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 6);

        Scene loginScene = new Scene(grid, 450, 300);
        primaryStage.setScene(loginScene);
        primaryStage.centerOnScreen();

        URL fxmlUrl = getClass().getClassLoader().getResource("fxml/SagradaScene.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);

        Parent root = fxmlLoader.load();
        sagradaScene = new Scene(root, 2000, 1000);
        sagradaSceneController = fxmlLoader.getController();
        sagradaSceneController.setLoginScene(loginScene);

        primaryStage.show();
    }
}
