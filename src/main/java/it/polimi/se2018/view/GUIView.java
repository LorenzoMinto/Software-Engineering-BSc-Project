package it.polimi.se2018.view;

import it.polimi.se2018.networking.ConnectionType;
import it.polimi.se2018.networking.NetworkingException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
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
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;

public class GUIView extends Application {

    /**
     * Reference to main Sagrada Scene's controller
     */
    private SagradaSceneController sagradaSceneController;

    /**
     * Reference to main Sagrada Scene
     */
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

        CheckBox rmiBox = new CheckBox("RMI");
        grid.add(rmiBox, 0,3);
        rmiBox.setSelected(true);

        CheckBox socketBox = new CheckBox("Socket");
        grid.add(socketBox, 1,3);
        socketBox.setOnAction(e -> {
            if (socketBox.isSelected()) {
                rmiBox.setSelected(false);
            }
        });
        rmiBox.setOnAction(e -> {
            if (rmiBox.isSelected()) {
                socketBox.setSelected(false);
            }
        });

        Label portLabel = new Label("Port:");
        grid.add(portLabel, 0,5);

        TextField portTextField = new TextField("0");
        grid.add(portTextField, 1,5);

        Label serverLabel = new Label("Server name:");
        grid.add(serverLabel, 0,4);

        TextField serverNameTextField = new TextField("//localhost/sagradaserver");
        grid.add(serverNameTextField, 1,4);

        Button btn = new Button("Log in to Sagrada");
        //start the client and the game screen. This should be done only once in the whole application. SagradaScene becomes permanent.
        btn.setOnAction(e -> {
            try {
                sagradaSceneController.connectToRemoteServer(rmiBox.isSelected() ? ConnectionType.RMI : ConnectionType.SOCKET,
                        serverNameTextField.getText(), Integer.parseInt(portTextField.getText()));
            } catch (NetworkingException e1) {
                return;
            }
            sagradaSceneController.showWaitingRoom();

            primaryStage.setScene(sagradaScene);
            primaryStage.show();
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

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        //set Stage boundaries to visible bounds of the main screen
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());

        primaryStage.setMinHeight(primaryScreenBounds.getWidth()/1.5*13.5/16);
        primaryStage.setMinWidth(primaryScreenBounds.getWidth()/1.5);

        sagradaScene = new Scene(root, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());
        sagradaSceneController = fxmlLoader.getController();

        primaryStage.show();
    }
}
