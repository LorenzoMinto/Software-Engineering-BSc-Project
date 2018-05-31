package it.polimi.se2018.view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;

public class ViewGUI extends Application {

    private TextField userTextField;

    private Scene sagradaScene;
    private Text welcomeMessage;

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

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 2);

        userTextField = new TextField("Rubens");
        grid.add(userTextField, 1, 2);

        Button btn = new Button("Log in to Sagrada");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                primaryStage.setScene(sagradaScene);
                welcomeMessage.setText(welcomeMessage.getText() + " " + userTextField.getText());
                primaryStage.show();
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
        Parent root = FXMLLoader.load(fxmlUrl);
        sagradaScene = new Scene(root, 1800,1000);


        primaryStage.show();
    }
}
